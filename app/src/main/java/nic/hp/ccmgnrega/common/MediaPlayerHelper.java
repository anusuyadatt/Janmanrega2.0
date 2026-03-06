package nic.hp.ccmgnrega.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniComputeResponse;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniConfigResponse;
import retrofit2.Call;

public class MediaPlayerHelper {

    private static MediaPlayer mediaPlayer;
    private static Call<BhashiniConfigResponse> configCall;
    private static Call<BhashiniComputeResponse> computeCall;
    static private AlertDialog loadingAudioDialog;
    final static long[] time = {System.currentTimeMillis()};
    final static long[] curr = {System.currentTimeMillis()};
    static ImageView speakerButton;
    static Context context;
    static TaskCompletionListener taskCompletionListener;

    public static void setTaskCompletionListener(TaskCompletionListener taskCompletionListener) {
        MediaPlayerHelper.taskCompletionListener = taskCompletionListener;
    }
    public static void playMedia(Context context, ImageView speakerButton, String speech) {
        time[0] = System.currentTimeMillis();
        curr[0] = System.currentTimeMillis();
        MediaPlayerHelper.speakerButton = speakerButton;
        MediaPlayerHelper.context = context;
        setUpLoadingDialog();
        makeCalls(speech);

    }

    private static void setUpMediaPlayer(ImageView speakerButton) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        curr[0] = System.currentTimeMillis();
                        Log.v("MediaPlayerHelper", "Time taken for audio to start: " + (curr[0] - time[0]));
                        time[0] = curr[0];
                        if (loadingAudioDialog != null && loadingAudioDialog.isShowing()) {
                            loadingAudioDialog.dismiss();
                        }
                    } else {
                        releaseMediaPlayer();
                    }
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.v("MediaPlayerHelper", "Reached mediaPlayer onCompletion");
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    releaseMediaPlayer();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.v("MediaPlayerHelper", "Reached mediaPlayer onError");
                    releaseMediaPlayer();
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setUpLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Loading Audio...")
                .setCancelable(false)
                .setPositiveButton("Cancel Audio", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        releaseMediaPlayer();
                        dialog.dismiss();
                    }
                });
        loadingAudioDialog = builder.create();
        loadingAudioDialog.show();
    }

    private static void makeCalls(String speech) {
        String languageCode = MySharedPref.getAppLangCode(context);
        Log.v("MediaPlayerHelper", "Speech: " + speech);
        configCall = Utility.makeTtsConfigCall(languageCode, new TtsResponseCallback() {
            @Override
            public void onResponse(String serviceId, String audioVoice) {
                curr[0] = System.currentTimeMillis();
                Log.v("MediaPlayerHelper", "Time taken for config call: " + (curr[0]- time[0]));
                time[0] = curr[0];

                curr[0] = System.currentTimeMillis();
                Log.v("MediaPlayerHelper", "Time taken for speech prep: " + (curr[0] - time[0]));
                time[0] = curr[0];

                computeCall = Utility.makeTtsComputeCall(languageCode, serviceId, audioVoice, speech, new ResponseCallback() {
                    @Override
                    public void onResponse(String base64FormattedString) {
                        curr[0] = System.currentTimeMillis();
                        Log.v("MediaPlayerHelper", "Time taken for compute call: " + (curr[0]- time[0]));
                        time[0] = curr[0];

                        try{
                            String url = "data:audio/wav;base64,"+base64FormattedString;
                            setUpMediaPlayer(speakerButton);


                            try {
                                byte[] decoded = Base64.decode(base64FormattedString, Base64.DEFAULT);
                                File tempWav = File.createTempFile("temp","wav", context.getCacheDir());
                                tempWav.deleteOnExit();
                                FileOutputStream fos = new FileOutputStream(tempWav);
                                fos.write(decoded);
                                fos.close();

                                FileInputStream fis = new FileInputStream(tempWav);
                                mediaPlayer.setDataSource(fis.getFD());

                                curr[0] = System.currentTimeMillis();
                                Log.v("MediaPlayerHelper", "Time taken for setting data source: " + (curr[0]- time[0]));
                                time[0] = curr[0];
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setVolume(100f, 100f);
                                mediaPlayer.setLooping(false);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {
                        curr[0] = System.currentTimeMillis();
                        Log.v("MediaPlayerHelper", "Time taken for compute call failing: " + (curr[0]- time[0]));
                        time[0] = curr[0];
                        releaseMediaPlayer();
                    }
                });
            }

            @Override
            public void onFailure() {
                releaseMediaPlayer();
            }
        });
    }


    public static void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer = null;
            } else {
                mediaPlayer = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        if (configCall != null) {
            configCall.cancel();
        }
        if (computeCall != null) {
            computeCall.cancel();
        }
        MediaStateManager.setAllStatesToStopped();
        ExpandableListMediaStateManager.setAllStatesToStopped();
        if (taskCompletionListener != null) {
            taskCompletionListener.onAllTasksCompleted();
        }
        if (speakerButton != null) {
            speakerButton.setBackground(ContextCompat.getDrawable(context, R.drawable.speaker));
        }
        if (loadingAudioDialog != null && loadingAudioDialog.isShowing()) {
            loadingAudioDialog.dismiss();
        }
    }
}
