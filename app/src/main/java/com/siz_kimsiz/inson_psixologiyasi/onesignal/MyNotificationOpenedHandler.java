package com.siz_kimsiz.inson_psixologiyasi.onesignal;


import android.app.Application;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;



import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.siz_kimsiz.inson_psixologiyasi.MainActivity;
import com.siz_kimsiz.inson_psixologiyasi.QuizPromptActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by androidbash on 12/14/2016.
 */

public class MyNotificationOpenedHandler extends AppCompatActivity implements OneSignal.NotificationOpenedHandler {

    String category=null,subject=null,categoryimage=null,subjectdescription=null,subjectimage=null;
private Application application;
public MyNotificationOpenedHandler(Application application){
    this.application=application;
}


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String activityToBeOpened;

        //While sending a Push notification from OneSignal dashboard
        // you can send an addtional data named "activityToBeOpened" and retrieve the value of it and do necessary operation
        //If key is "activityToBeOpened" and value is "MyAnotherActivity", then when a user clicks
        //on the notification, MyAnotherActivity will be opened.
        //Else, if we have not set any additional data MainActivity is opened.
        if (data != null) {

            category = data.optString("categoryname", null);
            subject = data.optString("subjectname", null);
//            categoryimage=data.optString("categoryimage",null);

            getcategoryfromJson();
            getsubjectfromJson();
//            subjectdescription=data.optString("subjectdescription",null);
//            subjectimage=data.optString("subjectimage",null);



            System.out.println("category id" + category);
            System.out.println("subject id" + subject);
            System.out.println("categoryimage id" + categoryimage);
            System.out.println("subject_image " + subjectimage);
            System.out.println("subject_description " + subjectdescription);


            //  Toast.makeText(MyApplication.getContext(), subject + " "+category, Toast.LENGTH_LONG).show();


//            Log.i(TAG, "Notification subject:" + subject);
//            Log.i(TAG, "Notification category:" + category);

            if (category != null && subject != null  && categoryimage!=null   && subjectimage!=null  && subjectdescription!=null) {
                Intent intent = new Intent(application, QuizPromptActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

//                intent.putExtra("subjectName", subject);
//                intent.putExtra("categoryName", category);
//                intent.putExtra("categoryimage",categoryimage);
                 intent.putExtra("subjectName", subject);
                intent.putExtra("subjectdescrition", subjectdescription);
                intent.putExtra("subjectimage",subjectimage);
                intent.putExtra("categoryname",category);
                intent.putExtra("categoryimage",categoryimage);
                application.startActivity(intent);

            }
            else{
                Intent intent = new Intent(application, MainActivity.class);
                application.startActivity(intent);
            }

        }

        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
        //do the necessary operation.
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            if (result.action.actionID.equals("ActionOne")) {
                Toast.makeText(MyApplication.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
            } else if (result.action.actionID.equals("ActionTwo")) {
                Toast.makeText(MyApplication.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
            }
        }
    }


    private String loadJson(String filename){
        StringBuilder str = new StringBuilder("");
        try {
            InputStream input = application.getAssets().open(filename);
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


        String str2=loadJson("quiz_category.json");


        try {
            JSONObject object1 = new JSONObject(str2);
            JSONArray table = object1.getJSONArray("table");

            for (int i = 0 ;i <table.length()  ; i++) {
                System.out.println(i);

                String categoryName2=table.getJSONObject(i).get("category_name").toString();


                if(category.equals(categoryName2)) {

                    categoryimage=table.getJSONObject(i).get("category_image").toString();
                    return;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getsubjectfromJson(){


        String str2=loadJson("quiz_subjects.json");


        try {
            JSONObject object1 = new JSONObject(str2);
            JSONArray table = object1.getJSONArray("subject");

            for (int i = 0 ;i <table.length()  ; i++) {
                System.out.println(i);

                String categoryName2=table.getJSONObject(i).get("categoryName").toString();
                String subjectName2=table.getJSONObject(i).get("subject_name").toString();

                if(category.equals(categoryName2)  && subjectName2.equals(subject)) {
                    subjectimage=table.getJSONObject(i).get("subject_image").toString();
                    subjectdescription=table.getJSONObject(i).get("subject_description").toString();
                    return;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
