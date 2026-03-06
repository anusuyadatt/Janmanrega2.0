package nic.hp.ccmgnrega.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nic.hp.ccmgnrega.JobCardHolderActivity;
import nic.hp.ccmgnrega.MainActivity;
import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.SearchByJobCardActivity;
import nic.hp.ccmgnrega.SplashActivity;
import nic.hp.ccmgnrega.WorkerQueryNewActivity;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.Constant;
import nic.hp.ccmgnrega.common.Helper;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.common.TokenInterceptor;
import nic.hp.ccmgnrega.data.JobCardDataAccess;
import nic.hp.ccmgnrega.fragment.SearchFragment;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.model.JobCardData;
import nic.hp.ccmgnrega.model.TokenData;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchedJobCardAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> alJobCardList;
    ProgressDialog pd;
    protected ConnectionDetector connectionDetector;
    String strConcernMode = "";

    public SearchedJobCardAdapter(Context context, ArrayList<String> alJobCardList, String strConcernMode) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.alJobCardList = alJobCardList;
        this.strConcernMode = strConcernMode;
    }

    public int getCount() {
        return alJobCardList.size();
    }

    public String getItem(int position) {
        return alJobCardList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.jobcard_list_item, parent, false);
        connectionDetector = new ConnectionDetector(context);
        pd = new ProgressDialog(context);
        pd.setMessage(context.getString(R.string.please_wait));
        pd.setCanceledOnTouchOutside(false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml("<u>" + alJobCardList.get(position) + "</u>"));
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    getToken(alJobCardList.get(position).trim());
                } else
                    Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();

            }
        });
        return convertView;
    }
    private void getToken(String strJobCardNumber) {
        Constant.startVolleyDialog(context);
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
                    MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.network_req_failed), "04");
                }
                Constant.dismissVolleyDialog();
            }

            @Override
            public void onFailure(Call<TokenData> call, Throwable t) {
                Constant.dismissVolleyDialog();
                MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.network_req_failed), "05");

            }
        });
    }
    private void getJobCardData(String jobCardNumber,String token) {
        if (connectionDetector.isConnectingToInternet()) {
            if (!pd.isShowing())
                pd.show();

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
                    .getJobCardData(jobCardNumber);

            call.enqueue(new Callback<JobCardData>() {
                @Override
                public void onResponse(Call<JobCardData> call, Response<JobCardData> response) {
                    if (response.isSuccessful()) {
                        if (pd.isShowing()) pd.dismiss();
                        JobCardData jobCardData = response.body();
                        if (jobCardData.getStatus() != null && !jobCardData.getStatus().equalsIgnoreCase("False")) {
                            jobCardData.setJobCardId(jobCardNumber);
                            if (strConcernMode.equalsIgnoreCase("JCF")) {
                                SearchFragment.jobCardData = jobCardData;
                                JobCardDataAccess.extractData(SearchFragment.jobCardData, context);
                                final Intent intent = new Intent(context, JobCardHolderActivity.class);
                                intent.putExtra("jobCardId", jobCardNumber);
                                context.startActivity(intent);
                            } else {
                                SearchByJobCardActivity.jobCardData = jobCardData;
                                Intent intent = new Intent(context, WorkerQueryNewActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("strJobCardNumber", jobCardNumber);
                                intent.putExtra("strJobCardDataMode", "JCA");
                                context.startActivity(intent);
                            }

                        } else {
                            Toast toast = Toast.makeText(context, SearchFragment.jobCardData.getRemarks(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        if (pd.isShowing()) pd.dismiss();
                        String message = context.getString(R.string.network_req_failed);
                        if (response.message().equalsIgnoreCase("Data not found"))
                            message = context.getString(R.string.job_not_found);
                        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                        toast.show();
                    }

                }

                @Override
                public void onFailure(Call<JobCardData> call, Throwable t) {
                    if (pd.isShowing()) pd.dismiss();
                    Toast toast = Toast.makeText(context, context.getString(R.string.network_req_failed), Toast.LENGTH_LONG);
                    toast.show();
                }
            });

        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            if (pd.isShowing()) pd.dismiss();
        }

    }
    private void getJobCardDataBack(String jobCardNumber) {
        if (connectionDetector.isConnectingToInternet()) {
            if (!pd.isShowing())
                pd.show();
            JobCardDataAccess.clearData();
            Call<JobCardData> call = new Retrofit.Builder()
                    .baseUrl(Helper.getBaseURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Api.class)
                    .getJobCardData(jobCardNumber);
            call.enqueue(new Callback<JobCardData>() {
                @Override
                public void onResponse(Call<JobCardData> call, Response<JobCardData> response) {
                    if (response.isSuccessful()) {
                        if (pd.isShowing()) pd.dismiss();
                        JobCardData jobCardData = response.body();
                        if (jobCardData.getStatus() != null && !jobCardData.getStatus().equalsIgnoreCase("False")) {
                            jobCardData.setJobCardId(jobCardNumber);
                            if (strConcernMode.equalsIgnoreCase("JCF")) {
                                SearchFragment.jobCardData = jobCardData;
                                JobCardDataAccess.extractData(SearchFragment.jobCardData, context);
                                final Intent intent = new Intent(context, JobCardHolderActivity.class);
                                intent.putExtra("jobCardId", jobCardNumber);
                                context.startActivity(intent);
                            } else {
                                SearchByJobCardActivity.jobCardData = jobCardData;
                                Intent intent = new Intent(context, WorkerQueryNewActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("strJobCardNumber", jobCardNumber);
                                intent.putExtra("strJobCardDataMode", "JCA");
                                context.startActivity(intent);
                            }

                        } else {
                            Toast toast = Toast.makeText(context, jobCardData.getRemarks(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        if (pd.isShowing()) pd.dismiss();
                        String message = context.getString(R.string.network_req_failed);
                        if (response.message().equalsIgnoreCase("Data not found"))
                            message = context.getString(R.string.job_not_found);
                        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                        toast.show();
                    }

                }

                @Override
                public void onFailure(Call<JobCardData> call, Throwable t) {
                    if (pd.isShowing()) pd.dismiss();
                    Toast toast = Toast.makeText(context, context.getString(R.string.network_req_failed), Toast.LENGTH_LONG);
                    toast.show();
                }
            });

        } else {
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            if (pd.isShowing()) pd.dismiss();
        }

    }


}
