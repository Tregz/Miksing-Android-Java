package com.tregz.miksing.home;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseDialog;
import com.tregz.miksing.core.date.DateUtil;
import com.tregz.miksing.core.text.TextUtil;
import com.tregz.miksing.data.item.work.Work;
import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.home.list.ListCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

class HomeDialog extends BaseDialog {
    private final String TAG = HomeDialog.class.getSimpleName();

    HomeDialog(@NonNull final Context context, @NonNull final WebView webView) {
        super(context);
        builder.setTitle(webView.getContext().getString(R.string.request_youtube));
        final EditText input = input();
        builder.setPositiveButton(okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = input.getText().toString();
                if (link.contains("youtu")) {
                    boolean list = link.contains("list=");
                    Log.d(TAG, "isList?" + list);
                    request(webView, getId(link, list), list);
                }
            }
        });
        show();
    }

    HomeDialog(@NonNull final Context context) {
        super(context);
    }

    void clear() {
        builder.setTitle(context.getString(R.string.clear));
        builder.setPositiveButton(okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context instanceof HomeView) ((HomeView) context).onClearItemDetails();
            }
        });
        show();
    }

    void save() {
        builder.setTitle(context.getString(R.string.save));
        builder.setPositiveButton(okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context instanceof HomeView) ((HomeView) context).onSaveItem();
            }
        });
        show();
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

    private String getId(String link, boolean list) {
        String trim = "";
        if (list && link.contains("list=")) trim = "list=";
        else if (link.contains("watch?v=")) trim = "watch?v=";
        else if (link.contains(".be/")) trim = ".be/";
        String id = link.substring(link.indexOf(trim) + trim.length());
        if (id.contains("?")) id = id.substring(0, id.indexOf('?'));
        return id;
    }

    private void getData(final WebView webView, final boolean list) {
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
                            } else readYouTubeData(value, list);
                        } else getData(webView, list);
                    }
                });
            }
        }, 1000); // if data not yet available, delayed loop to try again
    }

    private void readYouTubeData(String data, boolean list) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = new JSONArray(jsonObject.getString("items"));
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
                if (list) {
                    JSONObject resourceId = json.getJSONObject("resourceId");
                    id = TextUtil.stripQuotes(resourceId.getString("videoId"));
                } else id = TextUtil.stripQuotes(array.getJSONObject(i).getString("id"));
                Work song = new Song(id, new Date());
                if (born != null) Log.d(TAG, born.toString());
                song.setReleasedAt(born);
                song.setArtist(author);
                song.setTitle(title);
                ListCollection.getInstance().add(song);
                if (array.length() == 1) {
                    if (context instanceof HomeView) ((HomeView) context).onFillItemDetails(song);
                } else {
                    if (context instanceof HomeView) ((HomeView) context).onSaved();
                }
            }
        } catch (JSONException e) {
            if (e.getMessage() != null) Log.e(TAG, e.getMessage());
        }
    }
}
