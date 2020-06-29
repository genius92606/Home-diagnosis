package info.androidhive.camerafileupload;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


//AppCompatActivity

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks{

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();


    private Activity activity;
    public static final int GetPhotoCode = 1001;

    private ImageView mShowImage;
    String imageFilePath;

    private boolean isCameraPermission = false;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private Button btnRecordVideo,btnChangeCamera;
    private Chronometer simpleChronometer;

    private static int act;
    private static String username, password, direction;

    private static String timeStamp;


    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private CameraPreview mPreview;
    private TextView title;
    private ImageView action;


    int whichCamera=0;
    int maxSize=480;
    private boolean isRecording = false;

    int tempSize=0;
    @Override


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i = getIntent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // image or video path that is captured in previous activity
        act = i.getIntExtra("act",0);
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
        direction = i.getStringExtra("direction");
        activity = this;
        initView();
        initListener();


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }


        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perms)){
            Toast.makeText(this,"打開攝影機",Toast.LENGTH_SHORT).show();
            // Create an instance of Camera
            mCamera = getCameraInstance(whichCamera);
            //mCamera=Camera.open();

            Camera.Parameters params = mCamera.getParameters();


            //Set autofocus

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // Autofocus mode is supported
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }



            //Get video record quality (afo-bot should quit this)

            if (params.getSupportedVideoSizes() != null) {
                List<Camera.Size> sizeList = params.getSupportedVideoSizes();
                for (int a = 0; a < sizeList.size(); a++) {

                    if (tempSize<sizeList.get(a).height)
                        tempSize=sizeList.get(a).height;
                    Log.d("Camera", String.valueOf(sizeList.get(a).width) + ", " + sizeList.get(a).height);
                }
            } else {
                // Video sizes may be null, which indicates that all the supported
                // preview sizes are supported for video recording.
                List<Camera.Size> sizeList = params.getSupportedPreviewSizes();
                for (int a = 0; a < sizeList.size(); a++) {

                    if (tempSize<sizeList.get(a).height)
                        tempSize=sizeList.get(a).height;
                    Log.d("Camera", String.valueOf(sizeList.get(a).width) + ", " + sizeList.get(a).height);
                }
            }


            if (tempSize>=1080) {
                Log.d("Genius","Set record quality to 1080");
                maxSize=1080;
            }
            else if(tempSize<1080&&tempSize>=720){
                Log.d("Genius","Set record quality to 720");
                maxSize=720;
            }
            else{
                Log.d("Genius","Set record quality to 480");
                maxSize=480;
            }






            mCamera.setParameters(params);


            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);



            //Log.d("Genius","Height: "+preview.getMeasuredHeight());


        }else{
            EasyPermissions.requestPermissions(this, "您在一開始的登入畫面允許開啟權限了嗎?\n 我們需要這些權限來啟動錄影功能!",123,perms);
        }





    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        activity = this;
        initView();
        initListener();


        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perms)){

            // Create an instance of Camera
            mCamera = getCameraInstance(whichCamera);
            //mCamera=Camera.open();

            Camera.Parameters params = mCamera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // Autofocus mode is supported
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(params);
            }


            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);


        }else{
            EasyPermissions.requestPermissions(this, "您在一開始的登入畫面允許開啟權限了嗎?\n 我們需要這些權限來啟動錄影功能!",123,perms);
        }


    }

    private void initView(){
        //mShowImage=(ImageView) findViewById(R.id.show_image);
        // btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
        btnChangeCamera = (Button)findViewById(R.id.btnChangeCamera);
        simpleChronometer = (Chronometer)findViewById(R.id.simpleChronometer);
        simpleChronometer.setText("按錄影機開始錄影");
        simpleChronometer.setFormat("00:%s");
        title = (TextView)findViewById(R.id.textView2);
        action= (ImageView)findViewById(R.id.imageView3);
        switch(act) {
            case 1:
                Glide.with(this).load(R.drawable.action1).fitCenter().into(action);
                break;
            case 2:
                Glide.with(this).load(R.drawable.action2).fitCenter().into(action);
                break;
            case 3:
                Glide.with(this).load(R.drawable.action3).fitCenter().into(action);
                break;
            case 4:
                Glide.with(this).load(R.drawable.action4).fitCenter().into(action);
                break;
            case 5:
                Glide.with(this).load(R.drawable.action5).fitCenter().into(action);
                break;
            case 6:
                Glide.with(this).load(R.drawable.action6).fitCenter().into(action);
                break;
            case 7:
                Glide.with(this).load(R.drawable.action7).fitCenter().into(action);
                break;
            case 8:
                Glide.with(this).load(R.drawable.action8).fitCenter().into(action);
                break;

        }

    }
    private void initListener(){


        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video


                recordVideo();
            }
        });

        btnChangeCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video


                changeCamera();
            }
        });
        if (direction.equals("Left"))
            title.setText("動作"+act+"  左手");
        else
            title.setText("動作"+act+"  右手");
    }


    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            int numbers=Camera.getNumberOfCameras();
            /*Toast.makeText(getApplicationContext(),
                    String.valueOf(numbers), Toast.LENGTH_LONG)
                    .show();*/
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    private void changeCamera(){

        mCamera.stopPreview();
        mCamera.release();
        if (whichCamera==0) //如果是後鏡頭的話
        {
            whichCamera=1;
            btnChangeCamera.setBackgroundResource(R.drawable.flip_front);


        }
        else{
            whichCamera=0;
            btnChangeCamera.setBackgroundResource(R.drawable.flip_back);
        }
        mCamera = getCameraInstance(whichCamera);
        List<Camera.Size> sizeList = mCamera.getParameters().getSupportedVideoSizes();

        for (int i = 0; i < sizeList.size(); i++) {

            if (tempSize<sizeList.get(i).height)
                tempSize=sizeList.get(i).height;
            Log.d("Camera", String.valueOf(sizeList.get(i).width) + ", " + sizeList.get(i).height);
        }


        if (tempSize>=1080)
        {
            Log.d("Camera","Set record quality to 1080");
            maxSize=1080;
        }
        else if(tempSize<1080&&tempSize>=720){
            Log.d("Camera","Set record quality to 720");
            maxSize=720;
        }
        else{
            Log.d("Camera","Set record quality to 480");
            maxSize=480;
        }

        //Log.d("Camera", String.valueOf(sizeList.get(0).height));

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }


    /**
     * Launching camera app to record video
     */

    private void recordVideo() {





        if (isRecording) {
            // stop recording and release camera
            mediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            //btnRecordVideo.setText("錄影");
            btnRecordVideo.setBackgroundResource(R.drawable.video_camera);
            //setCaptureButtonText("Capture");
            isRecording = false;
            /*
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);




            // set video quality
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // start the video capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);*/
            launchUploadActivity(false);

        } else {

            new CountDownTimer(6000, 1000) {

                public void onTick(long millisUntilFinished) {
                    simpleChronometer.setText("將於 " + millisUntilFinished / 1000 + " 秒後開始錄影");
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    //simpleChronometer.setText("錄影開始!");
                    // initialize video camera
                    if (prepareVideoRecorder()) {
                        // Camera is available and unlocked, MediaRecorder is prepared,
                        // now you can start recording
                        mediaRecorder.start();

                        new CountDownTimer(8000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                simpleChronometer.setText("錄影將在 " + millisUntilFinished / 1000 + " 秒後結束");
                            }

                            public void onFinish() {
                                if(isRecording== true)
                                {
                                    mediaRecorder.stop();  // stop the recording
                                    releaseMediaRecorder(); // release the MediaRecorder object
                                    mCamera.lock();         // take camera access back from MediaRecorder
                                    btnRecordVideo.setBackgroundResource(R.drawable.video_camera);
                                    isRecording = false;
                                    launchUploadActivity(false);
                                }

                            }

                        }.start();
                        // inform the user that recording has started
                        //btnRecordVideo.setText("停止");
                        btnRecordVideo.setBackgroundResource(R.drawable.video_camera_work);

                        //simpleChronometer.setBase(SystemClock.elapsedRealtime());
                        //simpleChronometer.start();
                        //setCaptureButtonText("Stop");
                        isRecording = true;

                    } else {
                        // prepare didn't work, release the camera
                        releaseMediaRecorder();
                        // inform user
                    }
                }

            }.start();

        }




        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //myContext = this;





        // name
