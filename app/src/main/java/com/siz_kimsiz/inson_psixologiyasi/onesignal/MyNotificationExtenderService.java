package com.siz_kimsiz.inson_psixologiyasi.onesignal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import android.util.Log;


import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;
import com.siz_kimsiz.inson_psixologiyasi.R;

import java.math.BigInteger;


/**
 * Created by androidbash on 12/14/2016.
 */

public class MyNotificationExtenderService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                System.out.println("Going to change Bell icon !");
                // Sets the background notification color to Red on Android 5.0+ devices.
                Bitmap icon = BitmapFactory.decodeResource(MyApplication.getContext().getResources(),
                        R.drawable.ic_stat_onesignal_default);
                builder.setLargeIcon(icon);

                return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}