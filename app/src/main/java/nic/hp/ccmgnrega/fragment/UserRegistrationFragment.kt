package nic.hp.ccmgnrega.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.goodiebag.pinview.Pinview
import nic.hp.ccmgnrega.MainActivity
import nic.hp.ccmgnrega.R
import nic.hp.ccmgnrega.SplashActivity
import nic.hp.ccmgnrega.adapter.BlockAdapter
import nic.hp.ccmgnrega.adapter.DistrictAdapter
import nic.hp.ccmgnrega.adapter.StateAdapter
import nic.hp.ccmgnrega.adapter.UserTypeAdapter
import nic.hp.ccmgnrega.common.Api
import nic.hp.ccmgnrega.common.Constant
import nic.hp.ccmgnrega.common.CurrentFinancialYear
import nic.hp.ccmgnrega.common.DbHelper
import nic.hp.ccmgnrega.common.Helper
import nic.hp.ccmgnrega.common.MyAlert
import nic.hp.ccmgnrega.common.MySharedPref
import nic.hp.ccmgnrega.databinding.FragmentUserRegistrationBinding
import nic.hp.ccmgnrega.model.BlockModel
import nic.hp.ccmgnrega.model.ConnectionDetector
import nic.hp.ccmgnrega.model.DistrictModel
import nic.hp.ccmgnrega.model.StateModel
import nic.hp.ccmgnrega.model.UserTypeModel
import nic.hp.ccmgnrega.webview.BasicAuthInterceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URLEncoder

class UserRegistrationFragment : BaseFragment() {
    protected var rootView: View? = null

    lateinit var binding: FragmentUserRegistrationBinding
    var strName = "";

    var selectedStateCode = "0";
    var selectedStateName = "";
    var selectedDistrictCode = "0";
    var selectedDistrictName = "";
    var selectedBlockCode = "0";
    var selectedBlockName = "";
    var selectedUserTypeCode = "0";
    var selectedUserTypeName = "0";
    var financialYear = ""
    var isInternet: ConnectionDetector? = null
    protected var dbWriterFragment: SQLiteDatabase? = null
    protected var dbHelperFragment: DbHelper? = null
    protected var dbReaderFragment: SQLiteDatabase? = null
    var encStrMobileNo = ""; var encStrName = ""; var encSelectedStateCode = ""; var encSelectedDistrictCode = ""; var encSelectedBlockCode = "";
    var encSelectedUserTypeCode =""; var encDefaultValue = ""; var encOpr ="";    var encLangid = ""; var encLoc = ""; var encIpAddress = ""

