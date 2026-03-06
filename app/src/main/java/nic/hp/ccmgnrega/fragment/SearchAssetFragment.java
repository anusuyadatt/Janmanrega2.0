package nic.hp.ccmgnrega.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nic.hp.ccmgnrega.MainActivity;
import nic.hp.ccmgnrega.R;
import nic.hp.ccmgnrega.SplashActivity;
import nic.hp.ccmgnrega.ViewSearchedAssetsActivity;
import nic.hp.ccmgnrega.adapter.BlockAdapter;
import nic.hp.ccmgnrega.adapter.CustomListingAdapter;
import nic.hp.ccmgnrega.adapter.DistrictAdapter;
import nic.hp.ccmgnrega.adapter.PanchayatAdapter;
import nic.hp.ccmgnrega.adapter.StateAdapter;
import nic.hp.ccmgnrega.common.Api;
import nic.hp.ccmgnrega.common.CurrentFinancialYear;
import nic.hp.ccmgnrega.common.Helper;
import nic.hp.ccmgnrega.common.MyAlert;
import nic.hp.ccmgnrega.common.MySharedPref;
import nic.hp.ccmgnrega.model.BlockModel;
import nic.hp.ccmgnrega.model.ConnectionDetector;
import nic.hp.ccmgnrega.common.DbHelper;
import nic.hp.ccmgnrega.model.DistrictModel;
import nic.hp.ccmgnrega.model.PanchayatModel;
import nic.hp.ccmgnrega.model.StateModel;
import nic.hp.ccmgnrega.views.spinner.SearchableSpinner;
import nic.hp.ccmgnrega.webview.BasicAuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint({"Range", "SetTextI18n"})

public class SearchAssetFragment extends BaseFragment {
    protected View rootView;
    protected String cLong = "";
    protected String cLat = "";
    
    protected Spinner spinnerState, spinnerDistrict, spinnerBlock, spinnerPanchayat, spinnerAssetCategory,spinnerWorkType, spinnerFinYear;
    protected SearchableSpinner spinnerAssetID;
    List<String> listAssetCategoryID = new ArrayList<>();
    List<String> listAssetCategoryName = new ArrayList<>();

    List<String> listWorkTypeID = new ArrayList<>();
    List<String> listWorkTypeName = new ArrayList<>();

    List<String> listFinYearName = new ArrayList<>();

    List<String> listAsset = new ArrayList<>();
    List<String> listAssetID = new ArrayList<>();
    List<String> listAssetName = new ArrayList<>();
    List<String> listWorkCode = new ArrayList<>();

