package com.dean.mutilprocess;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dean.aidl.IMyAidlInterface;

public class MainActivity extends Activity {

    private static String TAG = "cheng";
    private IMyAidlInterface myAidlInterface;

    @Override
    protected void onStart() {
        super.onStart();

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 我们没办法在构造Intent的时候就显式声明.
        Intent intent = new Intent("com.dean.aidl.IMyAidlInterface");
        // 既然没有办法构建有效的component,那么给它设置一个包名也可以生效的
        intent.setPackage("com.dean.mutilprocess");// the service package
        // 绑定服务，可设置或触发一些特定的事件
        boolean isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onMainClick(View v) {
        try {
            myAidlInterface.print("adsfasdf");
        } catch (RemoteException e) {
            Log.d(TAG, "onMainClick:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onMainClick2(View v) {
        try {
            String remoteData = myAidlInterface.getRemoteData("send client data");
            Toast.makeText(this, remoteData, Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            Log.d(TAG, "onMainClick:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
