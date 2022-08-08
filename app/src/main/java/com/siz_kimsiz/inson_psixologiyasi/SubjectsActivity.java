package com.siz_kimsiz.inson_psixologiyasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.onesignal.OneSignal;
import com.siz_kimsiz.inson_psixologiyasi.onesignal.MyNotificationOpenedHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SubjectsActivity extends AppCompatActivity  implements RecyclerViewAdapterMain.ItemListener {

    @Override
    public void onItemClick(DataModel item) {
    }
    public final static String INDEX = "index";
    private RecyclerView recyclerView;
    private ArrayList<String> subjectList;

    ListView list;
    Intent WSApp;
    SharedPreferences mSharedPreferences;
    String marketLink;


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SubjectsActivity.this,MainActivity.class);
        startActivity(intent);
        SubjectsActivity.this.finish();

    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this.getApplication()))
                .init();

        final String categoryName= (String)getIntent().getSerializableExtra("categoryName");
        final  String categoryimage= (String)getIntent().getSerializableExtra("categoryimage");


        getsubjectfromJson(categoryName);
        setTitle(categoryName);
        if(subjectList.size()!=0) {
            setContentView(R.layout.activity_quiz_subject);
            String[] mobileArray = new String[subjectList.size()];
            int i = 0;
            for (String subject : subjectList) {
                mobileArray[i] = subject;
                i++;
            }
            Integer[] imageArray=new Integer[imagelist.size()];
            i=0;
            int id;
            for (String image : imagelist) {
                id = getResources().getIdentifier(image, "drawable", getPackageName());
                System.out.println("Image source"+image);
                imageArray[i] = id;
                i++;
            }

            recyclerView = (RecyclerView) findViewById(R.id.sub_list);
            ArrayList  arrayList = new ArrayList();
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
                            quizposition=position;

                            String subjectName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textView)).getText().toString();
                            Intent intent = new Intent(SubjectsActivity.this, QuizPromptActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.putExtra("subjectName", subjectName);
                            intent.putExtra("subjectdescrition", subjectdescription.get(position));
                            intent.putExtra("subjectimage",imagelist.get(position));
                            intent.putExtra("categoryname",categoryName);
                            intent.putExtra("categoryimage",categoryimage);

                            startActivity(intent);
                            SubjectsActivity.this.finish();
//
//                            String subjectName = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.textView)).getText().toString();
//                           Intent intent = new Intent(SubjectsActivity.this, QuizActivity.class);
//
//                            intent.putExtra("subjectName", subjectName);
//                            intent.putExtra("categoryName", categoryName);
//                            intent.putExtra("categoryimage",categoryimage);
//                            startActivity(intent);
                        }
                    }));
        }
        else{
            Intent intent = new Intent(SubjectsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            SubjectsActivity.this.finish();
        }
    }
    private String loadJson(){
        StringBuilder str = new StringBuilder("");
        try {
            InputStream input = this.getAssets().open("quiz_subjects.json");
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


    private static ArrayList<String> imagelist;
    private static ArrayList<String> subjectdescription;
    private static int quizposition;

    public static int getPostition() {
        return quizposition;
    }

    public static ArrayList<String> getImagelist() {
        return imagelist;
    }

    public static ArrayList<String> getSubjectdescription() {
        return subjectdescription;
    }

    private void getsubjectfromJson(String categoryName){


        String str2=loadJson();
        subjectList=new ArrayList<String>();
        imagelist=new ArrayList<String>();
        subjectdescription=new ArrayList<String>();

        try {
            JSONObject object1 = new JSONObject(str2);
            JSONArray table = object1.getJSONArray("subject");

            for (int i = 0 ;i <table.length()  ; i++) {
                System.out.println(i);

                String categoryName2=table.getJSONObject(i).get("categoryName").toString();
                if(categoryName.equals(categoryName2)) {
                    subjectList.add(table.getJSONObject(i).get("subject_name").toString());
                    imagelist.add(table.getJSONObject(i).get("subject_image").toString());
                    subjectdescription.add(table.getJSONObject(i).get("subject_description").toString());

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
