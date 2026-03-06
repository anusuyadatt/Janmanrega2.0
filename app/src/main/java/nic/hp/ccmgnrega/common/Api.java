package nic.hp.ccmgnrega.common;

import java.util.List;

import nic.hp.ccmgnrega.SplashActivity;
import nic.hp.ccmgnrega.model.AssetComplaintStatusData;
import nic.hp.ccmgnrega.model.BlockModel;
import nic.hp.ccmgnrega.model.CategoryData;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniComputeRequest;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniComputeResponse;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniConfigRequest;
import nic.hp.ccmgnrega.model.Bhashini.BhashiniConfigResponse;
import nic.hp.ccmgnrega.model.DistrictModel;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.JobComplaintStatusData;
import nic.hp.ccmgnrega.model.PanchayatModel;
import nic.hp.ccmgnrega.model.ResponseData;
import nic.hp.ccmgnrega.model.StateData;
import nic.hp.ccmgnrega.model.StateModel;
import nic.hp.ccmgnrega.model.SubCategoryData;
import nic.hp.ccmgnrega.model.TokenData;
import nic.hp.ccmgnrega.model.UploadAssetFeedbackModel;
import nic.hp.ccmgnrega.model.UploadEkycExemptionModel;
import nic.hp.ccmgnrega.model.UploadWorkerQueryModel;

