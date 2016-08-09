package com.dean.mutilprocess;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

/**
 * Created by chw on 2016/8/8.
 * 不用aidl，实现跨进程通讯
 */

public class MyAidlAcivity extends Activity{
    private static final String TAG = "myaidltest:activity";
    MyPrinterInterface mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_aidl);
        setTitle("My interface another client Activity");
    }


    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
    }


    private void doBindService() {
        Intent intent = new Intent();
        intent.setClassName("com.dean.mutilprocess", "com.dean.service.MyRemoteService");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
    }


    private void doUnbindService() {
        if (mService != null) {
            unbindService(mConnection);
        }
    }

    public void onButtonClick(View v) {
        if (mService == null) {
            Log.e(TAG, "what the fucl service is not ready");
            return;
        }
        try {
            mService.print("In another application, create a client based on user defined IPC interfaces");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "on service connected, service obj " + service);
            mService = MyPrinterInterface.Stub.asInterface(service);
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
        }
    };

    public interface MyPrinterInterface extends android.os.IInterface {
        public void print(String msg) throws android.os.RemoteException;

        public abstract class Stub extends Binder implements MyPrinterInterface {
            private static final String DESCRIPTOR = "MyPrinterInterface";
            private static final String TAG = "MyPrinterInterface.Stub";

            public Stub() {
                attachInterface(this, DESCRIPTOR);
            }

            public static MyPrinterInterface asInterface(IBinder obj) {
                if ((obj == null)) {
                    return null;
                }


                Log.e(TAG, "we are talking to a remote one, we must use a proxy object to wrapper binder");
                return new Stub.Proxy(obj);
            }

            static final int TRANSACTION_print = (IBinder.FIRST_CALL_TRANSACTION + 0);

            private static class Proxy implements MyPrinterInterface {
                private IBinder mRemote;

                Proxy(IBinder remote) {
                    mRemote = remote;
                }

                @Override
                public IBinder asBinder() {
                    return mRemote;
                }

                public String getInterfaceDescriptor() {
                    return DESCRIPTOR;
                }

                @Override
                public void print(String msg) throws android.os.RemoteException {
                    Parcel _data = Parcel.obtain();
                    Parcel _reply = Parcel.obtain();
                    try {
                        _data.writeInterfaceToken(DESCRIPTOR);
                        _data.writeString(msg);
                        mRemote.transact(Stub.TRANSACTION_print, _data, _reply, 0);
                        Log.e(TAG, "lalalala, let us passing the parameters and calling the message");
                        _reply.readException();
                    } finally {
                        _reply.recycle();
                        _data.recycle();
                    }
                }
            }
        }
    }
}
