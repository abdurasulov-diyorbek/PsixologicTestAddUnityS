package com.siz_kimsiz.inson_psixologiyasi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    public TextView tv1;
    public ImageView emotion;
    ArrayList<String[]> list;
    String marketLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initAd();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this.getApplication()))
                .init();

        int score = (Integer) getIntent().getSerializableExtra("marks");
        int TotalQuestion = (Integer) getIntent().getSerializableExtra("question");
        final String categoryName = getIntent().getSerializableExtra("categoryName").toString();
        final String subjectName = getIntent().getSerializableExtra("subjectName").toString();
        final String categoryimage = getIntent().getSerializableExtra("categoryimage").toString();


        getsubjectfromJson(categoryName, subjectName);



        setTitle(subjectName);

        int count = 0;
        for (String s[] : list) {
            System.out.println("RecordNumber " + (count++));
            System.out.println("My scores and Image Path" + s[0]);
            System.out.println("My scores and Image Path" + s[1]);
            System.out.println("My scores and Image Path" + s[2]);
            System.out.println("My scores and Image Path" + s[3]);

        }

        String s[] = list.get(0);
        String s1[] = list.get(1);
        String s2[] = list.get(2);
        String s3[] = list.get(3);
        String s4[] = list.get(4);

        tv1 = (TextView) findViewById(R.id.score_text);
        emotion = (ImageView) findViewById(R.id.score_image);

        int minScore = Integer.parseInt(s[0]);
        int maxScore = Integer.parseInt(s[1]);
        String Image = s[2];
        String textResult = s[3];


        int minScore2 = Integer.parseInt(s1[0]);
        int maxScore2 = Integer.parseInt(s1[1]);
        String Image2 = s1[2];
        String textResult2 = s1[3];


        int minScore3 = Integer.parseInt(s2[0]);
        int maxScore3 = Integer.parseInt(s2[1]);
        String Image3 = s2[2];
        String textResult3 = s2[3];


        int minScore4 = Integer.parseInt(s3[0]);
        int maxScore4 = Integer.parseInt(s3[1]);
        String Image4 = s3[2];
        String textResult4 = s3[3];


        int minScore5 = Integer.parseInt(s4[0]);
        int maxScore5 = Integer.parseInt(s4[1]);
        String Image5 = s4[2];
        String textResult5 = s4[3];

        ImageView QimV = (ImageView) findViewById(R.id.score_image);

        if (score <= maxScore) {
            int id = getResources().getIdentifier(Image, "drawable", getPackageName());
            QimV.setImageResource(id);
            //  tv1.setText(textResult+"  \n Your Score "+score);
            tv1.setText(textResult);

        } else if (score >= minScore2 && score <= maxScore2) {
            int id = getResources().getIdentifier(Image2, "drawable", getPackageName());
            QimV.setImageResource(id);
            tv1.setText(textResult2);
        } else if (score >= minScore3 && score <= maxScore3) {
            int id = getResources().getIdentifier(Image3, "drawable", getPackageName());
            QimV.setImageResource(id);
            tv1.setText(textResult3);
        } else if (score >= minScore4 && score <= maxScore4) {

            int id = getResources().getIdentifier(Image4, "drawable", getPackageName());
            QimV.setImageResource(id);
            tv1.setText(textResult4);
        } else {
            int id = getResources().getIdentifier(Image5, "drawable", getPackageName());
            QimV.setImageResource(id);
            tv1.setText(textResult5);
        }


        final Button buttonMainMenu = (Button) findViewById(R.id.btn_go_main_page);
        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadAndShowInterAd();
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

        final Button playAgain = (Button) findViewById(R.id.btn_play_again);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("play again button click");
                loadAndShowInterAd();

                Intent intent = new Intent(ResultActivity.this, QuizPromptActivity.class);
                intent.putExtra("subjectName", subjectName);
                intent.putExtra("subjectdescrition", SubjectsActivity.getSubjectdescription().get(SubjectsActivity.getPostition()));
                intent.putExtra("subjectimage", SubjectsActivity.getImagelist().get(SubjectsActivity.getPostition()));
                intent.putExtra("categoryname", categoryName);
                intent.putExtra("categoryimage", categoryimage);
                startActivity(intent);


            }

        });


        final AppCompatActivity context = this;
        final Button buttonShare = (Button) findViewById(R.id.btn_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPostShare) {
                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                20);

                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                20);
                    } else {

                        Bitmap bitmap = takeScreenshot();
                        saveBitmap(bitmap);
                        shareIt();
                    }
                }
            }

        });


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


    private String loadJson() {
        StringBuilder str = new StringBuilder("");
        try {
            InputStream input = this.getAssets().open("quiz_subjects.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String JsonString = "";
            while ((JsonString = br.readLine()) != null) {
                str.append(JsonString + "\n");
            }
            br.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString().trim();
    }

    private void getsubjectfromJson(String categoryName, String subjectName) {
        list = new ArrayList<String[]>();

        String str2 = loadJson();
        try {
            JSONObject object1 = new JSONObject(str2);
            JSONArray table = object1.getJSONArray("subject");

            for (int i = 0; i < table.length(); i++) {
                System.out.println(i);

                String categoryName2 = table.getJSONObject(i).get("categoryName").toString();
                String subjectname2 = table.getJSONObject(i).get("subject_name").toString();

                if (categoryName.equals(categoryName2) && subjectName.equals(subjectname2)) {
                    String[] s = new String[4];
                    s[0] = table.getJSONObject(i).get("score_range_1_min").toString();
                    s[1] = table.getJSONObject(i).get("score_range_1_max").toString();
                    s[2] = table.getJSONObject(i).get("score_range_1_image").toString();
                    s[3] = table.getJSONObject(i).get("score_range_1_text").toString();

                    list.add(s);
                    s = new String[4];
                    s[0] = table.getJSONObject(i).get("score_range_2_min").toString();
                    s[1] = table.getJSONObject(i).get("score_range_2_max").toString();
                    s[2] = table.getJSONObject(i).get("score_range_2_image").toString();
                    s[3] = table.getJSONObject(i).get("score_range_2_text").toString();


                    list.add(s);
                    s = new String[4];
                    s[0] = table.getJSONObject(i).get("score_range_3_min").toString();
                    s[1] = table.getJSONObject(i).get("score_range_3_max").toString();
                    s[2] = table.getJSONObject(i).get("score_range_3_image").toString();
                    s[3] = table.getJSONObject(i).get("score_range_3_text").toString();


                    list.add(s);
                    s = new String[4];
                    s[0] = table.getJSONObject(i).get("score_range_4_min").toString();
                    s[1] = table.getJSONObject(i).get("score_range_4_max").toString();
                    s[2] = table.getJSONObject(i).get("score_range_4_image").toString();
                    s[3] = table.getJSONObject(i).get("score_range_4_text").toString();


                    list.add(s);

                    s = new String[4];
                    s[0] = table.getJSONObject(i).get("score_range_5_min").toString();
                    s[1] = table.getJSONObject(i).get("score_range_5_max").toString();
                    s[2] = table.getJSONObject(i).get("score_range_5_image").toString();
                    s[3] = table.getJSONObject(i).get("score_range_5_text").toString();

                    list.add(s);

                    s = new String[6];
                    s[0] = table.getJSONObject(i).get("score_range_6_min").toString();
                    s[1] = table.getJSONObject(i).get("score_range_6_max").toString();
                    s[2] = table.getJSONObject(i).get("score_range_6_image").toString();
                    s[3] = table.getJSONObject(i).get("score_range_6_text").toString();

                    list.add(s);

                    return;
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }





    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private File imagePath;

    private void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/scrnshot.png"); ////File imagePath
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

    }

    private Boolean isPostShare = false;

    private void shareIt() {


        marketLink = "https://play.google.com/store/apps/details?id=";
        Uri uri = Uri.fromFile(imagePath);
        System.out.println("image path" + imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        Intent intent = sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Test natijasi.\n" + getResources().getString(R.string.app_name) + " android ilovasi \n" + marketLink + getPackageName());
        startActivity(Intent.createChooser(sharingIntent, "Do'stlaringizga ulashing!"));
        isPostShare = true;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //   intent.putExtra("categoryName", categoryname);
        //     intent.putExtra("categoryimage",categoryimage);
        startActivity(intent);
        ResultActivity.this.finish();

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
                UnityAds.show(ResultActivity.this,Config.interstitialAdPlacement);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s) {

            }
        });

    }
}
