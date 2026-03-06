package nic.hp.ccmgnrega.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteAssetHelper extends com.readystatesoftware.sqliteasset.SQLiteAssetHelper {
    private Context context;
    private static final String DATABASE_NAME = "mgnrega_asset.db";
    private static final int DATABASE_VERSION = 1;
    public SQLiteAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
