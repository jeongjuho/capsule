package com.example.my.recapsule;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotiRiceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id",0);
        Toast.makeText(context, "onReceive 실행"+id, Toast.LENGTH_LONG).show();
        aa(context);

    }
    void aa(Context context){
        Intent intent = new Intent(context,RandomCapsule.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager NotiManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE); //알람매니저 생성
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("캡슐오픈");
        builder.setContentText("캡슐을 열어주세용!!");
        builder.setSmallIcon(R.drawable.capsuleimg);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification noti = builder.build();
        noti.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        NotiManager.notify(123,noti);


    }
}
