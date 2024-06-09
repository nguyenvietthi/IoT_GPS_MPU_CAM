package com.example.followvehicle.ui.home.children;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.followvehicle.R;
import com.example.followvehicle.api.api;

public class CameraFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_camera, container, false);

        WebView webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Đặt URL của luồng video từ Flask server
        String videoUrl = api.getBaseUrl();
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(videoUrl);
        return view;
    }
}