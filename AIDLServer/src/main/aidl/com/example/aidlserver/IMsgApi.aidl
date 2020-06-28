// IMsgApi.aidl
package com.example.aidlserver;

// Declare any non-default types here with import statements

interface IMsgApi {

    /**
     * 客户端向 AIDL Server 端发送数据
     */
    void sendMsgToAIDLServer(String aString);

    /**
     * 同步调用 AIDL Server 端获取数据
     */
    String syncCallAIDLServer(String aString);

    /**
     * 异步调用 AIDL Server 端获取数据
     */
    oneway void asyncCallAIDLServer(String aString);
}
