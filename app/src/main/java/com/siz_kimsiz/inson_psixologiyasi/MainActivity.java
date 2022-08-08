package com.siz_kimsiz.inson_psixologiyasi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapterMain.ItemListener {


    @Override
    public void onItemClick(DataModel item) {

    }

    public final static String INDEX = "index";
    public final static String QUIZ_QUESTION_INDEX="quizindex";
    public final static String QUIZ_ANSWER_INDEX = "quizanswerindex";
    public final static String QUIZ_CHOICES_INDEX = "quizchoicesindex";
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private HashMap<Integer,ArrayList<String>> choice;
    private ArrayList<String> categorylist;
    private   RecyclerView  recyclerView;
    Intent WSApp;

    SharedPreferences mSharedPreferences;
    String marketLink;
    public MainActivity(){
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        choice = new HashMap<Integer,ArrayList<String>>();
    }

    private ArrayList<String> imagelist;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAd();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this.getApplication()))

                .init();

        ImageView about_app = (ImageView) findViewById(R.id.app_logoo);

        about_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getResources().getString(R.string.app_name));
                alert.setIcon(R.drawable.logout_logo);
                alert.setMessage("  Suqrot aytganidek “O’zingni angla va sen dunyoni anglaysan."+
                        "\n     Siz qanday inson ekanligingizni, tasavvuringiz, e’tiboringiz, fazilatlaringiz qay darajada shakllanganligini, atrofdagi insonlar  bilan muomalangiz, munosabatlaringiz  qandayligini yaxshi bilasizmi?"+
            //            "\nHech o’ylab ko’rganmisiz, buni bilish sizga atrofingizdagi odamlar bilan muomila qilishingizda qanchalik foydali bo’lishi mumkin?"+
                "\n     Ushbu psixologik testlar to’plami sizga “siz aslida kimligingizni” ko’rsatib beradi."+
                "\n     Sizga taklif etilayotgan test savollariga o’zingizga yoqqan javobni emas,  haqiqatga yaqinini tanlang." +
                "\n     Ishonamizki, siz har doim o’zingizni tahlil qilishga harakat qilasiz va yillar davomida shakllangan fe’lingizdagi kamchilikalri yo’qotib, ularni yaxshi fazilatlarga almashtirib borasiz."+
                "\n     Ilova sizga manzur bo’lgan bo’lsa, do’stlaringizga tavsiya qiling!");

                marketLink = getResources().getString(R.string.market_link);
                alert.setPositiveButton(getResources().getString(R.string.tavsiya_qilaman),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = getResources().getString(R.string.app_name)+ " android ilovasi \nGoogle play'dan ko'chirib oling - \n"+ marketLink;;
                                //Your app Name
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android app");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.tavsiya_qiling)));
                            }

                        });

                alert.setNegativeButton(getResources().getString(R.string.keyinroq),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                //alert.show();
                AlertDialog alert11 = alert.create();
                alert11.show();

                Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setBackgroundColor(Color.WHITE);
                buttonbackground.setTextColor(Color.BLUE);
                Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setBackgroundColor(Color.WHITE);
                buttonbackground1.setTextColor(Color.BLUE);
            }
        });






        getcategoryfromJson();


        String[] mobileArray = new String[categorylist.size()];

        int i = 0;
        for (String category : categorylist) {
            mobileArray[i] = category;
            i++;
        }


        Integer[] imageArray=new Integer[imagelist.size()];
        i=0;
        int id;
        for (String image : imagelist) {

            id = getResources().getIdentifier(image, "drawable", getPackageName());
            imageArray[i] = id;
            i++;
        }


        System.out.println(categorylist);

        recyclerView = (RecyclerView) findViewById(R.id.ca_list);
        final ArrayList  arrayList = new ArrayList();


        for(int count=0;count<mobileArray.length;count++) {
            arrayList.add(new DataModel(mobileArray[count], imageArray[count]));

        }


        RecyclerViewAdapterMain adapter = new RecyclerViewAdapterMain(this,arrayList,this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager);


        recyclerView.addOnItemTouchListener(new MyTouchListener(this,
                recyclerView,
                new MyTouchListener.OnTouchActionListener() {
                    @Override
                    public void onLeftSwipe(View view, int position) {
//code as per your need
                    }

                    @Override
                    public void onRightSwipe(View view, int position) {
//code as per your need
                    }

                    @Override
                    public void onClick(View view, int position) {
                        //  String categoryName = (String) adapterView.getItemAtPosition(i);

                        String categoryName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textView)).getText().toString();
                        String imagepath=imagelist.get(position).toString();
                        System.out.println("this is category image location "+imagepath);
                        System.out.println("this is category name " +categoryName);

                        Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("categoryName", categoryName);
                        intent.putExtra("categoryimage",imagepath);
                        startActivity(intent);
                        MainActivity.this.finish();

                    }
                }));

