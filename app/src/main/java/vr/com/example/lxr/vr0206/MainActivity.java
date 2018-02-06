package vr.com.example.lxr.vr0206;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private VrPanoramaView vr;
    private @SuppressLint("StaticFieldLeak")
    AsyncTask<Void, Void, Bitmap> voidVoidBitmapAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        vr.setInfoButtonEnabled(false);
        vr.setFullscreenButtonEnabled(false);
        vr.setDisplayMode(VrPanoramaView.DisplayMode.FULLSCREEN_STEREO);
        vr.setEventListener(new MyListner());
        //设置立体显示
        voidVoidBitmapAsyncTask = new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    InputStream open= getAssets().open("andes.jpg");
                    Bitmap bitmap= BitmapFactory.decodeStream(open);
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                VrPanoramaView.Options options= new VrPanoramaView.Options();
                //设置立体显示
                options.inputType= VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
                vr.loadImageFromBitmap(bitmap,options);
                super.onPostExecute(bitmap);

            }
        };
        voidVoidBitmapAsyncTask.execute();
    }

    private void initView() {
        vr = (VrPanoramaView) findViewById(R.id.vr);
    }

    @Override
    protected void onPause() {
        vr.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vr.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vr.shutdown();
        if (voidVoidBitmapAsyncTask!=null){
            if (!voidVoidBitmapAsyncTask.isCancelled()){
              voidVoidBitmapAsyncTask.cancel(true);
            }
        }
    }
    class MyListner extends  VrPanoramaEventListener{
        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
            Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
        }
    }
}
