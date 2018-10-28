/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.s215087038.wefixx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.s215087038.wefixx.pdf.BaseSampleActivity;
import com.example.s215087038.wefixx.pdf.DownloadFile;
import com.example.s215087038.wefixx.pdf.FileUtil;
import com.example.s215087038.wefixx.pdf.PDFPagerAdapter;
import com.example.s215087038.wefixx.pdf.RemotePDFViewPager;


public class PDFActivity extends BaseSampleActivity implements DownloadFile.Listener {

    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    String doc = "http://sict-iis.nmmu.ac.za/wefixx/files/request_reports/";
    PDFPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_report);
        setContentView(R.layout.activity_pdf);

        root = (LinearLayout) findViewById(R.id.remote_pdf_root);
        Bundle bundle = getIntent().getExtras();
        String report = bundle.getString("report");
        doc = doc + report;
        get_report();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }

    protected void get_report() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(ctx, getUrlFromEditText(), listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    protected String getUrlFromEditText() {
        return doc.trim();
    }

    public static void open(Context context) {
        Intent i = new Intent(context, PDFActivity.class);
        context.startActivity(i);
    }



    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}