import nic.hp.ccmgnrega.model.VillageModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

   // String BASE_URL = "https://nregarep2.nic.in/";
    String GET_PLAYSTORE_APP_VERSION_URL = "https://mobileappshp.nic.in/MyDiary/MobileAppVersions.svc/GetAppVersion?";
    /* BHASHINI Urls*/
    String BHASHINI_COMPUTE_BASE_URL = "https://dhruva-api.bhashini.gov.in/services/inference/";
    String BHASHINI_CONFIG_BASE_URL = "https://meity-auth.ulcacontrib.org/ulca/apis/v0/model/";
    /* BHASHINI Urls*/
    /* Bhuvan Urls*/
    String ASSET_XY_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega_v1/nrega2.php?x=";//Near By Assets Module  (getAssetMap) New
  //  String ASSET_XY_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega/nrega2.php?x=";//Near By Assets Module  (getAssetMap) OLD
    //  String ASSET_XY_URL = " https://bhuvan-staging1.nrsc.gov.in/scan_ve/venkatesh/janmnrega/nrega2.php?x=";//Near By Assets Module  (getAssetMap)
    String PANCHAYAT_ASSET_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega_v1/webservice.php?gpcode=";//Search Asset Module (get Asset List by GP Code) New
   // String PANCHAYAT_ASSET_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega/webservice.php?gpcode=";//Search Asset Module (get Asset List by GP Code) Old
    String ASSET_NEAR_BY_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega_v1/nrega_v1.php?pc="; //Search Asset Module (getAssetMap) New
    //String ASSET_NEAR_BY_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega/nrega_v1.php?pc="; //Search Asset Module (getAssetMap) OLD
    //  String ASSET_NEAR_BY_URL = "https://bhuvan-staging1.nrsc.gov.in/scan_ve/venkatesh/janmnrega/nrega_v1.php?pc="; //Search Asset Module (getAssetMap)
    String ASSET_NEAR_BY_XY_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega_v1/bufferassets.php?xx=";//Send Feedback Module (get Asset List with in 20 meter) New
 //   String ASSET_NEAR_BY_XY_URL = "https://bhuvan-app2.nrsc.gov.in/janmnrega/bufferassets.php?xx=";//Send Feedback Module (get Asset List with in 20 meter) OLD
    //  String ASSET_NEAR_BY_XY_URL = "https://bhuvan-staging1.nrsc.gov.in/scan_ve/venkatesh/janmnrega/get-asset-list.php?buff=20&x=";//Send Feedback Module (get Asset List with in 20 meter)
    String ASSET_DETAIL_URL = "https://bhuvan-app2.nrsc.gov.in/mgnrega/usrtasks/nrega_phase2/get/getDetailsnic.php?sno=";//Send Feedback Module (get particular asset detail)

    /* Bhuvan Urls*/
    @POST("pipeline")
    @Headers({
            "Authorization:zyXImKodGxEISfjf4Rv3Tp8eaTq43ETwgWOczffqT1wg_7zZbRxrNwqgxbif0921",
            "User-Agent: Thunder Client (https://www.thunderclient.com)"
    })
    Call<BhashiniComputeResponse> getBhashiniComputeResponse(@Body BhashiniComputeRequest bhashiniComputeRequest);

    @POST("getModelsPipeline")
    @Headers({
            "userID:fa14838ac3e94e069cfec1cc1b00ce62",
            "ulcaApiKey:374d5413d3-9410-4462-8a02-9c3a966da30a"
    })
    Call<BhashiniConfigResponse> getBhashiniConfigResponse(@Body BhashiniConfigRequest bhashiniConfigRequest);


    ////  Nrega Api method start
    String grant_type = "password";

    @FormUrlEncoded
    @POST("nregaapi/token")
    Call<TokenData> getToken(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @POST("nregaapi/api/JanManrega")
    Call<JobCardData> getJobCardData(@Field("Reg_no") String regNo);

    @POST("nregaapi/api/Janmanrega_Feedback")
    Call<ResponseData> uploadFeedbackData(@Body UploadWorkerQueryModel uploadWorkerQueryModel);

    @FormUrlEncoded
    @POST("netnrega/StateServices/Janmanrega_categoryService.svc/GetCat")
    Call<CategoryData> getCategoryData(@Field("state_code") String stateCode);

    @FormUrlEncoded
    @POST("netnrega/StateServices/Janmanrega_categoryService.svc/GetsubCat")
    Call<SubCategoryData> getSubCategoryData(@Field("state_code") String stateCode, @Field("complaint_cat_code") String catCode);


    @POST("netnrega/StateServices/Janmanrega_categoryService.svc/GetCount")
    Call<String> getCount();


    @POST("netnrega/locationmaster_JASON.svc/getstate")
    Call<List<StateModel>> getStateList();


    @FormUrlEncoded
    @POST("netnrega/locationmaster_JASON.svc/getDist")
    Call<List<DistrictModel>> getDistrictList(@Field("stCode") String stateCode, @Field("requestid") String requestId, @Field("finyear") String finYear);

    @FormUrlEncoded
    @POST("netnrega/locationmaster_JASON.svc/getBlk")
    Call<List<BlockModel>> getBlockList(@Field("stCode") String stateCode, @Field("requestid") String requestId, @Field("finyear") String finYear);


    @FormUrlEncoded
    @POST("netnrega/locationmaster_JASON.svc/getPanch")
    Call<List<PanchayatModel>> getPanchayatList(@Field("bCode") String blockCode, @Field("finyear") String finYear);

    @FormUrlEncoded
    @POST("netnrega/locationmaster_JASON.svc/getVill")
    Call<List<VillageModel>> getVillageList(@Field("pCode") String blockCode, @Field("finyear") String finYear);

    @FormUrlEncoded
    @POST("nregaapi/api/janmanrega_jobcard_status")
    Call<List<JobComplaintStatusData>> getJobComplaintStatusData(@Field("st_code") String stateCode, @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("nregaapi/api/janmanrega_asset_status")
    Call<List<AssetComplaintStatusData>> getAssetComplaintStatus(@Field("st_code") String stateCode, @Field("mobile_no") String mobile_no);

    @POST("nregaapi/api/saveassetfeedback")
    Call<ResponseData> uploadAssetFeedbackModel(@Body UploadAssetFeedbackModel uploadWorkerQueryModel);



    @POST("Netnrega/StateServices/LocationMaster_lgd.svc/states")
    Call<ResponseBody> getLgdStateList();

    @FormUrlEncoded
    @POST("Netnrega/StateServices/LocationMaster_lgd.svc/districts")
    Call<ResponseBody> getLgdDistrictList(@Field("lgd_st_code") String lgd_st_code, @Field("fin_yr") String fin_yr);

    @FormUrlEncoded
    @POST("Netnrega/StateServices/LocationMaster_lgd.svc/blocks")
    Call<ResponseBody> getLgdBlockList(@Field("lgd_st_code") String lgd_st_code, @Field("lgd_Dist_code") String lgd_Dist_code, @Field("fin_yr") String fin_yr);


    @FormUrlEncoded
    @POST("Netnrega/StateServices/LocationMaster_lgd.svc/panchayats")
    Call<ResponseBody> getLgdPanchayatList(@Field("lgd_st_code") String lgd_st_code, @Field("lgd_Dist_code") String lgd_Dist_code, @Field("lgd_blk_code") String lgd_blk_code, @Field("fin_yr") String fin_yr);

    @FormUrlEncoded
    @POST("docupload/api/workcode")
    @Headers({
            "username:e55e61011dd8002cf65279fd8d7f302aa2d107ddf1b80859c7b0d612e4ee23f2fdb6ec5d0cb59e5a7006eebc5e999d8aa77d2ad1dd01404c3916f5e64cd376a9",
            "password:1d9364cd43bfd10e2f7e75323ad5280c8d3e217196994350290b8e30b7443505970168404b944f3860085fc0cc8fab3af2ddf15dd2f9ed5ce07bc96b037ee45a"
    })
    Call<ResponseBody> getWorkList(@Field("panchayat_code") String panchayat_code);

    @FormUrlEncoded
    @POST("docupload/api/MustrollNo")
    @Headers({
            "username:e55e61011dd8002cf65279fd8d7f302aa2d107ddf1b80859c7b0d612e4ee23f2fdb6ec5d0cb59e5a7006eebc5e999d8aa77d2ad1dd01404c3916f5e64cd376a9",
            "password:1d9364cd43bfd10e2f7e75323ad5280c8d3e217196994350290b8e30b7443505970168404b944f3860085fc0cc8fab3af2ddf15dd2f9ed5ce07bc96b037ee45a"
    })
    Call<ResponseBody> getMusterList(@Field("panchayat_code") String panchayat_code,@Field("work_code") String work_code);

    @FormUrlEncoded
    @POST("docupload/api/MustrollNo")
    @Headers({
            "username:e55e61011dd8002cf65279fd8d7f302aa2d107ddf1b80859c7b0d612e4ee23f2fdb6ec5d0cb59e5a7006eebc5e999d8aa77d2ad1dd01404c3916f5e64cd376a9",
            "password:1d9364cd43bfd10e2f7e75323ad5280c8d3e217196994350290b8e30b7443505970168404b944f3860085fc0cc8fab3af2ddf15dd2f9ed5ce07bc96b037ee45a"
    })
    Call<ResponseBody> getJobCardList(@Field("panchayat_code") String panchayat_code,@Field("work_code") String work_code,@Field("msr_no") String msr_no);
    @FormUrlEncoded
    @POST("docupload/api/MustrollNo")
    @Headers({
            "username:e55e61011dd8002cf65279fd8d7f302aa2d107ddf1b80859c7b0d612e4ee23f2fdb6ec5d0cb59e5a7006eebc5e999d8aa77d2ad1dd01404c3916f5e64cd376a9",
            "password:1d9364cd43bfd10e2f7e75323ad5280c8d3e217196994350290b8e30b7443505970168404b944f3860085fc0cc8fab3af2ddf15dd2f9ed5ce07bc96b037ee45a"
    })
    Call<ResponseBody> getApplicantList(@Field("panchayat_code") String panchayat_code,@Field("work_code") String work_code,@Field("msr_no") String msr_no,@Field("reg_no") String reg_no);



    @POST("docupload/api/Janmanrega_Image")
    @Headers({
            "username:e55e61011dd8002cf65279fd8d7f302aa2d107ddf1b80859c7b0d612e4ee23f2fdb6ec5d0cb59e5a7006eebc5e999d8aa77d2ad1dd01404c3916f5e64cd376a9",
            "password:1d9364cd43bfd10e2f7e75323ad5280c8d3e217196994350290b8e30b7443505970168404b944f3860085fc0cc8fab3af2ddf15dd2f9ed5ce07bc96b037ee45a"
    })
    Call<ResponseBody> uploadEkycExemption(@Body UploadEkycExemptionModel uploadEkycExemptionModel);
}
