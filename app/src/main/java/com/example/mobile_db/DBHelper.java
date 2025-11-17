package com.example.mobile_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {


    private static String DB_NAME = "app.db";
    private static String DB_LOCATION;
    private static final int DB_VERSION = 1;

    private final Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        DB_LOCATION = context.getApplicationInfo().dataDir + "/databases/";

        copyDB();
    }

    private boolean checkDB() {
        File fileDB = new File(DB_LOCATION+ DB_NAME);
        return fileDB.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

    }

    private void copyDB(){
        if (!checkDB()){
            this.getReadableDatabase();
            try {
                copyDBFile();
            }
            catch (IOException e){}
        }
    }

    private void copyDBFile() throws IOException {
        InputStream inputStream = myContext.getAssets().open(DB_NAME);
        OutputStream outSt = new FileOutputStream(DB_LOCATION+DB_NAME);
        byte[] buff = new byte[1024];
        int len;
        while ((len=inputStream.read(buff))> 0){
            outSt.write(buff, 0, len);
        }
        outSt.flush();
        outSt.close();
        inputStream.close();
    }
}
