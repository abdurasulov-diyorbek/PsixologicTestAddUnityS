package com.siz_kimsiz.inson_psixologiyasi;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.onesignal.OneSignal;
import com.siz_kimsiz.inson_psixologiyasi.onesignal.MyNotificationOpenedHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 19.01.2018.
 */

public class WsAppListActivity extends Activity implements AdapterView.OnItemClickListener {

    String[] member_names, member_link;
    TypedArray profile_pics;
    String[] statues;
    String[] contactType;

    List<WsRowItem> rowItems, rowItems_link;
    ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ws_app_list);

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this.getApplication()))
                .init();

DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.8));


        rowItems = new ArrayList<WsRowItem>();

        member_names = getResources().getStringArray(R.array.ws_app_names);

        profile_pics = getResources().obtainTypedArray(R.array.ws_profile_pics);

        statues = getResources().getStringArray(R.array.ws_statues);

        member_link = getResources().getStringArray(R.array.ws_links);

        contactType = getResources().getStringArray(R.array.ws_contactType);

        for (int i = 0; i < member_link.length; i++) {
            WsRowItem item = new WsRowItem(member_names[i], profile_pics.getResourceId(i, -1), statues[i], member_link[i],  contactType[i]);
            rowItems.add(item);
        }



     /*   for (int i = 0; i < member_link.length; i++) {
            WsRowItem item = new WsRowItem(member_link[i],
                    profile_pics.getResourceId(i, -1), statues[i],
                    contactType[i]);
            rowItems_link.add(item);
        }
*/
        mylistview = (ListView) findViewById(R.id.list);
        WsCustomAdapter adapter = new WsCustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String member_name = rowItems.get(position).getMember_name();

        Toast.makeText(getApplicationContext(), "" + member_name, Toast.LENGTH_SHORT).show();
        String member_link = rowItems.get(position).getLinks();
        Uri uri = Uri.parse(member_link);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));


    }

}
