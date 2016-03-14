package cn.ismartv.recyclerview.sample.core;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import cn.ismartv.log.interceptor.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huaijie on 5/28/15.
 */
public class IsmartvClient extends Thread {
    private static final String TAG = "IsmartvClient";

    private static final int SUCCESS = 0x0001;
    private static final int FAILURE = 0x0002;
    private static final int DEFAULT_TIMEOUT = 10000;
    private String url;
    private CallBack callback;
    private MessageHandler messageHandler = new MessageHandler();

    public IsmartvClient(String url, CallBack callback) {
        this.callback = callback;
        this.url = url;
    }


    @Override
    public void run() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Message message = messageHandler.obtainMessage();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            Log.i(TAG, "--->\n" +
                    "\turl is: " + "\t" + url + "\n" +
                    "\tresult is: " + "\t" + result + "\n");
            message.what = SUCCESS;
            message.obj = result;
        } catch (IOException e) {
            message.what = FAILURE;
            message.obj = e;
        }
        messageHandler.sendMessage(message);
    }


    public interface CallBack {
        void onSuccess(String result);

        void onFailed(Exception exception);
    }


    class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    callback.onSuccess((String) msg.obj);
                    break;
                case FAILURE:
                    callback.onFailed((Exception) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }
}
