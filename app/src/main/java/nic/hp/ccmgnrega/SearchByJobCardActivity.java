package nic.hp.ccmgnrega;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.adapter.BlockAdapter;
import nic.hp.ccmgnrega.adapter.CategoryAdapter;
import nic.hp.ccmgnrega.adapter.DistrictAdapter;
import nic.hp.ccmgnrega.adapter.PanchayatAdapter;
import nic.hp.ccmgnrega.adapter.SearchedJobCardAdapter;
import nic.hp.ccmgnrega.adapter.StateAdapter;
import nic.hp.ccmgnrega.adapter.SubCategoryAdapter;
import nic.hp.ccmgnrega.adapter.VillageAdapter;
import nic.hp.ccmgnrega.adapter.WorkerAdapter;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.CurrentFinancialYear;
import nic.hp.ccmgnrega.common.DbHelper;
import nic.hp.ccmgnrega.common.Helper;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.common.RedAsterisk;
import nic.hp.ccmgnrega.common.TokenInterceptor;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.fragment.BaseFragment;
import nic.hp.ccmgnrega.model.BlockModel;
import nic.hp.ccmgnrega.model.CategoryData;
import nic.hp.ccmgnrega.model.CategoryModel;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.DistrictModel;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.JobCardDetail;
import nic.hp.ccmgnrega.model.PanchayatModel;
import nic.hp.ccmgnrega.model.ResponseData;
import nic.hp.ccmgnrega.model.StateModel;
import nic.hp.ccmgnrega.model.SubCategoryData;
import nic.hp.ccmgnrega.model.SubCategoryModel;
import nic.hp.ccmgnrega.model.TokenData;
import nic.hp.ccmgnrega.model.UploadWorkerQueryModel;
import nic.hp.ccmgnrega.model.VillageModel;
import nic.hp.ccmgnrega.model.WorkerModel;
import nic.hp.ccmgnrega.webview.BasicAuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchByJobCardActivity extends AppCompatActivity {
    public static JobCardData jobCardData;
    private static int shortDelay = 2000;
    protected ConnectionDetector connectionDetector;
    SearchedJobCardAdapter searchedJobCardAdapter;
    protected DbHelper dbHelper;
    protected SQLiteDatabase dbReader;
    protected SQLiteDatabase dbWriter;
    protected Spinner spinnerState, spinnerDistrict, spinnerBlock, spinnerPanchayat, spinnerVillage;
    String financialYear = "", selectedStateCode = "0", selectedStateShortName = "0", selectedDistrictCode = "0",
            selectedBlockCode = "0", selectedPanchayatCode = "0", selectedVillageCode = "0";

    EditText etFamilyId;
    Button btnSubmit;
    EditText jobCardNumberInput;
    ImageView searchButton;
    TextView loadingTextView,heading;
    View overlayView,tvJobCardList;
    ListView lvJobCard;
    String strJobCardNumber = "",statusMessage;
    String strActivityCode="010-";//Max
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_jobcard);
        context=getApplicationContext();

        findById();
        loadActionBar();
        dbHelper = new DbHelper(context);
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
        connectionDetector = new ConnectionDetector(SearchByJobCardActivity.this);
        heading.setText(" " + getApplicationContext().getString(R.string.workers_query));
        if (getSearchedJobCardList(context).size() > 0) {
            searchedJobCardAdapter = new SearchedJobCardAdapter(SearchByJobCardActivity.this, getSearchedJobCardList(context),"JCA");
           lvJobCard.setAdapter(searchedJobCardAdapter);
          tvJobCardList.setVisibility(View.VISIBLE);
        }
        else {
           lvJobCard.setAdapter(null);
           tvJobCardList.setVisibility(View.GONE);
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobCardNumberInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (connectionDetector.isConnectingToInternet()) {
                    jobCardData = new JobCardData();
                    if (jobCardNumberInput.getText().toString().isEmpty())
                        MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_job_card_number), strActivityCode+"13");
                    else
                        getToken(jobCardNumberInput.getText().toString().trim());
                } else {
                    searchButton.setEnabled(false);
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.no_internet), strActivityCode+"06");
                    enableSearchButtonAfterDelay();
                }
            }
        });
        //////////////Search By Family ID Section//////////////////
        CurrentFinancialYear fDate = new CurrentFinancialYear();
        this.financialYear = fDate.getFinacialYear();
        getStateList();
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                etFamilyId.setText("");
                if (position == 0) {
                    selectedStateCode = "0";
                    selectedStateShortName = "0";
                    selectedDistrictCode = "0";
                    selectedBlockCode = "0";
                    selectedPanchayatCode = "0";
                    selectedVillageCode = "0";
                    btnSubmit.setVisibility(View.GONE);
                    spinnerDistrict.setAdapter(null);
                    spinnerBlock.setAdapter(null);
                    spinnerPanchayat.setAdapter(null);
                    spinnerVillage.setAdapter(null);


                } else {
                    StateModel stateModel = ((StateAdapter) spinnerState.getAdapter()).getItem(position);
                    selectedStateCode = stateModel.getStateCode();
                    selectedStateShortName = stateModel.getShortName().trim();

                    if (connectionDetector.isConnectingToInternet()) {
                        getDistrictList(selectedStateCode, "NEWREQUEST", financialYear);
                    } else
                        Toast.makeText(SearchByJobCardActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                etFamilyId.setText("");
                if (position == 0) {
                    selectedDistrictCode = "0";
                    selectedBlockCode = "0";
                    selectedPanchayatCode = "0";
                    selectedVillageCode = "0";
                    btnSubmit.setVisibility(View.GONE);
                    spinnerBlock.setAdapter(null);
                    spinnerPanchayat.setAdapter(null);
                    spinnerVillage.setAdapter(null);

                } else {
                    DistrictModel districtModel = ((DistrictAdapter) spinnerDistrict.getAdapter()).getItem(position);
                    selectedDistrictCode = districtModel.getDistrictCode();
                    getBlockListByDistrictCode(selectedDistrictCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                etFamilyId.setText("");
                if (position == 0) {
                    selectedBlockCode = "0";
                    selectedPanchayatCode = "0";
                    selectedVillageCode = "0";
                    btnSubmit.setVisibility(View.GONE);
                    spinnerPanchayat.setAdapter(null);
                    spinnerVillage.setAdapter(null);
                } else {
                    BlockModel blockModel = ((BlockAdapter) spinnerBlock.getAdapter()).getItem(position);
                    selectedBlockCode = blockModel.getBlockCode();
                    if (connectionDetector.isConnectingToInternet())
                        getPanchayatList(selectedBlockCode, financialYear);
                    else
                        Toast.makeText(SearchByJobCardActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerPanchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                etFamilyId.setText("");
                if (position == 0) {
                    selectedPanchayatCode = "0";
                    selectedVillageCode = "0";
                    btnSubmit.setVisibility(View.GONE);
                    spinnerVillage.setAdapter(null);
                } else {
                    PanchayatModel panchayatModel = ((PanchayatAdapter) spinnerPanchayat.getAdapter()).getItem(position);
                    selectedPanchayatCode = panchayatModel.getPanchayatCode();
                    if (connectionDetector.isConnectingToInternet())
                        getVillageList(selectedPanchayatCode, financialYear);
                    else
                        Toast.makeText(SearchByJobCardActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                etFamilyId.setText("");
                if (position == 0) {
                    selectedVillageCode = "0";
                    btnSubmit.setVisibility(View.GONE);

                } else {
                    VillageModel villageModel = ((VillageAdapter) spinnerVillage.getAdapter()).getItem(position);
                    selectedVillageCode = villageModel.getVillageCode();
                    btnSubmit.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFamilyId.getText().toString().isEmpty())
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_fill_family_id), strActivityCode+"01");
                else {
                    String strFamilyId = etFamilyId.getText().toString().trim();
                    strJobCardNumber = "";
                    if (selectedStateShortName.equalsIgnoreCase("BH") || selectedStateShortName.equalsIgnoreCase("HP")) {
                        strJobCardNumber = selectedStateShortName + "-" + getLastSubString(selectedDistrictCode.trim(), 2) +
                                "-" + getLastSubString(selectedBlockCode.trim(), 3) +
                                "-" + getLastSubString(selectedPanchayatCode.trim(), 3) +
                                "-" + getLastSubString(selectedVillageCode.trim(), 8) +
                                "/" + strFamilyId;
                    } else if (selectedStateShortName.equalsIgnoreCase("RJ")) {
                        strJobCardNumber = selectedStateShortName + "-" + getLastSubString(selectedVillageCode.trim(), 18) + "/" + strFamilyId;
                    } else {
                        strJobCardNumber = selectedStateShortName + "-" + getLastSubString(selectedDistrictCode.trim(), 2) +
                                "-" + getLastSubString(selectedBlockCode.trim(), 3) +
                                "-" + getLastSubString(selectedPanchayatCode.trim(), 3) +
                                "-" + getLastSubString(selectedVillageCode.trim(), 3) +
                                "/" + strFamilyId;
                    }

                    final Dialog dialogAlert = new Dialog(SearchByJobCardActivity.this);
                    MyAlert.dialogForCancelOk
                            (SearchByJobCardActivity.this, R.mipmap.icon_info, context.getString(R.string.submission_alert),
                                    String.format(getResources().getString(R.string.your_jobcard_is_this_to_continue), strJobCardNumber),
                                    dialogAlert,
                                    SearchByJobCardActivity.this.getString(R.string.yes),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogAlert.dismiss();
                                            if (connectionDetector.isConnectingToInternet()) {
                                                jobCardData = new JobCardData();
                                                getToken(strJobCardNumber);
                                            } else
                                                MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.no_internet), strActivityCode+"02");

                                        }
                                    },
                                    SearchByJobCardActivity.this.getString(R.string.no),
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
                                    }, strActivityCode+"07");


                }

            }
        });
        //////////////Search By Family ID Section//////////////////

    }




    private void findById() {
        jobCardNumberInput=findViewById(R.id.searchJobCard);
        heading=findViewById(R.id.heading);
        searchButton=findViewById(R.id.searchButton);
        loadingTextView=findViewById(R.id.loadingTextView);
        overlayView=findViewById(R.id.overlayView);
        spinnerState=findViewById(R.id.spinnerState);
        spinnerDistrict=findViewById(R.id.spinnerDistrict);
        spinnerBlock=findViewById(R.id.spinnerBlock);
        spinnerPanchayat=findViewById(R.id.spinnerPanchayat);
        spinnerVillage=findViewById(R.id.spinnerVillage);
        etFamilyId=findViewById(R.id.etFamilyId);
        tvJobCardList=findViewById(R.id.tvJobCardList);
        lvJobCard=findViewById(R.id.lvJobCard);
        btnSubmit=findViewById(R.id.btnSubmit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
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

    private void enableSearchButtonAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchButton.setEnabled(true);
            }
        }, shortDelay);
    }

    private void getToken(String strJobCardNumber) {
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
                    getJobCardData(strJobCardNumber,response.body().getAccess_token());

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
    private void getJobCardData(String strJobCardNumber,String token) {
        Constant.startVolleyDialog(SearchByJobCardActivity.this);
        searchButton.setEnabled(false);
//        loadingTextView.setVisibility(View.VISIBLE);
//        overlayView.setVisibility(View.VISIBLE);
        JobCardDataAccess.clearData();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(token))
                .build();
        Call<JobCardData> call = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class)
                .getJobCardData(strJobCardNumber);
        call.enqueue(new Callback<JobCardData>() {
            @Override
            public void onResponse(Call<JobCardData> call, Response<JobCardData> response) {
                if (response.isSuccessful()) {
                    jobCardData = response.body();
                    if (jobCardData.getStatus() != null && !jobCardData.getStatus().equalsIgnoreCase("False")) {
                        jobCardData.setJobCardId(strJobCardNumber);
                        insertSearchedJobCard(strJobCardNumber);
                        if (getSearchedJobCardList(context).size() > 0) {
                            searchedJobCardAdapter = new SearchedJobCardAdapter(SearchByJobCardActivity.this, getSearchedJobCardList(context),"JCA");
                            lvJobCard.setAdapter(searchedJobCardAdapter);
                            tvJobCardList.setVisibility(View.VISIBLE);
                        } else {
                            lvJobCard.setAdapter(null);
                            tvJobCardList.setVisibility(View.GONE);
                        }
                        navigateToUserFragment(strJobCardNumber);
                        jobCardNumberInput.setText("");
                    } else {
                        String message = jobCardData.getRemarks();
                        if (jobCardData.getRemarks().contains("Data not found"))
                            message = context.getString(R.string.job_not_found);
                        MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), message, strActivityCode+"03");

                    }
                } else {
                    String message = context.getString(R.string.network_req_failed);
                    if (response.message().equalsIgnoreCase("Data not found"))
                        message = context.getString(R.string.job_not_found);
                    MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), message, strActivityCode+"04");


                }
                searchButton.setEnabled(true);
                loadingTextView.setVisibility(View.GONE);
                overlayView.setVisibility(View.GONE);
                Constant.dismissVolleyDialog();
            }
            @Override
            public void onFailure(Call<JobCardData> call, Throwable t) {
                searchButton.setEnabled(true);
                loadingTextView.setVisibility(View.GONE);
                overlayView.setVisibility(View.GONE);
                Constant.dismissVolleyDialog();
                MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.network_req_failed), strActivityCode+"05");

            }
        });
    }

    private void getJobCardDataBack(String strJobCardNumber) {
        Constant.startVolleyDialog(SearchByJobCardActivity.this);
        searchButton.setEnabled(false);
//        loadingTextView.setVisibility(View.VISIBLE);
//        overlayView.setVisibility(View.VISIBLE);
        JobCardDataAccess.clearData();
        Call<JobCardData> call = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class)
                .getJobCardData(strJobCardNumber);
        call.enqueue(new Callback<JobCardData>() {
            @Override
            public void onResponse(Call<JobCardData> call, Response<JobCardData> response) {
                if (response.isSuccessful()) {
                    jobCardData = response.body();
                    if (jobCardData.getStatus() != null && !jobCardData.getStatus().equalsIgnoreCase("False")) {
                        jobCardData.setJobCardId(strJobCardNumber);
                        insertSearchedJobCard(strJobCardNumber);
                        if (getSearchedJobCardList(context).size() > 0) {
                            searchedJobCardAdapter = new SearchedJobCardAdapter(SearchByJobCardActivity.this, getSearchedJobCardList(context),"JCA");
                           lvJobCard.setAdapter(searchedJobCardAdapter);
                            tvJobCardList.setVisibility(View.VISIBLE);
                        } else {
                            lvJobCard.setAdapter(null);
                            tvJobCardList.setVisibility(View.GONE);
                        }
                    navigateToUserFragment(strJobCardNumber);
                        jobCardNumberInput.setText("");
                    } else {
                        String message = jobCardData.getRemarks();
                        if (jobCardData.getRemarks().contains("Data not found"))
                            message = context.getString(R.string.job_not_found);
                        MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), message, strActivityCode+"03");

                    }
                } else {
                    String message = context.getString(R.string.network_req_failed);
                    if (response.message().equalsIgnoreCase("Data not found"))
                        message = context.getString(R.string.job_not_found);
                    MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), message, strActivityCode+"04");


                }
                searchButton.setEnabled(true);
                loadingTextView.setVisibility(View.GONE);
                overlayView.setVisibility(View.GONE);
                Constant.dismissVolleyDialog();
            }
            @Override
            public void onFailure(Call<JobCardData> call, Throwable t) {
                searchButton.setEnabled(true);
                loadingTextView.setVisibility(View.GONE);
                overlayView.setVisibility(View.GONE);
                Constant.dismissVolleyDialog();
                MyAlert.showAlert(SearchByJobCardActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.network_req_failed), strActivityCode+"05");

            }
        });
    }
    private void insertSearchedJobCard(String strJobCardNUmber) {
        dbWriter.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_NAME_LAST_UPDATE, strJobCardNUmber);
        dbWriter.insert(DbHelper.TABLE_NAME_LAST_UPDATE, null, cv);
        dbWriter.setTransactionSuccessful();
        dbWriter.endTransaction();
    }

    @SuppressLint("Range")
    private ArrayList<String> getSearchedJobCardList(Context context) {
        ArrayList<String> alSearchedJobCardList = new ArrayList<>();
        String[] projectionUser = {DbHelper.COLUMN_NAME_LAST_UPDATE};
        Cursor cursor = dbReader.query(DbHelper.TABLE_NAME_LAST_UPDATE, projectionUser, null, null, null, null, "ID DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String jobCardNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_LAST_UPDATE));
                if ((alSearchedJobCardList.size() < 3) && (!alSearchedJobCardList.contains(jobCardNumber)))
                    alSearchedJobCardList.add(jobCardNumber);

            } while (cursor.moveToNext());

            cursor.close();
        }
        return alSearchedJobCardList;
    }
    public void navigateToUserFragment(String strJobCardNumber) {
        Intent intent = new Intent(context, WorkerQueryNewActivity.class);
        intent.putExtra("strJobCardNumber", strJobCardNumber);
        intent.putExtra("strJobCardDataMode", "JCA");
        startActivity(intent);
    }
    public void getStateList() {
        final ProgressDialog dialog = new ProgressDialog(SearchByJobCardActivity.this);
        dialog.setMessage(context.getString(R.string.please_wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                .build();
        Api api = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class);
        Call<List<StateModel>> call = api.getStateList();
        call.enqueue(new Callback<List<StateModel>>() {
            @Override
            public void onResponse(Call<List<StateModel>> call, Response<List<StateModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            ArrayList<StateModel> alState = (ArrayList<StateModel>) response.body();
                            alState.add(0, new StateModel(context.getString(R.string.select)));
                            populateSpinnerState(alState, spinnerState);
                            dialog.dismiss();
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            dialog.dismiss();
                            final Dialog dialogAlert2 = new Dialog(SearchByJobCardActivity.this);
                            MyAlert.dialogForOk
                                    (context, R.mipmap.icon_warning, context.getString(R.string.warning),
                                            context.getString(R.string.network_req_failed),
                                            dialogAlert2,
                                            context.getString(R.string.ok),
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogAlert2.dismiss();
                                                    finish();
                                                }
                                            }, strActivityCode + "011");
                        }
                    } else {
                        dialog.dismiss();
                        final Dialog dialogAlert2 = new Dialog(SearchByJobCardActivity.this);
                        MyAlert.dialogForOk
                                (context, R.mipmap.icon_warning, context.getString(R.string.warning),
                                        context.getString(R.string.network_req_failed),
                                        dialogAlert2,
                                        context.getString(R.string.ok),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogAlert2.dismiss();
                                                finish();
                                            }
                                        }, strActivityCode + "010");
                    }

                }
                else {
                    dialog.dismiss();
                    final Dialog dialogAlert2 = new Dialog(SearchByJobCardActivity.this);
                    MyAlert.dialogForOk
                            (context, R.mipmap.icon_warning, context.getString(R.string.warning),
                                    context.getString(R.string.network_req_failed),
                                    dialogAlert2,
                                    context.getString(R.string.ok),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogAlert2.dismiss();
                                            finish();
                                        }
                                    }, strActivityCode + "09");
                }
            }

            @Override
            public void onFailure(Call<List<StateModel>> call, Throwable t) {
                final Dialog dialogAlert2 = new Dialog(SearchByJobCardActivity.this);
                MyAlert.dialogForOk
                        (context, R.mipmap.icon_warning, context.getString(R.string.warning),
                                context.getString(R.string.network_req_failed),
                                dialogAlert2,
                                context.getString(R.string.ok),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogAlert2.dismiss();
                                        finish();
                                    }
                                }, strActivityCode + "08");
                dialog.dismiss();
            }
        });

    }


    public void getDistrictList(String stateCode, String requestId, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(SearchByJobCardActivity.this);
        dialog.setMessage(context.getString(R.string.please_wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                .build();
        Api api = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class);
        Call<List<DistrictModel>> call = api.getDistrictList(stateCode, requestId, finYear);
        call.enqueue(new Callback<List<DistrictModel>>() {
            @Override
            public void onResponse(Call<List<DistrictModel>> call, Response<List<DistrictModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            dialog.dismiss();
                            ArrayList<DistrictModel> alDistrict = (ArrayList<DistrictModel>) response.body();
                            alDistrict.add(0, new DistrictModel(context.getString(R.string.select)));
                            populateSpinnerDistrict(alDistrict, spinnerDistrict);
                            getBlockList(stateCode, requestId, finYear);
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<DistrictModel>> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

    public void getBlockList(String stateCode, String requestId, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(SearchByJobCardActivity.this);
        dialog.setMessage(context.getString(R.string.please_wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                .build();
        Api api = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class);
        Call<List<BlockModel>> call = api.getBlockList(stateCode, requestId, finYear);
        call.enqueue(new Callback<List<BlockModel>>() {
            @Override
            public void onResponse(Call<List<BlockModel>> call, Response<List<BlockModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            dbWriter.beginTransaction();
                            dbWriter.delete(DbHelper.TABLE_NAME_BLOCK_MASTER, null, null);
                            List<BlockModel> alBLock = response.body();
                            for (int i = 0; i < alBLock.size(); i++) {
                                BlockModel blockModel = alBLock.get(i);
                                ContentValues blockValues = new ContentValues();
                                blockValues.put(DbHelper.COLUMN_NAME_STATE_STATE_CODE, blockModel.getStateCode());
                                blockValues.put(DbHelper.COLUMN_NAME_DISTRICT_MASTER_ID, blockModel.getDistrictCode());
                                blockValues.put(DbHelper.COLUMN_NAME_BLOCK_MASTER_ID, blockModel.getBlockCode());
                                blockValues.put(DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME, blockModel.getBlockName());
                                blockValues.put(DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL, blockModel.getBlockNameLocal());
                                blockValues.put(DbHelper.COLUMN_NAME_FINANCIAL_YEAR, blockModel.getFinYear());
                                dbWriter.insert(DbHelper.TABLE_NAME_BLOCK_MASTER, null, blockValues);
                                blockValues.clear();
                            }

                            dbWriter.setTransactionSuccessful();
                            dbWriter.endTransaction();
                            dialog.dismiss();

                        } catch (SQLiteConstraintException e) {
                            dbWriter.endTransaction();
                            statusMessage = e.getMessage();
                            Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<BlockModel>> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

    public void getPanchayatList(String blockCode, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(SearchByJobCardActivity.this);
        dialog.setMessage(context.getString(R.string.please_wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                .build();
        Api api = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class);
        Call<List<PanchayatModel>> call = api.getPanchayatList(blockCode, finYear);
        call.enqueue(new Callback<List<PanchayatModel>>() {
            @Override
            public void onResponse(Call<List<PanchayatModel>> call, Response<List<PanchayatModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            dialog.dismiss();
                            ArrayList<PanchayatModel> alPanchayat = (ArrayList<PanchayatModel>) response.body();
                            alPanchayat.add(0, new PanchayatModel(context.getString(R.string.select)));
                            populateSpinnerPanchayat(alPanchayat, spinnerPanchayat);
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PanchayatModel>> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

    public void getVillageList(String pCode, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(SearchByJobCardActivity.this);
        dialog.setMessage(context.getString(R.string.please_wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(SplashActivity.strNregaName, SplashActivity.strNregaPwd))
                .build();
        Api api = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class);
        Call<List<VillageModel>> call = api.getVillageList(pCode, finYear);
        call.enqueue(new Callback<List<VillageModel>>() {
            @Override
            public void onResponse(Call<List<VillageModel>> call, Response<List<VillageModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            ArrayList<VillageModel> alVillage = (ArrayList<VillageModel>) response.body();
                            alVillage.add(0, new VillageModel(context.getString(R.string.select)));
                            populateSpinnerVillage(alVillage, spinnerVillage);
                            dialog.dismiss();
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<VillageModel>> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

    @SuppressLint("Range")
    private void getBlockListByDistrictCode(String districtCode) {
        ArrayList<BlockModel> alBlock = new ArrayList<>();
        alBlock.add(0, new BlockModel(context.getString(R.string.select)));
        String[] columns = {DbHelper.COLUMN_NAME_STATE_STATE_CODE, DbHelper.COLUMN_NAME_DISTRICT_MASTER_ID, DbHelper.COLUMN_NAME_BLOCK_MASTER_ID, DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME,
                DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL, DbHelper.COLUMN_NAME_FINANCIAL_YEAR};
        Cursor cursorMenu = dbWriter.query(DbHelper.TABLE_NAME_BLOCK_MASTER, columns, "DistrictID = ?", new String[]{districtCode}, null, null, DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME);
        if (cursorMenu != null) {
            if (cursorMenu.getCount() > 0 && cursorMenu.moveToFirst()) {
                do {
                    alBlock.add(new BlockModel(
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_STATE_STATE_CODE)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_DISTRICT_MASTER_ID)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_BLOCK_MASTER_ID)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_FINANCIAL_YEAR)),
                            ""));
                } while (cursorMenu.moveToNext());
            }
            cursorMenu.close();
        }
        populateSpinnerBlock(alBlock, spinnerBlock);

    }

    private static String getLastSubString(String strCode, int lastDigit) {
        if (strCode.length() <= lastDigit)
            return strCode;
        else
            return strCode.substring(strCode.length() - lastDigit);
    }

    private void populateSpinnerState(ArrayList<StateModel> alStateModel, Spinner sp) {
        if (!alStateModel.isEmpty() && alStateModel.size() > 0) {
            StateAdapter dbAdapter = new StateAdapter(SearchByJobCardActivity.this, android.R.layout.simple_spinner_item, alStateModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

    private void populateSpinnerDistrict(ArrayList<DistrictModel> alDistrictModel, Spinner sp) {
        if (!alDistrictModel.isEmpty() && alDistrictModel.size() > 0) {
            DistrictAdapter dbAdapter = new DistrictAdapter(SearchByJobCardActivity.this, android.R.layout.simple_spinner_item, alDistrictModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);
        }
    }

    private void populateSpinnerBlock(ArrayList<BlockModel> alBlockModel, Spinner sp) {
        if (!alBlockModel.isEmpty() && alBlockModel.size() > 0) {
            BlockAdapter dbAdapter = new BlockAdapter(SearchByJobCardActivity.this, android.R.layout.simple_spinner_item, alBlockModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);
        }
    }

    private void populateSpinnerPanchayat(ArrayList<PanchayatModel> alPanchayatModel, Spinner sp) {
        if (!alPanchayatModel.isEmpty() && alPanchayatModel.size() > 0) {
            PanchayatAdapter dbAdapter = new PanchayatAdapter(SearchByJobCardActivity.this, android.R.layout.simple_spinner_item, alPanchayatModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);
        }
    }

    private void populateSpinnerVillage(ArrayList<VillageModel> alVillageModel, Spinner sp) {
        if (!alVillageModel.isEmpty() && alVillageModel.size() > 0) {
            VillageAdapter dbAdapter = new VillageAdapter(SearchByJobCardActivity.this, android.R.layout.simple_spinner_item, alVillageModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);
        }
    }
    @Override
    public void onDestroy() {
        dbWriter.close();
        super.onDestroy();
    }

}