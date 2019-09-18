package com.marcospoerl.simplypace.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebView;

import com.marcospoerl.simplypace.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setText();
    }

    protected abstract int getLayoutResourceId();

    protected abstract int getWebViewId();

    protected abstract int getRawResourceId();

    private void setText() {
        final String content = readRawTextFile();
        final WebView webView = (WebView) findViewById(getWebViewId());
        webView.loadDataWithBaseURL("file:///android_asset/", content, "text/html", null, null);
    }

    private String readRawTextFile() {
        final InputStream is = getResources().openRawResource(getRawResourceId());
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (final IOException e) {
            return null;
        }
        return sb.toString();
    }

}