/*
        Intent intent = new Intent(MainActivity.this, TakePicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", fileUri.getPath());
        intent.putExtras(bundle);
        startActivityForResult(intent, GetPhotoCode);

*/





        //startActivityForResult(new Intent(MainActivity.this, TakePicActivity.class), GetPhotoCode);
    }


    private boolean prepareVideoRecorder(){

        mCamera.stopPreview();
        mCamera.release();
        mCamera = getCameraInstance(whichCamera);
        mediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)

/*
        if(maxSize==1080)
            mediaRecorder.setProfile(CamcorderProfile.get(whichCamera,CamcorderProfile.QUALITY_1080P));
        else if(maxSize==720)
            mediaRecorder.setProfile(CamcorderProfile.get(whichCamera,CamcorderProfile.QUALITY_720P));
        else
            mediaRecorder.setProfile(CamcorderProfile.get(whichCamera,CamcorderProfile.QUALITY_480P));
*/
        mediaRecorder.setProfile(CamcorderProfile.get(whichCamera,CamcorderProfile.QUALITY_480P));


        // Step 4: Set output file
        File temp=getOutputMediaFile(MEDIA_TYPE_VIDEO);
        fileUri= Uri.fromFile(temp);
        mediaRecorder.setOutputFile(temp.toString());

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public static Camera getCameraInstance(int whichCamera){
        Camera c = null;
        try {
            c = Camera.open(whichCamera); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }



    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode== AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){}
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "您取消拍照", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "您取消錄影", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(MainActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("username",username);
        i.putExtra("act",act);
        i.putExtra("direction",direction);
        i.putExtra("time",timeStamp);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        //return Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider",getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        /*timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());*/
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    +"IMG_" + timeStamp + ".jpg");

        } else if (type == MEDIA_TYPE_VIDEO) {
            /*mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");*/
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + username + "_Motion" + act + "_" + direction + "_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }



}

