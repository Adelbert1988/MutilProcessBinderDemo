package com.dean.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by chw on 2016/8/8.
 * 不用aidl，实现跨进程通讯
 */

public class MyRemoteService extends Service{

    private static final String TAG = "myaidltest:service";
    private Handler mHandler = new Handler();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private MyPrinterInterfaceStub mBinder = new MyPrinterInterfaceStub() {
        @Override
        public void print(String msg) throws RemoteException {
            MyRemoteService.this.print(msg);
        }
    };

    public void print(String msg) {
        try {
            Log.e(TAG, "Preparing printer...");
            Thread.sleep(1000);
            Log.e(TAG, "Connecting printer...");
            Thread.sleep(1000);
            Log.e(TAG, "Printing.... " + msg);
            Thread.sleep(1000);
            Log.e(TAG, "Done");
        } catch (InterruptedException e) {

        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyRemoteService.this, "MyRemoteService Printing is done.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface MyServicePrinterInterface extends android.os.IInterface {
        void print(String msg) throws android.os.RemoteException;
    }


    abstract class MyPrinterInterfaceStub extends Binder implements MyServicePrinterInterface {
        private static final String DESCRIPTOR = "MyPrinterInterface";
        private static final String TAG = "MyPrinterInterfaceStub";


        public MyPrinterInterfaceStub() {
            attachInterface(this, DESCRIPTOR);
        }


        @Override
        public IBinder asBinder() {
            return this;
        }


        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws android.os.RemoteException {
            Log.e(TAG, "onTransact, code is " + code);
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    Log.e(TAG, "onTransact, code is " + code + ", when this happens");
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_print: {
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0;
                    _arg0 = data.readString();
                    Log.e(TAG, "ontransact, arg is " + _arg0 + ", when this happened?");
                    this.print(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        static final int TRANSACTION_print = (IBinder.FIRST_CALL_TRANSACTION + 0);
    }
}
