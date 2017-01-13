package com.nibiru.evil_ap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nibiru.evil_ap.log.Client;
import com.nibiru.evil_ap.log.DatabaseManager;
import com.nibiru.evil_ap.log.LogDbContract;
import com.nibiru.evil_ap.log.LogDbHelper;
import com.nibiru.evil_ap.log.LogEntry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nibiru on 2016-12-17.
 */

public class SharedClass {
    /**************************************CLASS FIELDS********************************************/
    protected final String TAG = getClass().getSimpleName();
    private volatile byte[] imgData;
    private volatile List<String> payloads;
    private volatile DatabaseManager mDbManager;
    private IMVP.ModelOps mModel;
    /**************************************CLASS METHODS*******************************************/
    public SharedClass(InputStream is, Context ctx, IMVP.ModelOps model){
        try {
            loadStream(is);
            DatabaseManager.initializeInstance(new LogDbHelper(ctx));
            mDbManager = DatabaseManager.getInstance();
            mDbManager.openDatabase();
            mDbManager.cleanDatabase();
            mModel = model;
            //TODO: close ?
            //DatabaseManager.getInstance().closeDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //no need for synchronized since we only use one database connections
    //and log entries will be added in a queue manner
    public void addRequest(Client c, String host, String reqLine, String headers){
        mDbManager.addRequest(c, host, reqLine, headers);
    }

    //get client log
    public List<LogEntry> getClientLog(Client c){
        return mDbManager.getClientLog(c);
    }

    synchronized void loadImage(String path){
        File file = new File(path);
        try {
            InputStream imgStream = new FileInputStream(file);
            loadStream(imgStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void loadStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        imgData = buffer.toByteArray();
    }

    synchronized void setPayloads(List<String> p){
        payloads = p;
    }

    public Client getClientByIp(String ip){
        return mModel.getClientByIp(ip);
    }
    public List<String> getPayloads(){
        return payloads;
    }
    public byte[] getImgData(){
        return imgData;
    }
    public byte[] getImgChunk(int start, int end){
        return Arrays.copyOfRange(imgData, start, end);
    }
    public int getImgDataLength(){
        return imgData.length;
    }
}
