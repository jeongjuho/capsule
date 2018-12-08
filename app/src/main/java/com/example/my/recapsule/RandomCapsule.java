package com.example.my.recapsule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


public class RandomCapsule extends Fragment implements AbsListView.OnScrollListener {
    SweetCount sweetcount = new SweetCount();
    Followcheck followcheck = new Followcheck();
    Button createbt;
    LinearLayout mSwipeRefreshLayout;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private int OFFSET = 0;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    ArrayList<InfoClass> list;
    public static ArrayList<InfoClass> saveList;
    public static Context mContext;
    public static PAdapter adapter;
    ListView listView;
    String randomCapsule;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<String> sweetct = new ArrayList<>();
   public static ArrayList<String> sweetcheck = new ArrayList<>();

    public ArrayList<String> fcheck = new ArrayList<>();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), Member_login.class);
                startActivity(intent);
                getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_main, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        pref = this.getActivity().getSharedPreferences("setting", 0);
        editor = pref.edit();


        mSwipeRefreshLayout = (LinearLayout) view.findViewById(R.id.swipe_layout);


        list = new ArrayList<InfoClass>();
        saveList = new ArrayList<InfoClass>();
        listView = (ListView) view.findViewById(R.id.viewPager);
        adapter = new PAdapter(getContext(), R.id.viewPager, list, saveList);

        listView.setOnScrollListener(this);
        listView.setAdapter(adapter);


        mContext = getActivity();


        setHasOptionsMenu(true);
        sweetcount.execute();
        followcheck.execute();

        try {

            randomCapsule = new getRandomCapsule().execute().get();
            jsonObject = new JSONObject(randomCapsule);
            jsonArray = jsonObject.getJSONArray("response");
        } catch (Exception e) {

            e.printStackTrace();

        }
        getItem(3);
        Log.d("TEST", Boolean.toString(mLockListView));
        final EditText search = (EditText) view.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCapsule(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoClass item = (InfoClass) listView.getItemAtPosition(position);
                Intent it = new Intent(getContext(), RandomDetail.class);
                it.putExtra("title", item.getTitle());
                it.putExtra("text", item.getText());
                it.putExtra("nick", item.getNick());
                it.putExtra("profile", item.getProfile());


                it.putExtra("gps", item.getAddr());
                it.putExtra("num", item.getNum());
                it.putExtra("image", item.getIcon());
                it.putExtra("strPrivate", item.getStrPrivate());
                startActivity(it);

            }
        });


        return view;

    }

    public void remain1() {
        ((FragmentAcivity) getActivity()).refresh();
    }

    public void remain2() {
        OFFSET = 0;
        try {
            jsonObject = new JSONObject(new getRandomCapsule().execute().get());
            jsonArray = jsonObject.getJSONArray("response");
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class getRandomCapsule extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/capsule_list.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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

    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            Toast.makeText(RandomCapsule.mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        int count = totalItemCount - visibleItemCount;

        if (firstVisibleItem >= count && totalItemCount != 0
                && mLockListView == false) {
            Log.i("TEST", "Loading next items");
            OFFSET += 3;
            if (OFFSET <= jsonArray.length()) {
                getItem(3);
            } else {
                return;
            }

        }

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    private void getItem(final int size) {
        int color = R.drawable.follow;
        String ftext = "구독하기";
        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;


        for (int i = 0; i < size; i++) {

            try {


                String tilte, text, id, nick, addr, cimage, pimage,num;
                int count=0;
                int drawable;
                JSONObject object = jsonArray.getJSONObject(i + OFFSET);

                tilte = object.getString("title");
                text = object.getString("text");
                id = object.getString("idStr");
                nick = object.getString("nick");
                num = object.getString("num");

                StringTokenizer stringTokenizer = new StringTokenizer(object.getString("gps"), ",");
                String a = stringTokenizer.nextToken();
                String b = stringTokenizer.nextToken();
                color = R.drawable.follow;
                for (int j = 0; j < fcheck.size(); j++) {
                    if (id.equals(fcheck.get(j))) {
                        color = R.drawable.bleckfollow;
                    }
                }
                drawable = R.drawable.heart;
                for(int j=0;j<sweetct.size();j++){
                    if(num.equals(sweetct.get(j))){
                        count = count+1;
                            if(num.equals(sweetct.get(j))&&Member_login.idSession.equals(sweetcheck.get(j))){
                                drawable = R.drawable.redheart;

                        }

                    }
                }



                addr = getAddress(mContext, Double.parseDouble(a), Double.parseDouble(b));

                String strPrivate = object.getString("strPrivate");

                pimage = "http://ggcapsule.dothome.co.kr/profile/" + object.getString("pimage");


                boolean check = new RandomCapsule.getProfile().execute("http://ggcapsule.dothome.co.kr/capsuleImage/" + object.getString("image")).get();
                if (check) {
                    cimage = "http://ggcapsule.dothome.co.kr/capsuleImage/" + object.getString("image");
                }else {
                    cimage="dummy";
                }
                list.add(new InfoClass(i, tilte, text, id, cimage, object.getString("num"), addr, nick, pimage, strPrivate, color, ftext,count,drawable));
                saveList.add(new InfoClass(i, tilte, text, id, cimage, object.getString("num"), addr, nick, pimage, strPrivate, color, ftext,count,drawable));


            } catch (Exception e) {
                e.printStackTrace();
            }


            // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
            // 리스트뷰의 락을 해제합니다.

            adapter.notifyDataSetChanged();
            mLockListView = false;
        }


        // 속도의 딜레이를 구현하기 위한 꼼수


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();

                mLockListView = false;
            }
        }, 1000);

    }

    public void searchCapsule(String search) {
        list.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getId().contains(search) || saveList.get(i).getTitle().contains(search) || saveList.get(i).getText().contains(search)) {
                list.add(saveList.get(i));
            }
        }

        adapter.notifyDataSetChanged();
    }


    class Followcheck extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/follow_list1.php";
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
                Log.d("JSONTEST", stringBuilder.toString());
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;


                String fid;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    fid = object.getString("fid");
                    fcheck.add(fid);
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


    class SweetCount extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ggcapsule.dothome.co.kr/sweet_list.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
                Log.d("JSONTEST", stringBuilder.toString());
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String num,swid;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);

                    num = object.getString("num");
                    swid = object.getString("swid");
                    sweetcheck.add(swid);
                    sweetct.add(num);
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
    }

