package nic.hp.ccmgnrega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

import nic.hp.ccmgnrega.adapter.CategoryAdapter;
import nic.hp.ccmgnrega.adapter.SearchedJobCardAdapter;
import nic.hp.ccmgnrega.adapter.StateAdapter;
import nic.hp.ccmgnrega.adapter.SubCategoryAdapter;
import nic.hp.ccmgnrega.adapter.WorkerAdapter;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.common.RedAsterisk;
import nic.hp.ccmgnrega.common.TokenInterceptor;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.fragment.BaseFragment;
import nic.hp.ccmgnrega.model.CategoryData;
import nic.hp.ccmgnrega.model.CategoryModel;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.JobCardDetail;
import nic.hp.ccmgnrega.model.JobComplaintStatusData;
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

public class ViewComplaintStatusActivity extends AppCompatActivity {
    TableLayout tabComplaintDetail;

    protected ConnectionDetector connectionDetector;
    ImageView searchButton;
    EditText etComplaintId;
    public static JobCardData jobCardData;
    Context context;
    RelativeLayout rlComplaint;
    LinearLayout linearComplaintDesc;
    ImageView imgComplaint;
    String strActivityCode = "011-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint_status);
        context = getApplicationContext();
        findById();
        loadActionBar();
      //  getToken("33","6267553365");
/*
        rlComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearComplaintDesc.getVisibility()==View.GONE){
                    linearComplaintDesc.setVisibility(View.VISIBLE);
                    imgComplaint.setBackgroundResource(R.drawable.ic_baseline24_show_data);
                    rlComplaint.setBackgroundResource(R.drawable.border_up);
                }
                else {
                    linearComplaintDesc.setVisibility(View.GONE);
                    imgComplaint.setBackgroundResource(R.drawable.ic_baseline24_hide_data);
                    rlComplaint.setBackgroundResource(R.drawable.border);
                }
            }
        });
*/

     connectionDetector = new ConnectionDetector(ViewComplaintStatusActivity.this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etComplaintId.onEditorAction(EditorInfo.IME_ACTION_DONE);
              //  if (connectionDetector.isConnectingToInternet())
                {
                    /*jobCardData = new JobCardData();

                  if (etComplaintId.getText().toString().isEmpty())
                        MyAlert.showAlert(ViewComplaintStatusActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.please_enter_complaint_id), strActivityCode + "01");
                    else
                        getJobCardData(etComplaintId.getText().toString().trim());
*/
                    tabComplaintDetail.setVisibility(View.VISIBLE);
                    tabComplaintDetail.removeAllViews();
                    for (int i = 0; i < 5 ; i++) {
                        TableRow tableRow = new TableRow(context);
                        TextView label = new TextView(context);
                        label.setText("Complaint Status");
                       tableRow.setPadding(0, 0, 0, 0);
                        tableRow.setLayoutParams(new TableRow.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));

                        //    label.setText(jobCardTabLabelValueList.get(i).first + ":");

                        label.setTextColor(context.getResources().getColor(R.color.blue_text));
                        label.setTextColor(Color.parseColor("#3F51B5"));
                        label.setTypeface(null, Typeface.BOLD);
                        label.setLayoutParams(new TableRow.LayoutParams(0, 100, 1.0f));
                        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                        label.setGravity(Gravity.CENTER);
                         label.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.tab_row_style));
                        tableRow.addView(label);

                        TextView value = new TextView(context);
                        //  value.setText(jobCardTabLabelValueList.get(i).second);
                        value.setText("Completed");
                        value.setTextColor(ContextCompat.getColor(context, R.color.all_text));
                        value.setLayoutParams(new TableRow.LayoutParams(0, 100, 1.0f));
                        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                        value.setGravity(Gravity.CENTER);
                         value.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.tab_row_style));
                        tableRow.addView(value);
                        // tableRow.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.tab_row_style));
                        tabComplaintDetail.addView(tableRow, i);
                    }


                } /*else {
                    searchButton.setEnabled(false);
                    Toast.makeText(ViewComplaintStatusActivity.this, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    enableSearchButtonAfterDelay();
                }*/
            }
        });
    }

    private void findById() {
       /* rlComplaint= findViewById(R.id.rlComplaint);
        linearComplaintDesc= findViewById(R.id.linearComplaintDesc);
        imgComplaint= findViewById(R.id.imgComplaint);*/
      etComplaintId = findViewById(R.id.etComplaintId);
       searchButton = findViewById(R.id.searchButton);
        tabComplaintDetail = findViewById(R.id.tabComplaintDetail);
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


    private void enableSearchButtonAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchButton.setEnabled(true);
            }
        }, 2000);
    }

/*    private void getToken(String stateCode,String mobileno) {
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
                getJobCardComplaintStatus(stateCode,mobileno,response.body().getAccess_token());

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
    private void getJobCardComplaintStatus(String stateCode,String mobileno,String token) {
        Constant.startVolleyDialog(ViewComplaintStatusActivity.this);
        searchButton.setEnabled(false);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(token))
                .build();
        Call<List<JobComplaintStatusData>> call = new Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api.class)
                .JobComplaintStatusData(stateCode,mobileno);
        call.enqueue(new Callback<List<JobComplaintStatusData>>() {
            @Override
            public void onResponse(Call<List<JobComplaintStatusData>> call, Response<List<JobComplaintStatusData>> response) {
                if (response.isSuccessful()) {
                    ArrayList<JobComplaintStatusData> alVillage = (ArrayList<JobComplaintStatusData>) response.body();


                } else {
                    String message = context.getString(R.string.network_req_failed);
                    MyAlert.showAlert(ViewComplaintStatusActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), message, strActivityCode+"04");
                }
                searchButton.setEnabled(true);
                Constant.dismissVolleyDialog();
            }
            @Override
            public void onFailure(Call<List<JobComplaintStatusData>> call, Throwable t) {
                searchButton.setEnabled(true);
                Constant.dismissVolleyDialog();
                MyAlert.showAlert(ViewComplaintStatusActivity.this, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.network_req_failed), strActivityCode+"05");

            }
        });
    }*/

}