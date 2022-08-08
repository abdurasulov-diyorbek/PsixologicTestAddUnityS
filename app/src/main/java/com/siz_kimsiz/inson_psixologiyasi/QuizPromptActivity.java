package com.siz_kimsiz.inson_psixologiyasi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onesignal.OneSignal;
import com.siz_kimsiz.inson_psixologiyasi.onesignal.MyNotificationOpenedHandler;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;


public class QuizPromptActivity extends AppCompatActivity {

    private Activity mActivity;
    private Context mContext;
    Button mBtnYes, mBtnNo;
    String subjectdescrition = "";
    String subjectimage = "";
    String subjectName = "";

    public void setdataonLayout() {
        int subjectimageid = getResources().getIdentifier(subjectimage, "drawable", getPackageName());
        Drawable drawable = getResources().getDrawable(subjectimageid);

        ImageView imageView = (ImageView) findViewById(R.id.subject_image);
        imageView.setImageDrawable(drawable);


        TextView sdescrition = (TextView) findViewById(R.id.subject_description);
        sdescrition.setText(subjectdescrition);


        TextView subjectname = (TextView) findViewById(R.id.subject_title);
        subjectname.setText(subjectName);


    }

    private String categoryimage = "", categoryname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initVar();

        initView();
        initAd();
        initListener();


        subjectdescrition = (String) getIntent().getSerializableExtra("subjectdescrition");
        subjectimage = (String) getIntent().getSerializableExtra("subjectimage");
        subjectName = (String) getIntent().getSerializableExtra("subjectName");
        categoryimage = (String) getIntent().getSerializableExtra("categoryimage");
        categoryname = (String) getIntent().getSerializableExtra("categoryname");


        setdataonLayout();
       /* admobInterstitial = new InterstitialAd(this);
        admobInterstitial.setAdUnitId(getResources().getString(R.string.admob_interstitial));

        admobInterstitial.loadAd(new AdRequest.Builder().build());
        if (admobInterstitial.isLoaded()) {
            admobInterstitial.show();
        }*/

       /* if (!(isNetworkAvailable(this))) {
            System.out.println("Internet is not avaiable");
            findViewById(R.id.admob_adview_fragment).setVisibility(View.GONE);
        }*/
        loadAndShowInterAd();


    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initVar() {
        mActivity = QuizPromptActivity.this;
        mContext = mActivity.getApplicationContext();
    }

    private void initView() {
        setContentView(R.layout.activity_quiz_promt);
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this.getApplication()))
                .init();
        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);
        setTitle("Test haqida");

    }


    public void initListener() {
        mBtnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadAndShowInterAd();
                Intent intent = new Intent(QuizPromptActivity.this, QuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("subjectName", subjectName);
                intent.putExtra("categoryName", categoryname);
                intent.putExtra("categoryimage", categoryimage);
                startActivity(intent);
                QuizPromptActivity.this.finish();

            }
        });
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAndShowInterAd();
                Intent intent = new Intent(QuizPromptActivity.this, SubjectsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("categoryName", categoryname);
                intent.putExtra("categoryimage", categoryimage);
                startActivity(intent);
                QuizPromptActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(QuizPromptActivity.this, QuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                QuizPromptActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuizPromptActivity.this, SubjectsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("categoryName", categoryname);
        intent.putExtra("categoryimage", categoryimage);
        startActivity(intent);
        QuizPromptActivity.this.finish();
    }
    private void initAd() {
        UnityAds.initialize(this, Config.GameID, null, Config.TEST_MODE, Config.ENABLED_LOAD);
        RelativeLayout bannerAdContainer = findViewById(R.id.adView);
        BannerView topBanner = new BannerView(this, Config.bannerAdPlacement, new UnityBannerSize(320, 50));
        topBanner.setListener(new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerView) {
                bannerAdContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBannerClick(BannerView bannerView) {

            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {

            }

            @Override
            public void onBannerLeftApplication(BannerView bannerView) {

            }
        });
        topBanner.load();
        bannerAdContainer.addView(topBanner);


    }

    private void loadAndShowInterAd()
    {
        UnityAds.load(Config.interstitialAdPlacement, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                UnityAds.show(QuizPromptActivity.this,Config.interstitialAdPlacement);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s) {

            }
        });

    }
}
