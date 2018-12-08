package com.example.my.recapsule;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeActivity extends Activity {

    DatePicker mDate;
    String mTxtDate, result;
    private Button dbsave;
    int cYear,cMonth,cDay,sYear,sMonth,sDay;

    DBHelper dbHelper;

    final static String dbName = "t_table.db";
    final static int dbVersion = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date today = new Date();
        String cdate = sdf.format(today);
        String ccdate[] = cdate.split("년 ");
        String cccdate[] = ccdate[1].split("월 ");
        String ccccdate[] = cccdate[1].split("일");
        cYear = Integer.parseInt(ccdate[0]);
        cMonth = Integer.parseInt(cccdate[0]);
        cDay = Integer.parseInt(ccccdate[0]);
        //현재 년,월,일

        mDate = (DatePicker)findViewById(R.id.datepicker);
        dbsave = (Button)findViewById(R.id.btnnow);

        dbHelper = new DBHelper(this, dbName, null, dbVersion);

        //처음 DatePicker를 오늘 날짜로 초기화, 리스너 등록
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        mTxtDate = String.format("%d/%d/%d", year,monthOfYear + 1, dayOfMonth);
                    }
                });

        //저장
        dbsave.setOnClickListener(new View.OnClickListener() {
            //버튼 클릭시 DatePicker로부터 값을 읽어와서 Toast메시지로 보여준다.
            @Override
            public void onClick(View v) {

                sYear = mDate.getYear();
                sMonth = mDate.getMonth()+1;
                sDay = mDate.getDayOfMonth();

                if(mDate.getMonth()+1<10)
                    result = String.format("%d년 0%d월 %d일", sYear,sMonth, sDay);
                else if(mDate.getDayOfMonth()+1<10)
                    result = String.format("%d년 %d월 0%d일", sYear,sMonth, sDay);
                else
                    result = String.format("%d년 %d월 %d일", sYear,sMonth, sDay);


                SQLiteDatabase db;
                String sql;

                db = dbHelper.getReadableDatabase();
                sql = "SELECT * FROM t_table;";
                Cursor cursor = db.rawQuery(sql, null);
                if(cursor.getCount() > 0) {
                    while (cursor.moveToNext())
                        //Toast.makeText(getApplicationContext(), "이미 " + cursor.getString(1) + "로 설정이 되어있습니다. ", Toast.LENGTH_LONG).show();
                    cursor.close();
                    dbHelper.close();
                    TimeActivity.this.finish();
                }
                else if (cYear > sYear || (cYear == sYear) && (cMonth > sMonth) || (cYear == sYear) && (cMonth == sMonth) && (cDay > sDay)){
                    //Toast.makeText(getApplicationContext(),"지난 날짜로는 설정할 수 없습니다.", Toast.LENGTH_LONG).show();
                    cursor.close();
                    dbHelper.close();
                }
                else if((cYear == mDate.getYear()) && (cMonth == mDate.getMonth() + 1) && (cDay == mDate.getDayOfMonth())){
                    Toast.makeText(getApplicationContext(),"오늘 날짜로는 설정할 수 없습니다.", Toast.LENGTH_LONG).show();
                    cursor.close();
                    dbHelper.close();
                }
                else {
                    db = dbHelper.getWritableDatabase();
                    sql = String.format("INSERT INTO t_table VALUES('%s', '%s');", "MOON", result);
                    db.execSQL(sql);
                    //Toast.makeText(getApplicationContext(), result+"로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    dbHelper.close();
                    TimeActivity.this.finish();
                }
            }
        });
    }

    static class DBHelper extends SQLiteOpenHelper {

        //생성자 - database 파일을 생성한다.
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //DB 처음 만들때 호출. - 테이블 생성 등의 초기 처리.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE t_table (name TEXT PRIMARY KEY, date INTEGER);");
        }

        //DB 업그레이드 필요 시 호출. (version값에 따라 반응)
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS t_table");
            onCreate(db);
        }

    }
}