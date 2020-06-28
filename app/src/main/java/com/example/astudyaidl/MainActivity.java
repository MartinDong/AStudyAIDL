package com.example.astudyaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aidlserver.IMsgApi;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private IMsgApi mIMsgApi;

    // 当绑定的service异常断开连接后，会自动执行此方法
    private IBinder.DeathRecipient mDeathRecipient;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIMsgApi = IMsgApi.Stub.asInterface(service);
            try {
                if (mDeathRecipient != null) {
                    // 注册死亡代理
                    service.linkToDeath(mDeathRecipient, 0);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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

        // 设置服务断开重连的处理
        mDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                Log.e(TAG, "监听到了AIDLMsgService异常死亡💀");
            }
        };

        bindSignService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindSignService();
    }

    /**
     * 注册服务，通过Service拿到Binder对象
     */
    private void bindSignService() {
        Intent intent = new Intent();
        intent.setAction("com.example.aidlserver.AIDLMsgService");
        //从 Android 5.0开始 隐式Intent绑定服务的方式已不能使用,所以这里需要设置Service所在服务端的包名
        intent.setPackage("com.example.aidlserver");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 注销服务
     */
    private void unbindSignService() {
        try {
            if (mServiceConnection != null) {
                unbindService(mServiceConnection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btnSendMsgToAIDLServer(View view) {
        Log.d(TAG, "btnSendMsgToAIDLServer");
        if (mIMsgApi != null) {
            try {
                mIMsgApi.sendMsgToAIDLServer("我是客户端发送的数据。");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnSyncCallAIDLServer(View view) {
        Log.d(TAG, "btnSyncCallAIDLServer");
        if (mIMsgApi != null) {
            try {
                String result = mIMsgApi.syncCallAIDLServer("我是客户端发送的同步调用。");
                Log.d(TAG, "接收到了AIDL Server 的返回数据：" + result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAsyncCallAIDLServer(View view) {
        Log.d(TAG, "btnAsyncCallAIDLServer");
        if (mIMsgApi != null) {
            try {
                mIMsgApi.asyncCallAIDLServer("我是客户端发送的异步调用。");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}