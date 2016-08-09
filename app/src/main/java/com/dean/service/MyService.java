package com.dean.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dean.aidl.IMyAidlInterface;

/**
 * Created by chw on 2016/8/8.
 * 使用aidl实现跨进程
 */

public class MyService extends Service{
    private static String TAG = "cheng";
    public class MyRemoteServiceImpl extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void print(String msg) throws RemoteException {
            Log.d(TAG, "MyService print:" + msg);
        }

        @Override
        public String getRemoteData(String clientData) throws RemoteException {
            Log.d(TAG, "getRemoteData:" + clientData);
            return "remote data";
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyRemoteServiceImpl();
    }
}