///In this using list view
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_listview, mobileArray);
//
//        adapter = new
//                CustomList(MainActivity.this, mobileArray, imageArray);
//        list=(ListView)findViewById(R.id.ca_list);
//        list.setAdapter(adapter);
//
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                String categoryName = (String) adapterView.getItemAtPosition(i);
//                System.out.println(categoryName);
//                Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
//                intent.putExtra("categoryName", categoryName);
//                startActivity(intent);
//
//            }
//        });
//
        ImageButton ourapps = (ImageButton) findViewById(R.id.otherapps);
        ourapps.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //         Uri uri = Uri.parse("https://play.google.com/store/apps/dev?id=5966232434122480987");
                //         startActivity(new Intent(Intent.ACTION_VIEW, uri));

                startActivity(new Intent(MainActivity.this, WsAppListActivity.class));

            }
        });

        //Rate App
        ImageButton rate = (ImageButton) findViewById(R.id.rateapps);
        rate.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });


        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        marketLink = "https://play.google.com/store/apps/details?id=";
        ImageButton share = (ImageButton) findViewById(R.id.shareapps);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //Your App Description with market link
                String shareBody = "Sizga tavsiya qilaman \n"+getResources().getString(R.string.app_name)+ " android ilovasi \n" + marketLink+ getPackageName();
                //Your app Name
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android app");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Yaqinlaringizga tavsiya qiling!"));

            }
        });
    }




    @Override
    protected void onStop() {
        super.onStop();
        questions.clear();
        answers.clear();
        choice.clear();
    }


    private String loadCategoryJson(){
        StringBuilder str = new StringBuilder("");
        try {
            InputStream input = this.getAssets().open("quiz_category.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String JsonString="";
            while((JsonString = br.readLine()) != null){
                str.append(JsonString+"\n");
            }
            br.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString().trim();
    }

    private void getcategoryfromJson(){

        String str2=loadCategoryJson();
        categorylist=new ArrayList<String>();
        imagelist=new ArrayList<String>();
        try {
            JSONObject object1 = new JSONObject(str2);
            JSONArray table = object1.getJSONArray("table");

            for (int i = 0 ;i <table.length()  ; i++) {
                System.out.println(i);
                categorylist.add(table.getJSONObject(i).get("category_name").toString());
                imagelist.add(table.getJSONObject(i).get("category_image").toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        loadAndShowInterAd();
        AlertDialog.Builder alert = new AlertDialog.Builder(  MainActivity.this);
        alert.setTitle("Siz kimsiz? - Inson psixologiyasi");
        alert.setIcon(R.drawable.logout_logo);
        alert.setMessage("Chiqishni xohlaysizmi?");
        //      alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(neededColor);
        //     alert.Builder(context, R.style.AlertDialogTheme)
        alert.setPositiveButton("Ha",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        MainActivity.this.finish();
                    }

                });

        alert.setNegativeButton("Yo'q",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        //   alert.show();
        AlertDialog alert11 = alert.create();
        alert11.show();

        Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground.setBackgroundColor(Color.WHITE);
        buttonbackground.setTextColor(Color.BLUE);

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(Color.WHITE);
        buttonbackground1.setTextColor(Color.BLUE);
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
                UnityAds.show(MainActivity.this,Config.interstitialAdPlacement);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s) {

            }
        });

    }

}
