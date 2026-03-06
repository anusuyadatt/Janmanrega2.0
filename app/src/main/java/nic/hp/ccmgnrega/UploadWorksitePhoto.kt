package nic.hp.ccmgnrega

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.transition.Visibility
import com.github.dhaval2404.imagepicker.ImagePicker
import nic.hp.ccmgnrega.adapter.EkycJobCardAdapter
import nic.hp.ccmgnrega.adapter.EkycWorkerAdapter
import nic.hp.ccmgnrega.adapter.ExemptionReasonAdapter
import nic.hp.ccmgnrega.adapter.LgdBlockAdapter
import nic.hp.ccmgnrega.adapter.LgdDistrictAdapter
import nic.hp.ccmgnrega.adapter.LgdPanchayatAdapter
import nic.hp.ccmgnrega.adapter.LgdStateAdapter
import nic.hp.ccmgnrega.adapter.MusterAdapter
import nic.hp.ccmgnrega.adapter.WorkAdapter
import nic.hp.ccmgnrega.common.Api
import nic.hp.ccmgnrega.common.Constant
import nic.hp.ccmgnrega.common.GPSTracker
import nic.hp.ccmgnrega.common.GlobalLocationService
import nic.hp.ccmgnrega.common.Helper
import nic.hp.ccmgnrega.common.MyAlert
import nic.hp.ccmgnrega.common.MySharedPref
import nic.hp.ccmgnrega.model.ConnectionDetector
import nic.hp.ccmgnrega.model.ExemptionReasonModel
import nic.hp.ccmgnrega.model.JobcardModel
import nic.hp.ccmgnrega.model.LgdBlockModel
import nic.hp.ccmgnrega.model.LgdDistrictModel
import nic.hp.ccmgnrega.model.LgdPanchayatModel
import nic.hp.ccmgnrega.model.LgdStateModel
import nic.hp.ccmgnrega.model.MusterModel
import nic.hp.ccmgnrega.model.UploadEkycExemptionModel
import nic.hp.ccmgnrega.model.WorkModel
import nic.hp.ccmgnrega.model.WorkerModel
import nic.hp.ccmgnrega.webview.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class UploadWorksitePhoto : AppCompatActivity() {
    var spinnerState: Spinner? = null
    var spinnerDistrict: Spinner? = null
    var spinnerBlock: Spinner? = null
    var spinnerPanchayat: Spinner? = null
    var spinnerWork: Spinner? = null
    var spinnerMuster: Spinner? = null
    var spinnerJobCard: Spinner? = null
    var spinnerWorker: Spinner? = null
    var spinnerReason: Spinner? = null
    var btnSubmit: Button?=null
    var imgCapture: ImageView?=null
    var llJobCard: LinearLayout?=null
    var llWorker: LinearLayout?=null
    var etDate: EditText?=null
    var heading: TextView?=null

    val strActivityCode="12-"

    var alStateList = ArrayList<LgdStateModel>()
    var alDistrictList = ArrayList<LgdDistrictModel>()
    var alBlockList = ArrayList<LgdBlockModel>()
    var alPanchayatList = ArrayList<LgdPanchayatModel>()
    var alExemptionReasonList = ArrayList<ExemptionReasonModel>()
    var alWorkList = ArrayList<WorkModel>()
    var alMusterList = ArrayList<MusterModel>()
    var alWorkerList = ArrayList<WorkerModel>()
    var alJobCardList = ArrayList<JobcardModel>()

    var stateCode="0"
    var stateLgdCode="0"

    var stateName=""

    var districtName=""
    var districtCode="0"
    var districtLgdCode="0"

    var blockName=""
    var blockCode="0"
    var blockLgdCode="0"

    var panchayatName=""
    var panchayatCode="0"
    var panchayatLgdCode="0"

    var workCode="0"
    var workName=""

    var musterCode="0"
    var musterName=""
    var dateFrom=""
    var dateTo=""

    var reasonCode="0"
    var reasonName=""

    var applicantId="0"
    var applicantName=""

    var strJobCardNumber=""

    var strLongitude = "0.0"
    var strLatitude = "0.0"

    var dLatitude = 0.0
    var dLongitude: Double = 0.0
    var dAccuracy: Double = 0.0
    var mTimer1: Timer? = null
    var mTt1: TimerTask? = null
    val mTimerHandler = Handler()
    val CAMERA_REQUEST: Int = 1888

    var connectionDetector: ConnectionDetector? = null
    var strBase64Image=""
    var exemptionDate=""

    var gpsTracker: GPSTracker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_worksite_photo)
        loadActionBar()
        findById()
        gpsTracker = GPSTracker(this@UploadWorksitePhoto)
        connectionDetector = ConnectionDetector(this@UploadWorksitePhoto)
        if (hasAllPermissions()) {
            enableGPS()                                 // already granted
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionsLauncher.launch(
                    arrayOf(                                          // 2️⃣ ask for both at once
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            }
            else {
                requestPermissionsLauncher.launch(
                    arrayOf(                                          // 2️⃣ ask for both at once
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )

            }

        }

        getStateList()

        heading!!.setText("  " + getApplicationContext().getString(R.string.ekyc_exemption))


        alExemptionReasonList.clear()
        alExemptionReasonList.add(ExemptionReasonModel("1","Device not working"))
      //  alExemptionReasonList.add(ExemptionReasonModel("2","Network not available"))
      //  alExemptionReasonList.add(ExemptionReasonModel("3","e-Kyc Pending"))
        alExemptionReasonList!!.add(0, ExemptionReasonModel("0",applicationContext.getString(R.string.select)))
        val  adapter= ExemptionReasonAdapter(
            applicationContext,
            alExemptionReasonList
        )
        spinnerReason!!.adapter=adapter

        spinnerState!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {

                    stateCode = "0"
                    stateLgdCode = "0"
                    stateName = ""

                    districtCode = "0"
                    districtLgdCode = "0"
                    districtName = ""

                    blockCode = "0"
                    blockLgdCode = "0"
                    blockName = ""

                    panchayatCode = "0"
                    panchayatLgdCode = "0"
                    panchayatName = ""

                    workCode="0"
                    workName=""

                    musterCode="0"
                    musterName=""

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"


                    spinnerDistrict!!.setAdapter(null)
                    spinnerBlock!!.setAdapter(null)
                    spinnerPanchayat!!.setAdapter(null)
                    spinnerWork!!.setAdapter(null)
                    spinnerMuster!!.setAdapter(null)
                    spinnerJobCard!!.setAdapter(null)
                    spinnerWorker!!.setAdapter(null)


                }
                else {
                    val stateModel = spinnerState!!.adapter.getItem(position) as LgdStateModel
                    stateCode = stateModel.stateCode!!
                    stateLgdCode = stateModel.lgdStateCode!!
                    stateName = stateModel.stateName!!
                    getDistrictList()
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }
        spinnerDistrict!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    districtCode = "0"
                    districtLgdCode = "0"
                    districtName = ""

                    blockCode = "0"
                    blockLgdCode = "0"
                    blockName = ""

                    panchayatCode = "0"
                    panchayatLgdCode = "0"
                    panchayatName = ""

                    workCode="0"
                    workName=""

                    musterCode="0"
                    musterName=""

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"


                    spinnerBlock!!.setAdapter(null)
                    spinnerPanchayat!!.setAdapter(null)
                    spinnerWork!!.setAdapter(null)
                    spinnerMuster!!.setAdapter(null)
                    spinnerJobCard!!.setAdapter(null)
                    spinnerWorker!!.setAdapter(null)
                }
                else {
                    val districtModel = spinnerDistrict!!.adapter.getItem(position) as LgdDistrictModel
                    districtCode = districtModel.districtCode!!
                    districtLgdCode = districtModel.lgdDistrictCode!!
                    districtName = districtModel.districtName!!
                    getBlockList()
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }
        spinnerBlock!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    blockCode = "0"
                    blockLgdCode = "0"
                    blockName = ""

                    panchayatCode = "0"
                    panchayatLgdCode = "0"
                    panchayatName = ""

                    workCode="0"
                    workName=""

                    musterCode="0"
                    musterName=""

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"

                    spinnerPanchayat!!.setAdapter(null)
                    spinnerWork!!.setAdapter(null)
                    spinnerMuster!!.setAdapter(null)
                    spinnerJobCard!!.setAdapter(null)
                    spinnerWorker!!.setAdapter(null)
                }
                else {
                    val blockModel = spinnerBlock!!.adapter.getItem(position) as LgdBlockModel
                    blockCode = blockModel.blockCode!!
                    blockLgdCode = blockModel.lgdBlockCode!!
                    blockName = blockModel.blockName!!
                    getPanchayatList()
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }
        spinnerPanchayat!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    panchayatCode = "0"
                    panchayatLgdCode = "0"
                    panchayatName = ""

                    workCode="0"
                    workName=""

                    musterCode="0"
                    musterName=""

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"

                    spinnerWork!!.setAdapter(null)
                    spinnerMuster!!.setAdapter(null)
                    spinnerJobCard!!.setAdapter(null)
                    spinnerWorker!!.setAdapter(null)
                }
                else {
                    val panchayatModel = spinnerPanchayat!!.adapter.getItem(position) as LgdPanchayatModel
                    panchayatCode = panchayatModel.panchayatCode!!
                    panchayatLgdCode = panchayatModel.lgdPanchayatCode!!
                    panchayatName = panchayatModel.panchayatName!!
                    getWorkList()
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }

        spinnerReason!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    reasonCode="0"
                    reasonName=""

                }
                else {
                    val exemptionReasonModel = spinnerReason!!.adapter.getItem(position) as ExemptionReasonModel
                    reasonCode = exemptionReasonModel.reasonId!!
                    reasonName = exemptionReasonModel.reasonName!!
                    if(reasonCode=="3"){
                        llJobCard!!.visibility=View.VISIBLE
                        llWorker!!.visibility=View.VISIBLE

                    }
                    else {
                        llJobCard!!.visibility = View.GONE
                        llWorker!!.visibility = View.GONE
                        strJobCardNumber=""
                        applicantName=""
                        applicantId="0"
                        spinnerJobCard!!.setSelection(0)
                        spinnerWorker!!.setSelection(0)
                    }



                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }
        spinnerWork!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    workCode="0"
                    workName=""

                    musterCode="0"
                    musterName=""

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"


                    spinnerMuster!!.setAdapter(null)
                    spinnerJobCard!!.setAdapter(null)
                    spinnerWorker!!.setAdapter(null)
                }
                else {
                    val workModel = spinnerWork!!.adapter.getItem(position) as WorkModel
                    workCode = workModel.workCode!!
                    workName = workModel.workName!!
                    getMusterList()


                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }
        spinnerMuster!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    musterCode="0"
                    musterName=""

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"

                    spinnerJobCard!!.setAdapter(null)
                    spinnerWorker!!.setAdapter(null)
                }
                else {
                    val musterModel = spinnerMuster!!.adapter.getItem(position) as MusterModel
                    musterCode = musterModel.musterCode!!
                    musterName = musterModel.musterName!!
                    dateFrom= musterModel.dt_from!!
                    dateTo= musterModel.dt_to!!
                    if(reasonCode=="3")
                        getJobCardList()


                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }


        spinnerJobCard!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {

                    strJobCardNumber=""

                    applicantName=""
                    applicantId="0"

                    spinnerWorker!!.setAdapter(null)

                }
                else {
                    val model = spinnerJobCard!!.adapter.getItem(position) as JobcardModel
                    strJobCardNumber = model.jobCardNumber!!
                    getApplicantList()

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }
        spinnerWorker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position==0) {
                    applicantName=""
                    applicantId="0"

                }
                else {
                    val model = spinnerWorker!!.adapter.getItem(position) as WorkerModel
                    applicantId = model.applicantId!!
                    applicantName = model.applicantName!!

                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle case when nothing is selected
            }
        }

        imgCapture!!.setOnClickListener {
          /*  ImagePicker.with(this@UploadWorksitePhoto)
                .crop()
                .cameraOnly() //User can only capture image using Camera
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .start(CAMERA_REQUEST)  */

            ImagePicker.with(this@UploadWorksitePhoto)
                .cameraOnly() //User can only capture image using Camera
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .start(CAMERA_REQUEST)
        }

        etDate!!.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    /// exemptionDate = "$dayOfMonth/${month + 1}/$year"
                    exemptionDate = "$year/${month + 1}/$dayOfMonth"
                    etDate!!.setText(exemptionDate)
                    Toast.makeText(
                        this@UploadWorksitePhoto,
                        exemptionDate,
                        Toast.LENGTH_LONG
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
        btnSubmit!!.setOnClickListener {
            if(strLatitude=="0.0"||strLongitude=="0.0") {
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(
                    this@UploadWorksitePhoto,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.enable_gps),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            dialogAlert2.dismiss()
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                            finish()
                        }
                    },
                    strActivityCode + "060"
                )

            }
            else if(stateCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select_state), strActivityCode + "046")
            else if(districtCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select_district), strActivityCode + "047")
            else if(blockCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select_block), strActivityCode + "048")
            else if(panchayatCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select_panchayat), strActivityCode + "049")
            else if(workCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select)+" "+applicationContext.getString(R.string.work_name), strActivityCode + "050")
            else if(musterCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select)+" "+applicationContext.getString(R.string.muster), strActivityCode + "051")
            else if(reasonCode=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select)+" "+applicationContext.getString(R.string.exemption_reason), strActivityCode + "058")
            else if(reasonCode=="3" && (strJobCardNumber!=null && strJobCardNumber.isEmpty()))
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select)+" "+applicationContext.getString(R.string.job_card), strActivityCode + "077")
            else if(reasonCode=="3" && applicantId=="0")
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select)+" "+applicationContext.getString(R.string.worker_name), strActivityCode + "078")

          /*  else if(exemptionDate==null || (exemptionDate!=null && exemptionDate.isEmpty()))
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_select)+" "+applicationContext.getString(R.string.exemption_date), strActivityCode + "059")
           */ else if(strBase64Image==null || (strBase64Image!=null && strBase64Image.isEmpty()))
                MyAlert.showAlert(this@UploadWorksitePhoto, R.mipmap.icon_warning, applicationContext.getString(R.string.submit_warning), applicationContext.getString(R.string.please_capture_photo), strActivityCode + "052")
            else
            {
                Toast.makeText(
                    this@UploadWorksitePhoto,
                    strLatitude+":"+strLongitude,
                    Toast.LENGTH_LONG
                ).show()

                if (connectionDetector!!.isConnectingToInternet()) {
                    val dialogAlert = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForCancelOk(
                        this@UploadWorksitePhoto,
                        R.mipmap.icon_info,
                        applicationContext.getString(R.string.submission_alert),
                        applicationContext.getString(R.string.submit_confirmation),
                        dialogAlert,
                        applicationContext.getString(R.string.submit),
                        object : View.OnClickListener {
                            override fun onClick(view: View?) {
                                dialogAlert.dismiss()
                                uploadEkycExemption()
                            }
                        },
                        applicationContext.getString(R.string.cancel),
                        object : View.OnClickListener {
                            override fun onClick(view: View?) {
                                dialogAlert.dismiss()
                            }
                        },
                        object : View.OnClickListener {
                            override fun onClick(view: View?) {
                                dialogAlert.dismiss()
                            }
                        },
                        strActivityCode + "053"
                    )
                } else MyAlert.showAlert(
                    this@UploadWorksitePhoto,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.submit_warning),
                    getApplicationContext().getString(R.string.no_internet),
                    strActivityCode + "054"
                )

            }

        }

    }


    fun getStateList() {
        alStateList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.lgdStateList
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonArray = JSONArray(responseString)
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alStateList.add(
                                                LgdStateModel(jsonObject.optString("state_code"),jsonObject.optString("lgd_st_code"),jsonObject.optString("State_Name"))
                                            )
                                        }
                                        if (alStateList.size > 0) {
                                            alStateList!!.add(0, LgdStateModel("0","0",applicationContext.getString(R.string.select)))
                                            val  adapter= LgdStateAdapter(
                                                applicationContext,
                                                alStateList
                                            )
                                            spinnerState!!.adapter=adapter
                                        }
                                        else {
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()
                                                    finish()
                                                },
                                                strActivityCode + "01"
                                            )

                                        }
                                    }
                                    else {
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()
                                                finish()
                                            },
                                            strActivityCode + "02"
                                        )
                                    }
                                }
                                else {
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()
                                            finish()
                                        },
                                        strActivityCode + "03"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
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
                                strActivityCode + "04"
                            )
                        }
                    } else {
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
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
                            strActivityCode + "05"
                        )
                    }
                } else {
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
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
                        strActivityCode + "06"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
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
                    strActivityCode + "07"
                )
                dialog.dismiss()
            }
        })
    }
    fun getDistrictList() {
        alDistrictList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getLgdDistrictList(stateLgdCode,getFinancialYear())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonArray = JSONArray(responseString)
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alDistrictList.add(
                                                LgdDistrictModel(jsonObject.optString("state_code"),jsonObject.optString("lgd_st_code"),jsonObject.optString("State_Name"),
                                                    jsonObject.optString("district_code"),jsonObject.optString("lgd_dist_code"),jsonObject.optString("district_name"))
                                            )
                                        }
                                        if (alDistrictList.size > 0) {
                                            alDistrictList!!.add(0, LgdDistrictModel("0","0","0","0","0",applicationContext.getString(R.string.select)))
                                            val  adapter= LgdDistrictAdapter(applicationContext, alDistrictList)
                                            spinnerDistrict!!.adapter=adapter
                                        }
                                        else {
                                            spinnerState!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()
                                                },
                                                strActivityCode + "08"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerState!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "09"
                                        )
                                    }
                                }
                                else {
                                    spinnerState!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "010"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerState!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "011"
                            )
                        }
                    } else {
                        spinnerState!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()

                            },
                            strActivityCode + "012"
                        )
                    }
                } else {
                    spinnerState!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "013"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerState!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "014"
                )
                dialog.dismiss()
            }
        })
    }
    fun getBlockList() {
        alBlockList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getLgdBlockList(stateLgdCode,districtLgdCode,getFinancialYear())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonArray = JSONArray(responseString)
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alBlockList.add(LgdBlockModel(jsonObject.optString("state_code"),jsonObject.optString("lgd_st_code"),jsonObject.optString("state_name"),
                                                jsonObject.optString("district_code"),jsonObject.optString("lgd_dist_code"),jsonObject.optString("district_name"),
                                                jsonObject.optString("block_code"),jsonObject.optString("lgd_block_code"),jsonObject.optString("block_name")))

                                        }
                                        if (alBlockList.size > 0) {
                                            alBlockList!!.add(0,
                                                LgdBlockModel("0","0","0","0","0","0","0","0",applicationContext.getString(R.string.select))
                                            )
                                            val  adapter=
                                                LgdBlockAdapter(applicationContext, alBlockList)
                                            spinnerBlock!!.adapter=adapter
                                        }
                                        else {
                                            spinnerDistrict!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()

                                                },
                                                strActivityCode + "015"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerDistrict!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "016"
                                        )
                                    }
                                }
                                else {
                                    spinnerDistrict!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "017"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerDistrict!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()
                                    // finish()
                                },
                                strActivityCode + "018"
                            )
                        }
                    } else {
                        spinnerDistrict!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
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
                    spinnerDistrict!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "019"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerDistrict!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "020"
                )
                dialog.dismiss()
            }
        })
    }
    fun getPanchayatList() {
        alPanchayatList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getLgdPanchayatList(stateLgdCode,districtLgdCode,blockLgdCode,getFinancialYear())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonArray = JSONArray(responseString)
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alPanchayatList.add(LgdPanchayatModel(jsonObject.optString("state_code"),jsonObject.optString("lgd_st_code"),jsonObject.optString("state_name"),
                                                jsonObject.optString("district_code"),jsonObject.optString("lgd_dist_code"),jsonObject.optString("district_name"),
                                                jsonObject.optString("block_code"),jsonObject.optString("lgd_block_code"),jsonObject.optString("block_name"),
                                                jsonObject.optString("Panchayat_code"),jsonObject.optString("lgd_panch_code"),jsonObject.optString("Panchayat_name")))

                                        }
                                        if (alPanchayatList.size > 0) {
                                            alPanchayatList!!.add(0,
                                                LgdPanchayatModel("0","0","0","0","0","0","0","0","0","0","0",applicationContext.getString(R.string.select))
                                            )
                                            val  adapter=
                                                LgdPanchayatAdapter(applicationContext, alPanchayatList)
                                            spinnerPanchayat!!.adapter=adapter
                                        }
                                        else {
                                            spinnerBlock!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()

                                                },
                                                strActivityCode + "021"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerBlock!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "022"
                                        )
                                    }
                                }
                                else {
                                    spinnerBlock!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "023"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerBlock!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "024"
                            )
                        }
                    } else {
                        spinnerBlock!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()

                            },
                            strActivityCode + "025"
                        )
                    }
                } else {
                    spinnerBlock!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "026"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerBlock!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "027"
                )
                dialog.dismiss()
            }
        })
    }
    fun getWorkList() {
        alWorkList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getWorkList(/*"0601001001"*/panchayatCode)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonObject= JSONObject(responseString)
                                    val jsonArray = JSONArray(jsonObject.optString("workcodeList"))
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alWorkList.add(
                                                WorkModel(jsonObject.optString("work_code"),jsonObject.optString("work_code"))
                                            )

                                        }
                                        if (alWorkList.size > 0) {
                                            alWorkList!!.add(0,
                                                WorkModel("0",applicationContext.getString(R.string.select))
                                            )
                                            val  adapter=
                                                WorkAdapter(applicationContext, alWorkList)
                                            spinnerWork!!.adapter=adapter
                                        }
                                        else {
                                            spinnerPanchayat!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()

                                                },
                                                strActivityCode + "028"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerPanchayat!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "029"
                                        )
                                    }
                                }
                                else {
                                    spinnerPanchayat!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "030"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerPanchayat!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "031"
                            )
                        }
                    } else {
                        spinnerPanchayat!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()

                            },
                            strActivityCode + "032"
                        )
                    }
                } else {
                    spinnerPanchayat!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "033"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerPanchayat!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "034"
                )
                dialog.dismiss()
            }
        })
    }
    fun getMusterList() {
        alMusterList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getMusterList(/*"0601001001","0601001001/LD/161"*/panchayatCode,workCode)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonObject= JSONObject(responseString)
                                    val jsonArray = JSONArray(jsonObject.optString("msrnoList"))
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alMusterList.add(
                                                MusterModel(jsonObject.optString("msr_no"),jsonObject.optString("msr_no"),jsonObject.optString("dt_from"),jsonObject.optString("dt_to"))
                                            )

                                        }
                                        if (alMusterList.size > 0) {
                                            alMusterList!!.add(0,
                                                MusterModel("0",applicationContext.getString(R.string.select),"","")
                                            )
                                            val  adapter=
                                                MusterAdapter(applicationContext, alMusterList)
                                            spinnerMuster!!.adapter=adapter
                                        }
                                        else {
                                            spinnerWork!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()

                                                },
                                                strActivityCode + "035"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerWork!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "036"
                                        )
                                    }
                                }
                                else {
                                    spinnerWork!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "037"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerWork!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "038"
                            )
                        }
                    } else {
                        spinnerWork!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()

                            },
                            strActivityCode + "039"
                        )
                    }
                } else {
                    spinnerWork!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "040"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerWork!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "041"
                )
                dialog.dismiss()
            }
        })
    }
    fun getJobCardList() {
        alJobCardList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getJobCardList(/*"0601001001","0601001001/LD/161","10"*/panchayatCode,workCode,musterCode)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonObject= JSONObject(responseString)
                                    val jsonArray = JSONArray(jsonObject.optString("msrnoList"))
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alJobCardList.add(
                                                JobcardModel(jsonObject.optString("reg_no")/*,jsonObject.optString("msr_no"),jsonObject.optString("dt_from"),jsonObject.optString("dt_to")*/)
                                            )

                                        }
                                        if (alJobCardList.size > 0) {
                                            alJobCardList!!.add(0,
                                                JobcardModel(/*"0",*/applicationContext.getString(R.string.select))
                                            )
                                            val  adapter=
                                                EkycJobCardAdapter(applicationContext, alJobCardList)
                                            spinnerJobCard!!.adapter=adapter
                                        }
                                        else {
                                            spinnerMuster!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()

                                                },
                                                strActivityCode + "079"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerMuster!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "080"
                                        )
                                    }
                                }
                                else {
                                    spinnerMuster!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "081"
                                    )
                                }
                            }


                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerMuster!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "082"
                            )
                        }
                    } else {
                        spinnerMuster!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()

                            },
                            strActivityCode + "083"
                        )
                    }
                } else {
                    spinnerMuster!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "08440"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerMuster!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "085"
                )
                dialog.dismiss()
            }

        })
    }
    fun getApplicantList() {
        alWorkerList.clear()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
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
        val call = api.getApplicantList(/*"0601001001","0601001001/LD/161","10","CG-01-001-001-001/1221"*/panchayatCode,workCode,musterCode,strJobCardNumber)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            var responseString = response.body()?.string()
                            if (responseString != null && !responseString.isEmpty()) {
                                responseString = responseString.replace("\\\"", "\"")
                                responseString = responseString.removePrefix("\"").removeSuffix("\"")
                                if (!responseString.equals("null")) {
                                    val jsonObject= JSONObject(responseString)
                                    val jsonArray = JSONArray(jsonObject.optString("msrnoList"))
                                    if (jsonArray.length()>0) {
                                        for (i in 0 until jsonArray.length()) {
                                            val jsonObject = jsonArray.getJSONObject(i)
                                            alWorkerList.add(
                                                WorkerModel(jsonObject.optString("applicant_no"),jsonObject.optString("applicant_name")/*,jsonObject.optString("dt_from"),jsonObject.optString("dt_to")*/)
                                            )

                                        }
                                        if (alWorkerList.size > 0) {
                                            alWorkerList!!.add(0,
                                                WorkerModel("0",applicationContext.getString(R.string.select))
                                            )
                                            val  adapter=
                                                EkycWorkerAdapter(applicationContext, alWorkerList)
                                            spinnerWorker!!.adapter=adapter
                                        }
                                        else {
                                            spinnerJobCard!!.setSelection(0)
                                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                            MyAlert.dialogForOk(applicationContext,
                                                R.mipmap.icon_warning,
                                                applicationContext.getString(R.string.warning),
                                                applicationContext.getString(R.string.data_not_found),
                                                dialogAlert2,
                                                applicationContext.getString(R.string.ok),
                                                {
                                                    dialogAlert2.dismiss()

                                                },
                                                strActivityCode + "086"
                                            )

                                        }
                                    }
                                    else {
                                        spinnerJobCard!!.setSelection(0)
                                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                        MyAlert.dialogForOk(applicationContext,
                                            R.mipmap.icon_warning,
                                            applicationContext.getString(R.string.warning),
                                            applicationContext.getString(R.string.data_not_found),
                                            dialogAlert2,
                                            applicationContext.getString(R.string.ok),
                                            {
                                                dialogAlert2.dismiss()

                                            },
                                            strActivityCode + "087"
                                        )
                                    }
                                }
                                else {
                                    spinnerJobCard!!.setSelection(0)
                                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                                    MyAlert.dialogForOk(applicationContext,
                                        R.mipmap.icon_warning,
                                        applicationContext.getString(R.string.warning),
                                        applicationContext.getString(R.string.data_not_found),
                                        dialogAlert2,
                                        applicationContext.getString(R.string.ok),
                                        {
                                            dialogAlert2.dismiss()

                                        },
                                        strActivityCode + "088"
                                    )
                                }
                            }

                            /*  val alState = response.body() as java.util.ArrayList<StateModel?>?
                              alState!!.add(0, StateModel(applicationContext.getString(R.string.select)))
                              populateSpinnerState(alState, spinnerState!!)*/
                            dialog.dismiss()
                        } catch (e: Exception) {
                            spinnerJobCard!!.setSelection(0)
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                            MyAlert.dialogForOk(applicationContext,
                                R.mipmap.icon_warning,
                                applicationContext.getString(R.string.warning),
                                applicationContext.getString(R.string.network_req_failed),
                                dialogAlert2,
                                applicationContext.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "089"
                            )
                        }
                    } else {
                        spinnerJobCard!!.setSelection(0)
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(applicationContext,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.warning),
                            applicationContext.getString(R.string.network_req_failed),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()

                            },
                            strActivityCode + "090"
                        )
                    }
                } else {
                    spinnerJobCard!!.setSelection(0)
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                    MyAlert.dialogForOk(applicationContext,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.warning),
                        applicationContext.getString(R.string.network_req_failed),
                        dialogAlert2,
                        applicationContext.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "091"
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                spinnerJobCard!!.setSelection(0)
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(applicationContext,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.network_req_failed),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "092"
                )
                dialog.dismiss()
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadEkycExemption() {
        if (connectionDetector!!.isConnectingToInternet()) {
            Constant.startVolleyDialog(this)
            val client = OkHttpClient.Builder()
                .addInterceptor(
                    BasicAuthInterceptor(
                        SplashActivity.strNregaName,
                        SplashActivity.strNregaPwd
                    )
                )
                .build()
            val call = Retrofit.Builder()
                .baseUrl(Helper.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api::class.java)
                .uploadEkycExemption(
                    UploadEkycExemptionModel(
                        musterCode,
                        MySharedPref.getMobileNumber(applicationContext),
                        /*"06"*/stateCode,
                        /*"0601001001"*/panchayatCode,
                        reasonCode,
                        workCode,
                        dateFrom,
                        dateTo,
                        strLatitude,
                        strLongitude,
                        strBase64Image,
                        exemptionDate,MySharedPref.getMobileNumber(applicationContext),
                        reasonName,strJobCardNumber,applicantId)
                    /* .uploadEkycExemption(
                    UploadEkycExemptionModel(
                        "9",
                        "9711414859",
                        "06",
                        "0601001001",
                        "1",
                        "0601001001/IF/112",
                        "2025/11/30",
                        "2025/12/10",
                        strLatitude,
                        strLongitude,
                        strBase64Image,
                        "2025/12/10","9711414859",
                         "Device not working")
                )*/
                )

            Log.d("TAG", "uploadEkycExemption: musterCode"+musterCode)
            Log.d("TAG", "uploadEkycExemption: stateCode"+stateCode)
            Log.d("TAG", "uploadEkycExemption: panchayatCode"+panchayatCode)
            Log.d("TAG", "uploadEkycExemption: reasonCode"+reasonCode)
            Log.d("TAG", "uploadEkycExemption: workCode"+workCode)
            Log.d("TAG", "uploadEkycExemption: dateFrom"+dateFrom)
            Log.d("TAG", "uploadEkycExemption: dateTo"+dateTo)
            Log.d("TAG", "uploadEkycExemption: strLatitude"+strLatitude)
            Log.d("TAG", "uploadEkycExemption: strLongitude"+strLongitude)
            Log.d("TAG", "uploadEkycExemption: strBase64Image"+strBase64Image)
            Log.d("TAG", "uploadEkycExemption: exemptionDate"+exemptionDate)
            Log.d("TAG", "uploadEkycExemption: reasonName"+reasonName)
            Log.d("TAG", "uploadEkycExemption: strJobCardNumber"+strJobCardNumber)
            Log.d("TAG", "uploadEkycExemption: applicantId"+applicantId)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    var resultResponse = response.body()?.string()
                    var jsonObject= JSONObject(resultResponse)
                    if (response.isSuccessful()) {
                        val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                        MyAlert.dialogForOk(
                            this@UploadWorksitePhoto,
                            R.mipmap.icon_info,
                            applicationContext.getString(R.string.submit_info),
                            jsonObject.optString("message"),
                            dialogAlert2,
                            applicationContext.getString(R.string.ok),
                            object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    dialogAlert2.dismiss()
                                    finish()
                                }
                            },
                            strActivityCode + "042"
                        )

                        Constant.dismissVolleyDialog()
                    }
                    else {
                        MyAlert.showAlert(
                            this@UploadWorksitePhoto,
                            R.mipmap.icon_warning,
                            applicationContext.getString(R.string.submit_warning),
                            jsonObject.optString("message"),
                            strActivityCode + "043"
                        )
                        Constant.dismissVolleyDialog()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    MyAlert.showAlert(
                        this@UploadWorksitePhoto,
                        R.mipmap.icon_warning,
                        applicationContext.getString(R.string.submit_warning),
                        t.cause.toString() + "\n" + t.message,
                        strActivityCode + "044"
                    )
                    Constant.dismissVolleyDialog()
                }
            })
        }
        else MyAlert.showAlert(
            this@UploadWorksitePhoto,
            R.mipmap.icon_warning,
            applicationContext.getString(R.string.submit_warning),
            applicationContext.getString(R.string.no_internet),
            strActivityCode + "045"
        )
    }

    private fun findById() {
        spinnerState =findViewById<Spinner>(R.id.spinnerState)
        spinnerDistrict =findViewById<Spinner>(R.id.spinnerDistrict)
        spinnerBlock =findViewById<Spinner>(R.id.spinnerBlock)
        spinnerPanchayat =findViewById<Spinner>(R.id.spinnerPanchayat)
        spinnerWork =findViewById<Spinner>(R.id.spinnerWork)
        spinnerMuster =findViewById<Spinner>(R.id.spinnerMuster)
        spinnerJobCard =findViewById<Spinner>(R.id.spinnerJobCard)
        spinnerWorker =findViewById<Spinner>(R.id.spinnerWorker)
        spinnerReason =findViewById<Spinner>(R.id.spinnerReason)
        btnSubmit =findViewById<Button>(R.id.btnSubmit)
        imgCapture =findViewById<ImageView>(R.id.imgCapture)
        etDate =findViewById<EditText>(R.id.etDate)
        llJobCard =findViewById<LinearLayout>(R.id.llJobCard)
        llWorker =findViewById<LinearLayout>(R.id.llWorker)
        heading=findViewById<TextView>(R.id.heading)




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

    fun getFinancialYear(): String{
        var str=""
        var month = 0
        var year = 0
        var cyear = 0
        val cal = Calendar.getInstance()
        cal.setTime(Date())
        month = cal.get(Calendar.MONTH);
        val advance = if (month <= 3) -1 else 0
        year = cal.get(Calendar.YEAR) + advance;
        cyear = year + 1;
        str= year.toString() + "-" + cyear;
        return str
    }
    fun stopTimer() {
        if (mTimer1 != null) {
            mTimer1!!.cancel()
            mTimer1!!.purge()
        }
    }
    private fun startTimer() {
        //  Constant.startVolleyDialog(this@UploadWorksitePhoto)
        // Toast.makeText(applicationContext,"startTimer",Toast.LENGTH_LONG).show()
        val dialog = ProgressDialog(this@UploadWorksitePhoto)
        dialog.setMessage("Fetching Location.. Please wait")
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        mTimer1 = Timer()
        mTt1 = object : TimerTask() {
            override fun run() {
                mTimerHandler.post(object : Runnable {
                    override fun run() {
                        // Toast.makeText(applicationContext,"run",Toast.LENGTH_LONG).show()

                        val serviceIntent =
                            Intent(this@UploadWorksitePhoto, GlobalLocationService::class.java)
                        startService(serviceIntent)
                        if (Constant.dLatitude != null && (Constant.dLatitude > 0.0)) {
                            dLatitude = (Math.round(Constant.dLatitude * 1000000.0) / 1000000.0)
                            dLongitude = (Math.round(Constant.dLongitude * 1000000.0) / 1000000.0)
                            dAccuracy = (Math.round(Constant.dAccuracy * 100.0) / 100.0)
                            if ((dLatitude > 0.0) && (dLongitude > 0.0) /* && (dAccuracy < 25.0)*/) {
                                stopTimer()
                                stopService(serviceIntent)
                                //Constant.dismissVolleyDialog()
                                dialog.dismiss()
                                strLongitude = dLongitude.toString()
                                strLatitude = dLatitude.toString()
                            }
                        }
                    }
                })
            }
        }
        mTimer1!!.schedule(mTt1, 1, 5000)
    }
    fun enableGPS() {
        try {
            if (isLocationEnabled(applicationContext)) {
                startTimer()
            }
            else {
                dLatitude = 0.0
                dLongitude = 0.0
                dAccuracy = 0.0
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(
                    this@UploadWorksitePhoto,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.enable_gps),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            dialogAlert2.dismiss()
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                            finish()
                        }
                    },
                    strActivityCode + "061"
                )

            }
        } catch (e: java.lang.Exception) {
            val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
            MyAlert.dialogForOk(
                this@UploadWorksitePhoto,
                R.mipmap.icon_warning,
                applicationContext.getString(R.string.warning),
                applicationContext.getString(R.string.enable_gps),
                dialogAlert2,
                applicationContext.getString(R.string.ok),
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        dialogAlert2.dismiss()
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                        finish()
                    }
                },
                strActivityCode + "062"
            )
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true

    }
    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) { super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data!!.getData()
            Log.d("TAG", "onActivityResult:imageUri "+imageUri)
            if (imageUri != null) {
                val imageStream: InputStream? =
                    this@UploadWorksitePhoto.getContentResolver().openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(imageStream)
                Log.d("TAG", "onActivityResult:bitmap "+bitmap)
                if(bitmap!=null) {
                    imgCapture!!.setImageBitmap(bitmap)
                    strBase64Image = Constant.encodeImage(bitmap)
                    /*  Toast.makeText(applicationContext,"strBase64Image"+strBase64Image,
                          Toast.LENGTH_LONG).show()*/
                }

            }
        }
    }
    /////////////////////Permission Grant Section///////////////////
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted =
                result.values.all { it }                // true if every permission granted
            if (granted) {
                startTimer()                           // safe to start WebRTC
            } else {
                val dialogAlert2 = Dialog(this@UploadWorksitePhoto)
                MyAlert.dialogForOk(
                    this@UploadWorksitePhoto,
                    R.mipmap.icon_warning,
                    applicationContext.getString(R.string.warning),
                    applicationContext.getString(R.string.allow_permission),
                    dialogAlert2,
                    applicationContext.getString(R.string.ok),
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            dialogAlert2.dismiss()
                            val intent = Intent()
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.setData(Uri.fromParts("package", getPackageName(), null))
                            startActivityForResult(intent, 997)
                            finish()
                        }
                    },
                    strActivityCode + "059"
                )
            }
        }
    private fun hasAllPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return  Manifest.permission.CAMERA.isGranted() && Manifest.permission.READ_MEDIA_IMAGES.isGranted() && Manifest.permission.ACCESS_FINE_LOCATION.isGranted() && Manifest.permission.ACCESS_COARSE_LOCATION.isGranted()
        } else {
            return  Manifest.permission.CAMERA.isGranted() && Manifest.permission.WRITE_EXTERNAL_STORAGE.isGranted() && Manifest.permission.READ_EXTERNAL_STORAGE.isGranted() && Manifest.permission.ACCESS_FINE_LOCATION.isGranted() && Manifest.permission.ACCESS_COARSE_LOCATION.isGranted()
        }
    }private fun String.isGranted(): Boolean =
        ContextCompat.checkSelfPermission(this@UploadWorksitePhoto, this) ==
                PackageManager.PERMISSION_GRANTED

}