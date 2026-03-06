package nic.hp.ccmgnrega.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

public class DbHelper extends SQLiteOpenHelper {
    String prgma = "PRAGMA journal_mode=DELETE";
    public static final String DATABASE_NAME = decode("Y2NtZ25yZWdhLmRi");///ccmgnrega
    public static final String COLUMN_NAME_ID = decode("SUQ=");

    public static final String TABLE_NAME_LANGUAGE_MASTER = decode("dGJsX2xhbmd1YWdlX21hc3Rlcg==");//"tbl_language_master";
    // Last Update Table
    public static final String TABLE_NAME_LAST_UPDATE = decode("dGJsX2xhc3R1cGRhdGU=");//tbl_lastupdate
    public static final String COLUMN_NAME_LAST_UPDATE = decode("bGFzdF91cGRhdGU=");//last_update
    public static final String TABLE_NAME_STATE_MASTER = decode("dGJsX3N0YXRlX21hc3Rlcg==");//"tbl_state_master";
    public static final String COLUMN_NAME_STATE_STATE_CODE = decode("U3RhdGVJRA==");//"StateID";
    public static final String TABLE_NAME_LANGUAGE_RESOURCE_MASTER = decode("dGJsX2xhbmd1YWdlX3Jlc291cmNlX21hc3Rlcg==");//"tbl_language_resource_master";
    public static final String TABLE_NAME_MENU = decode("dGJsX21lbnU=");//"tbl_menu";
    public static final String TABLE_NAME_USER_TYPE = decode("dGJsX3VzZXJfdHlwZQ==");//"tbl_user_type";
    public static final String TABLE_NAME_DISTRICT_MASTER = decode("dGJsX2Rpc3RyaWN0X21hc3Rlcg==");//"tbl_district_master";
    public static final String COLUMN_NAME_DISTRICT_MASTER_ID = decode("RGlzdHJpY3RJRA==");//"DistrictID";
    public static final String COLUMN_NAME_FINANCIAL_YEAR = decode("RmluYW5jaWFsWWVhcg==");//"FinancialYear";

    public static final String TABLE_NAME_BLOCK_MASTER = decode("dGJsX2Jsb2NrX21hc3Rlcg==");//"tbl_block_master";
    public static final String COLUMN_NAME_BLOCK_MASTER_ID = decode("QmxvY2tJRA==");//"BlockID";
    public static final String COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME = decode("QmxvY2tOYW1l");//"BlockName";
    public static final String COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL = decode("QmxvY2tOYW1lTG9jYWw=");//"BlockNameLocal";

    public static final String TABLE_NAME_PANCHAYAT_MASTER = decode("dGJsX3BhbmNoYXlhdF9tYXN0ZXI=");//"tbl_panchayat_master";
    public static final String TABLE_NAME_VILLAGE_MASTER = decode("dGJsX3ZpbGxhZ2VfbWFzdGVy");//"tbl_village_master";

    public static final String TABLE_NAME_USER_MASTER = decode("dGJsX3VzZXJfbWFzdGVy");//"tbl_user_master";
    public static final String TABLE_NAME_CONTACT_MASTER = decode("dGJsX3VzZXJfY29udGFjdHM=");//"tbl_user_contacts";
    public static final String TABLE_NAME_OTP = decode("dGJsX290cA==");//"tbl_otp";
    public static final String TABLE_NAME_QUESTION_MASTER = decode("dGJsX3F1ZXN0aW9uX21hc3Rlcg==");//"tbl_question_master";
    //Asset Details
    public static final String TABLE_NAME_ASSET_DETAIL = decode("dGJsX2Fzc2V0X2RldGFpbA==");//"tbl_asset_detail";
    public static final String COLUMN_NAME_ASSET_DETAIL_ASSET_ID = decode("YXNzZXRfaWQ=");//"asset_id";
    public static final String COLUMN_NAME_ASSET_DETAIL_ASSET_NAME = decode("YXNzZXRfbmFtZQ==");//"asset_name";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR = decode("d29ya19maW5hbmNpYWxfeWVhcg==");//"work_financial_year";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_CODE = decode("d29ya19jb2Rl");//"work_code";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_NAME = decode("d29ya19uYW1l");//"work_name";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE = decode("d29ya19zdWJjYXRlZ29yeV9jb2Rl");//"work_subcategory_code";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY = decode("c3ViX2NhdGVnb3J5");//"sub_category";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID = decode("d29ya19jYXRlZ29yeV9pZA==");//"work_category_id";
    public static final String COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME = decode("Y2F0ZWdvcnlfbmFtZQ==");//"category_name";
    private static final int DATABASE_VERSION = 1;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
/*
        db.execSQL("DROP TABLE IF EXISTS tbl_jobcardnumber");
        db.execSQL("create table tbl_jobcardnumber (ID INTEGER primary key autoincrement, jobcard_number text)");*/

