package com.zy.gifdownloader;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class MainService extends Service {
    public static final String TAG = MainService.class.getSimpleName();
    OkHttpClient mOkHttpClient;
    private Movie mMovie;
    private static int imageIndex = 0;
    ClipboardManager mClipboardManager;
    ClipboardManager.OnPrimaryClipChangedListener mClipChangedListener;
    class MyClipListener implements ClipboardManager.OnPrimaryClipChangedListener {

            public void onPrimaryClipChanged() {
                ClipData clipData = mClipboardManager.getPrimaryClip();
                if (clipData != null) {
                    String text = clipData.getItemAt(0).getText().toString();
                    if (text.startsWith("http")) {
                        downloadImage(text);
                    }
                }
            }
        }

    private void writeBitmapToDisk(InputStream response) {
        Log.d(TAG, "writeBitmapToDisk: start");
        /*
        GifDrawable oldDrawable = null;
        GifDrawable drawable = null;
        try {
            oldDrawable = new GifDrawable(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        GifDrawableBuilder builder = new GifDrawableBuilder();
        builder.sampleSize(2);
        try {
            drawable = builder.from(response).threadPoolSize(5).with(oldDrawable).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        FileOutputStream os = null;
        File gifImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "gif");
        Log.d(TAG, "writeBitmapToDisk: gifImage 路径" + gifImage.getAbsolutePath());
        if (!gifImage.exists()) {
            gifImage.mkdir();
        }
        String name = String.valueOf(System.currentTimeMillis()) + ".gif";
        File imageFile = new File(gifImage.getAbsolutePath() + File.separator + name);
        if (!imageFile.exists()) {

            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "writeBitmapToDisk: create imageFile");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(response, null, options);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
        try {
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        try {
            //br = new BufferedReader(new InputStreamReader(response));
            //bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(imageFile)));
            os = new FileOutputStream(imageFile);
            byte[] bytes = new byte[1024];
            int i = 0;
            try {
                while ((i = response.read(bytes)) != -1) {
                    //这里非常重要，如果只写os.write(bytes),文件显示不全
                    os.write(bytes,0,i);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        */
    }

    private void downloadImage(String text) {
        Request request = new Request.Builder().url(text).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainService.this, "图片下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //Toast.makeText(MainService.this, "图片下载成功", Toast.LENGTH_SHORT).show();
                InputStream is = response.body().byteStream();
                writeBitmapToDisk(is);

            }
        });

        /*
        //不采用Volley这种请求方式
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest imageRequest = new ImageRequest(text, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Toast.makeText(MainService.this, "图片下载成功", Toast.LENGTH_SHORT).show();
                writeBitmapToDisk(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainService.this, "图片下载失败", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(imageRequest);
        */
    }

    public MainService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipChangedListener = new MyClipListener();
        mClipboardManager.addPrimaryClipChangedListener(mClipChangedListener);
        mOkHttpClient = new OkHttpClient();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipboardManager.removePrimaryClipChangedListener(mClipChangedListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}
