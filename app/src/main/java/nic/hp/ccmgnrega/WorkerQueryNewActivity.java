package nic.hp.ccmgnrega;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.adapter.CategoryAdapter;
import nic.hp.ccmgnrega.adapter.StateAdapter;
import nic.hp.ccmgnrega.adapter.SubCategoryAdapter;
import nic.hp.ccmgnrega.adapter.WorkerAdapter;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.Helper;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.common.RedAsterisk;
import nic.hp.ccmgnrega.common.TokenInterceptor;
import nic.hp.ccmgnrega.fragment.BaseFragment;
import nic.hp.ccmgnrega.fragment.SearchFragment;
import nic.hp.ccmgnrega.model.CategoryData;
import nic.hp.ccmgnrega.model.CategoryModel;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.JobCardDetail;
import nic.hp.ccmgnrega.model.ResponseData;
import nic.hp.ccmgnrega.model.StateModel;
import nic.hp.ccmgnrega.model.SubCategoryData;
import nic.hp.ccmgnrega.model.SubCategoryModel;
import nic.hp.ccmgnrega.model.TokenData;
import nic.hp.ccmgnrega.model.UploadWorkerQueryModel;
import nic.hp.ccmgnrega.model.WorkerModel;
import nic.hp.ccmgnrega.webview.BasicAuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerQueryNewActivity extends AppCompatActivity {
    TextInputEditText etComplaintDesc, etName, etMobile, etOtp;
    TextInputLayout etComplaintDescLayout,etNameLayout,etMobileLayout,etOtpLayout;
    LinearLayout llWorkerQueryHeader, linearSubSection, llComplaintSubCategory;
    Spinner spinnerState, spinnerWorker, spinnerComplaintCategory, spinnerComplaintSubCategory;

    protected ConnectionDetector connectionDetector;
    BaseFragment baseFragment;
    String  selectedStateCode,selectedDistrictCode,selectedBlockCode,selectedPanchayatCode, selectedWorkerName, selectedWorkerCode, selectedCatName, selectedCatCode, selectedSubCatName, selectedSubCatCode;

    EditText jobCardNumberInput;
    public static JobCardData jobCardData;
    public static ResponseData responseData;
    public static CategoryData categoryData;
    public static CategoryModel categoryModel;
    public static SubCategoryData subCategoryData;
    public static SubCategoryModel subCategoryModel;

    TextView tvJobCardNumber, tvState, tvWorkerName, tvComplaintCategory, tvComplaintSubCategory,heading;
    List<JobCardDetail> jobCardDetailList;
    List<CategoryModel> categoryList;
    List<SubCategoryModel> subCategoryList;
    ArrayList<WorkerModel> alApplicantWorker = new ArrayList<>();
    ArrayList<CategoryModel> alCategory = new ArrayList<>();
    ArrayList<SubCategoryModel> alSubCategory = new ArrayList<>();
    ProgressDialog pd;
    Context context;
    int statusCode = 0;
    String status, statusMessage;

    String strJobCardNumber = "";
    Handler handler;
    Runnable runnable;
    Button btnSubmit, btnGetOtp;

    String strJobCardDataMode = "";
    String strActivityCode = "09-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_query_new);
        context = getApplicationContext();
        findById();
        setMandatoryMark();
        loadActionBar();
        baseFragment = new BaseFragment();
        connectionDetector = new ConnectionDetector(WorkerQueryNewActivity.this);
        heading.setText(" " + getApplicationContext().getString(R.string.workers_query));
        pd = new ProgressDialog(WorkerQueryNewActivity.this);
        pd.setMessage(context.getString(R.string.please_wait));
        pd.setCanceledOnTouchOutside(false);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                btnGetOtp.setText(context.getString(R.string.resend));
                btnGetOtp.setClickable(true);
                btnGetOtp.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.green_color, getApplicationContext().getTheme()));
                etMobile.setEnabled(true);
                etOtp.setEnabled(true);
            }
        };
        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (args.getString("strJobCardNumber") != null)
                strJobCardNumber = args.getString("strJobCardNumber");
                 strJobCardDataMode = args.getString("strJobCardDataMode");
        }
        tvJobCardNumber.setText(context.getString(R.string.job_card) + ": " + strJobCardNumber);
        if (strJobCardDataMode.equalsIgnoreCase("JCF")) {
            jobCardDetailList = SearchFragment.jobCardData.getJobCardDetails();
            selectedStateCode = SearchFragment.jobCardData.getState_code();
            selectedDistrictCode = SearchFragment.jobCardData.getDistrict_code();
            selectedBlockCode = SearchFragment.jobCardData.getBlock_code();
            selectedPanchayatCode = SearchFragment.jobCardData.getPanchayat_code();
        }
        else {
            jobCardDetailList = SearchByJobCardActivity.jobCardData.getJobCardDetails();
            selectedStateCode = SearchByJobCardActivity.jobCardData.getState_code();
            selectedDistrictCode = SearchByJobCardActivity.jobCardData.getDistrict_code();
            selectedBlockCode = SearchByJobCardActivity.jobCardData.getBlock_code();
            selectedPanchayatCode = SearchByJobCardActivity.jobCardData.getPanchayat_code();

        }
        if (jobCardDetailList != null && jobCardDetailList.size() > 0) {
            alApplicantWorker.clear();
            alApplicantWorker.add(0, new WorkerModel(getApplicationContext().getString(R.string.select)));
            for (int i = 0; i < jobCardDetailList.size(); i++) {
                JobCardDetail jobCardDetail = jobCardDetailList.get(i);
                alApplicantWorker.add(new WorkerModel(jobCardDetail.getApplicantID(), jobCardDetail.getApplicantName()));
            }
            populateSpinnerWorker(alApplicantWorker, spinnerWorker);
        }

        getCategoryData(selectedStateCode);
        spinnerWorker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedWorkerCode = "";
                    selectedWorkerName = "";
                } else {
                    WorkerModel workerModel = ((WorkerAdapter) spinnerWorker.getAdapter()).getItem(position);
                    selectedWorkerCode = workerModel.getApplicantId();
                    selectedWorkerName = workerModel.getApplicantName();

                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerComplaintCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedCatCode = "";
                    selectedCatName = "";
                    selectedSubCatCode = "";
                    selectedSubCatName = "";
                    spinnerComplaintSubCategory.setAdapter(null);
                    tvComplaintSubCategory.setVisibility(View.GONE);
                    llComplaintSubCategory.setVisibility(View.GONE);
                } else {
                    categoryModel = ((CategoryAdapter) spinnerComplaintCategory.getAdapter()).getItem(position);
                    selectedCatCode = categoryModel.getCategoryCode();
                    selectedCatName = categoryModel.getCategoryName();
                    selectedSubCatCode = "";
                    selectedSubCatName = "";
                    spinnerComplaintSubCategory.setAdapter(null);
                    if (Integer.parseInt(selectedCatCode) == 0) {
                        tvComplaintSubCategory.setVisibility(View.GONE);
                        llComplaintSubCategory.setVisibility(View.GONE);
                    } else {
                        tvComplaintSubCategory.setVisibility(View.VISIBLE);
                        llComplaintSubCategory.setVisibility(View.VISIBLE);
                        getSubCategoryData(selectedStateCode, selectedCatCode);
                    }
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerComplaintSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedSubCatCode = "";
                    selectedSubCatName = "";
                } else {
                    subCategoryModel = ((SubCategoryAdapter) spinnerComplaintSubCategory.getAdapter()).getItem(position);
                    selectedSubCatCode = subCategoryModel.getSubCategoryCode();
                    selectedSubCatName = subCategoryModel.getSubCategoryName();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMobile.getText().toString().trim().isEmpty()) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.get_otp) + " " + context.getString(R.string.warning), context.getString(R.string.please_fill_mobile), strActivityCode + "025");
                } else if (etMobile.getText().toString().trim().length() < 10) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.get_otp) + " " + context.getString(R.string.warning), context.getString(R.string.please_fill_valid_mobile), strActivityCode + "024");
                } else {
                    if (connectionDetector.isConnectingToInternet()) {
                        new GetOtpFromServer().execute();
                        btnGetOtp.setClickable(false);
                        btnGetOtp.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.grey, getApplicationContext().getTheme()));
                        try {
                            handler.postDelayed(runnable, 15 * 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.get_otp) + " " + context.getString(R.string.warning), context.getString(R.string.no_internet), strActivityCode + "023");

                    }
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          if (selectedWorkerCode.isEmpty())
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_worker_name), strActivityCode + "03");
                else if (selectedCatCode.isEmpty())
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_category), strActivityCode + "04");
                else if (selectedSubCatCode.isEmpty() && (Integer.parseInt(selectedCatCode) != 0))
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_select_subcategory), strActivityCode + "05");
                else if (etComplaintDesc.getText().toString().trim().isEmpty()) {
                    etComplaintDesc.setFocusable(true);
                    etComplaintDesc.requestFocus();
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_complaint_desc), strActivityCode + "06");
                } else if (etName.getText().toString().trim().isEmpty()) {
                    etName.setFocusable(true);
                    etName.requestFocus();
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_name), strActivityCode + "07");
                } else if (etMobile.getText().toString().trim().isEmpty()) {

                    etMobile.setFocusable(true);
                    etMobile.requestFocus();
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_mobile), strActivityCode + "08");
                } else if (etMobile.getText().toString().trim().length() < 10) {
                    etMobile.setFocusable(true);
                    etMobile.requestFocus();

                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_valid_mobile), strActivityCode + "09");
                } else if (etOtp.getText().toString().trim().isEmpty()) {
                    etOtp.setFocusable(true);
                    etOtp.requestFocus();

                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_otp), strActivityCode + "010");
                } else {
                    if (connectionDetector.isConnectingToInternet()) {
                        final Dialog dialogAlert = new Dialog(WorkerQueryNewActivity.this);
                        MyAlert.dialogForCancelOk
                                (context, R.mipmap.icon_info, context.getString(R.string.submission_alert),
                                        context.getString(R.string.submit_confirmation),
                                        dialogAlert,
                                        context.getString(R.string.submit),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogAlert.dismiss();
                                                etOtp.setEnabled(false);
                                                new VerifyOtpByServer(etMobile.getText().toString(), etOtp.getText().toString(), MySharedPref.getOtpID(context)).execute();
                                            }
                                        },
                                        context.getString(R.string.cancel),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogAlert.dismiss();
                                            }
                                        },
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogAlert.dismiss();
                                            }
                                        }, strActivityCode + "019");
                    }
                    else
                        MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), getApplicationContext().getString(R.string.no_internet), strActivityCode + "018");

                }
            }
        });

    }

    private void setMandatoryMark() {

        RedAsterisk.setAstrikOnTextView(tvWorkerName, context.getString(R.string.worker_name));
        RedAsterisk.setAstrikOnTextView(tvComplaintCategory, context.getString(R.string.complaint_category));
        RedAsterisk.setAstrikOnTextView(tvComplaintSubCategory, context.getString(R.string.complaint_sub_category));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etComplaintDescLayout, context.getString(R.string.complaint_description));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etNameLayout, context.getString(R.string.your_name));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etMobileLayout, context.getString(R.string.mobile_number));
        RedAsterisk.setAstrikOnTextInputTextInputLayout(etOtpLayout, "OTP");
        /* RedAsterisk.setAstrikOnTextView(tvState, context.getString(R.string.state));
        RedAsterisk.setAstrikOnTextInputEditText(etComplaintDesc, context.getString(R.string.complaint_description));
        RedAsterisk.setAstrikOnTextInputEditText(etName, context.getString(R.string.your_name));
        RedAsterisk.setAstrikOnTextInputEditText(etMobile, context.getString(R.string.mobile_number));
        RedAsterisk.setAstrikOnTextInputEditText(etOtp, "OTP");*/



    }


    private void findById() {
        heading = findViewById(R.id.heading);
        tvJobCardNumber = findViewById(R.id.tvJobCardNumber);
        tvState = findViewById(R.id.tvState);
        tvWorkerName = findViewById(R.id.tvWorkerName);
        tvComplaintCategory = findViewById(R.id.tvComplaintCategory);
        tvComplaintSubCategory = findViewById(R.id.tvComplaintSubCategory);
        spinnerState = findViewById(R.id.spinnerState);
        jobCardNumberInput = findViewById(R.id.searchJobCard);
        spinnerWorker = findViewById(R.id.spinnerWorker);
        spinnerComplaintCategory = findViewById(R.id.spinnerComplaintCategory);
        spinnerComplaintSubCategory = findViewById(R.id.spinnerComplaintSubCategory);
        etComplaintDesc = findViewById(R.id.etComplaintDesc);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etOtp = findViewById(R.id.etOtp);
      //  llWorkerQueryHeader = findViewById(R.id.llWorkerQueryHeader);
        linearSubSection = findViewById(R.id.linearSubSection);
        llComplaintSubCategory = findViewById(R.id.llComplaintSubCategory);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        btnSubmit = findViewById(R.id.btnSubmit);
        etComplaintDescLayout = findViewById(R.id.etComplaintDescLayout);
        etNameLayout = findViewById(R.id.etNameLayout);
        etMobileLayout = findViewById(R.id.etMobileLayout);
        etOtpLayout = findViewById(R.id.etOtpLayout);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    public void loadActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_white_back);
        @SuppressWarnings("deprecation")
        Drawable d = getResources().getDrawable(R.drawable.header_bg2);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + getApplicationContext().getString(R.string.app_name) + "</b></font></center>"));
        getSupportActionBar().setIcon(R.mipmap.ic_india_emblem);

    }



    private void getCategoryData(String stateCode) {
        if (connectionDetector.isConnectingToInternet()) {
            if (!pd.isShowing()) pd.show();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                    .build();

            Call<CategoryData> call = new Retrofit.Builder()
                    .baseUrl(Helper.getBaseURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(Api.class)
                    .getCategoryData(stateCode);

            call.enqueue(new Callback<CategoryData>() {
                @Override
                public void onResponse(Call<CategoryData> call, Response<CategoryData> response) {
                    if (response.isSuccessful()) {
                        categoryData = response.body();
                        categoryList = categoryData.getCategoryList();
                        if (categoryList != null && categoryList.size() > 0) {
                            alCategory.clear();
                            alCategory = (ArrayList<CategoryModel>) categoryList;
                            alCategory.add(0, new CategoryModel(getApplicationContext().getString(R.string.select)));
                            populateSpinnerCategory(alCategory, spinnerComplaintCategory);
                        } else {
                            Toast toast = Toast.makeText(WorkerQueryNewActivity.this, categoryData.getRemarks(), Toast.LENGTH_LONG);
                            toast.show();
                            spinnerState.setSelection(0);
                        }
                        if (pd.isShowing()) pd.dismiss();
                    } else {
                        Toast toast = Toast.makeText(WorkerQueryNewActivity.this, context.getString(R.string.network_req_failed), Toast.LENGTH_LONG);
                        toast.show();
                        spinnerState.setSelection(0);
                        if (pd.isShowing()) pd.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<CategoryData> call, Throwable t) {
                    Toast toast = Toast.makeText(WorkerQueryNewActivity.this, context.getString(R.string.network_req_failed), Toast.LENGTH_LONG);
                    toast.show();
                    spinnerState.setSelection(0);
                    if (pd.isShowing()) pd.dismiss();
                }
            });

        } else {
            spinnerState.setSelection(0);
            Toast.makeText(WorkerQueryNewActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void getSubCategoryData(String stateCode, String catCode) {
        if (connectionDetector.isConnectingToInternet()) {
            if (!pd.isShowing()) pd.show();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                    .build();

            Call<SubCategoryData> call = new Retrofit.Builder()
                    .baseUrl(Helper.getBaseURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(Api.class)
                    .getSubCategoryData(stateCode, catCode);

            call.enqueue(new Callback<SubCategoryData>() {
                @Override
                public void onResponse(Call<SubCategoryData> call, Response<SubCategoryData> response) {
                    if (response.isSuccessful()) {
                        subCategoryData = response.body();
                        subCategoryList = subCategoryData.getSubCategoryList();
                        if (subCategoryList != null && subCategoryList.size() > 0) {
                            alSubCategory.clear();
                            alSubCategory = (ArrayList<SubCategoryModel>) subCategoryList;
                            alSubCategory.add(0, new SubCategoryModel(getApplicationContext().getString(R.string.select)));
                            populateSpinnerSubCategory(alSubCategory, spinnerComplaintSubCategory);
                        } else {
                            Toast toast = Toast.makeText(WorkerQueryNewActivity.this, categoryData.getRemarks(), Toast.LENGTH_LONG);
                            toast.show();
                            spinnerComplaintCategory.setSelection(0);

                        }
                        if (pd.isShowing()) pd.dismiss();
                    } else {
                        String message = context.getString(R.string.network_req_failed);
                        Toast toast = Toast.makeText(WorkerQueryNewActivity.this, message + "\n" + response.message(), Toast.LENGTH_LONG);
                        toast.show();
                        spinnerComplaintCategory.setSelection(0);
                        if (pd.isShowing()) pd.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<SubCategoryData> call, Throwable t) {
                    String message = context.getString(R.string.network_req_failed);
                    Toast toast = Toast.makeText(WorkerQueryNewActivity.this, message + "\n" + t.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    spinnerComplaintCategory.setSelection(0);
                    if (pd.isShowing()) pd.dismiss();
                }
            });
        } else {
            spinnerComplaintCategory.setSelection(0);
            Toast.makeText(WorkerQueryNewActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }
    private void getToken() {
        Constant.startVolleyDialog(this);
        Call<TokenData> call = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getToken(SplashActivity.strTName,SplashActivity.strTPwd,Api.grant_type);
        call.enqueue(new Callback<TokenData>() {
            @Override
            public void onResponse(Call<TokenData> call, Response<TokenData> response) {
                if (response.isSuccessful()) {
                    submitFeedback(response.body().getAccess_token());

                } else {
                    MyAlert.showAlert(getApplicationContext(), R.mipmap.icon_warning, getApplicationContext().getString(R.string.submit_warning), getApplicationContext().getString(R.string.network_req_failed), strActivityCode + "04");
                }
                Constant.dismissVolleyDialog();
            }

            @Override
            public void onFailure(Call<TokenData> call, Throwable t) {
                Constant.dismissVolleyDialog();
                MyAlert.showAlert(getApplicationContext(), R.mipmap.icon_warning, getApplicationContext().getString(R.string.submit_warning), getApplicationContext().getString(R.string.network_req_failed), strActivityCode + "05");
            }
        });
    }



    private void submitFeedback(String token) {
        if (connectionDetector.isConnectingToInternet()) {
            if (!pd.isShowing()) pd.show();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(token))
                    .build();
            Call<ResponseData> call = new Retrofit.Builder()
                    .baseUrl(Helper.getBaseURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(Api.class)
                    .uploadFeedbackData(new UploadWorkerQueryModel(selectedStateCode, selectedDistrictCode,selectedBlockCode,selectedPanchayatCode,selectedWorkerCode, strJobCardNumber, selectedCatCode, selectedSubCatCode, etComplaintDesc.getText().toString().trim(), etName.getText().toString().trim(), etMobile.getText().toString().trim()));
            //.uploadFeedbackData(new UploadWorkerQueryModel("06", "0601","0601001","0601001001","01", "CG-01-001-001-001/1", "selectedCatCode", "selectedSubCatCode", "complintdesc", "Tset", "7818932374"));
            // .uploadFeedbackData(new UploadWorkerQueryModel("02","4","AP-01-013-007-043/010002"/*"CG-01-001-001-001/1"*/,"04","0404","desc","Nalin","7818932374"));
            // .uploadFeedbackData(new UploadWorkerQueryModel("06","01",""/*"CG-01-001-001-001/1"*/,"01","0101","desc","Nalin","9899999999"));
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    responseData = response.body();
                    if (response.isSuccessful()) {
                        final Dialog dialogAlert2 = new Dialog(WorkerQueryNewActivity.this);
                        MyAlert.dialogForOk
                                (context, R.mipmap.icon_info, context.getString(R.string.submit_info),
                                        responseData.getMessage(),
                                        dialogAlert2,
                                        context.getString(R.string.ok),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogAlert2.dismiss();
                                                finish();
                                            }
                                        }, strActivityCode + "021");

                        if (pd.isShowing()) pd.dismiss();
                    } else {
                        MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), responseData.getMessage() + "\n" + response.message(), strActivityCode + "012");
                        if (pd.isShowing()) pd.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), t.getCause() + "\n" + t.getMessage(), strActivityCode + "013");
                    if (pd.isShowing()) pd.dismiss();
                }
            });
        } else
            MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.no_internet), strActivityCode + "014");

    }

    private void populateSpinnerWorker(ArrayList<WorkerModel> alApplicantWorker, Spinner sp) {
        if (!alApplicantWorker.isEmpty() && alApplicantWorker.size() > 0) {
            WorkerAdapter dbAdapter = new WorkerAdapter(WorkerQueryNewActivity.this, android.R.layout.simple_spinner_item, alApplicantWorker);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

    private void populateSpinnerCategory(ArrayList<CategoryModel> alCategory, Spinner sp) {
        if (!alCategory.isEmpty() && alCategory.size() > 0) {
            CategoryAdapter dbAdapter = new CategoryAdapter(WorkerQueryNewActivity.this, android.R.layout.simple_spinner_item, alCategory);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

    private void populateSpinnerSubCategory(ArrayList<SubCategoryModel> alCategory, Spinner sp) {
        if (!alCategory.isEmpty() && alCategory.size() > 0) {
            SubCategoryAdapter dbAdapter = new SubCategoryAdapter(WorkerQueryNewActivity.this, android.R.layout.simple_spinner_item, alCategory);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

    class GetOtpFromServer extends AsyncTask<Void, Void, String> {
        final private ProgressDialog dialog = new ProgressDialog(WorkerQueryNewActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getApplicationContext().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;
            try {
                String apiUrl = (Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/SaveOTP?")
                        + "mobile=" + URLEncoder.encode(baseFragment.encrypt(etMobile.getText().toString()), "UTF-8");
                response = getResponse(apiUrl);

                if (statusCode == 200) {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject msgObject = jObject.getJSONObject("message");
                    statusMessage = msgObject.getString("message");
                    status = msgObject.getString("status");
                    if (status.equals("true")/* && statusMessage.equals("OTP Sent Successfully")*/) {
                        try {
                            JSONArray otpJsonArray = jObject.getJSONArray("otp");
                            for (int i = 0; i < otpJsonArray.length(); i++) {
                                JSONObject otpObject = otpJsonArray.getJSONObject(i);
                                MySharedPref.setOtpID(context, otpObject.getString("OTPID"));
                            }
                            return "true";
                        } catch (Exception e) {
                            return statusMessage = "Error: " + e.getMessage();
                        }
                    } else
                        return statusMessage;


                } else {
                    statusMessage = getApplicationContext().getString(R.string.otp_not_received);
                    return statusMessage;
                }
            } catch (Exception e) {
                return statusMessage = "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result.contains("true")) {
                etMobile.setEnabled(false);
                Toast.makeText(WorkerQueryNewActivity.this, statusMessage, Toast.LENGTH_LONG).show();
            } else {
                etMobile.setEnabled(true);
                Toast.makeText(WorkerQueryNewActivity.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class VerifyOtpByServer extends AsyncTask<Void, Void, String> {
        String MobNo;
        String otpid;
        String pwd;
        String statusMessage;

        final private ProgressDialog dialog = new ProgressDialog(WorkerQueryNewActivity.this);

        VerifyOtpByServer(String MobileNo, String otppwd, String id) {
            this.MobNo = MobileNo;
            this.pwd = otppwd;
            this.otpid = id;

        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(context.getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;

            try {
                String apiUrl = (Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/CheckOTP?")
                        + "MobileNo=" + URLEncoder.encode(baseFragment.encrypt(this.MobNo), "UTF-8")
                        + "&userpwd=" + URLEncoder.encode(baseFragment.encrypt(this.pwd), "UTF-8")
                        + "&otpId=" + URLEncoder.encode(this.otpid, "UTF-8");
                response = getResponse(apiUrl);
                if (statusCode == 200) {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject message = jObject.getJSONObject("message");
                    statusMessage = message.getString("message");
                    return statusMessage;
                } else {
                    return statusMessage = getApplicationContext().getString(R.string.something_went_wrong);
                }
            } catch (Exception e) {
                return statusMessage = "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (statusMessage.equals("OTP Successfully Matched")) {
                final Dialog dialogAlert2 = new Dialog(WorkerQueryNewActivity.this);
                MyAlert.dialogForOk
                        (context, R.mipmap.icon_info, context.getString(R.string.submit_info),
                                context.getString(R.string.otp_matched) + "\n" + context.getString(R.string.please_submit_form),
                                dialogAlert2,
                                context.getString(R.string.submit),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogAlert2.dismiss();
                                        getToken();
                                    }
                                }, strActivityCode + "017");

            } else {
                etOtp.setEnabled(true);
                MyAlert.showAlert(WorkerQueryNewActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), statusMessage, strActivityCode + "016");
            }
        }
    }


    public String getResponse(String Url) {
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            statusCode = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return baseFragment.convertStreamToString(in);
        } catch (Exception e) {
            return "Error " + e.getMessage();
        }

    }

    private void populateSpinnerState(ArrayList<StateModel> alStateModel, Spinner sp) {
        if (!alStateModel.isEmpty() && alStateModel.size() > 0) {
            StateAdapter dbAdapter = new StateAdapter(WorkerQueryNewActivity.this, android.R.layout.simple_spinner_item, alStateModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

}