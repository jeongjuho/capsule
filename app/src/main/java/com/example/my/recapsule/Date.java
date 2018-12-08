package com.example.my.recapsule;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date extends Activity implements DatePicker.OnDateChangedListener {
    DatePicker datePicker; // 위젯으로 누를떄 이벤트발생
    GregorianCalendar date; //실제 날짜가저장됨
    AlarmManager manager;
    Button savebtn;

    static int year=0,second=0,minute=0,hour=0,day=0,month =0;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
        date = new GregorianCalendar();
        datePicker.init(date.get(Calendar.YEAR)
                , date.get(Calendar.MONTH)
                , date.get(Calendar.DAY_OF_MONTH)
                , this);
        savebtn = (Button) findViewById(R.id.savebtn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH)+1;
                day = date.get(Calendar.DAY_OF_MONTH);
                hour = date.get(Calendar.HOUR_OF_DAY);
                minute = date.get(Calendar.MINUTE);
                second = date.get(Calendar.SECOND);
                setalarm(1);
                finish();
            }
        });
    }

    public void setalarm(int id) {
        date.set(Calendar.HOUR_OF_DAY, 22);
        date.set(Calendar.MINUTE, 07);
        date.set(Calendar.SECOND, 0);
        manager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent(id));

        Toast.makeText(getApplicationContext(), "저장완료"
                + date.get(Calendar.YEAR)
                + date.get(Calendar.MONTH)
                + date.get(Calendar.DAY_OF_MONTH)
                + date.get(Calendar.HOUR_OF_DAY)
                + date.get(Calendar.MINUTE)
                + date.get(Calendar.SECOND), Toast.LENGTH_LONG).show();

    }

    public PendingIntent pendingIntent(int id) {
        Intent i = new Intent(getApplicationContext(), NotiRiceiver.class);
        i.putExtra("id",id+1);
        PendingIntent pi = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), i, 0);
        return pi;
    }

    @Override
    //날짜변동시 실행되는 메소드
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
