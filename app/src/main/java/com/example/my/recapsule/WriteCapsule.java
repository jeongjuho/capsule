package com.example.my.recapsule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by my on 2018-09-10.
 */

public class WriteCapsule extends Fragment {
    ImageButton datebt,gpsbt;
    Button savebt;
    EditText content;
    EditText title;
    ImageButton image;
    ImageView cImage;
    private final int GALLERY_CODE=1112;
    private AlertDialog alertDialog;
    //삽입결과 테스트용 텍스트박스
    String strDate;
    static String upLoadPath;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    CheckBox cb;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_syn, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.synBtn:

                Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_writing, container, false);
        title=(EditText)view.findViewById(R.id.title);
        content=(EditText)view.findViewById(R.id.content);
        datebt = (ImageButton)view.findViewById(R.id.timebtn);
        gpsbt = (ImageButton)view.findViewById(R.id.positionbtn);
        cb=(CheckBox)view.findViewById(R.id.privateBtn);
        savebt=(Button)view.findViewById(R.id.saveBtn);
        image=(ImageButton)view.findViewById(R.id.image);
        cImage=(ImageView)view.findViewById(R.id.c_image);
        upLoadServerUri = "http://ggcapsule.dothome.co.kr/capsuleImage.php";
        setHasOptionsMenu(true);
        datebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Date.class);
                startActivity(intent);
            }
        });

        gpsbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Gps.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    checkVerify(1);
                }
                else
                {
                    //new RetrieveFeedTask().execute();
                    selectGallery();
                }

            }
        });
        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEdit = title.getText().toString().trim();
                String getEdit2=content.getText().toString().trim();
                String year = String.valueOf(Date.year);
                String month = String.valueOf(Date.month);
                String day=String.valueOf(Date.day);
                String latitude = String.valueOf(Gps.ReLatitude);
                String longitude = String.valueOf(Gps.ReLongitude);
                String date = year+month+day;
                String gps = latitude+","+longitude;
                String nullTest=year+month+day.trim();
                String nullTest2=latitude+","+longitude.trim();
                if (getEdit.getBytes().length<=0){
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            getContext());

                    // 제목셋팅
                    alertDialogBuilder.setTitle("경고!");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("제목은 필수입니다.")
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
                }else if (getEdit2.getBytes().length<=0){
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            getContext());

                    // 제목셋팅
                    alertDialogBuilder.setTitle("경고!");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("내용은 필수입니다.")
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
                }else if (nullTest.equals("000")){
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            getContext());

                    // 제목셋팅
                    alertDialogBuilder.setTitle("경고!");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("시간설정은 필수입니다.")
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
                }else if (nullTest2.equals("0.0,0.0")){
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            getContext());

                    // 제목셋팅
                    alertDialogBuilder.setTitle("경고!");

                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("위치설정은 필수입니다.")
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
                }else {
                    java.util.Date today = new java.util.Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                    strDate = sdf.format(today);
                    String profile = null;

                    Log.d("year", String.valueOf(Date.year));
                    Log.d("month", String.valueOf(Date.month));
                    Log.d("위도2", String.valueOf(Gps.ReLatitude));
                    Log.d("경도2", String.valueOf(Gps.ReLongitude));
                    Log.d("id", String.valueOf(Member_login.idSession));

                    String text = content.getText().toString();
                    String text2 = title.getText().toString();
                    String id = Member_login.idSession;
                    String path = Member_login.idSession + strDate + ".jpg";
                    String strPriave;
                    if (cb.isChecked()) {
                        strPriave = "true";
                    } else {
                        strPriave = "false";
                    }
                    try {
                        boolean check = new WriteCapsule.getProfile().execute("http://ggcapsule.dothome.co.kr/profile/" + Member_login.idSession + "profile.jpg").get();
                        Log.d("PATH1", Boolean.toString(check));
                        if (check) {
                            profile = Member_login.idSession + "profile.jpg";
                        } else {
                            profile = "person.png";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    String nick = Member_login.nick;
                    Log.d("PATH", profile);

                    Log.d("STRDATE", strDate);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkVerify(2);
                    } else {
                        new WriteCapsule.RetrieveFeedTask().execute();

                    }


                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    alertDialog = builder.setMessage("데이터 저장에 성공했습니다..")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent it = new Intent(getContext(), FragmentAcivity.class);
                                                    startActivity(it);
                                                    getActivity().finish();
                                                }
                                            })
                                            .create();
                                    alertDialog.show();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("데이터 저장에 실패 했습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    Writing_insert writeInsert = new Writing_insert(text2, text, date, gps, id, path, nick, profile, strDate, strPriave, listener);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(writeInsert);
                }
            }
        });
        return view;
    }
    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    break;

                default:
                    break;
            }

        }
    }
    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        cImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        Log.d("TEST",cursor.getString(column_index));
        upLoadPath=cursor.getString(column_index);

        return cursor.getString(column_index);
    }
    public void checkVerify(int num)
    {
        if (
                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )
        {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // ...
            }
            if (num==1){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }


        }
        else
        {
            if (num==1){
                selectGallery();
            }
            else {
                new WriteCapsule.RetrieveFeedTask().execute();
            }

            //

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (grantResults.length > 0)
            {
                for (int i=0; i<grantResults.length; ++i)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                        // 하나라도 거부한다면.
                        new android.app.AlertDialog.Builder(getContext()).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getContext().getPackageName()));
                                getContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();

                        return;
                    }
                }
                //new RetrieveFeedTask().execute();
                selectGallery();
            }
        }
        else {
            if (grantResults.length > 0)
            {
                for (int i=0; i<grantResults.length; ++i)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                        // 하나라도 거부한다면.
                        new android.app.AlertDialog.Builder(getContext()).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getContext().getPackageName()));
                                getContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();

                        return;
                    }
                }
                new WriteCapsule.RetrieveFeedTask().execute();

            }
        }
    }
    public int uploadFile(String sourceFileUri) {



        String fileName = Member_login.idSession+strDate+".jpg";
        Log.d("uploadTest",fileName);


        HttpURLConnection conn = null;

        DataOutputStream dos = null;

        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(sourceFileUri);



        if (!sourceFile.isFile()) {







            Log.e("uploadFile", "Source File not exist :"

                    +upLoadPath);






            return 0;



        }

        else

        {

            try {



                // open a URL connection to the Servlet

                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(upLoadServerUri);



                // Open a HTTP  connection to  the URL

                conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true); // Allow Inputs

                conn.setDoOutput(true); // Allow Outputs

                conn.setUseCaches(false); // Don't use a Cached Copy

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");

                conn.setRequestProperty("ENCTYPE", "multipart/form-data");

                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("uploaded_file", fileName);



                dos = new DataOutputStream(conn.getOutputStream());



                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""

                        + fileName + "\"" + lineEnd);



                dos.writeBytes(lineEnd);



                // create a buffer of  maximum size

                bytesAvailable = fileInputStream.available();



                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                buffer = new byte[bufferSize];



                // read file and write it into form...

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);



                while (bytesRead > 0) {



                    dos.write(buffer, 0, bufferSize);

                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);



                }



                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);



                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();

                String serverResponseMessage = conn.getResponseMessage();



                Log.i("uploadFile", "HTTP Response is : "

                        + serverResponseMessage + ": " + serverResponseCode);



                if(serverResponseCode == 200){


                    Log.i("uploadFile", "업로드 성공");


                }



                //close the streams //

                fileInputStream.close();

                dos.flush();

                dos.close();



            } catch (MalformedURLException ex) {





                ex.printStackTrace();






                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {





                e.printStackTrace();





                Log.e("server Exception", "Exception : "

                        + e.getMessage(), e);

            }



            return serverResponseCode;



        } // End else block

    }
    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        //AsyncTask를 사용했을때 백그라운드로 실행할 내용
        protected Void doInBackground(String... urls) {
            try {
                uploadFile(upLoadPath);
                return null;

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
        //doInBackground작업이 끝나고 호출된다
        protected void onPostExecute() {
            // TODO:  this.exception이 발생했는지 체크한다
            // TODO:  받아온 feed를 가지고 적절하게 처리
        }
    }
    class getProfile extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            /*try {
                URL url = new URL(strings[0]);

                URLConnection con = url.openConnection();

                HttpURLConnection exitCode = (HttpURLConnection) con;
                Log.d("FileTEST",Integer.toString(exitCode.getResponseCode()));
                if (exitCode.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/
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

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}