    String financialYear = "", selectedStateCode = "0",  selectedDistrictCode = "0", selectedBlockCode = "0", selectedPanchayatCode = "0",selectedPanchayatName = "";
    Button btnSubmit;
    LinearLayout linearAssetCategory,linearTypeOfWork, linearFinancialYear, linearAssetWork;
    TextView headingTV,assetCategoryTV,workTypeTV,finYearTV,assetIDTV;
    String strActivityCode="03-";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_search_panchayat, container, false);
        connectionDetector = new ConnectionDetector(getActivity());
        dbHelper = new DbHelper(getActivity());
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
        headingTV = rootView.findViewById(R.id.heading);
        assetCategoryTV = rootView.findViewById(R.id.assetCategoryTV);
        workTypeTV = rootView.findViewById(R.id.workTypeTV);
        finYearTV = rootView.findViewById(R.id.finYearTV);
        assetIDTV = rootView.findViewById(R.id.assetIDTV);
        spinnerState = rootView.findViewById(R.id.spinnerState);
        spinnerDistrict = rootView.findViewById(R.id.spinnerDistrict);
        spinnerBlock = rootView.findViewById(R.id.spinnerBlock);
        spinnerPanchayat = rootView.findViewById(R.id.spinnerPanchayat);
        linearAssetCategory = rootView.findViewById(R.id.linearAssetCategory);
        linearTypeOfWork = rootView.findViewById(R.id.linearTypeOfWork);
        linearFinancialYear = rootView.findViewById(R.id.linearFinancialYear);
        linearAssetWork = rootView.findViewById(R.id.linearAssetWork);
        spinnerAssetCategory = rootView.findViewById(R.id.spinnerAssetCategory);
        spinnerWorkType = rootView.findViewById(R.id.spinnerWorkType);
        spinnerFinYear = rootView.findViewById(R.id.spinnerFinYear);
        spinnerAssetID = rootView.findViewById(R.id.spinnerAssetID);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        Bundle args = getArguments();
        if (args != null) {
            if (args.getString("menu") != null)
                menu = args.getString("menu");
            if (args.getString("cLong") != null)
                cLong = args.getString("cLong");
            if (args.getString("cLat") != null) 
                cLat = args.getString("cLat");
        }
        headingTV.setText(" "+menu);
        CurrentFinancialYear fDate = new CurrentFinancialYear();
        this.financialYear = fDate.getFinacialYear();
        if (Double.parseDouble(cLong) == 0 || Double.parseDouble(cLat) == 0) {
            Toast.makeText(getActivity(), getContext().getString(R.string.gps_reading), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("displayPosition", "0");
            startActivity(intent);
        }
        getStateList();
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    selectedStateCode="0";
                    assetCategoryTV.setVisibility(View.GONE);
                    linearAssetCategory.setVisibility(View.GONE);
                    workTypeTV.setVisibility(View.GONE);
                    linearTypeOfWork.setVisibility(View.GONE);
                    finYearTV.setVisibility(View.GONE);
                    linearFinancialYear.setVisibility(View.GONE);
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);

                    listAssetCategoryID.clear();
                    listAssetCategoryName.clear();
                    listWorkTypeID.clear();
                    listWorkTypeName.clear();
                    listFinYearName.clear();
                    listAsset.clear();
                    listAssetID.clear();
                    listAssetName.clear();
                    listWorkCode.clear();

                    spinnerDistrict.setAdapter(null);
                    spinnerBlock.setAdapter(null);
                    spinnerPanchayat.setAdapter(null);
                    spinnerAssetCategory.setAdapter(null);
                    spinnerWorkType.setAdapter(null);
                    spinnerFinYear.setAdapter(null);
                    spinnerAssetID.setAdapter(null);
                    
                }
                else {
                    StateModel stateModel = ((StateAdapter) spinnerState.getAdapter()).getItem(position);
                    selectedStateCode = stateModel.getStateCode();
                    if (connectionDetector.isConnectingToInternet()) {
                        getDistrictList(selectedStateCode, "NEWREQUEST", financialYear);
                    } else
                        Toast.makeText(getActivity(), getContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    assetCategoryTV.setVisibility(View.GONE);
                    linearAssetCategory.setVisibility(View.GONE);
                    workTypeTV.setVisibility(View.GONE);
                    linearTypeOfWork.setVisibility(View.GONE);
                    finYearTV.setVisibility(View.GONE);
                    linearFinancialYear.setVisibility(View.GONE);
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);

                    listAssetCategoryID.clear();
                    listAssetCategoryName.clear();
                    listWorkTypeID.clear();
                    listWorkTypeName.clear();
                    listFinYearName.clear();
                    listAsset.clear();
                    listAssetID.clear();
                    listAssetName.clear();
                    listWorkCode.clear();
                    spinnerBlock.setAdapter(null);
                    spinnerPanchayat.setAdapter(null);
                    spinnerAssetCategory.setAdapter(null);
                    spinnerWorkType.setAdapter(null);
                    spinnerFinYear.setAdapter(null);
                    spinnerAssetID.setAdapter(null);
                }
                else {
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
                if(position==0){
                    assetCategoryTV.setVisibility(View.GONE);
                    linearAssetCategory.setVisibility(View.GONE);
                    workTypeTV.setVisibility(View.GONE);
                    linearTypeOfWork.setVisibility(View.GONE);
                    finYearTV.setVisibility(View.GONE);
                    linearFinancialYear.setVisibility(View.GONE);
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);


                    listAssetCategoryID.clear();listAssetCategoryName.clear();
                    listWorkTypeID.clear();listWorkTypeName.clear();
                    listFinYearName.clear();listAsset.clear();
                    listAssetID.clear();listAssetName.clear();
                    listWorkCode.clear();


                    spinnerPanchayat.setAdapter(null);
                    spinnerAssetCategory.setAdapter(null);
                    spinnerWorkType.setAdapter(null);
                    spinnerFinYear.setAdapter(null);
                    spinnerAssetID.setAdapter(null);
                }
                else {
                    BlockModel blockModel = ((BlockAdapter) spinnerBlock.getAdapter()).getItem(position);
                    selectedBlockCode = blockModel.getBlockCode();
                    if (connectionDetector.isConnectingToInternet())
                        getPanchayatList(selectedBlockCode, financialYear);
                    else
                        Toast.makeText(getActivity(), getContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        
        spinnerPanchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    assetCategoryTV.setVisibility(View.GONE);
                    linearAssetCategory.setVisibility(View.GONE);
                    workTypeTV.setVisibility(View.GONE);
                    linearTypeOfWork.setVisibility(View.GONE);
                    finYearTV.setVisibility(View.GONE);
                    linearFinancialYear.setVisibility(View.GONE);
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);
                    listAssetCategoryID.clear();
                    listAssetCategoryName.clear();
                    listWorkTypeID.clear();
                    listWorkTypeName.clear();
                    listFinYearName.clear();
                    listAsset.clear();
                    listAssetID.clear();
                    listAssetName.clear();
                    listWorkCode.clear();


                    spinnerAssetCategory.setAdapter(null);
                    spinnerWorkType.setAdapter(null);
                    spinnerFinYear.setAdapter(null);
                    spinnerAssetID.setAdapter(null);
                }
                else {
                    PanchayatModel panchayatModel = ((PanchayatAdapter) spinnerPanchayat.getAdapter()).getItem(position);
                    selectedPanchayatCode = panchayatModel.getPanchayatCode();
                    if (connectionDetector.isConnectingToInternet()) 
                        new InitPanchayatAssetTask(selectedPanchayatCode).execute();
                    else
                        Toast.makeText(getActivity(), getContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerAssetCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    workTypeTV.setVisibility(View.GONE);
                    linearTypeOfWork.setVisibility(View.GONE);
                    finYearTV.setVisibility(View.GONE);
                    linearFinancialYear.setVisibility(View.GONE);
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);


                    listWorkTypeID.clear();listWorkTypeName.clear();
                    listFinYearName.clear();listAsset.clear();listAssetID.clear();
                    listAssetName.clear();listWorkCode.clear();

                    spinnerWorkType.setAdapter(null);
                    spinnerFinYear.setAdapter(null);
                    spinnerAssetID.setAdapter(null);
                }

               else  {
                    workTypeTV.setVisibility(View.VISIBLE);
                    linearTypeOfWork.setVisibility(View.VISIBLE);
                    populateSpinnerWorkType();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerWorkType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    finYearTV.setVisibility(View.GONE);
                    linearFinancialYear.setVisibility(View.GONE);
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);
                    listFinYearName.clear();listAsset.clear();
                    listAssetID.clear();listAssetName.clear();
                    listWorkCode.clear();
                    spinnerFinYear.setAdapter(null);
                    spinnerAssetID.setAdapter(null);
                }
              else  {
                    finYearTV.setVisibility(View.VISIBLE);
                    linearFinancialYear.setVisibility(View.VISIBLE);
                    populateSpinnerFinYear();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerFinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    assetIDTV.setVisibility(View.GONE);
                    linearAssetWork.setVisibility(View.GONE);
                    listAsset.clear();listAssetID.clear();listAssetName.clear();listWorkCode.clear();
                    spinnerAssetID.setAdapter(null);
                }
                else  {
                    assetIDTV.setVisibility(View.VISIBLE);
                    linearAssetWork.setVisibility(View.VISIBLE);
                    populateSpinnerAssetID();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        spinnerAssetID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String categoryName="",workType = "",finYear="",assetID="",workCode="";
                if(listAssetCategoryID.size()>0)
                 categoryName = listAssetCategoryID.get(spinnerAssetCategory.getSelectedItemPosition());
                if(listWorkTypeID.size()>0)
                 workType = listWorkTypeID.get(spinnerWorkType.getSelectedItemPosition());
                if(listFinYearName.size()>0)
                 finYear = listFinYearName.get(spinnerFinYear.getSelectedItemPosition());
                if(listAssetID.size()>0)
                 assetID = listAssetID.get(spinnerAssetID.getSelectedItemPosition());
                if(listWorkCode.size()>0)
                 workCode = listWorkCode.get(spinnerAssetID.getSelectedItemPosition());

                if (categoryName.equals(getContext().getString(R.string.all))) {
                    categoryName = "";
                }
                if (workType.equals(getContext().getString(R.string.all))) {
                    workType = "";
                }
                if (finYear.equals(getContext().getString(R.string.all))) {
                    finYear = "";
                }
                if (assetID.equals(getContext().getString(R.string.all))) {
                    assetID = "";
                    workCode = "";
                }


                if (connectionDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(getActivity(), ViewSearchedAssetsActivity.class);
                    intent.putExtra("category", categoryName);
                    intent.putExtra("finYear", finYear);
                    intent.putExtra("asset", assetID);
                    intent.putExtra("workType", workType);
                    intent.putExtra("workCode", workCode);
                    intent.putExtra("cLong", cLong);
                    intent.putExtra("cLat", cLat);
                    intent.putExtra("block", selectedBlockCode);
                    intent.putExtra("panchayatName", selectedPanchayatName);
                    intent.putExtra("panchayat", selectedPanchayatCode);
                    startActivity(intent);

                }
                else
                    MyAlert.showAlert(getContext(), R.mipmap.icon_warning, getContext().getString(R.string.warning), getContext().getString(R.string.no_internet),strActivityCode+"01");


            }
        });
        return rootView;
    }
    public void getStateList() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getContext().getString(R.string.please_wait));
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
                            alState.add(0, new StateModel(getContext().getString(R.string.select)));
                            populateSpinnerState(alState, spinnerState);
                            dialog.dismiss();
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            Toast.makeText(getContext(), statusMessage, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<StateModel>> call, Throwable t) {
                Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }
    public void getDistrictList(String stateCode, String requestId, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getContext().getString(R.string.please_wait));
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
                            alDistrict.add(0, new DistrictModel(getContext().getString(R.string.select)));
                            populateSpinnerDistrict(alDistrict, spinnerDistrict);
                            getBlockList(stateCode, requestId, finYear);
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            Toast.makeText(getContext(), statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<DistrictModel>> call, Throwable t) {
                Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }
    public void getBlockList(String stateCode, String requestId, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getContext().getString(R.string.please_wait));
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
                            Toast.makeText(getContext(), statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<BlockModel>> call, Throwable t) {
                Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }
    @SuppressLint("Range")
    private void getBlockListByDistrictCode(String districtCode) {
        ArrayList<BlockModel> alBlock = new ArrayList<>();
        alBlock.add(0, new BlockModel(getContext().getString(R.string.select)));
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
    public void getPanchayatList(String blockCode, String finYear) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getContext().getString(R.string.please_wait));
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
                            alPanchayat.add(0, new PanchayatModel(getContext().getString(R.string.select)));
                            populateSpinnerPanchayat(alPanchayat, spinnerPanchayat);
                        } catch (Exception e) {
                            statusMessage = e.getMessage();
                            Toast.makeText(getContext(), statusMessage, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PanchayatModel>> call, Throwable t) {
                Toast.makeText(getContext(), getContext().getString(R.string.data_not_found) + "\n" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }
    @SuppressLint("StaticFieldLeak")
    class InitPanchayatAssetTask extends AsyncTask<Void, Void, String> {
        String gpcode;
        final private ProgressDialog dialog = new ProgressDialog(getActivity());

        InitPanchayatAssetTask(String gpcode) {
            this.gpcode = gpcode;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getContext().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response;
            try {
                String apiUrl =Api.PANCHAYAT_ASSET_URL+ URLEncoder.encode(this.gpcode, "UTF-8");
                response = getResponse(apiUrl);
                if (statusCode == 200) {
                    JSONArray assets = new JSONArray(response);
                    try {
                        dbWriter.beginTransaction();
                        dbWriter.delete(DbHelper.TABLE_NAME_ASSET_DETAIL, null, null);
                        for (int a = 0; a < assets.length(); a++) {
                            JSONObject asset = assets.getJSONObject(a);
                            ContentValues assetValues = new ContentValues();
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_NAME, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_NAME));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID));
                            assetValues.put(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME, asset.getString(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME));
                            dbWriter.insert(DbHelper.TABLE_NAME_ASSET_DETAIL, null, assetValues);
                            assetValues.clear();
                        }

                        dbWriter.setTransactionSuccessful();
                        dbWriter.endTransaction();

                        if (assets.length() > 0) {
                            return "true";
                        } else {
                            return "norecord";
                        }

                    } catch (SQLiteConstraintException e) {
                        dbWriter.endTransaction();
                        return "Error: " + e.getMessage();
                    }
                } else {
                    return getContext().getString(R.string.data_not_found);
                }
            } catch (Exception e) {
                return "Error " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result.contains("true")) {
                assetCategoryTV.setVisibility(View.VISIBLE);
                linearAssetCategory.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                populateSpinnerAssetCategory();
            } else {
                assetCategoryTV.setVisibility(View.GONE);
                linearAssetCategory.setVisibility(View.GONE);
                workTypeTV.setVisibility(View.GONE);
                linearTypeOfWork.setVisibility(View.GONE);
                finYearTV.setVisibility(View.GONE);
                linearFinancialYear.setVisibility(View.GONE);
                assetIDTV.setVisibility(View.GONE);
                linearAssetWork.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getContext().getString(R.string.data_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void populateSpinnerState(ArrayList<StateModel> alStateModel, Spinner sp) {
        if (!alStateModel.isEmpty() && alStateModel.size() > 0) {
            StateAdapter dbAdapter = new StateAdapter(getActivity(), android.R.layout.simple_spinner_item, alStateModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

    private void populateSpinnerDistrict(ArrayList<DistrictModel> alDistrictModel, Spinner sp) {
        if (!alDistrictModel.isEmpty() && alDistrictModel.size() > 0) {
            DistrictAdapter dbAdapter = new DistrictAdapter(getActivity(), android.R.layout.simple_spinner_item, alDistrictModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);
        }
    }

    private void populateSpinnerBlock(ArrayList<BlockModel> alBlockModel, Spinner sp) {
        if (!alBlockModel.isEmpty() && alBlockModel.size() > 0) {
            BlockAdapter dbAdapter = new BlockAdapter(getActivity(), android.R.layout.simple_spinner_item, alBlockModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }

    private void populateSpinnerPanchayat(ArrayList<PanchayatModel> alPanchayatModel, Spinner sp) {
        if (!alPanchayatModel.isEmpty() && alPanchayatModel.size() > 0) {
            PanchayatAdapter dbAdapter = new PanchayatAdapter(getActivity(), android.R.layout.simple_spinner_item, alPanchayatModel);
            dbAdapter.notifyDataSetChanged();
            sp.setAdapter(dbAdapter);

        }
    }
    private void populateSpinnerAssetCategory() {
        listAssetCategoryID.clear();
        listAssetCategoryName.clear();
        String query1 = "select * from tbl_asset_detail group by work_category_id order by category_name";
        Cursor cursor = dbWriter.rawQuery(query1, null);

        listAssetCategoryID.add(getContext().getString(R.string.all));
        listAssetCategoryName.add(getContext().getString(R.string.all));
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    listAssetCategoryID.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID)));
                    listAssetCategoryName.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        final CustomListingAdapter spinnerArrayAdapter = new CustomListingAdapter(getActivity(), listAssetCategoryName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerAssetCategory.setAdapter(spinnerArrayAdapter);
    }

    private void populateSpinnerWorkType() {
        String groupby = "sub_category";
        String orderby = "sub_category";
        String[] query1 = {DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID, DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_NAME,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME};
        Cursor cursor = dbReader.query(DbHelper.TABLE_NAME_ASSET_DETAIL, query1, null, null, groupby, null, orderby);
        listWorkTypeID.clear();
        listWorkTypeName.clear();
        listWorkTypeID.add(getContext().getString(R.string.all));
        listWorkTypeName.add(getContext().getString(R.string.all));
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    listWorkTypeID.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE)));
                    listWorkTypeName.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        final CustomListingAdapter spinnerArrayAdapter = new CustomListingAdapter(getActivity(), listWorkTypeName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkType.setAdapter(spinnerArrayAdapter);
    }

    private void populateSpinnerFinYear() {
        String groupby = "work_financial_year";
        String orderby = "work_financial_year";
        String[] query1 = {DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID, DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_NAME,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME};
        Cursor cursor = dbReader.query(DbHelper.TABLE_NAME_ASSET_DETAIL, query1, null, null, groupby, null, orderby);
        listFinYearName.clear();
        listFinYearName.add(getContext().getString(R.string.all));
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    listFinYearName.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR)));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }


        final CustomListingAdapter spinnerArrayAdapter = new CustomListingAdapter(getActivity(), listFinYearName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFinYear.setAdapter(spinnerArrayAdapter);
    }


    private void populateSpinnerAssetID() {
        String groupby = "work_name";
        String orderby = "work_name";
        String[] query1 = {DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID, DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_NAME,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY,
                DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID, DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME};
        Cursor cursor = dbReader.query(DbHelper.TABLE_NAME_ASSET_DETAIL, query1, null, null, groupby, null, orderby);


        listAsset.clear();listAssetID.clear();listWorkCode.clear();listAssetName.clear();
        listAsset.add(getContext().getString(R.string.all));listAssetID.add(getContext().getString(R.string.all));
        listWorkCode.add("");listAssetName.add(getContext().getString(R.string.all));
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    String assetName;
                    assetName = getContext().getString(R.string.asset_name) + " : " + cursor.getString(cursor.getColumnIndex((DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_NAME)));
                    assetName += "\n" + getContext().getString(R.string.asset_code) + " : " + cursor.getString(cursor.getColumnIndex((DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID)));
                    assetName += "\n" + getContext().getString(R.string.work_name) + " : " + cursor.getString(cursor.getColumnIndex((DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME)));
                    assetName += "\n" +getContext().getString(R.string.work_code) + " : " + cursor.getString(cursor.getColumnIndex((DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE)));
                    listAsset.add(assetName);
                    listAssetID.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID)));
                    listWorkCode.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE)));
                    listAssetName.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_ASSET_ID))
                            + cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_CODE))
                            +cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME_ASSET_DETAIL_WORK_NAME)));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }


        final CustomListingAdapter spinnerArrayAdapter = new CustomListingAdapter(getActivity(), listAsset);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerAssetID.setAdapter(spinnerArrayAdapter);
    }
 
    @Override
    public void onDestroy() {
        dbWriter.close();
        super.onDestroy();
    }

}