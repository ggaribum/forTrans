/*

 Fragment_Detail_Info -> 팁 표시한 채로 탭 이동시 이상함 (Best만)

 */


package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Image;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Main;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Notify;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Test;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

import static onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class BaseActivity extends AppCompatActivity {

    public static boolean isKorean;
    private TextView tv_languageCheck;

    public static List<Model_Test> testArr;
    public static ArrayList<Model_Image> imageList;
    public static ArrayList<Model_Main> mainList;
    public static ArrayList<Model_Notify> notifyList;
    Intent intent;

    public static String name;
    public static String email;

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "MainActivity";

    public static int isLogined;  // 0 : 로그인 안됨  1 : 구글 로그인   2 : 페북 로그인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        email = "";
        name = "";

        tv_languageCheck = (TextView) findViewById(R.id.tv_languageCheck);
        if(tv_languageCheck.getText().toString().equals("Korean ver.")){
            isKorean = true;
        }else if(tv_languageCheck.getText().toString().equals("English ver.")){
            isKorean = false;
        }

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni != null && ni.isConnected()) {

            intent = new Intent(getApplicationContext(), LoadingActivity.class);

            notifyList = new ArrayList<>();
            testArr = new ArrayList<>();
            imageList = new ArrayList<>();
            mainList = new ArrayList<>();

            isLogined = chkLoogined(); // 이걸 로그인이 호출되는 시점마다 해줘야함!!

            if (!checkPermissionWRITE_EXTERNAL_STORAGE(this)) {

            } else {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        }else{
            String message = "네트워크 설정을 확인한 뒤, 앱을 완전히 종료하고 다시 실행시켜 주십시오.";
            if(!isKorean) message = "Please check a network state and restart the application.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }


    }

    public boolean checkPermissionWRITE_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    showDialog("Permission", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                }
                return false;

            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + "about external storage has required. \nPlease grant the permission to use application.");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String message = "권한이 허가되었습니다. \n잠시후 앱이 실행됩니다.";
                    if(!isKorean) message = "Permission has granted. \nPlease wait a second.";
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                } else {
                    String message = "권한 허가가 이루어지지 않았습니다. \n앱을 완전히 종료하고 다시 실행시켜 주십시오.";
                    if(!isKorean) message = "Permission has not granted. \nPlease restart the application.";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }



    public int chkLoogined(){

        int chkLogin=0;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this /* FragmentActivity */ ,new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult connectionResult) {
                                Log.d(TAG, "onConnectionFailed:" + connectionResult);

                            }
                        } /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                email=acct.getEmail();
                name=acct.getDisplayName();

                chkLogin=1;

            } else {
            }
        }

        if(chkLogin!=1) {

            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            if (accessToken != null) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result", object.toString());

                        try {
                            email = object.getString("email");       // 이메일
                            name = object.getString("name");         // 이름



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                chkLogin=2;

            }
        }

        return chkLogin;


    }


}
