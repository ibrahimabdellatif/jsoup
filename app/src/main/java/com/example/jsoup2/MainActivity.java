package com.example.jsoup2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.icu.text.StringPrepParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Document document;
    String url;
    ProgressDialog mProgressDialog;
    TextView t1, t2;
    ImageView img;
    String title, desc, img_url;
    Button btn;
    EditText et;
    Bitmap bitmap;
    String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.imgIcon);
        t1 = (TextView) findViewById(R.id.txtTitle);
        //t2 = (TextView) findViewById(R.id.txtDesc);
        //btn = (Button) findViewById(R.id.button);
        et = (EditText) findViewById(R.id.editText);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("before changed" , "before");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                Log.d("on changed" , "on changed");
            }

            @Override
            public void afterTextChanged(Editable no) {

                Log.d("after changed" , "after");
                url = et.getText().toString();
                new FetchWebsiteData().execute();
            }

        });


        ////////////////////
        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = et.getText().toString();
                new FetchWebsiteData().execute();

            }
        });
*/
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {
        String websiteTitle, websiteDescription, imgurl;

        @Override
        protected void onPreExecute() {
            if (URLUtil.isValidUrl(url)) {

                try {
                    super.onPreExecute();
                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Log.d("OnPreExecute" , "enter valid url");
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (URLUtil.isValidUrl(url)){
            try {
                // Connect to website
                Document document = Jsoup.connect(url).userAgent(UserAgent).get();
                // Get the html document title
                websiteTitle = document.title();
                Elements description = document.select("meta[name=description]");
                // Locate the content attribute
                websiteDescription = description.attr("content");
                String ogImage = null;
                Elements metaOgImage = document.select("meta[property=og:image]");
                if (metaOgImage != null) {
                    imgurl = metaOgImage.first().attr("content");
                    System.out.println("src :<<<------>>> " + ogImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            }else {}
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            try {


            t1.setText(websiteTitle);
            //t2.setText(websiteDescription);
            Picasso.with(getApplicationContext()).load(imgurl).into(img);
            mProgressDialog.dismiss();
        }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}