 package com.example.pscwww.barcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button bt_scan;
    TextView tv_barcode, tv_title, tv_author, tv_price, tv_publisher;
    boolean inTitle, inAuthor, inPrice, inPublisher;
    String title, author, price, publisher;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_scan.setOnClickListener(this);

        tv_barcode = (TextView) findViewById(R.id.tv_barcode);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_publisher = (TextView) findViewById(R.id.tv_publisher);

        inTitle = false;
        inAuthor = false;
        inPrice = false;
        inPublisher = false;

        title = null;
        author = null;
        price = null;
        publisher = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_scan :
                Log.d("TEST", "SCAN TEST");
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "ALL");
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0) {

            if(resultCode == Activity.RESULT_OK)
            {
                String contents = data.getStringExtra("SCAN_RESULT");
                //위의 contents 값에 scan result가 들어온다.
                tv_barcode.setText(contents);
                NetworkTask myNetworkTask = new NetworkTask();
                myNetworkTask.execute(contents);
                //updateData(contents);

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_app_exit)        // 제목 설정
                .setMessage(R.string.content_app_exit)        // 메세지 설정
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //프로그램 종료
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //없음
                    }
                });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();
    }
/*

    class NaverSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... search) {
            String KEY = "a27db10e1543001e0a42be9b18adb38b";
            String url = "http://openapi.naver.com/search?key=" + KEY + "&query=isbn&target=book_adv&start=1&display=10&d_isbn="+search[0];
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";
            try {
                boolean flag1 = false;
                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");
                int eventType = parser.getEventType();
                Log.d("TITLE", parser.toString());
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            Log.d("TITLESSSSSSSSS", parser.getName());
                            if (parser.getName().equals("title")) {
                                Log.d("TITLE", parser.getName());
                                flag1 = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (flag1 == true) {
                                returnResult += parser.getText() + "\n";
                                flag1 = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnResult;
        }
        @Override
        protected void onPostExecute(String result) {
            tv_title.setText(result);
        }
    }
*/

    public class NetworkTask extends AsyncTask<String, String, String>{
        String query = "physics";
        String queryutf = URLEncoder.encode(query);
        protected String doInBackground(String... contents) {

            try{
                URL url = new URL("http://openapi.naver.com/search?"
                        +"key=a27db10e1543001e0a42be9b18adb38b"
                        +"&query=isbn"
                        +"&d_isbn="+contents[0]
                        +"&display=10"
                        +"&start=1"
                        +"&target=book_adv");
                Log.d("CONTENT", contents[0]);
                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);
                String imagegeg = (String) parser.getProperty("image");
                Log.e("image",imagegeg);
                int parserEvent = parser.getEventType();
                Log.d("??","111111111111111");
                while (parserEvent != XmlPullParser.END_DOCUMENT){
                    switch(parserEvent){
                        case XmlPullParser.START_DOCUMENT:
                            Log.d("START_DOCUMENT","start-document");
                            break;
                        case XmlPullParser.START_TAG:  //parser가 시작 태그를 만나면 실행
                            Log.d("START_TAG","start-tag");
                            if(parser.getName().equals("title")){
//                                Log.d("TITLE", title);
                                inTitle = true;
                            }
                            if(parser.getName().equals("author")){
                                inAuthor = true;
                            }
                            if(parser.getName().equals("price")){
                                inPrice = true;
                            }
                            if(parser.getName().equals("publisher")){ //mapx 만나면 내용을 받을수 있게 하자
                                inPublisher = true;
                            }
                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            Log.d("TEXT","text");
                            if(inTitle){ //isTitle이 true일 때 태그의 내용을 저장.
                                title = parser.getText().toString();
                                Log.d("TITLE", title);
                                //tv_title.setText(title);
                                inTitle = false;
                            }
                            if(inAuthor){ //isAddress이 true일 때 태그의 내용을 저장.
                                author = parser.getText();
                                Log.d("AUTHOR",author);
                                //tv_author.setText(author);
                                inAuthor = false;
                            }
                            if(inPrice){ //isMapx이 true일 때 태그의 내용을 저장.
                                price = parser.getText().toString();
                                Log.d("PRICE",price);
                                //tv_price.setText(price);
                                inPrice = false;
                            }
                            if(inPublisher){ //isMapy이 true일 때 태그의 내용을 저장.
                                publisher = parser.getText();
                                Log.d("PUBLISHER",publisher);
                                //tv_publisher.setText(publisher);
                                inPublisher = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            Log.d("END_TAG","end-tag");
                           // tv_barcode.setText("파싱끝"+contents[0]);
                            break;

                    }
                    parserEvent = parser.next();
                }

            } catch(Exception e){
//                tv_title.setText("에러가..났습니다...");
                e.printStackTrace();

            }


            return null;
        }
    }

}
