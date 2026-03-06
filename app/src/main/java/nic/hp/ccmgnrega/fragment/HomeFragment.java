package nic.hp.ccmgnrega.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.goodiebag.pinview.Pinview;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import nic.hp.ccmgnrega.GiveFeedbackWorkerReportActivity;
import nic.hp.ccmgnrega.MainActivity;
import nic.hp.ccmgnrega.NearByAssetsMapActivity;
import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.SendFeedBackActivity;
import nic.hp.ccmgnrega.UploadWorksitePhoto;
import nic.hp.ccmgnrega.WorkerQueryNewActivity;
import nic.hp.ccmgnrega.adapter.OptionsAdapter;
import nic.hp.ccmgnrega.adapter.ViewPagerAdapter;
import nic.hp.ccmgnrega.common.Helper;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.common.DbHelper;

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    protected View rootView;

    GridView gvOptions;
    private ArrayList<String> alName = new ArrayList();
    private ArrayList<Integer> alImage = new ArrayList();
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;
    protected String cLong = "",cLat = "",langCode="en";
    String strActivityCode="01-";



    @Override
    public View onCreateView(LayoutInflater inflater2, ViewGroup container, Bundle savedInstanceState) {
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        connectionDetector = new ConnectionDetector(getActivity());
        dbHelper = new DbHelper(getActivity());
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
        rootView = inflater2.inflate(R.layout.fragment_home, container, false);
        gvOptions = rootView. findViewById(R.id.gvOptions);
        Bundle args = getArguments();
        if (args != null) {
            if (args.getString("menu") != null)
                menu = args.getString("menu");
            if (args.getString("cLong") != null)
                cLong = args.getString("cLong");
            if (args.getString("cLat") != null)
                cLat = args.getString("cLat");
        }
        if(MySharedPref.getAppLangCode(getContext()) ==null)
            MySharedPref.setAppLangCode(getContext(),"en");
        langCode=MySharedPref.getAppLangCode(getContext());
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
        sliderDotspanel = (LinearLayout)  rootView.findViewById(R.id.sliderDots);
        viewPager = (ViewPager)  rootView.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];
        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot_drawer));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
            dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot_drawer));
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%dotscount);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);

        alName.add(getContext().getString(R.string.know_worker_att_pay));
        alImage.add(R.mipmap.icon_know_workers);
        alName.add(getContext().getString(R.string.nearby_assets));
        alImage.add(R.mipmap.icon_nearby_assets);
        alName.add(getContext().getString(R.string.search_assets));
        alImage.add(R.mipmap.icon_search_assets);
        alName.add(getContext().getString(R.string.give_asset_feedback));
        alImage.add(R.mipmap.ic_give_asset_feedback);
        alName.add(getContext().getString(R.string.permissible_work_list));
        alImage.add(R.mipmap.icon_permissible_work_list);
        alName.add(getContext().getString(R.string.preferences));
        alImage.add(R.mipmap.icon_preferences);
       // alName.add(getContext().getString(R.string.mark_nearby_assets));
       // alImage.add(R.mipmap.icon_nearby_assets);
        gvOptions.setOnItemClickListener(this);
        gvOptions.setAdapter(new OptionsAdapter(getContext(), alName, alImage));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(viewPager.getContext(), R.drawable.non_active_dot_drawer));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(viewPager.getContext(), R.drawable.active_dot_drawer));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (alName.get(i).equalsIgnoreCase(getContext().getString(R.string.search_assets))) {
            if (connectionDetector.isConnectingToInternet()) {
                final Intent mainIntent = new Intent(getContext(), MainActivity.class);
                mainIntent.putExtra("displayPosition", "-1");
                mainIntent.putExtra("homeDisplayPosition", "1");
                startActivity(mainIntent);
                getActivity().finish();
            }
            else
                MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"01");

        }
        else if (alName.get(i).equalsIgnoreCase(getContext().getString(R.string.nearby_assets))) {
            if (connectionDetector.isConnectingToInternet()) {
                final Intent mainIntent = new Intent(getContext(), MainActivity.class);
                mainIntent.putExtra("displayPosition",  "-1");
                mainIntent.putExtra("homeDisplayPosition",  "2");
                startActivity(mainIntent);
                getActivity().finish();
            }
            else
                MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"02");

        }
        else if (alName.get(i).equalsIgnoreCase(getContext().getString(R.string.know_worker_att_pay))) {

            /*Toast.makeText(getContext(),""+Helper.getBaseURL(),Toast.LENGTH_LONG).show();

            final Intent mainIntent = new Intent(getContext(), MainActivity.class);
            mainIntent.putExtra("displayPosition", "-1");
            mainIntent.putExtra("homeDisplayPosition", "3");
            startActivity(mainIntent);*/
            if (connectionDetector.isConnectingToInternet()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if(MySharedPref.getLoginPin(getContext()).isEmpty()){
                        final Dialog dialogAlert2 = new Dialog(getActivity());
                        MyAlert.dialogForOk
                                (getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning),
                                        getContext().getString(R.string.please_register_first),
                                        dialogAlert2,
                                        getContext().getString(R.string.ok),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogAlert2.dismiss();
                                                final Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                                mainIntent.putExtra("displayPosition", "6");
                                                startActivity(mainIntent);
                                                getActivity().finish();
                                            }
                                        }, strActivityCode + "06");

                    }
                    else {
                        verifyLoginPin(getContext(),"-1","3",false);
                    }
                }
            }
            else
                MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"03");
        }
        else if (alName.get(i).equalsIgnoreCase(getContext().getString(R.string.preferences))) {
            final Intent mainIntent = new Intent(getContext(), MainActivity.class);
            mainIntent.putExtra("displayPosition",  "-1");
            mainIntent.putExtra("homeDisplayPosition",  "4");
            startActivity(mainIntent);
            getActivity().finish();
        }
        else if (alName.get(i).equalsIgnoreCase(getContext().getString(R.string.give_asset_feedback))) {
            if (connectionDetector.isConnectingToInternet()) {

                if(MySharedPref.getLoginPin(getContext()).isEmpty()){
                    final Dialog dialogAlert2 = new Dialog(getActivity());
                    MyAlert.dialogForOk
                            (getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning),
                                    getContext().getString(R.string.please_register_first),
                                    dialogAlert2,
                                    getContext().getString(R.string.ok),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogAlert2.dismiss();
                                            final Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                            mainIntent.putExtra("displayPosition", "6");
                                            startActivity(mainIntent);
                                            getActivity().finish();
                                        }
                                    }, strActivityCode + "05");

                }
                else {
                    verifyLoginPin(getContext(), "0", "0", true);

                }

            }
            else
                MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"04");

        }
        else if (alName.get(i).equalsIgnoreCase(getContext().getString(R.string.permissible_work_list))) {
            final Intent mainIntent = new Intent(getContext(), MainActivity.class);
            mainIntent.putExtra("displayPosition",  "-1");
            mainIntent.putExtra("homeDisplayPosition",  "7");
            startActivity(mainIntent);
            getActivity().finish();
        }
        else if(alName.get(i).equalsIgnoreCase(getContext().getString(R.string.mark_nearby_assets))) {
            Intent intent = new Intent(getContext(), NearByAssetsMapActivity.class);
            intent.putExtra("cLong", cLong);
            intent.putExtra("cLat", cLat);
            startActivity(intent);
          /*  if (connectionDetector.isConnectingToInternet()) {
                if(MySharedPref.getLoginPin(getContext()).isEmpty()){
                    final Dialog dialogAlert2 = new Dialog(getActivity());
                    MyAlert.dialogForOk
                            (getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning),
                                    getContext().getString(R.string.please_register_first),
                                    dialogAlert2,
                                    getContext().getString(R.string.ok),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogAlert2.dismiss();
                                            final Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                            mainIntent.putExtra("displayPosition", "6");
                                            startActivity(mainIntent);
                                            getActivity().finish();
                                        }
                                    }, strActivityCode + "10");

                }
                else {
                    verifyLoginPin(getContext(), "1", "0", true);

                }

            }
            else
                MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"11");*/

        }
    }

    private void verifyLoginPin(Context context,String displayPositon,String homeDisplayPositon,Boolean isActivity) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.login_pin_dialog);
        Button submitButton = (Button) dialog.findViewById(R.id.buttonSubmit);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        Pinview pinview = dialog.findViewById(R.id.pinview);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputLoginPin = pinview.getValue();
                if (inputLoginPin.isEmpty()) {
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.please_enter_pin), strActivityCode + "07");
                    pinview.requestFocus();
                    pinview.clearValue();
                }  else if(!inputLoginPin.isEmpty()&&inputLoginPin.length()<4) {
                    pinview.requestFocus();
                    pinview.clearValue();
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.please_enter_valid_pin), strActivityCode + "08");
                }   else if(!inputLoginPin.isEmpty() && !inputLoginPin.equalsIgnoreCase(MySharedPref.getLoginPin(context))) {
                    pinview.requestFocus();
                    pinview.clearValue();
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.warning), context.getString(R.string.input_pin_wrong), strActivityCode + "09");
                }  else {
                    dialog.dismiss();
                    if(!isActivity) {
                        final Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.putExtra("displayPosition", displayPositon);
                        mainIntent.putExtra("homeDisplayPosition", homeDisplayPositon);
                        startActivity(mainIntent);

                    }
                    else {
                        if(displayPositon.equalsIgnoreCase("0")) {
                            Intent intent = new Intent(context, GiveFeedbackWorkerReportActivity.class);
                            intent.putExtra("cLong", cLong);
                            intent.putExtra("cLat", cLat);
                            startActivity(intent);
                        } else if (displayPositon.equalsIgnoreCase("1")) {
                            Intent intent = new Intent(context, NearByAssetsMapActivity.class);
                            intent.putExtra("cLong", cLong);
                            intent.putExtra("cLat", cLat);
                            startActivity(intent);
                        }


                    }

                }

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
        }

        });
        pinview.requestPinEntryFocus();
        dialog.show();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }
}