package com.example.my.recapsule;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by my on 2018-09-10.
 */

public class MyCapsule extends Fragment {
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    String userId = Member_login.idSession;
    static CustomAdapter customAdapter;
    private Context context;
    private ArrayList<InfoClass2> infolist;
    GpsInfo gps;
    GridView listview;
    TextView ttv1, ttv2, ttv3, nick;

    static ImageView profile;
    int date;
    ArrayList<InfoClass2> list;
    String delNum;
    String followers,subscribers;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_mycapsule, container, false);
        Log.d("ONCREATE", "oncreateTest");
        list = new ArrayList<InfoClass2>();
        listview = (GridView) view.findViewById(R.id.grid);
        customAdapter = new CustomAdapter(getContext(), R.id.grid, list);
        listview.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        ttv1 = (TextView) view.findViewById(R.id.ttv1);
        ttv2 = (TextView) view.findViewById(R.id.ttv2);
        ttv3 = (TextView) view.findViewById(R.id.ttv3);
        nick = (TextView) view.findViewById(R.id.nick);
        nick.setText(Member_login.nick);
        profile = (ImageView) view.findViewById(R.id.profile);
        context = getContext();
        Log.d("TEST","탭눌렀을때 뜨는것은?1");
        new getFollowInfo().execute();

        new getMyCapsule().execute();
        ttv2.setText(subscribers);
        ttv3.setText(followers);
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        try {
            boolean check = new getProfile().execute("http://ggcapsule.dothome.co.kr/profile/" + Member_login.idSession + "profile.jpg").get();
            if (check) {
                Glide.with(context).load("http://ggcapsule.dothome.co.kr/profile/" + Member_login.idSession + "profile.jpg").into(profile);
            } else {
                Glide.with(context).load("http://ggcapsule.dothome.co.kr/profile/person.png").into(profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isPermission) {
            callPermission();

        }
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {

                InfoClass2 item = (InfoClass2) listview.getItemAtPosition(position);
                delNum=item.getNum();
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder.setTitle("경고!");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("삭제하시겠습니까?")
                        .setCancelable(false)
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new deleteCapsule().execute();
                                deleteItem(position);
                                ttv1.setText(Integer.toString(customAdapter.getCount()));
                                customAdapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {


                                    }
                                });

                // 다이얼로그 생성
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();

                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                String a = null, b = null;
                //getContent getContent = new getContent();
                InfoClass2 item = (InfoClass2) listview.getItemAtPosition(position);

                java.util.Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String strDate = sdf.format(today);

                Log.d("DATETEST", strDate);
                Log.d("DBDATETEST", Integer.toString(date));

                int nowDate = Integer.parseInt(strDate);
                date = item.getDate();


                if (!isPermission) {
                    callPermission();
                    return;
                } else {
                    try {


                        //String saveGps = getContent.execute(item.getNum()).get();
                        String saveGps = item.getGps();
                        StringTokenizer stringTokenizer = new StringTokenizer(saveGps, ",");
                        a = stringTokenizer.nextToken();
                        b = stringTokenizer.nextToken();
                        Log.d("DATETEST", a + b);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    double distance = distance(Double.parseDouble(a), Double.parseDouble(b));
                    Log.d("DISTANCETEST", Double.toString(distance));
                    if (distance <= 100 && date <= nowDate) {
                        Intent intent = new Intent(context, MycapsuleDetail.class);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("text", item.getText());
                        intent.putExtra("title", item.getTitle());
                        intent.putExtra("num", item.getNum());
                        intent.putExtra("image", item.getIcon());
                        intent.putExtra("addr",item.getGps());
                        intent.putExtra("pos",position);
                        context.startActivity(intent);
                    } else if (date > nowDate && distance < 100) {
                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                context);

                        // 제목셋팅
                        alertDialogBuilder.setTitle("경고!");

                        // AlertDialog 셋팅
                        alertDialogBuilder
                                .setMessage("설정된 시간이 아닙니다.")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {


                                            }
                                        });

                        // 다이얼로그 생성
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                        // 다이얼로그 보여주기
                        alertDialog.show();
                    } else if (distance > 100 && date > nowDate) {
                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                context);

                        // 제목셋팅
                        alertDialogBuilder.setTitle("경고!");

                        // AlertDialog 셋팅
                        alertDialogBuilder
                                .setMessage("설정된 시간과 장소가 아닙니다.")
                                .setCancelable(false)
                                .setNegativeButton("위치확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent it = new Intent(getContext(),Navigation.class);
                                        InfoClass2 item = (InfoClass2) listview.getItemAtPosition(position);
                                        String saveGps = item.getGps();
                                        StringTokenizer stringTokenizer = new StringTokenizer(saveGps, ",");
                                        String a = stringTokenizer.nextToken();
                                        String b = stringTokenizer.nextToken();
                                        it.putExtra("a",a);
                                        it.putExtra("b",b);
                                        startActivity(it);
                                    }
                                })
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {


                                            }
                                        });

                        // 다이얼로그 생성
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                        // 다이얼로그 보여주기
                        alertDialog.show();
                    } else if (distance > 100 && date < nowDate) {
                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                context);

                        // 제목셋팅
                        alertDialogBuilder.setTitle("경고!");

                        // AlertDialog 셋팅
                        alertDialogBuilder
                                .setMessage("설정된 위치가 아닙니다.")
                                .setCancelable(false)
                                .setNegativeButton("위치확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent it = new Intent(getContext(),Navigation.class);
                                        InfoClass2 item = (InfoClass2) listview.getItemAtPosition(position);
                                        String saveGps = item.getGps();
                                        StringTokenizer stringTokenizer = new StringTokenizer(saveGps, ",");
                                        String a = stringTokenizer.nextToken();
                                        String b = stringTokenizer.nextToken();
                                        it.putExtra("a",a);
                                        it.putExtra("b",b);
                                        startActivity(it);
                                    }
                                })
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {


                                            }
                                        });

                        // 다이얼로그 생성
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                        // 다이얼로그 보여주기
                        alertDialog.show();


                    }
                }
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfilePopupActivity.class);
                startActivity(intent);


            }
        });

        return view;
    }

    public void deleteItem(int position){
        MyCapsule.customAdapter.deleteItem(position);
    }


    class getMyCapsule extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/myCapsuleList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String postParameters = "userId=" + userId;
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String tilte, text, id, num, image, gps;
                int date;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    tilte = object.getString("title");
                    text = object.getString("text");
                    id = object.getString("idStr");
                    num = object.getString("num");
                    //date=Integer.parseInt(object.getString("date"));
                    image = "http://ggcapsule.dothome.co.kr/capsuleImage/" + object.getString("image");
                    gps = object.getString("gps");
                    date = Integer.parseInt(object.getString("date"));

                    String nowDate;
                    java.util.Date today=new java.util.Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
                    nowDate=sdf.format(today);


                        java.util.Date beginDate = sdf.parse(nowDate);
                        java.util.Date endDate = sdf.parse(object.getString("date"));
                        long diff = endDate.getTime() - beginDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);


                    list.add(new InfoClass2(tilte, text, id, image, num, gps, date,Long.toString(diffDays)));


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ttv1.setText(Integer.toString(customAdapter.getCount()));
                            customAdapter.notifyDataSetChanged();
                        }
                    });


                    count++;
                }


                Log.d("errorCord", stringBuilder.toString());
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {

        }
    }




    public float distance(double latitude, double longitude) {


        gps = new GpsInfo(context);
        Location nowLocation = gps.getLocation();
        Location locationA = new Location("saveGps");
        Location locationB = new Location("nowGps");
        locationA.setLatitude(latitude);
        locationA.setLongitude(longitude);
        locationB.setLatitude(nowLocation.getLatitude());
        locationB.setLongitude(nowLocation.getLongitude());
        float distance = locationA.distanceTo(locationB);
        Log.d("DISTANCE", Float.toString(distance));
        return distance;

    }

    @Override
    public void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
        ttv1.setText(Integer.toString(customAdapter.getCount()));
        ttv2.setText(subscribers);
        ttv3.setText(followers);
        Log.d("TEST","탭눌렀을때 뜨는것은?2");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;


        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    class getProfile extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(strings[0]).openConnection();
                con.setRequestMethod("HEAD");
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


            return false;
        }
    }
    class deleteCapsule extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/delete.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                /*String postParameters = "num=" + num;
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();*/
                HttpPostData();





                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {

        }
    }
    public void HttpPostData() {
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://ggcapsule.dothome.co.kr/delete.php");       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            //http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            buffer.append("strNum").append("=").append(Integer.parseInt(delNum));                 // php 변수에 값 대입

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            final String myResult = builder.toString();                       // 전송결과를 전역 변수에 저장



        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData
    class getFollowInfo extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/FollowInfo.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String postParameters = "userId=" + Member_login.idSession;
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("JSONTEST",stringBuilder.toString());
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;


                JSONObject object = jsonArray.getJSONObject(count);

                /*if (object.getString("subscribers").equals("")){
                    subscribers=Integer.toString(0);
                }*/
                subscribers=object.getString("subscribers");



                /*if (object.getString("followers").equals("")){
                    followers=Integer.toString(0);
                }*/
                followers=object.getString("followers");




                Log.d("errorCord", stringBuilder.toString());
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {

        }
    }
}


