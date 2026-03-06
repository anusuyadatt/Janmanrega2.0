package nic.hp.ccmgnrega

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Html
import android.util.Pair
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ExpandableListAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nic.hp.ccmgnrega.adapter.ComplaintStatusExpandableListAdapter
import nic.hp.ccmgnrega.adapter.StateAdapter
import nic.hp.ccmgnrega.common.Api
import nic.hp.ccmgnrega.common.Constant
import nic.hp.ccmgnrega.common.Helper
import nic.hp.ccmgnrega.common.MyAlert
import nic.hp.ccmgnrega.common.TokenInterceptor
import nic.hp.ccmgnrega.databinding.ActivityViewComplaintSectionBinding
import nic.hp.ccmgnrega.model.AssetComplaintStatusData
import nic.hp.ccmgnrega.model.JobComplaintStatusData
import nic.hp.ccmgnrega.model.StateModel
import nic.hp.ccmgnrega.model.TokenData
import nic.hp.ccmgnrega.webview.BasicAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewComplaintSection : AppCompatActivity() {
    lateinit var binding: ActivityViewComplaintSectionBinding
    var expandableListAdapter: ExpandableListAdapter? = null
    var alComplaintIdList = ArrayList<String>()
    val complaintDetailMap = HashMap<String, ArrayList<Pair<String, String>>>()
    var lastExpandedPosition = -1
    var strActivityCode = "011-"
    var selectedStateCode="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewComplaintSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadActionBar()
        //  binding.heading.text=" "+applicationContext.getString(R.string.view_complaint_status)
        getStateList()
        binding.spinnerState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position == 0) {
                    selectedStateCode = "0"
                } else {
                    val stateModel = (binding.spinnerState.getAdapter() as StateAdapter).getItem(position)
                    selectedStateCode = stateModel!!.stateCode
                   // Toast.makeText(applicationContext,""+selectedStateCode,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        binding.searchButton.setOnClickListener(View.OnClickListener {
            var strMobileNo=binding.etComplaintId.text.toString()
            if(selectedStateCode=="0")
                MyAlert.showAlert(this@ViewComplaintSection, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select_state), strActivityCode + "01")
            else if(strMobileNo.isEmpty())
                MyAlert.showAlert(this@ViewComplaintSection, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_enter_mobile_no), strActivityCode + "02")
             else if(strMobileNo.length<10)
                MyAlert.showAlert(this@ViewComplaintSection, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_enter_valid_mobile_no), strActivityCode + "03")
          else
            getToken(selectedStateCode, strMobileNo) //  getToken("33","7818932374")6267553365
        })

        binding.expendableList.setOnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition !== -1 && groupPosition != lastExpandedPosition)
                binding.expendableList.collapseGroup(lastExpandedPosition)
            lastExpandedPosition = groupPosition
        }
    }



    private fun getToken(stateCode: String, mobileno: String) {
        Constant.startVolleyDialog(this)
        val call = Retrofit.Builder()
            .baseUrl(Helper.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
            .getToken(SplashActivity.strTName, SplashActivity.strTPwd, Api.grant_type)
        call.enqueue(object : Callback<TokenData> {
            override fun onResponse(call: Call<TokenData>, response: Response<TokenData>) {
                if (response.isSuccessful) {
                    getJobCardComplaintStatus(stateCode, mobileno, response.body()!!.access_token)
                } else {
                    MyAlert.showAlert(
                        this@ViewComplaintSection,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.submit_warning),
                        applicationContext.getString(R.string.network_req_failed),
                        strActivityCode + "04"
                    )
                }
                Constant.dismissVolleyDialog()
            }

            override fun onFailure(call: Call<TokenData>, t: Throwable) {
                Constant.dismissVolleyDialog()
                MyAlert.showAlert(
                    this@ViewComplaintSection,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.submit_warning),
                    applicationContext.getString(R.string.network_req_failed),
                    strActivityCode + "05"
                )
            }
        })
    }


    private fun getJobCardComplaintStatus(stateCode: String, mobileno: String, token: String) {
        Constant.startVolleyDialog(this@ViewComplaintSection)
        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(token))
            .build()
        val call = Retrofit.Builder()
            .baseUrl(Helper.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Api::class.java)
            .getJobComplaintStatusData(stateCode, mobileno)
        call.enqueue(object : Callback<List<JobComplaintStatusData?>?> {
            override fun onResponse(
                call: Call<List<JobComplaintStatusData?>?>,
                response: Response<List<JobComplaintStatusData?>?>
            ) {
                if (response.isSuccessful) {
                    binding.expendableList.visibility = View.VISIBLE
                    alComplaintIdList.clear()
                    complaintDetailMap.clear()
                    val alJobComplaintStatusData =
                        response.body() as java.util.ArrayList<JobComplaintStatusData?>?
                    for (i in 0 until alJobComplaintStatusData!!.size) {
                        val jobComplaintStatusObject = alJobComplaintStatusData.get(i)
                        if (jobComplaintStatusObject!!.complaint_id != null && !jobComplaintStatusObject.complaint_id.isEmpty())
                            populateJobComplaintDetailMap(jobComplaintStatusObject!!)
                    }
                    getAssetComplaintStatus(stateCode, mobileno, token)
                } else {
                    MyAlert.showAlert(
                        this@ViewComplaintSection,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.submit_warning),
                        applicationContext.getString(R.string.network_req_failed),
                        strActivityCode + "06"
                    )
                }

                Constant.dismissVolleyDialog()
            }

            override fun onFailure(call: Call<List<JobComplaintStatusData?>?>, t: Throwable) {

                Constant.dismissVolleyDialog()
                MyAlert.showAlert(
                    this@ViewComplaintSection,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.submit_warning),
                    applicationContext.getString(R.string.network_req_failed),
                    strActivityCode + "07"
                )
            }
        })
    }

    private fun getAssetComplaintStatus(stateCode: String, mobileno: String, token: String) {
        Constant.startVolleyDialog(this@ViewComplaintSection)
        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(token))
            .build()
        val call = Retrofit.Builder()
            .baseUrl(Helper.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Api::class.java)
            .getAssetComplaintStatus(stateCode, mobileno)
        call.enqueue(object : Callback<List<AssetComplaintStatusData?>?> {
            override fun onResponse(
                call: Call<List<AssetComplaintStatusData?>?>,
                response: Response<List<AssetComplaintStatusData?>?>
            ) {
                if (response.isSuccessful) {
                    val alAssetComplaintStatusData =
                        response.body() as java.util.ArrayList<AssetComplaintStatusData?>?
                    for (i in 0 until alAssetComplaintStatusData!!.size) {
                        val assetComplaintStatusObject = alAssetComplaintStatusData.get(i)
                        if (assetComplaintStatusObject!!.complaint_id != null && !assetComplaintStatusObject.complaint_id.isEmpty())
                            populateAssetComplaintDetailMap(assetComplaintStatusObject!!)
                    }
                    if(alComplaintIdList.size>0 && complaintDetailMap.size>0) {
                        expandableListAdapter = ComplaintStatusExpandableListAdapter(
                            applicationContext,
                            alComplaintIdList,
                            complaintDetailMap
                        )

                        binding.expendableList.setAdapter(expandableListAdapter)
                    }
                    else {
                        alComplaintIdList.clear()
                        complaintDetailMap.clear()
                        binding.expendableList.visibility = View.GONE
                        MyAlert.showAlert(
                            this@ViewComplaintSection,
                            R.mipmap.icon_info,
                            applicationContext.getString(R.string.submit_info),
                            applicationContext.getString(R.string.no_complaint_found),
                            strActivityCode + "08"
                        )
                    }

                } else {
                    MyAlert.showAlert(
                        this@ViewComplaintSection,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.submit_warning),
                        applicationContext.getString(R.string.network_req_failed),
                        strActivityCode + "09"
                    )
                }

                Constant.dismissVolleyDialog()
            }

            override fun onFailure(call: Call<List<AssetComplaintStatusData?>?>, t: Throwable) {

                Constant.dismissVolleyDialog()
                MyAlert.showAlert(
                    this@ViewComplaintSection,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.submit_warning),
                    applicationContext.getString(R.string.network_req_failed),
                    strActivityCode + "010"
                )
            }
        })
    }

    private fun populateJobComplaintDetailMap(jobComplaintStatusData: JobComplaintStatusData) {
        alComplaintIdList.add(jobComplaintStatusData!!.complaint_id)
        var alComplaintDetailObjectList = ArrayList<Pair<String, String>>()
        var strTaskStatusByLabel=""
        var strTaskStatusSinceLabel=""

        if (jobComplaintStatusData.status != null && !jobComplaintStatusData.status.isEmpty()) {

            if(jobComplaintStatusData.status=="P"){
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status), applicationContext.getString(R.string.pending)))
                strTaskStatusByLabel=applicationContext.getString(R.string.pending_at)
                strTaskStatusSinceLabel= applicationContext.getString(R.string.pending_since)
            }
            else if(jobComplaintStatusData.status=="D"){
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status), applicationContext.getString(R.string.closed)))

                strTaskStatusByLabel=applicationContext.getString(R.string.closed_by)
                strTaskStatusSinceLabel= applicationContext.getString(R.string.closed_at)
            }
            else if(jobComplaintStatusData.status=="F"){
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status), applicationContext.getString(R.string.forwarded)))
                strTaskStatusByLabel=applicationContext.getString(R.string.forwarded_by)
                strTaskStatusSinceLabel= applicationContext.getString(R.string.forwarded_at)
            }
            else
            {
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status),""))
                strTaskStatusByLabel=""
                 strTaskStatusSinceLabel=""

            }

        }
        if (jobComplaintStatusData.jobcardno != null && !jobComplaintStatusData.jobcardno.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.jobCardNumber),
                    jobComplaintStatusData.jobcardno
                )
            )
        if (jobComplaintStatusData.worker_name != null && !jobComplaintStatusData.worker_name.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.worker_name),
                    jobComplaintStatusData.worker_name
                )
            )
        if (jobComplaintStatusData.complaint_category != null && !jobComplaintStatusData.complaint_category.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.complaint_category),
                    jobComplaintStatusData.complaint_category
                )
            )
        if (jobComplaintStatusData.complaint_sub_category != null && !jobComplaintStatusData.complaint_sub_category.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.complaint_sub_category),
                    jobComplaintStatusData.complaint_sub_category
                )
            )
        if (jobComplaintStatusData.complaint_description != null && !jobComplaintStatusData.complaint_description.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.complaint_description),
                    jobComplaintStatusData.complaint_description
                )
            )
        if (jobComplaintStatusData.complaint_raised_since != null && !jobComplaintStatusData.complaint_raised_since.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.complaint_raised_since),
                    jobComplaintStatusData.complaint_raised_since
                )
            )
        if (jobComplaintStatusData.pending_at != null && !jobComplaintStatusData.pending_at.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    strTaskStatusByLabel,
                    jobComplaintStatusData.pending_at
                )
            )
        if (jobComplaintStatusData.pending_since != null && !jobComplaintStatusData.pending_since.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                 strTaskStatusSinceLabel,
                    jobComplaintStatusData.pending_since
                )
            )
        complaintDetailMap.put(jobComplaintStatusData!!.complaint_id, alComplaintDetailObjectList)
    }

    private fun populateAssetComplaintDetailMap(assetComplaintStatusData: AssetComplaintStatusData) {
        alComplaintIdList.add(assetComplaintStatusData!!.complaint_id)
        var alComplaintDetailObjectList = ArrayList<Pair<String, String>>()
        var strTaskStatusByLabel=""
        var strTaskStatusSinceLabel=""

        if (assetComplaintStatusData.status != null && !assetComplaintStatusData.status.isEmpty()) {
            if(assetComplaintStatusData.status=="P"){
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status), applicationContext.getString(R.string.pending)))
                strTaskStatusByLabel=applicationContext.getString(R.string.pending_at)
                strTaskStatusSinceLabel= applicationContext.getString(R.string.pending_since)
            }
           else if(assetComplaintStatusData.status=="D"){
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status),  applicationContext.getString(R.string.closed)))
                strTaskStatusByLabel=applicationContext.getString(R.string.closed_by)
                strTaskStatusSinceLabel= applicationContext.getString(R.string.closed_at)
            }
            else if(assetComplaintStatusData.status=="F"){
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status),  applicationContext.getString(R.string.forwarded)))
                strTaskStatusByLabel=applicationContext.getString(R.string.forwarded_by)
                strTaskStatusSinceLabel= applicationContext.getString(R.string.forwarded_at)
            }
            else
            {
                alComplaintDetailObjectList.add(Pair<String, String>(applicationContext.getString(R.string.status),""))
                strTaskStatusByLabel=""
                strTaskStatusSinceLabel=""

            }

        }
        if (assetComplaintStatusData.work_name != null && !assetComplaintStatusData.work_name.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.work_name),
                    assetComplaintStatusData.work_name
                )
            )
        if (assetComplaintStatusData.work_code != null && !assetComplaintStatusData.work_code.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.work_code),
                    assetComplaintStatusData.work_code
                )
            )
        if (assetComplaintStatusData.rating_provided != null && !assetComplaintStatusData.rating_provided.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.rating_provided),
                    assetComplaintStatusData.rating_provided
                )
            )
        if (assetComplaintStatusData.asset_visible != null && !assetComplaintStatusData.asset_visible.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.asset_visible),
                    assetComplaintStatusData.asset_visible
                )
            )
        if (assetComplaintStatusData.work_complete != null && !assetComplaintStatusData.work_complete.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.work_complete),
                    assetComplaintStatusData.work_complete
                )
            )
        if (assetComplaintStatusData.cib_exists != null && !assetComplaintStatusData.cib_exists.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.cib_exists),
                    assetComplaintStatusData.cib_exists
                )
            )
        if (assetComplaintStatusData.description_match != null && !assetComplaintStatusData.description_match.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.description_match),
                    assetComplaintStatusData.description_match
                )
            )
        if (assetComplaintStatusData.asset_useful != null && !assetComplaintStatusData.asset_useful.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.asset_useful),
                    assetComplaintStatusData.asset_useful
                )
            )
        if (assetComplaintStatusData.complaint_raised_since != null && !assetComplaintStatusData.complaint_raised_since.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    applicationContext.getString(R.string.complaint_raised_since),
                    assetComplaintStatusData.complaint_raised_since
                )
            )
        if (assetComplaintStatusData.pending_at != null && !assetComplaintStatusData.pending_at.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                    strTaskStatusByLabel,
                    assetComplaintStatusData.pending_at
                )
            )
        if (assetComplaintStatusData.pending_since != null && !assetComplaintStatusData.pending_since.isEmpty())
            alComplaintDetailObjectList.add(
                Pair<String, String>(
                   strTaskStatusSinceLabel,
                    assetComplaintStatusData.pending_since
                )
            )
        complaintDetailMap.put(assetComplaintStatusData!!.complaint_id, alComplaintDetailObjectList)
    }

    fun getStateList() {
        val dialog = ProgressDialog(this@ViewComplaintSection)
        dialog.setMessage(applicationContext.getString(R.string.please_wait))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val client = OkHttpClient.Builder()
            .addInterceptor(
                BasicAuthInterceptor(
                    SplashActivity.strNregaName,
                    SplashActivity.strNregaPwd
                )
            )
            .build()
        val api = Retrofit.Builder()
            .baseUrl(Helper.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Api::class.java)
        val call = api.stateList
        call.enqueue(object : Callback<List<StateModel?>?> {
            override fun onResponse(
                call: Call<List<StateModel?>?>,
                response: Response<List<StateModel?>?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val alState = response.body() as java.util.ArrayList<StateModel?>?
                            alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                            populateSpinnerState(alState, binding.spinnerState)
                            dialog.dismiss()
                        } catch (e: Exception) {
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@ViewComplaintSection)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()
                                    finish()
                                },
                                strActivityCode + "011"
                            )
                        }
                    } else {
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@ViewComplaintSection)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()
                                finish()
                            },
                            strActivityCode + "012"
                        )
                    }
                } else {
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@ViewComplaintSection)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()
                            finish()
                        },
                        strActivityCode + "013"
                    )
                }
            }

            override fun onFailure(call: Call<List<StateModel?>?>, t: Throwable) {
                val dialogAlert2 = Dialog(this@ViewComplaintSection)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()
                        finish()
                    },
                    strActivityCode + "014"
                )
                dialog.dismiss()
            }
        })
    }

    private fun populateSpinnerState(alStateModel: java.util.ArrayList<StateModel?>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            val dbAdapter = StateAdapter(this@ViewComplaintSection, android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }
    fun loadActionBar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.icon_white_back)
        val d = resources.getDrawable(R.drawable.header_bg2)
        supportActionBar!!.setBackgroundDrawable(d)
        supportActionBar!!.title =
            Html.fromHtml("<center><font color='#FFFFFF'><b>&nbsp;" + applicationContext.getString(R.string.app_name) + "</b></font></center>")
        supportActionBar!!.setIcon(R.mipmap.ic_india_emblem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true

    }
}

