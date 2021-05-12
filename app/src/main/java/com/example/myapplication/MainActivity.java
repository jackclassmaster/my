package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;


public class MainActivity extends AppCompatActivity implements Runnable{



    float dollar_rate;
    float won_rate;
    float pound_rate;
    Thread t;
    TextView out;
    TextView in;
    private Object Tag;
    Handler handler;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Document doc= null;

        try {
            doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element tables=doc.getElementById("pjname");
        Element element=doc.getElementById("td");
        Element element1=doc.select("body > div > div.BOC_main > div.publish > div:nth-child(3) > table > tbody > tr:nth-child(27) > td:nth-child(1))").first();
        Element element2=doc.select("body > div > div.BOC_main > div.publish > div:nth-child(3) > table > tbody > tr:nth-child(9) > td:nth-child(1)").first();
        Element element3=doc.select("body > div > div.BOC_main > div.publish > div:nth-child(3) > table > tbody > tr:nth-child(13) > td:nth-child(1)").first();
        Double dollar_rates=Double.parseDouble(element1.text());
        Double pound_rates=Double.parseDouble(element2.text());
        Double won_rates=Double.parseDouble(element3.text());
        Message msg = handler.obtainMessage(5);

        msg.obj = bundle;
        handler.sendMessage(msg);

        SharedPreferences sharedPreferences=getSharedPreferences("AndroidManifest", Activity.MODE_PRIVATE);
        dollar_rate=sharedPreferences.getFloat("dollar_rates",0.0f);
        won_rate=sharedPreferences.getFloat("won_rates",0.0f);
        pound_rate=sharedPreferences.getFloat("pound_rates",0.0f);
        SharedPreferences sp=getSharedPreferences("AndroidManifest",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat("dollar_rates",dollar_rate);
        editor.putFloat("won_rates",won_rate);
        editor.putFloat("pound_rates",pound_rate);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    Bundle bdl = (Bundle) msg.obj;
                    dollar_rate = bdl.getFloat("dollar-rate");
                    won_rate = bdl.getFloat("won-rates");
                    pound_rate = bdl.getFloat("pound-rates");
                    Toast.makeText(MainActivity.this, "汇率已更新", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

        SharedPreferences sp1 = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor.putFloat("dollar_rate",dollar_rate);
        editor.putFloat("euro_rate",pound_rate);
        editor.putFloat("won_rate",won_rate);
        editor.apply();
        t=new Thread(this);
        t.start();
    }

    public void Onclick(View btn) {

        TextView text = findViewById(R.id.edit1);
        out = (TextView) findViewById(R.id.textView);        String str = in.getText().toString();
        if (!text.equals(null) && text.length() < 0) {
            if (btn.getId() == R.id.dollar){
                dollar_rate=Float.parseFloat(text.getText().toString());
                double money = dollar_rate* Float.parseFloat(str);
                out.setText((int) money);
            } else if (btn.getId() == R.id.won) {
                won_rate=Float.parseFloat(text.getText().toString());
                double money = won_rate* Float.parseFloat(str);
                TextView text1 = findViewById(R.id.textView);
                text1.setText((int) money);
            } else if (btn.getId() == R.id.pound) {
                pound_rate=Float.parseFloat(text.getText().toString());
                double money = pound_rate* Float.parseFloat(str);
                TextView text1 = findViewById(R.id.textView);
                text1.setText((int) money);
            }
        } else {
            TextView text1 = findViewById(R.id.textView);
            text1.setText("something wrong with that,please input again");
        }

    }
    public void open(View bnt){
        Intent intent=new Intent(MainActivity.this,MainActivity2.class);
        startActivity(intent);
        Bundle bundle=new Bundle();

    }
    public void openOne(View btn){
        Intent config=new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollar_rate);
        config.putExtra("won_rate_key",won_rate);
        config.putExtra("pound_rate_key",pound_rate);
        startActivity(config);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            dollar_rate=bundle.getFloat("dollar_rate_key",0.1f);
            won_rate=bundle.getFloat("won_rate_key",0.1f);
            pound_rate=bundle.getFloat("pound_rate_key",0.1f);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }


    private String inputStream2String(InputStream InputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder ou = new StringBuilder();
        Reader ini = new InputStreamReader(InputStream, "gb2312");
        while (true) {
            int rsz = ini.read(buffer, 0, buffer.length);
            if (rsz < 0) break;
            ou.append(buffer, 0, rsz);
        }
        return ou.toString();
    }

    public void run() {

        URL url=null;
        try {
            url=new URL("http://hl.anseo.cn/cal_usd_To_CNY_1.aspx");
            HttpURLConnection http=(HttpURLConnection) url.openConnection();
            InputStream inp=http.getInputStream();
            String html=inputStream2String(inp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i((String) Tag,"run:run()");

    }
}