    companion object{
        var strMobileNo = "";
        var prevSelectedDistCode="0"; var prevSelectedBlockCode="0"; var strLoginPin="";var cLong="0.0";var cLat="0.0";
        var  encCLong="0.0"; var encCLat="0.0";var strOpr="A";    var strActivityCode = "12-";
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserRegistrationBinding.inflate(inflater, container, false)
        val args = arguments
        if (args != null && args.getString("menu") != null)
            menu = args.getString("menu")
        if (args!!.getString("cLong") != null)  cLong = args.getString("cLong")!!
        if (args.getString("cLat") != null)  cLat = args.getString("cLat")!!
        binding.heading!!.text = " " + menu
        val fDate = CurrentFinancialYear()
        this.financialYear = fDate.finacialYear
        isInternet = ConnectionDetector(activity)
        dbHelperFragment = DbHelper(context)
        dbReaderFragment = dbHelperFragment!!.readableDatabase
        dbWriterFragment = dbHelperFragment!!.writableDatabase



        getStateList(context)
        populateSpinnerUserType(binding.spinnerUserType)
        binding.spinnerState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    selectedStateCode = "0"; selectedStateName = ""; selectedDistrictCode = "0";
                    selectedDistrictName = "";selectedBlockCode = "0"; selectedBlockName = "";
                    binding.spinnerDistrict.adapter=null
                    binding.spinnerBlock.adapter=null
                } else {
                    val stateModel = (binding.spinnerState.adapter as StateAdapter).getItem(position)
                    selectedStateCode = stateModel!!.stateCode
                    selectedStateName = stateModel.stateName
                    /*  Toast.makeText(
                          context,
                          selectedStateCode + "selectedStateCode",
                          Toast.LENGTH_LONG
                      ).show()
                      Toast.makeText(
                          context,
                          selectedStateName + "selectedStateName",
                          Toast.LENGTH_LONG
                      ).show()*/
                    if (isInternet!!.isConnectingToInternet())
                        getDistrictList(selectedStateCode, "NEWREQUEST", financialYear)
                    else
                        Toast.makeText(activity, context!!.getString(R.string.no_internet), Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        binding.spinnerDistrict.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    selectedDistrictCode = "0";selectedDistrictName = "";selectedBlockCode =
                        "0"; selectedBlockName = "";
                    binding.spinnerBlock.adapter=null
                } else {
                    val districtModel =
                        (binding.spinnerDistrict.getAdapter() as DistrictAdapter).getItem(position)
                    selectedDistrictCode = districtModel!!.districtCode
                    selectedDistrictName = districtModel!!.districtName
                    /* Toast.makeText(
                         context,
                         selectedDistrictCode + "selectedDistrictCode",
                         Toast.LENGTH_LONG
                     ).show()
                     Toast.makeText(
                         context,
                         selectedDistrictName + "selectedDistrictName",
                         Toast.LENGTH_LONG
                     ).show()*/
                    getBlockListByDistrictCode(selectedDistrictCode)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        binding.spinnerBlock.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    selectedBlockCode = "0"; selectedBlockName = "";
                } else {
                    val blockModel =
                        (binding.spinnerBlock.getAdapter() as BlockAdapter).getItem(position)
                    selectedBlockCode = blockModel!!.blockCode
                    selectedBlockName = blockModel!!.blockName
                    /*    Toast.makeText(
                            context,
                            selectedBlockCode + "selectedBlockCode",
                            Toast.LENGTH_LONG
                        ).show()
                        Toast.makeText(
                            context,
                            selectedBlockName + "selectedBlockName",
                            Toast.LENGTH_LONG
                        ).show()*/
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        binding.spinnerUserType.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    selectedUserTypeCode = "0"; selectedUserTypeName = "";
                } else {
                    val userTypeModel =
                        (binding.spinnerUserType.getAdapter() as UserTypeAdapter).getItem(position)
                    selectedUserTypeCode = userTypeModel!!.userTypeCode
                    selectedUserTypeName = userTypeModel!!.userTypeName
                    /*  Toast.makeText(
                          context,
                          selectedUserTypeCode + "selectedUserTypeCode",
                          Toast.LENGTH_LONG
                      ).show()
                      Toast.makeText(
                          context,
                          selectedUserTypeName + "selectedUserTypeName",
                          Toast.LENGTH_LONG
                      ).show()*/

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        binding.btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                strMobileNo = binding.etMobile.text.toString()
                strName = binding.etName.text.toString()
                strLoginPin=binding.pinview.value.toString()
                if (strMobileNo.isEmpty())
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_enter_mobile_no),
                        strActivityCode + "02"
                    )
                else if (!strMobileNo.isEmpty() && strMobileNo.length < 10)
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_enter_valid_mobile_no),
                        strActivityCode + "03"
                    )
                else if (strName.isEmpty())
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_fill_name),
                        strActivityCode + "01"
                    )
                else if (selectedStateCode == "0")
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_select_state),
                        strActivityCode + "04"
                    )
                else if (selectedDistrictCode == "0")
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_select_district),
                        strActivityCode + "05"
                    )
                else if (selectedBlockCode == "0")
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_select_block),
                        strActivityCode + "06"
                    )
                else if (selectedUserTypeCode == "0")
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_select_usertype),
                        strActivityCode + "07"
                    )
                else if (strLoginPin.isEmpty())
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_enter_pin),
                        strActivityCode + "08"
                    )
                else if (!strLoginPin.isEmpty() && strLoginPin.length < 4)
                    MyAlert.showAlert(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.submit_warning),
                        context!!.getString(R.string.please_enter_valid_pin),
                        strActivityCode + "09"
                    )
                else {
                    if(strMobileNo.equals("9999999999")) {
                        MySharedPref.setLoginPin(context, strLoginPin)
                        MySharedPref.setMobileNumber(context, strMobileNo)

                        val dialogAlert2 = Dialog(context!!)
                        MyAlert.dialogForOk(
                            context,
                            R.mipmap.icon_info,
                            context!!.getString(R.string.submit_info),
                            context!!.getString(R.string.user_registered),
                            dialogAlert2,
                            context!!.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()
                                val mainIntent = Intent(context, MainActivity::class.java)
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                mainIntent.putExtra("displayPosition", "0")
                                context!!.startActivity(mainIntent)


                            },
                            "strActivityCode" + "14"
                        )

                    }
                    else {
                        encStrMobileNo = URLEncoder.encode(encrypt(strMobileNo))
                        encStrName = URLEncoder.encode(encrypt(strName))
                        encSelectedStateCode = URLEncoder.encode(encrypt(selectedStateCode))
                        encSelectedDistrictCode = URLEncoder.encode(encrypt(selectedDistrictCode))
                        encSelectedBlockCode = URLEncoder.encode(encrypt(selectedBlockCode))
                        encSelectedUserTypeCode = URLEncoder.encode(encrypt(selectedUserTypeCode))
                        encOpr = URLEncoder.encode(encrypt(strOpr))
                        encLangid = URLEncoder.encode(encrypt("en"))
                        encLoc = URLEncoder.encode(encrypt("0.0"))
                        encCLong = URLEncoder.encode(encrypt(cLong))
                        encCLat = URLEncoder.encode(encrypt(cLat))
                        encIpAddress = URLEncoder.encode(encrypt(getIPAddress()))
                        encDefaultValue = URLEncoder.encode(encrypt(""))
                        GetOtpFromServer(
                            context!!,
                            encStrMobileNo,
                            encStrName,
                            encSelectedStateCode,
                            encSelectedDistrictCode,
                            encSelectedBlockCode,
                            encSelectedUserTypeCode,
                            encOpr,
                            encLangid,
                            encLoc,
                            encIpAddress,
                            encDefaultValue
                        ).execute()
                    }
                }


            }

        })

        binding.etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 10) {
                    CheckIsRegister(context!!,s.toString().trim(),binding).execute()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun getStateList(context: Context?) {
        val dialog = ProgressDialog(activity)
        dialog.setMessage(context!!.getString(R.string.please_wait))
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
                            val alState = response.body() as java.util.ArrayList<StateModel>
                            alState!!.add(0, StateModel("0",context!!.getString(R.string.select),"",""))
                            populateSpinnerState(alState, binding.spinnerState)
                            dialog.dismiss()
                        } catch (e: Exception) {
                            statusMessage = e.message
                            dialog.dismiss()
                            val dialogAlert2 = Dialog(context)
                            MyAlert.dialogForOk(
                                context,
                                R.mipmap.icon_warning,
                                context!!.getString(R.string.warning),
                                context!!.getString(R.string.network_req_failed),
                                dialogAlert2,
                                context!!.getString(R.string.ok),
                                {
                                    dialogAlert2.dismiss()

                                },
                                strActivityCode + "010"
                            )
                        }
                    }
                    else {
                        dialog.dismiss()
                        val dialogAlert2 = Dialog(context)
                        MyAlert.dialogForOk(
                            context,
                            R.mipmap.icon_warning,
                            context!!.getString(R.string.warning),
                            context!!.getString(R.string.network_req_failed),
                            dialogAlert2,
                            context!!.getString(R.string.ok),
                            {
                                dialogAlert2.dismiss()
                            },
                            strActivityCode + "011"
                        )
                    }
                } else {
                    dialog.dismiss()
                    val dialogAlert2 = Dialog(context)
                    MyAlert.dialogForOk(
                        context,
                        R.mipmap.icon_warning,
                        context!!.getString(R.string.warning),
                        context!!.getString(R.string.network_req_failed),
                        dialogAlert2,
                        context!!.getString(R.string.ok),
                        {
                            dialogAlert2.dismiss()

                        },
                        strActivityCode + "12"
                    )
                }
            }

            override fun onFailure(call: Call<List<StateModel?>?>, t: Throwable) {
                val dialogAlert2 = Dialog(context)
                MyAlert.dialogForOk(
                    context,
                    R.mipmap.icon_warning,
                    context!!.getString(R.string.warning),
                    context!!.getString(R.string.network_req_failed),
                    dialogAlert2,
                    context!!.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()

                    },
                    strActivityCode + "13"
                )
                dialog.dismiss()
            }
        })
    }


    @SuppressLint("UseRequireInsteadOfGet")
    fun getDistrictList(stateCode: String?, requestId: String?, finYear: String?) {
        val dialog = ProgressDialog(context)
        dialog.setMessage(context!!.getString(R.string.please_wait))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val client = OkHttpClient.Builder().addInterceptor(
            BasicAuthInterceptor(
                SplashActivity.strNregaName,
                SplashActivity.strNregaPwd
            )
        ).build()
        val api = Retrofit.Builder()
            .baseUrl(Helper.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Api::class.java)
        val call = api.getDistrictList(stateCode, requestId, finYear)
        call.enqueue(object : Callback<List<DistrictModel?>?> {
            override fun onResponse(
                call: Call<List<DistrictModel?>?>,
                response: Response<List<DistrictModel?>?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            dialog.dismiss()
                            val alDistrict = response.body() as java.util.ArrayList<DistrictModel>
                            alDistrict!!.add(0, DistrictModel("0",context!!.getString(R.string.select),""))
                            populateSpinnerDistrict(alDistrict, binding.spinnerDistrict)
                            binding.spinnerDistrict.setSelection((binding.spinnerDistrict.adapter as DistrictAdapter).getItemPosition(prevSelectedDistCode))
                            getBlockList(stateCode, requestId, finYear)
                        } catch (e: Exception) {
                            statusMessage = e.message
                            Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            context,
                            """ ${context!!.getString(R.string.data_not_found)}  ${response.message()}   """.trimIndent(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    dialog.dismiss()
                    Toast.makeText(
                        context,
                        """ ${context!!.getString(R.string.data_not_found)}  ${response.message()}  """.trimIndent(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<DistrictModel?>?>, t: Throwable) {
                Toast.makeText(
                    context,
                    """   ${context!!.getString(R.string.data_not_found)} ${t.message}""".trimIndent(),
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }
        })
    }


    @SuppressLint("UseRequireInsteadOfGet")
    fun getBlockList(stateCode: String?, requestId: String?, finYear: String?) {
        val dialog = ProgressDialog(context)
        dialog.setMessage(context!!.getString(R.string.please_wait))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val client = OkHttpClient.Builder().addInterceptor(
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
        val call = api.getBlockList(stateCode, requestId, finYear)
        call.enqueue(object : Callback<List<BlockModel>?> {
            override fun onResponse(
                call: Call<List<BlockModel>?>,
                response: Response<List<BlockModel>?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            dbWriterFragment!!.beginTransaction()
                            dbWriterFragment!!.delete(DbHelper.TABLE_NAME_BLOCK_MASTER, null, null)
                            val alBLock = response.body()
                            for (i in alBLock!!.indices) {
                                val blockModel = alBLock[i]
                                val blockValues = ContentValues()
                                blockValues.put(
                                    DbHelper.COLUMN_NAME_STATE_STATE_CODE,
                                    blockModel.stateCode
                                )
                                blockValues.put(
                                    DbHelper.COLUMN_NAME_DISTRICT_MASTER_ID,
                                    blockModel.districtCode
                                )
                                blockValues.put(
                                    DbHelper.COLUMN_NAME_BLOCK_MASTER_ID,
                                    blockModel.blockCode
                                )
                                blockValues.put(
                                    DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME,
                                    blockModel.blockName
                                )
                                blockValues.put(
                                    DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL,
                                    blockModel.blockNameLocal
                                )
                                blockValues.put(
                                    DbHelper.COLUMN_NAME_FINANCIAL_YEAR,
                                    blockModel.finYear
                                )
                                dbWriterFragment!!.insert(
                                    DbHelper.TABLE_NAME_BLOCK_MASTER,
                                    null,
                                    blockValues
                                )
                                blockValues.clear()
                            }
                            dbWriterFragment!!.setTransactionSuccessful()
                            dbWriterFragment!!.endTransaction()
                            dialog.dismiss()
                        } catch (e: SQLiteConstraintException) {
                            dbWriterFragment!!.endTransaction()
                            statusMessage = e.message
                            Toast.makeText(context, statusMessage, Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            context,
                            """   ${context!!.getString(R.string.data_not_found)}   ${response.message()}  """.trimIndent(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    dialog.dismiss()
                    Toast.makeText(
                        context,
                        """    ${context!!.getString(R.string.data_not_found)}   ${response.message()}  """.trimIndent(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<BlockModel>?>, t: Throwable) {
                Toast.makeText(
                    context,
                    """ ${context!!.getString(R.string.data_not_found)}   ${t.message}  """.trimIndent(),
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }
        })
    }

    @SuppressLint("Range", "UseRequireInsteadOfGet")
    private fun getBlockListByDistrictCode(districtCode: String) {
        val alBlock = ArrayList<BlockModel>()
        alBlock.add(0, BlockModel("0","0","0",context!!.getString(R.string.select),"","",""))
        val columns = arrayOf(
            DbHelper.COLUMN_NAME_STATE_STATE_CODE,
            DbHelper.COLUMN_NAME_DISTRICT_MASTER_ID,
            DbHelper.COLUMN_NAME_BLOCK_MASTER_ID,
            DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME,
            DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL,
            DbHelper.COLUMN_NAME_FINANCIAL_YEAR
        )
        val cursorMenu = dbWriterFragment!!.query(
            DbHelper.TABLE_NAME_BLOCK_MASTER,
            columns,
            "DistrictID = ?",
            arrayOf(districtCode),
            null,
            null,
            DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME
        )
        if (cursorMenu != null) {
            if (cursorMenu.count > 0 && cursorMenu.moveToFirst()) {
                do {
                    alBlock.add(
                        BlockModel(
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_STATE_STATE_CODE)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_DISTRICT_MASTER_ID)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_BLOCK_MASTER_ID)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL)),
                            cursorMenu.getString(cursorMenu.getColumnIndex(DbHelper.COLUMN_NAME_FINANCIAL_YEAR)),
                            ""
                        )
                    )
                } while (cursorMenu.moveToNext())
            }
            cursorMenu.close()
        }
        populateSpinnerBlock(alBlock, binding.spinnerBlock)
        binding.spinnerBlock.setSelection((binding.spinnerBlock.adapter as BlockAdapter).getItemPosition(prevSelectedBlockCode))


    }

    private fun populateSpinnerState(alStateModel: java.util.ArrayList<StateModel>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            val dbAdapter =
                StateAdapter(context, android.R.layout.simple_spinner_item, alStateModel)
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }

    private fun populateSpinnerDistrict(alDistrictModel: java.util.ArrayList<DistrictModel>, sp: Spinner) {
        if (!alDistrictModel.isEmpty() && alDistrictModel.size > 0) {
            val dbAdapter =
                DistrictAdapter(context, android.R.layout.simple_spinner_item, alDistrictModel)
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }

    private fun populateSpinnerBlock(alBlockModel: java.util.ArrayList<BlockModel>, sp: Spinner) {
        if (!alBlockModel.isEmpty() && alBlockModel.size > 0) {
            val dbAdapter =
                BlockAdapter(context, android.R.layout.simple_spinner_item, alBlockModel)
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun populateSpinnerUserType(sp: Spinner) {
        var alUserTypeModel = ArrayList<UserTypeModel>()
        alUserTypeModel.add(UserTypeModel("0", context!!.getString(R.string.select)))
        alUserTypeModel.add(UserTypeModel("1", context!!.getString(R.string.gov_official)))
        alUserTypeModel.add(UserTypeModel("2", context!!.getString(R.string.mgnrega_func_worker)))
        alUserTypeModel.add(UserTypeModel("3", context!!.getString(R.string.ngo_staff)))
        alUserTypeModel.add(UserTypeModel("4", context!!.getString(R.string.sip)))
        val dbAdapter =
            UserTypeAdapter(context, android.R.layout.simple_spinner_item, alUserTypeModel)
        dbAdapter.notifyDataSetChanged()
        sp.adapter = dbAdapter

    }

    fun getIPAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return ""
    }

    class GetOtpFromServer(val context: Context,
                           val encStrMobileNo: String,
                           val encStrName: String,
                           val encSelectedStateCode: String,
                           val encSelectedDistrictCode: String,
                           val encSelectedBlockCode: String,
                           val encSelectedUserTypeCode: String,
                           val encOpr: String,
                           val encLangid: String,
                           val encLoc: String,
                           val encIpAddress: String,
                           val encDefaultValue: String
    ) : AsyncTask<Void, Void, String>() {
        var statusMessage=""
        var status="false"
        override fun onPreExecute() {
            Constant.startVolleyDialog(context)
            super.onPreExecute()
        }
        @SuppressLint("SuspiciousIndentation")
        override fun doInBackground(vararg params: Void?): String {
            val apiUrl = (Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/SaveOTP?" + "mobile=" + encStrMobileNo)
            var response = BaseFragment().getResponse(apiUrl)

            val jObject = JSONObject(response)
            val msgObject = jObject.getJSONObject("message")
            statusMessage = msgObject.getString("message")
            status=msgObject.optString("status")
            if (status == "true"/* && statusMessage == "OTP Sent Successfully"*/)
            {
                try {
                    val otpJsonArray = jObject.getJSONArray("otp")
                    for (i in 0 until otpJsonArray.length()) {
                        val otpObject = otpJsonArray.getJSONObject(i)
                        MySharedPref.setOtpID(context, otpObject.getString("OTPID"))
                    }
                } catch (e: java.lang.Exception) {
                    statusMessage=context.getString(R.string.otp_not_received)
                }
            }
            else
                statusMessage=context.getString(R.string.otp_not_received)

            return  status
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Constant.dismissVolleyDialog()
            if (status == "true") {
                UserRegistrationFragment().openOtpDialog(context,encStrMobileNo,encStrName,encSelectedStateCode,encSelectedDistrictCode,encSelectedBlockCode,encSelectedUserTypeCode,encOpr,encLangid,encLoc,encIpAddress,encDefaultValue)
            }
            else
                Toast.makeText(context,context.getString(R.string.otp_not_received),Toast.LENGTH_LONG).show()
        }


    }

    class UserRegister(
        val context: Context,
        val encStrMobileNo: String,
        val encStrName: String,
        val encSelectedStateCode: String,
        val encSelectedDistrictCode: String,
        val encSelectedBlockCode: String,
        val encSelectedUserTypeCode: String,
        val encOpr: String,
        val encLangid: String,
        val encLoc: String,
        val encIpAddress: String,
        val encDefaultValue: String
    ) : AsyncTask<Void, Void, String>() {
        var status = "false"
        override fun onPreExecute() {
            Constant.startVolleyDialog(context)
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): String {
            try {
                var apiUrl: String = Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/addedit?"
                apiUrl += "Opr=" + encOpr
                apiUrl += "&langid=" + encLangid
                apiUrl += "&mobile=" + encStrMobileNo
                apiUrl += "&name=" + encStrName
                apiUrl += "&usertype=" + encSelectedUserTypeCode
                apiUrl += "&stateid=" + encSelectedStateCode
                apiUrl += "&districtid=" + encSelectedDistrictCode
                apiUrl += "&blockid=" + encSelectedBlockCode
                apiUrl += "&panchayatid=" + encDefaultValue
                apiUrl += "&villageid=" + encDefaultValue
                apiUrl += "&pin=" + encDefaultValue
                apiUrl += "&email=" + encDefaultValue
                apiUrl += "&desig=" + encDefaultValue
                apiUrl += "&affil=" + encDefaultValue
                apiUrl += "&longitude=" + encLoc
                apiUrl += "&latitude=" + encLoc
                apiUrl += "&IPAdd=" + encIpAddress
                apiUrl += "&add1=" + encDefaultValue
                apiUrl += "&add2=" + encDefaultValue
                var response = BaseFragment().getResponse(apiUrl)
                val jObject = JSONObject(response)
                status=jObject.optString("status")


            } catch (e: Exception) {
                status = "false"
            }
            return status
        }
        //   {"message":"Successful User Register","messageLocal":"Successful User Register","status":"true"}
        //{"message":"User Already Registered with this Mobile number","messageLocal":"User Already Registered with this Mobile number","status":"true"}
        //{"message":"Value cannot be null.\r\nParameter name: s","messageLocal":"कृपया पुन: प्रयास करें!","status":"false"}

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (status=="true") {
                MySharedPref.setLoginPin(context, strLoginPin)
                MySharedPref.setMobileNumber(context, strMobileNo)

                val dialogAlert2 = Dialog(context)
                MyAlert.dialogForOk(context, R.mipmap.icon_info, context.getString(R.string.submit_info), context.getString(R.string.user_registered), dialogAlert2, context.getString(R.string.ok),
                    {
                        dialogAlert2.dismiss()
                        val mainIntent = Intent(context, MainActivity::class.java)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mainIntent.putExtra("displayPosition", "0")
                        context!!.startActivity(mainIntent)


                    }, "strActivityCode" + "14")



            } else
                MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), context.getString(R.string.network_req_failed), "12-" + "05")

            Constant.dismissVolleyDialog()
        }




    }

    class  VerifyOtpByServer( val context: Context,
                              val encStrMobileNo: String,
                              val encStrName: String,
                              val encSelectedStateCode: String,
                              val encSelectedDistrictCode: String,
                              val encSelectedBlockCode: String,
                              val encSelectedUserTypeCode: String,
                              val encOpr: String,
                              val encLangid: String,
                              val encLoc: String,
                              val encIpAddress: String,
                              val encDefaultValue: String,
                              val strOtp: String):AsyncTask<Void, Void, String>(){
        var statusMessage=""

        override fun onPreExecute() {
            super.onPreExecute()
            Constant.startVolleyDialog(context)
        }

        override fun doInBackground(vararg params: Void?): String {
            val apiUrl = (Helper.getBaseURL() + "CCMGNERGA/PublicService.svc/CheckOTP?"
                    + "MobileNo=" + encStrMobileNo
                    + "&userpwd=" + URLEncoder.encode(BaseFragment().encrypt(strOtp), "UTF-8")
                    + "&otpId=" + URLEncoder.encode(MySharedPref.getOtpID(context), "UTF-8"))
            var response = BaseFragment().getResponse(apiUrl)
            val jObject = JSONObject(response)
            val message = jObject.getJSONObject("message")
            statusMessage = message.getString("message")
            return statusMessage
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Constant.dismissVolleyDialog()
            if (statusMessage == "OTP Successfully Matched") {
                val dialogAlert2 = Dialog(context)
                MyAlert.dialogForOk(context, R.mipmap.icon_info, context.getString(R.string.submit_info),
                    context.getString(R.string.otp_matched)+ "\n" + context.getString(R.string.please_submit_form), dialogAlert2,
                    context.getString(R.string.submit),
                    {
                        dialogAlert2.dismiss()
                        UserRegister(context!!,
                            encStrMobileNo,
                            encStrName,
                            encSelectedStateCode,
                            encSelectedDistrictCode,
                            encSelectedBlockCode,
                            encSelectedUserTypeCode,
                            encOpr,
                            encLangid,
                            encLoc,
                            encIpAddress,
                            encDefaultValue
                        ).execute()
                    },
                    "012" + "024"
                )
            } else {
                MyAlert.showAlert(context, R.mipmap.icon_warning, context.getString(R.string.submit_warning), statusMessage, "012" + "016")
            }
        }


    }
    class  CheckIsRegister(val context: Context, val strMobileNo: String,val binding: FragmentUserRegistrationBinding): AsyncTask<Void, Void, String>() {
        var userName=""; var statePosition=0;  var userTypePosition=0;
        override fun onPreExecute() {
            super.onPreExecute()
            Constant.startVolleyDialog(context)
        }
        override fun doInBackground(vararg params: Void?): String {
            val apiUrl = (Helper.getBaseURL()+ "CCMGNERGA/PublicService.svc/isRegister?"
                    + "&mobile=" + URLEncoder.encode(BaseFragment().encrypt(strMobileNo)))
            var response = BaseFragment().getResponse(apiUrl)
            val jObject = JSONObject(response)
            val msgJsonObject=jObject.optJSONObject("message")
            val status=msgJsonObject.optString("status")
            if (status=="true"){
                val usersJSONArray = jObject.getJSONArray("user")
                if (usersJSONArray.length()>0) {
                    val userObject = usersJSONArray.getJSONObject(0)
                    userName=BaseFragment().decrypt(userObject.optString("Name"),SplashActivity.strSk)
                    statePosition=(binding.spinnerState.adapter as StateAdapter).getItemPosition(BaseFragment().decrypt(userObject.optString("StateID"),SplashActivity.strSk))
                    prevSelectedDistCode=BaseFragment().decrypt(userObject.optString("DistrictID"),SplashActivity.strSk)
                    prevSelectedBlockCode=BaseFragment().decrypt(userObject.optString("BlockID"),SplashActivity.strSk)
                    if(binding.spinnerUserType.adapter!=null)
                        userTypePosition= (binding.spinnerUserType.adapter as UserTypeAdapter).getItemPosition(BaseFragment().decrypt(userObject.optString("UserTypeID"),SplashActivity.strSk))
                }
                strOpr="E"
            }
            else
                strOpr="A"
            return status
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.etName.setText(userName)
            binding.spinnerState.setSelection(statePosition)
            binding.spinnerUserType.setSelection(userTypePosition)
            if (!MySharedPref.getLoginPin(context).isEmpty())
                binding.pinview.value=MySharedPref.getLoginPin(context)
            Constant.dismissVolleyDialog()
        }


    }



    @SuppressLint("UseRequireInsteadOfGet")
    fun openOtpDialog(context: Context,encStrMobileNo:String,
                      encStrName: String,
                      encSelectedStateCode: String,
                      encSelectedDistrictCode: String,
                      encSelectedBlockCode: String,
                      encSelectedUserTypeCode: String,
                      encOpr: String,
                      encLangid: String,
                      encLoc: String,
                      encIpAddress: String,
                      encDefaultValue: String
    ) {
        val dialog = Dialog(context!!)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.otp_dialog)
        val submitButton = dialog.findViewById<View>(R.id.buttonSubmit) as Button
        val buttonCancel = dialog.findViewById<View>(R.id.buttonCancel) as Button
        val pinview = dialog.findViewById(R.id.pinview) as Pinview
        pinview.requestPinEntryFocus()
        buttonCancel.text=context!!.getString(R.string.resend)
        submitButton.setOnClickListener {
            val strOtp: String = pinview.getValue()
            if (strOtp.isEmpty()) {
                MyAlert.showAlert(context, R.mipmap.icon_warning, context!!.getString(R.string.submit_warning), context!!.getString(R.string.please_fill_otp), strActivityCode + "15")
            }
            else if (!strOtp.isEmpty() && strOtp.length<6) {
                pinview.clearValue()
                MyAlert.showAlert(
                    context,
                    R.mipmap.icon_warning,
                    context!!.getString(R.string.submit_warning),
                    context!!.getString(R.string.please_enter_valid_otp),
                    strActivityCode + "16"
                )
            }

            else {
                VerifyOtpByServer(context!!,encStrMobileNo,encStrName,encSelectedStateCode,encSelectedDistrictCode,encSelectedBlockCode,encSelectedUserTypeCode,encOpr,encLangid,encLoc,encIpAddress,encDefaultValue,strOtp).execute()
                pinview.requestFocus()
                dialog.dismiss()
            }
        }


        buttonCancel.setOnClickListener {
            dialog.dismiss()
            GetOtpFromServer(context!!,encStrMobileNo,encStrName,encSelectedStateCode,encSelectedDistrictCode,encSelectedBlockCode,encSelectedUserTypeCode,encOpr,encLangid,encLoc,encIpAddress,encDefaultValue).execute()

        }
        dialog.show()
    }

    /* override fun getResponse(Url: String?): String? {
         return try {
             val url = URL(Url)
             val conn = url.openConnection() as HttpURLConnection
             conn.requestMethod = "GET"
             statusCode = conn.responseCode
             val `in`: InputStream = BufferedInputStream(conn.inputStream)
             convertStreamToString(`in`)
         } catch (e: java.lang.Exception) {
             "Error " + e.message
         }
     }

     override fun convertStreamToString(`is`: InputStream): String? {
         val reader = BufferedReader(InputStreamReader(`is`))
         val sb = StringBuilder()
         var line: String?
         try {
             while (reader.readLine().also { line = it } != null) {
                 sb.append(line).append('\n')
             }
         } catch (e: IOException) {
             e.printStackTrace()
         } finally {
             try {
                 `is`.close()
             } catch (e: IOException) {
                 e.printStackTrace()
             }
         }
         return sb.toString()
     }*/



    /*
        private fun registerUser(context: Context?) {
            Constant.startVolleyDialog(context)
            val call = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
                .registerUser(stateCode, mobileno)
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
    */


}


