package com.siz_kimsiz.inson_psixologiyasi.onesignal;


import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

/**
 * Created by androidbash on 12/14/2016.
 */

//This will be called when a notification is received while your app is running.
public class MyNotificationReceivedHandler  implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {
//        System.out.println("notification received");
//        JSONObject data = notification.payload.additionalData;
//        String customKey;
//
//        if (data != null) {
//            try {
//               String  poem = data.getString("poemid");
//               String bookid = data.getString("bookid");
//                System.out.println("poem id"+poem);
//                System.out.println("book id"+bookid);
//            }
//            catch (Exception e){
//
//            }
//            //While sending a Push notification from OneSignal dashboard
//            // you can send an addtional data named "customkey" and retrieve the value of it and do necessary operation
//            customKey = data.optString("customkey", null);
//
//
//            if (customKey != null)
//                Log.i("OneSignalExample", "customkey set with value: " + customKey);
//        }else{
//            System.out.println("data is null");
//        }
    }
}