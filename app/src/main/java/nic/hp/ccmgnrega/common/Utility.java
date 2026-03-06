package nic.hp.ccmgnrega.common;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniComputeRequest;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniComputeResponse;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniConfigRequest;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniConfigResponse;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniFunction;
import nic.hp.ccmgnrega.model.Bhashini.Config;
import nic.hp.ccmgnrega.model.Bhashini.ConfigCallPipelineTasksItem;
import nic.hp.ccmgnrega.model.Bhashini.InputData;
import nic.hp.ccmgnrega.model.Bhashini.InputItem;
import nic.hp.ccmgnrega.model.Bhashini.Language;
import nic.hp.ccmgnrega.model.Bhashini.LanguageConfig;
import nic.hp.ccmgnrega.model.Bhashini.PipelineRequestConfig;
import nic.hp.ccmgnrega.model.Bhashini.PipelineResponseConfigItem;
import nic.hp.ccmgnrega.model.Bhashini.PipelineResponseItem;
import nic.hp.ccmgnrega.model.Bhashini.PipelineTasksItem;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utility {

    private static Map<String,String> stateToLanguageMapping = new HashMap<>();

    public static String[] idHolder = new String[1];

    static {
        stateToLanguageMapping.put("AP","te");
        stateToLanguageMapping.put("AR","en");;
        stateToLanguageMapping.put("AS","hi");;
        stateToLanguageMapping.put("BH","hi");
        stateToLanguageMapping.put("CG","hi");
        stateToLanguageMapping.put("DL","hi");
        stateToLanguageMapping.put("GO","en");
        stateToLanguageMapping.put("GJ","gu");
        stateToLanguageMapping.put("HR","hi");
        stateToLanguageMapping.put("HP","hi");
        stateToLanguageMapping.put("JK","hi");
        stateToLanguageMapping.put("JH","hi");
        stateToLanguageMapping.put("KN","kn");
        stateToLanguageMapping.put("KR","ml");
        stateToLanguageMapping.put("LA","ml");
        stateToLanguageMapping.put("MP","hi");
        stateToLanguageMapping.put("MH","mr");
        stateToLanguageMapping.put("MN","en");;
        stateToLanguageMapping.put("MG","en");;
        stateToLanguageMapping.put("MZ","en");;
        stateToLanguageMapping.put("NG","hi");;
        stateToLanguageMapping.put("OR","or");
        stateToLanguageMapping.put("PO","ta");;
        stateToLanguageMapping.put("PJ","pa");
        stateToLanguageMapping.put("RJ","hi");
        stateToLanguageMapping.put("SK","hi");;
        stateToLanguageMapping.put("TN","ta");
        stateToLanguageMapping.put("TS","te");
        stateToLanguageMapping.put("TR","bn");;
        stateToLanguageMapping.put("UP","hi");
        stateToLanguageMapping.put("UT","hi");
        stateToLanguageMapping.put("WB","bn");
        stateToLanguageMapping.put("AN","hi");;
        stateToLanguageMapping.put("CH","hi");
        stateToLanguageMapping.put("DN","hi");;
        stateToLanguageMapping.put("LD","hi");;
    }

    public static String getLanguageFromState (String stateCode) {
        return stateToLanguageMapping.get(stateCode);
    }

    public static String getBhashiniLanguageCode (String userSelectedLanguage) {
        String languageCode = userSelectedLanguage.substring(0,2);
        if (languageCode.equalsIgnoreCase("ud")) {
            languageCode = "or";
        } else if (languageCode.equalsIgnoreCase("my")) {
            languageCode = "ml";
        }
        return languageCode;
    }

    public static void makeConfigCall(String sourceLanguage, String targetLanguage, ResponseCallback responseCallback) {
        BhashiniConfigRequest bhashiniConfigRequest = initializeBhashiniConfigRequest(sourceLanguage, targetLanguage);
        configCall(bhashiniConfigRequest, responseCallback);
    }

    private static BhashiniConfigRequest initializeBhashiniConfigRequest(String sourceLanguage, String targetLanguage) {
        if (sourceLanguage == null || sourceLanguage.equalsIgnoreCase(targetLanguage)) {
            return null;
        }
        BhashiniConfigRequest bhashiniConfigRequest = new BhashiniConfigRequest();

        List<ConfigCallPipelineTasksItem> pipelineTasksItems = new ArrayList<>();

        ConfigCallPipelineTasksItem pipelineTasksItem1 = new ConfigCallPipelineTasksItem();
        pipelineTasksItem1.setTaskType(BhashiniFunction.translation.name());
        LanguageConfig config1 = new LanguageConfig();
        Language language1 = new Language();
        language1.setSourceLanguage(sourceLanguage);
        language1.setTargetLanguage(targetLanguage);
        config1.setLanguage(language1);
        pipelineTasksItem1.setLanguageConfig(config1);
        pipelineTasksItems.add(pipelineTasksItem1);

        bhashiniConfigRequest.setPipelineTasks(pipelineTasksItems);

        PipelineRequestConfig pipelineRequestConfig = new PipelineRequestConfig();
        pipelineRequestConfig.setPipelineId(Constant.BHASHINI_PIPELINI_ID);
        bhashiniConfigRequest.setPipelineRequestConfig(pipelineRequestConfig);

        return bhashiniConfigRequest;
    }

    private static String configCall(BhashiniConfigRequest bhashiniConfigRequest, ResponseCallback responseCallback) {
        Call<BhashiniConfigResponse> configCall = new Retrofit.Builder()
                .baseUrl(Api.BHASHINI_CONFIG_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getBhashiniConfigResponse(bhashiniConfigRequest);

        configCall.enqueue(new Callback<BhashiniConfigResponse>() {
            @Override
            public void onResponse(Call<BhashiniConfigResponse> call, Response<BhashiniConfigResponse> response) {
                if (response.isSuccessful()) {
                    Log.v("Utility","Config call successful response received");
                    BhashiniConfigResponse bhashiniConfigResponse = response.body();
                    List<PipelineResponseConfigItem> pipelineResponseConfig = bhashiniConfigResponse.getPipelineResponseConfig();
                    String translationServiceId = null;
                    for (PipelineResponseConfigItem pipelineResponseConfigItem : pipelineResponseConfig) {
                        if (pipelineResponseConfigItem.getTaskType() != null && pipelineResponseConfigItem.getTaskType().equalsIgnoreCase(BhashiniFunction.translation.name())) {
                            if (pipelineResponseConfigItem.getConfigList() != null
                                    && !pipelineResponseConfigItem.getConfigList().isEmpty()
                                    && pipelineResponseConfigItem.getConfigList().get(0) != null) {
                                translationServiceId = pipelineResponseConfigItem.getConfigList().get(0).getServiceId();
                                Log.v("Utility","translationServiceId: " + translationServiceId);
                            }
                        }
                    }
                    idHolder[0] = translationServiceId;
                    responseCallback.onResponse(translationServiceId);
                } else {
                    Log.v("Utility","Config call successful response received");
                    responseCallback.onFailure();
                }
            }
            @Override
            public void onFailure(Call<BhashiniConfigResponse> call, Throwable t) {
                Log.v("Utility","Config call failed");
                responseCallback.onFailure();
            }
        });

        return idHolder[0];
    }

    public static void makeComputeCall (String sourceLanguage, String targetLanguage, String translationServiceId, String speech, ResponseCallback responseCallback) {
        BhashiniComputeRequest bhashiniComputeRequest = initializeBhashiniComputeRequest(sourceLanguage, targetLanguage, translationServiceId, speech);
        computeCall(bhashiniComputeRequest, responseCallback);
    }

    private static BhashiniComputeRequest initializeBhashiniComputeRequest(String sourceLanguage, String targetLanguage, String translationServiceId, String speech) {
        BhashiniComputeRequest bhashiniComputeRequest = new BhashiniComputeRequest();
        List<PipelineTasksItem> pipelineTasks = new ArrayList<>();

        PipelineTasksItem translationTask = new PipelineTasksItem();
        translationTask.setTaskType(BhashiniFunction.translation.name());
        Config translationConfig = new Config();
        Language translationLanguage = new Language();
        translationLanguage.setSourceLanguage(sourceLanguage);
        translationLanguage.setTargetLanguage(targetLanguage);
        translationConfig.setLanguage(translationLanguage);
        translationConfig.setServiceId(translationServiceId);
        translationTask.setConfig(translationConfig);
        pipelineTasks.add(translationTask);

        bhashiniComputeRequest.setPipelineTasks(pipelineTasks);

        InputData inputData = new InputData();
        InputItem inputItem = new InputItem();
        inputItem.setSource(speech);
        inputData.setInput(Arrays.asList(inputItem));
        bhashiniComputeRequest.setInputData(inputData);

        return bhashiniComputeRequest;
    }

    private static void computeCall(BhashiniComputeRequest bhashiniComputeRequest, ResponseCallback responseCallback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)
                .build();

        Call<BhashiniComputeResponse> call = new Retrofit.Builder()
                .baseUrl(Api.BHASHINI_COMPUTE_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getBhashiniComputeResponse(bhashiniComputeRequest);
        call.enqueue(new Callback<BhashiniComputeResponse>() {
            @Override
            public void onResponse(Call<BhashiniComputeResponse> call, Response<BhashiniComputeResponse> response) {
                if (response.isSuccessful()) {
                    Log.v(" Utility", "Compute call successful response received");
                    BhashiniComputeResponse bhashiniComputeResponse = response.body();
                    List<PipelineResponseItem> pipelineResponse = bhashiniComputeResponse.getPipelineResponse();
                    String translatedString = null;
                    for (PipelineResponseItem pipelineResponseItem : pipelineResponse) {
                        if (pipelineResponseItem.getTaskType() != null && pipelineResponseItem.getTaskType().equalsIgnoreCase(BhashiniFunction.translation.name())) {
                            if (pipelineResponseItem.getOutput() != null && !pipelineResponseItem.getOutput().isEmpty()) {
                                translatedString = pipelineResponseItem.getOutput().get(0).getTarget();
                                responseCallback.onResponse(translatedString);
                                break;
                            }
                        }
                    }
                } else {
                    Log.v("Utility", "Compute call failed response received");
                    responseCallback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BhashiniComputeResponse> call, Throwable t) {
                Log.v("Utility", "Compute call failed: " + t.getMessage());
                responseCallback.onFailure();
            }
        });
    }

    public static Call<BhashiniConfigResponse> makeTtsConfigCall(String sourceLanguage, TtsResponseCallback responseCallback) {
        BhashiniConfigRequest bhashiniConfigRequest = initializeTtsBhashiniConfigRequest(sourceLanguage);
        return ttsConfigCall(bhashiniConfigRequest, responseCallback);
    }

    private static BhashiniConfigRequest initializeTtsBhashiniConfigRequest(String sourceLanguage) {
        if (sourceLanguage == null) {
            return null;
        }
        BhashiniConfigRequest bhashiniConfigRequest = new BhashiniConfigRequest();

        List<ConfigCallPipelineTasksItem> pipelineTasksItems = new ArrayList<>();

        ConfigCallPipelineTasksItem pipelineTasksItem1 = new ConfigCallPipelineTasksItem();
        pipelineTasksItem1.setTaskType(BhashiniFunction.tts.name());
        LanguageConfig config1 = new LanguageConfig();
        Language language1 = new Language();
        language1.setSourceLanguage(sourceLanguage);
        config1.setLanguage(language1);
        pipelineTasksItem1.setLanguageConfig(config1);
        pipelineTasksItems.add(pipelineTasksItem1);

        bhashiniConfigRequest.setPipelineTasks(pipelineTasksItems);

        PipelineRequestConfig pipelineRequestConfig = new PipelineRequestConfig();
        pipelineRequestConfig.setPipelineId(Constant.BHASHINI_PIPELINI_ID);
        bhashiniConfigRequest.setPipelineRequestConfig(pipelineRequestConfig);

        return bhashiniConfigRequest;
    }

    private static Call<BhashiniConfigResponse> ttsConfigCall(BhashiniConfigRequest bhashiniConfigRequest, TtsResponseCallback responseCallback) {
        Call<BhashiniConfigResponse> configCall = new Retrofit.Builder()
                .baseUrl(Api.BHASHINI_CONFIG_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getBhashiniConfigResponse(bhashiniConfigRequest);

        configCall.enqueue(new Callback<BhashiniConfigResponse>() {
            @Override
            public void onResponse(Call<BhashiniConfigResponse> call, Response<BhashiniConfigResponse> response) {
                if (response.isSuccessful()) {
                    Log.v("Utility","TTS config call successful response received");
                    BhashiniConfigResponse bhashiniConfigResponse = response.body();
                    List<PipelineResponseConfigItem> pipelineResponseConfig = bhashiniConfigResponse.getPipelineResponseConfig();
                    String ttsServiceId = null;
                    String ttsAudioVoice = null;
                    for (PipelineResponseConfigItem pipelineResponseConfigItem : pipelineResponseConfig) {
                        if (pipelineResponseConfigItem.getTaskType() != null && pipelineResponseConfigItem.getTaskType().equalsIgnoreCase(BhashiniFunction.tts.name())) {
                            if (pipelineResponseConfigItem.getConfigList() != null
                                    && !pipelineResponseConfigItem.getConfigList().isEmpty()
                                    && pipelineResponseConfigItem.getConfigList().get(0) != null) {
                                ttsServiceId = pipelineResponseConfigItem.getConfigList().get(0).getServiceId();
                                Log.v("Utility","ttsServiceId: " + ttsServiceId);
                                List<String> supportedVoices = pipelineResponseConfigItem.getConfigList().get(0).getSupportedVoices();
                                if (supportedVoices != null && !supportedVoices.isEmpty()) {
                                    ttsAudioVoice = supportedVoices.get(0);
                                }
                            }
                        }
                    }
                    responseCallback.onResponse(ttsServiceId, ttsAudioVoice);
                } else {
                    Log.v("Utility","TTS config call failed response received");
                    responseCallback.onFailure();
                }
            }
            @Override
            public void onFailure(Call<BhashiniConfigResponse> call, Throwable t) {
                Log.v("Utility","Config call failed: " + t.getMessage());
                responseCallback.onFailure();
            }
        });

        return configCall;
    }

    public static Call<BhashiniComputeResponse> makeTtsComputeCall (String sourceLanguage, String ttsServiceId, String ttsAudioVoice, String speech, ResponseCallback responseCallback) {
        BhashiniComputeRequest bhashiniComputeRequest = initializeTtsBhashiniComputeRequest(sourceLanguage, ttsServiceId, ttsAudioVoice, speech);
        return ttsComputeCall(bhashiniComputeRequest, responseCallback);
    }

    private static BhashiniComputeRequest initializeTtsBhashiniComputeRequest(String sourceLanguage, String ttsServiceId, String ttsAudioVoice, String speech) {
        BhashiniComputeRequest bhashiniComputeRequest = new BhashiniComputeRequest();
        List<PipelineTasksItem> pipelineTasks = new ArrayList<>();

        PipelineTasksItem ttsTask = new PipelineTasksItem();
        ttsTask.setTaskType(BhashiniFunction.tts.name());
        Config ttsConfig = new Config();
        Language ttsLanguage = new Language();
        ttsLanguage.setSourceLanguage(sourceLanguage);
        ttsConfig.setLanguage(ttsLanguage);
        ttsConfig.setServiceId(ttsServiceId);
        ttsConfig.setGender(ttsAudioVoice);
        ttsTask.setConfig(ttsConfig);
        pipelineTasks.add(ttsTask);
        bhashiniComputeRequest.setPipelineTasks(pipelineTasks);

        InputData inputData = new InputData();
        InputItem inputItem = new InputItem();
        inputItem.setSource(speech);
        inputData.setInput(Arrays.asList(inputItem));
        bhashiniComputeRequest.setInputData(inputData);

        return bhashiniComputeRequest;
    }

    private static Call<BhashiniComputeResponse> ttsComputeCall(BhashiniComputeRequest bhashiniComputeRequest, ResponseCallback responseCallback) {
       /* OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build(); */
        OkHttpClient okHttpClient =  UnsafeOkHttpClient.getUnsafeOkHttpClient();



        Call<BhashiniComputeResponse> call = new Retrofit.Builder()
                .baseUrl(Api.BHASHINI_COMPUTE_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getBhashiniComputeResponse(bhashiniComputeRequest);

        call.enqueue(new Callback<BhashiniComputeResponse>() {
            @Override
            public void onResponse(Call<BhashiniComputeResponse> call, Response<BhashiniComputeResponse> response) {
                if (response.isSuccessful()) {
                    Log.v(" Utility", "TTS compute call successful response received");
                    BhashiniComputeResponse bhashiniComputeResponse = response.body();
                    List<PipelineResponseItem> pipelineResponse = bhashiniComputeResponse.getPipelineResponse();
                    String base64FormattedString = null;
                    for (PipelineResponseItem pipelineResponseItem : pipelineResponse) {
                        if (pipelineResponseItem.getTaskType() != null && pipelineResponseItem.getTaskType().equalsIgnoreCase(BhashiniFunction.tts.name())) {
                            if (pipelineResponseItem.getAudio() != null && !pipelineResponseItem.getAudio().isEmpty()) {
                                base64FormattedString = pipelineResponseItem.getAudio().get(0).getAudioContent();
                                responseCallback.onResponse(base64FormattedString);
                                break;
                            }
                        }
                    }
                } else {
                    Log.v(" Utility", "TTS compute call failed response response");
                    responseCallback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BhashiniComputeResponse> call, Throwable t) {
                Log.v(" Utility", "TTS compute call failed");
                responseCallback.onFailure();
            }
        });

        return call;
    }

    public static String removeQuotations(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        if (input.startsWith("'") && input.endsWith("'")) {
            return input.substring(1, input.length() - 1);
        } else {
            return input;
        }
    }

    public static String removeSpecialCharacters(String input) {
        return input.replaceAll("[@_!#$%^&*()<>?/\\\\|}{~:]", " ");
    }
    public static String replaceMultiplePeriods(String input, String pattern) {
        input = input.replaceAll("[@_!#$%^&*()<>?/\\\\|}{~:]", " ");
        String escapedPattern = Pattern.quote(pattern);
        String regex = "(?:" + escapedPattern + "\\s*)+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        String replacedString = m.replaceAll(pattern + " ");
        return  replacedString;
    }

    public static boolean translationAllowed(Context context) {
        if (context != null) {
            String userSelectedLanguage = MySharedPref.getAppLangCode(context);
            String userSelectedLanguageCode = Utility.getBhashiniLanguageCode(userSelectedLanguage);
            if (JobCardDataAccess.getJobCardId() != null) {
                String stateCode = JobCardDataAccess.getJobCardId().substring(0,2).toUpperCase();
                String regionalLanguageCode = Utility.getLanguageFromState(stateCode);
                if (userSelectedLanguageCode.equalsIgnoreCase(Constant.ENGLISH_LANGUAGE_CODE)
                        || userSelectedLanguageCode.equalsIgnoreCase(regionalLanguageCode)) {
                    return true;
                }
            }
        }
        return false;
    }
}
