package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.base.date.DateUtil;
import com.tregz.miksing.base.text.TextUtil;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.home.HomeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WarnFetch extends BaseWarning {

    public static final String TAG = WarnFetch.class.getSimpleName();
    private HomeView listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeView) listener = (HomeView) context;
        else onListenerError(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null && getActivity() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.fetch_youtube_title);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.text_input, null);
            final TextInputEditText edit = view.findViewById(R.id.et_input);
            TextInputLayout inputLayout = view.findViewById(R.id.input_layout);
            inputLayout.setHint(str(R.string.fetch_youtube_hint));

            // testing
            String testYT = "https://www.youtube.com/watch?v=";
            String testId = "dMK_npDG12Q&list=PLqFMT2yxu9RRT578WXXzWtax30n1xIv1X";
            String tester = testYT + testId;
            edit.setText(tester);
            alert.setView(view);

            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (edit.getText() == null || edit.getText().toString().isEmpty())
                        toast(R.string.request_link_must_not_null);
                    else {
                        String link = edit.getText().toString();
                        if (link.contains("youtu")) {
                            boolean list = link.contains("list=");
                            request(listener.getWebView(), getId(link, list), list);
                        } else toast(R.string.request_link_not_valid);
                    }
                }
            });
            return alert.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    private String getId(String link, boolean list) {
        String trim = "";
        if (list && link.contains("list=")) trim = "list=";
        else if (link.contains("watch?v=")) trim = "watch?v=";
        else if (link.contains(".be/")) trim = ".be/";
        String id = link.substring(link.indexOf(trim) + trim.length());
        if (id.contains("?")) id = id.substring(0, id.indexOf('?'));
        return id;
    }

    private void getData(final WebView webView, final boolean isList) {
        final String script = "(function(){return data;})()"; // try to get data from youtube api
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                webView.evaluateJavascript(script, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d(TAG, value);
                        if (!TextUtil.stripQuotes(value).equals("null")) {
                            if (TextUtil.stripQuotes(value).equals("error")) {
                                Log.e(TAG, "Error!");
                            } else readYouTubeData(value, isList);
                        } else getData(webView, isList);
                    }
                });
            }
        }, 1000); // if data not yet available, delayed loop to try again
    }

    private void readYouTubeData(String data, boolean isList) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = new JSONArray(jsonObject.getString("items"));
            List<Song> songs = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String title, author = "", id;
                JSONObject json = array.getJSONObject(i).getJSONObject("snippet");
                title = TextUtil.stripQuotes(json.getString("title"));
                if (title.contains(" - ")) {
                    String[] split = title.split(" - ");
                    author = split[0];
                    title = split[1];
                }
                Date born = Calendar.getInstance().getTime();
                String published = json.getString("publishedAt");
                Log.d(TAG, "publishedAt: " + published);
                try {
                    String date = published.substring(0, published.indexOf("T"));
                    born = DateUtil.simple(null).parse(date);
                } catch (ParseException e) {
                    if (e.getMessage() != null) Log.e(TAG, e.getMessage());
                }
                if (isList) {
                    JSONObject resourceId = json.getJSONObject("resourceId");
                    id = TextUtil.stripQuotes(resourceId.getString("videoId"));
                } else id = TextUtil.stripQuotes(array.getJSONObject(i).getString("id"));
                Song song = new Song(id, new Date());
                if (born != null) Log.d(TAG, born.toString());
                song.setReleasedAt(born);
                song.setArtist(author);
                song.setTitle(title);
                songs.add(song);
            }
            if (array.length() == 1)
                if (getContext() != null) ((HomeView) getContext()).onFillItemDetails(songs.get(0));
            else listener.onHttpRequestResult(songs);
        } catch (JSONException e) {
            if (e.getMessage() != null) Log.e(TAG, e.getMessage());
        }
    }

    private void request(WebView webView, String id, Boolean list) {
        String key = webView.getResources().getString(R.string.browser_key);
        String api = "https://www.googleapis.com/youtube/v3/";
        String path = api + (list ? "playlistItems?part=snippet&key=" : "videos?part=snippet&key=");
        String get = path + key + (list ? "&playlistId=" + id + "&max-results=50" : "&id=" + id);
        String script = "javascript:(function(){var request=new XMLHttpRequest();" +
                "request.open('GET','" + get + "',true);" +
                "request.onreadystatechange=function(){" +
                "if(this.readyState===4&&this.status===200){" +
                "data=JSON.parse(request.responseText);}else{data='error'}" +
                "};" +
                "request.send();})()";
        webView.loadUrl(script);
        getData(webView, list);
    }
}