        ///Drop UnNecessary Tables
        db.execSQL(delete(TABLE_NAME_LANGUAGE_MASTER));
        db.execSQL(delete(TABLE_NAME_USER_TYPE));
        db.execSQL(delete(TABLE_NAME_LANGUAGE_RESOURCE_MASTER));
        db.execSQL(delete(TABLE_NAME_MENU));
        db.execSQL(delete(TABLE_NAME_OTP));
        db.execSQL(delete(TABLE_NAME_USER_MASTER));
        db.execSQL(delete(TABLE_NAME_QUESTION_MASTER));
        db.execSQL(delete(TABLE_NAME_CONTACT_MASTER));
        db.execSQL(delete(TABLE_NAME_STATE_MASTER));
        db.execSQL(delete(TABLE_NAME_DISTRICT_MASTER));
        db.execSQL(delete(TABLE_NAME_PANCHAYAT_MASTER));
        db.execSQL(delete(TABLE_NAME_VILLAGE_MASTER));
        ///Drop UnNecessary Tables

        db.execSQL(delete(TABLE_NAME_LAST_UPDATE));
        db.execSQL(createTableLastUpdate(TABLE_NAME_LAST_UPDATE, new String[]{COLUMN_NAME_ID, COLUMN_NAME_LAST_UPDATE}));

        db.execSQL(delete(TABLE_NAME_BLOCK_MASTER));
        db.execSQL(createTableBlockMaster(TABLE_NAME_BLOCK_MASTER,
                new String[]{
                        COLUMN_NAME_STATE_STATE_CODE,
                        COLUMN_NAME_DISTRICT_MASTER_ID,
                        COLUMN_NAME_BLOCK_MASTER_ID,
                        COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME,
                        COLUMN_NAME_BLOCK_MASTER_BLOCK_NAME_LOCAL,
                        COLUMN_NAME_FINANCIAL_YEAR
                }));

        db.execSQL(delete(TABLE_NAME_ASSET_DETAIL));
        db.execSQL(createTableAssetMaster(TABLE_NAME_ASSET_DETAIL,
                new String[]{
                        COLUMN_NAME_ASSET_DETAIL_ASSET_ID,
                        COLUMN_NAME_ASSET_DETAIL_ASSET_NAME,
                        COLUMN_NAME_ASSET_DETAIL_WORK_FINANCIAL_YEAR,
                        COLUMN_NAME_ASSET_DETAIL_WORK_CODE,
                        COLUMN_NAME_ASSET_DETAIL_WORK_NAME,
                        COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY_CODE,
                        COLUMN_NAME_ASSET_DETAIL_WORK_SUB_CATEGORY,
                        COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_ID,
                        COLUMN_NAME_ASSET_DETAIL_WORK_CATEGORY_NAME
                }));


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private static String decode(String decodeString) {
        byte[] decodeValue = Base64.decode(decodeString, Base64.DEFAULT);
        return new String(decodeValue);
    }

    private String delete(String tableName) {
        String sqlDeleteTable;
        sqlDeleteTable = String.format(decode("RFJPUCBUQUJMRSBJRiBFWElTVFM=") + " %s", tableName);
        return sqlDeleteTable;
    }

    private String createTableLastUpdate(String tableName, String[] columns) {
        String sqlCreateTable;
        sqlCreateTable = String.format(decode("Y3JlYXRlIHRhYmxl") + " %s (%s INTEGER primary key autoincrement, %s text)", tableName, columns[0], columns[1]);
        return sqlCreateTable;
    }
    private String createTableBlockMaster(String tableName, String[] columns) {
        String sqlCreateTable;
        sqlCreateTable = String.format(decode("Y3JlYXRlIHRhYmxl") + " %s (%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)", tableName, columns[0], columns[1], columns[2], columns[3], columns[4], columns[5]);

        return sqlCreateTable;
    }
    private String createTableAssetMaster(String tableName, String[] columns) {
        String sqlCreateTable;
        sqlCreateTable = String.format(decode("Y3JlYXRlIHRhYmxl") + " %s ( %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )", tableName, columns[0], columns[1], columns[2], columns[3], columns[4], columns[5], columns[6], columns[7], columns[8]);

        return sqlCreateTable;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        @SuppressLint("Recycle") Cursor cc = db.rawQuery(prgma, null);
        super.onOpen(db);
        db.disableWriteAheadLogging();

    }

 /*
   ccmgnrega database structure

   tbl_asset_detail
    asset_id TEXT "asset_id" TEXT
    asset_name TEXT "asset_name" TEXT
    work_financial_year TEXT "work_financial_year" TEXT
    work_code TEXT "work_code" TEXT
    work_name TEXT "work_name" TEXT
    work_subcategory_code TEXT "work_subcategory_code" TEXT
    sub_category TEXT "sub_category" TEXT
    work_category_id TEXT "work_category_id" TEXT
    category_name TEXT "category_name" TEXT*/

  /*  tbl_block_master
    StateID TEXT "StateID" TEXT
    DistrictID TEXT "DistrictID" TEXT
    BlockID TEXT "BlockID" TEXT
    BlockName TEXT "BlockName" TEXT
    BlockNameLocal TEXT "BlockNameLocal" TEXT
    FinancialYear TEXT "FinancialYear" TEXT*/

  /*  tbl_lastupdate
    ID INTEGER "ID" INTEGER
    last_update TEXT "last_update" TEXT*/
}