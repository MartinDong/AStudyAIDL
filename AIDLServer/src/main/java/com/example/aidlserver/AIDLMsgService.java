package com.example.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class AIDLMsgService extends Service {
    private final String TAG = "AIDLMsgService";

    // 接收到客户端的消息
    private String receiveMsg;

    // 要下发给客户端的消息
    private String sendMsg;

    private Binder msgBinder = new IMsgApi.Stub() {

        @Override
        public void sendMsgToAIDLServer(String aString) throws RemoteException {
            Log.d(TAG, "接收到客户端发送来的信息：" + aString);
        }

        @Override
        public String syncCallAIDLServer(String aString) throws RemoteException {
            StringBuilder stringBuilder = new StringBuilder(aString);
            Log.d(TAG, "接收到客户端发送来的信息：" + stringBuilder.toString());
            Log.d(TAG, "Server 改造数据：" + stringBuilder.append("---Server修改了数据").toString());
            return stringBuilder.toString();
        }

        @Override
        public void asyncCallAIDLServer(String aString) throws RemoteException {
            StringBuilder stringBuilder = new StringBuilder(aString);
            Log.d(TAG, "接收到客户端发送来的信息：" + stringBuilder.toString());
            Log.d(TAG, "Server 改造数据：" + stringBuilder.append("---Server修改了数据").toString());
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "==onStartCommand==");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return msgBinder;
    }
}
