package com.siz_kimsiz.inson_psixologiyasi;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onesignal.OneSignal;
import com.siz_kimsiz.inson_psixologiyasi.onesignal.MyNotificationOpenedHandler;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class QuizActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemListener,RecyclerViewAdapterMain.ItemListener,RecyclerViewAdapterquizText.ItemListener
{
    public static final String SCORE_TAG = "score_tag";
    private TextView tv1, tv2,Qleft, textView;
    private Button bt1,bt2,bt3,bt4;

    private int count=0;
    private int numberQuestions=0;
    private int score=0;
    private String answerId[];
    private String subjectName;
    private String categoryname;
    private ArrayList<Integer> questionNumberCount;
    private int marks;

    private ArrayList<String> questions;
    public HashMap<Integer,ArrayList<String>> choice;
    public HashMap<Integer,ArrayList<String>> scorechoice;
    public HashMap<Integer,ArrayList<String>> questionOptionmages;
    private int lastButtonpressed=-1;
    private ArrayList imageList;

    private ArrayList<String> questionTypeList;
    String categoryimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("Farman","Quiz Activity");
        setContentView(R.layout.activity_quiz);
        initAd();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this.getApplication()))
                .init();

        quizButton = (Button) findViewById(R.id.butn_next);

        quizButton.setBackgroundColor(Color.GRAY);

        subjectName= (String)getIntent().getSerializableExtra("subjectName");
        categoryname=(String) getIntent().getSerializableExtra("categoryName");

        categoryimage=(String) getIntent().getSerializableExtra("categoryimage");

        setTitle(subjectName);



        String questionAnserString = loadJson();
        getDatafromJson(questionAnserString);

        if(questions.size()>0) {

            setquestionAnswer();
        }
        else{
            Intent intent = new Intent(QuizActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            QuizActivity.this.finish();

        }


        if((questions.size())==(count+1)){
            quizButton.setText("Tugatish");
        }


        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAnswerSelected==1) {

                    oldCick=0;
                    isAnswerSelected=0;

                    System.out.println("Question size" + questions.size());
                    System.out.println("Question No" + count + 1);


                    if (count < questions.size() - 1) {


                        quizButton.setBackgroundColor(Color.GRAY);
                        lastButtonpressed = -1;

                        count++;
                        numberQuestions++;
                        System.out.println("Count " + count);
                        System.out.println("questions " + numberQuestions);


                        setquestionAnswer();



                    } else {

                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("marks", marks);
                        intent.putExtra("question", questions.size());
                        intent.putExtra("categoryName", categoryname);
                        intent.putExtra("subjectName", subjectName);
                        intent.putExtra("categoryimage",categoryimage);

                        startActivity(intent);
                        QuizActivity.this.finish();

                    }
                }


            }
        });


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



    @Override
    public void onItemClick(DataModel item) {

        System.out.println("Button pressed");

    }


    public QuizActivity(){
        questions = new ArrayList<String>();

        choice=new HashMap<Integer, ArrayList<String>>();
        scorechoice=new HashMap<Integer, ArrayList<String>>();
    }

    private String loadJson(){
        StringBuilder str = new StringBuilder("");
        try {

            InputStream input = this.getAssets().open("quiz_questons.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String JsonString = "";
            while ((JsonString = br.readLine()) != null) {
                str.append(JsonString + "\n");
            }
            br.close();
            input.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }


        return str.toString().trim();
    }


    private void getDatafromJson(String str2){
        questionTypeList=new ArrayList<String>();
        imageList=new ArrayList();
        questionOptionmages=new HashMap<>();
        questionTypeList=new ArrayList<>();
        try {
            JSONObject object1 = new JSONObject(str2);
            JSONArray table = object1.getJSONArray("table");
            String categoryName;
            String subjectName;
            questionNumberCount=new ArrayList<Integer>();
            int k=0;
            for (int i = 0 ;i < table.length() ; i++) {

                categoryName=table.getJSONObject(i).get("categoryName").toString();
                subjectName=table.getJSONObject(i).get("subject_name").toString();
                if(categoryName.equals(this.categoryname)  && subjectName.equals(this.subjectName)) {

                    questions.add(table.getJSONObject(i).get("question_text").toString());

                    imageList.add(table.getJSONObject(i).get("question_image"));
                    String questionType="";
                    questionType = table.getJSONObject(i).get("quiz_type").toString();



                    ArrayList<String> myList = new ArrayList<>();
                    ArrayList<String> myList2 = new ArrayList<>();
                    ArrayList<String> myList3 = new ArrayList<>();



//                     for (int j = 0; j < 4; j++) {
//                         myList.add(table.getJSONObject(i).getJSONArray("answer_choice").getString(j));
//                     }

                    JSONArray table2=  table.getJSONObject(i).getJSONArray("answer_choice");
                    int t=table2.length();
                    System.out.println("score image length"+table2.length());
                    System.out.println("score image "+table2);
                    for(int j=0;j<table2.length();j++) {
                        System.out.println("bunty"+table2.get(j));
                        myList.add(table2.get(j).toString());
                    }


                    table2=  table.getJSONObject(i).getJSONArray("score_choice");
                    for(int j=0;j<table2.length();j++) {
                        System.out.println("bunty"+table2.get(j));
                        myList2.add(table2.get(j).toString());
                    }




                    if (questionType.equals("Image")){
//                         for (int j = 0; j < 4; j++) {
//
//                             myList3.add(table.getJSONObject(i).getJSONArray("answer_image").getString(j));
//                         }

                        table2=  table.getJSONObject(i).getJSONArray("answer_image");
                        for(int j=0;j<table2.length();j++) {
                            System.out.println("bunty"+table2.get(j));
                            myList3.add(table2.get(j).toString());
                        }
                    }

                    choice.put(k, myList);
                    scorechoice.put(k,myList2);
                    questionOptionmages.put(k,myList3);
                    questionTypeList.add(questionType);
                    questionNumberCount.add(k);
                    System.out.println("Questio setting value : "+k);
                    k++;
                }




            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getRandomList(questions,imageList,choice,scorechoice,questionOptionmages,questionTypeList,questionNumberCount);
    }



    private void getRandomList(ArrayList<String> questions,ArrayList imageList ,HashMap<Integer,ArrayList<String>> choice,HashMap<Integer,ArrayList<String>> scorechoice,HashMap<Integer,ArrayList<String>> questionOptionmages,ArrayList<String> questionTypeList,ArrayList<Integer> questionNumberCount){

        this.questions=new ArrayList<String>();
        this.imageList=new ArrayList();
        this.choice=new HashMap<Integer,ArrayList<String>>();
        this.scorechoice=new HashMap<Integer,ArrayList<String>>();
        this.questionOptionmages=new HashMap<Integer,ArrayList<String>>();
        this.questionTypeList=new ArrayList<String>();
        this.questionNumberCount=new ArrayList<Integer>();

        ArrayList<Integer> i=new ArrayList<>();
        int count=0,f=0;

        while(i.size()<questions.size()){
            int x = (int) (Math.random()*questions.size());
            if(!(i.contains(x))) {

                i.add(x);
                count=x;
                System.out.println("Value of "+count);


                this.questions.add(questions.get(count));
                this.imageList.add(imageList.get(count));
                this.choice.put(count,choice.get(count));
                this.scorechoice.put(count,scorechoice.get(count));
                this.questionOptionmages.put(count,questionOptionmages.get(count));
                this.questionTypeList.add(questionTypeList.get(count));
                this.questionNumberCount.add(count);


                System.out.println("Questio size "+this.questions);
                System.out.println("imageList size "+this.imageList);
                System.out.println("choice size "+this.choice);
                System.out.println("scorechoice size "+this.scorechoice);
                System.out.println("questionOptionmages size "+this.questionOptionmages);
                System.out.println("questionTypeList size "+this.questionTypeList);
                System.out.println("questionNumberCount size "+this.questionNumberCount);

            }
        }


    }


    private ListView list;
    private int isAnswerSelected=0;
    private  int oldCick=0;
    private    String[] mobileArray;
    ArrayAdapter adapter;
    private  ArrayList<Integer> ll;

    private  RecyclerView  recyclerView;

    public void setquestionAnswer() {

        answerId=new String[scorechoice.get(questionNumberCount.get(count)).size()];

        System.out.println("score choice size "+scorechoice.get(questionNumberCount.get(count)).size());
        System.out.println("image choice size "+choice.get(questionNumberCount.get(count)).size());
        System.out.println("answer choice image size "+questionOptionmages.get(questionNumberCount.get(count)).size());
        System.out.println("score choice : "+scorechoice.get(count));


        if ((questions.size()) == (count + 1)) {
            quizButton.setText("Tugatish");
        }

        //          answerId=choice.get(questionNumberCount.get(count)).get( Integer.parseInt(answers.get(count))-1);
        int f=0;
        for(String answer:scorechoice.get(questionNumberCount.get(count))){
            answerId[f]=answer;
            System.out.println("Your score " +f +": " +answer);

            f++;
        }

//          answerId[0] = scorechoice.get(questionNumberCount.get(count)).get(0);
//          answerId[1] = scorechoice.get(questionNumberCount.get(count)).get(1);
//          answerId[2] = scorechoice.get(questionNumberCount.get(count)).get(2);
//          answerId[3] = scorechoice.get(questionNumberCount.get(count)).get(3);


        String image = imageList.get(count).toString();

        ImageView QimV = (ImageView) findViewById(R.id.question_image);
        int id = getResources().getIdentifier(image, "drawable", getPackageName());
        System.out.println("My Id " + id);

        QimV.setImageResource(id);


//          tv1 = (TextView)findViewById(R.id.question_text);
        tv2 = (TextView) findViewById(R.id.question_text);
        Qleft = (TextView) findViewById(R.id.QLeft);


//          tv1.setText(questions.get(count));
        tv2.setText(questions.get(count));

        Qleft.setText((count + 1) + " / " + questions.size());
//                        bt1.setText(choice.get(questionNumberCount.get(count)).get(0));
//                        bt2.setText(choice.get(questionNumberCount.get(count)).get(1));
//                        bt3.setText(choice.get(questionNumberCount.get(count)).get(2));
//                        bt4.setText(choice.get(questionNumberCount.get(count)).get(3));


        mobileArray = new String[choice.get(questionNumberCount.get(count)).size()];
        System.out.println("score option "+choice.get(questionNumberCount.get(count)).size());

        int i = 0;
        for (String category : choice.get(questionNumberCount.get(count))) {
            mobileArray[i] = category;
            i++;
        }

        Integer[] imageArray = new Integer[questionOptionmages.get(questionNumberCount.get(count)).size()];
        i = 0;
        for (String imagee : questionOptionmages.get(questionNumberCount.get(count))) {

            id = getResources().getIdentifier(imagee, "drawable", getPackageName());
            imageArray[i] = id;
            i++;
        }

//                  //= new ArrayAdapter<String>(this,
//                  R.layout.activity_listview, choice.size());



        if(questionOptionmages.get(questionNumberCount.get(count)).size()>0){
            View v = (View) findViewById(R.id.questioNoptionList);
            v.setVisibility(View.GONE);

            v = (View) findViewById(R.id.recyclerView);
            v.setVisibility(View.VISIBLE);

            System.out.println("going to set image");
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            ArrayList  arrayList = new ArrayList();


            for(int count=0;count<mobileArray.length;count++) {
                arrayList.add(new DataModel(mobileArray[count], imageArray[count]));

            }


            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList, this);
            recyclerView.setAdapter(adapter);
            GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
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

                            try {
//code as per your need
                                System.out.println("Your click in this ");
                                int i = position;
                                //   System.out.println("adapter value" + adapter.getItem(i));
                                System.out.println("View pressed" + i + " : " + mobileArray.length);
                                System.out.println("answers" + answerId[i]);
                                quizButton.setBackgroundColor(getResources().getColor(R.color.green));


//                           recyclerView.getChildAt(oldCick).setBackgroundColor(getResources().getColor(R.color.colorAccent));
//
//                           recyclerView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.green));


                                GradientDrawable shape = new GradientDrawable();
                                shape.setCornerRadius(18);
//                            shape.setShape(GradientDrawable.RECTANGLE);
                                shape.setColor(getResources().getColor(R.color.colorAccent));

                                GradientDrawable shape1 = new GradientDrawable();
                                shape1.setCornerRadius(18);
                                shape1.setColor(getResources().getColor(R.color.green));
//                            shape.setStroke(3, Color.YELLOW);
//                            PaintDrawable paintDrawable=new PaintDrawable();

//                           recyclerView.getChildAt(oldCick).setBackgroundColor(getResources().getColor(R.color.colorAccent));

                                recyclerView.getChildAt(oldCick).findViewById(R.id.relativeLayout).setBackground(shape);


                                //    textView.setTextColor(Color.BLACK);
                                recyclerView.getChildAt(i).findViewById(R.id.relativeLayout).setBackground(shape1);


//                            recyclerView.getChildAt(i).findViewById(R.id.relativeLayout).setBackgroundResource(R.drawable.rounded_bg);
//                            recyclerView.getChildAt(oldCick).findViewById(R.id.relativeLayout).setBackgroundResource(R.drawable.rounded_bg);


                                isAnswerSelected = 1;
                                View b = (View) view;
                                String userAnswer = b.toString();
                                ColorDrawable buttonColor = (ColorDrawable) recyclerView.getChildAt(i).getBackground();
                                System.out.println("last button pressed" + lastButtonpressed);
                                if (lastButtonpressed != (-1)) {
                                    System.out.println("Old answer " + Integer.parseInt(answerId[oldCick]));
                                    marks = marks - Integer.parseInt(answerId[oldCick]);
                                    System.out.println("ReButtonPress");
                                    System.out.println("ReButtonPress and your marks now " + marks);
                                }

                                lastButtonpressed = i;
                                marks = Integer.parseInt(answerId[i]) + marks;
                                System.out.println("answers" + answerId);
                                oldCick = i;
                                System.out.println("answers" + answerId[i]);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }));


        }else {
            View v = (View) findViewById(R.id.recyclerView);
            v.setVisibility(View.GONE);

            v = (View) findViewById(R.id.questioNoptionList);
            v.setVisibility(View.VISIBLE);

            recyclerView = (RecyclerView) findViewById(R.id.questioNoptionList);
            textView = (TextView)findViewById(R.id.textView);
            ArrayList  arrayList = new ArrayList();

            System.out.println("mobile array length "+mobileArray.length);
            System.out.println("image array length "+imageArray.length);

            int idd = getResources().getIdentifier("", "drawable", getPackageName());



            for(int count=0;count<mobileArray.length;count++) {
                arrayList.add(new DataModel(mobileArray[count], idd));
            }




            RecyclerViewAdapterquizText adapter = new RecyclerViewAdapterquizText(this,arrayList,this);
            recyclerView.setAdapter(adapter);



//            RecyclerViewAdapterMain adapter = new RecyclerViewAdapterMain(this,arrayList,this);
//            recyclerView.setAdapter(adapter);
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
//code as per your need

                            try {
                                System.out.println("Your click in this ");
                                int i = position;
                                //   System.out.println("adapter value" + adapter.getItem(i));
                                System.out.println("View pressed" + i + " : " + mobileArray.length);
                                System.out.println("answers" + answerId[i]);
                                quizButton.setBackgroundColor(getResources().getColor(R.color.green));


//                            recyclerView.getChildAt(oldCick).setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                           recyclerView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.green));


                                GradientDrawable shape = new GradientDrawable();
                                shape.setCornerRadius(18);
//                            shape.setShape(GradientDrawable.RECTANGLE);
                                shape.setColor(getResources().getColor(R.color.colorAccent));

                                GradientDrawable shape1 = new GradientDrawable();
                                shape1.setCornerRadius(18);
                                shape1.setColor(getResources().getColor(R.color.green));
//                            shape.setStroke(3, Color.YELLOW);
//                            PaintDrawable paintDrawable=new PaintDrawable();

//                           recyclerView.getChildAt(oldCick).setBackgroundColor(getResources().getColor(R.color.colorAccent));

                                recyclerView.getChildAt(oldCick).findViewById(R.id.relativeLayout).setBackground(shape);


                                //    textView.setTextColor(Color.BLACK);
                                recyclerView.getChildAt(i).findViewById(R.id.relativeLayout).setBackground(shape1);


//                            recyclerView.getChildAt(i).findViewById(R.id.relativeLayout).setBackgroundResource(R.drawable.rounded_bg);
//                            recyclerView.getChildAt(oldCick).findViewById(R.id.relativeLayout).setBackgroundResource(R.drawable.rounded_bg);


                                isAnswerSelected = 1;
                                View b = (View) view;
                                String userAnswer = b.toString();
                                System.out.println("last button pressed" + lastButtonpressed);
                                if (lastButtonpressed != (-1)) {
                                    System.out.println("Old answer " + Integer.parseInt(answerId[oldCick]));
                                    marks = marks - Integer.parseInt(answerId[oldCick]);
                                    System.out.println("ReButtonPress");
                                    System.out.println("ReButtonPress and your marks now " + marks);
                                }

                                lastButtonpressed = i;
                                marks = Integer.parseInt(answerId[i]) + marks;
                                System.out.println("answers" + answerId);
                                oldCick = i;
                                System.out.println("answers" + answerId[i]);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }));

        }

    }
    Button quizButton ;

    @Override
    public void onBackPressed()

    {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(  QuizActivity.this);
        alert.setTitle(subjectName);
        alert.setMessage("Testni tugatmasdan chiqmoqchimisiz?");
        //      alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(neededColor);
        //     alert.Builder(context, R.style.AlertDialogTheme)
        alert.setPositiveButton("Ha",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        Intent intent = new Intent(QuizActivity.this, SubjectsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("categoryName", categoryname);
                        intent.putExtra("categoryimage",categoryimage);
                        startActivity(intent);
                        QuizActivity.this.finish();
                        finish();
                    }

                });

        alert.setNegativeButton("Yo'q",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //   alert.show();
        android.app.AlertDialog alert11 = alert.create();
        alert11.show();

        Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground.setBackgroundColor(Color.WHITE);
        buttonbackground.setTextColor(Color.BLUE);

        Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(Color.WHITE);
        buttonbackground1.setTextColor(Color.BLUE);

        return;
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
}
