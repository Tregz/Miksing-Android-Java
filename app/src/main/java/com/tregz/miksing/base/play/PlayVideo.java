package com.tregz.miksing.base.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class PlayVideo extends WebView {

    public PlayVideo(Context context) {
        super(context);
        init();
    }

    public PlayVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayVideo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        getSettings().setMediaPlaybackRequiresUserGesture(false);
        getSettings().setJavaScriptEnabled(true);
        String frames = div(0) + div(1) + div(2);
        String script = "<script type='text/javascript'>var data, players;" + api() + "</script>";
        String body = "<body style='margin:0;padding:0;'>" + frames + script + "</body>";
        String html = "<html>" + body + "</html>";
        loadData(html, "text/html", null);
    }

    public void load(String id) {
        loadUrl("javascript:(function(){players[2].loadVideoById('" + id + "');})()");
    }

    private String div(int ordinal) {
        String style = "width:100%;height:100%;position:absolute;"; // visibility:hidden;
        return "<div id='player_" + ordinal + "' style='" + style + "'></div>";
    }

    private String api() {
        String src = "https://www.youtube.com/iframe_api";
        String tag = "var tag = document.createElement('script');tag.src='" + src + "';";
        String script = "var script = " + getElements("script") + "[0];";
        String insert = "script.parentNode.insertBefore(tag,script);";
        String states = onPlayerStateChange() + onTesterStateChange();
        return states + onYouTubeIframeAPIReady() + tag + script + insert ;
    }

    private String onYouTubeIframeAPIReady() {
        String onReady = "'onReady':function(e){e.target.mute();}";
        String testing = onReady + ",'onStateChange':onTesterStateChange";
        String onState = "'onStateChange':onPlayerStateChange";
        String player0 = player(Player.TESTING.ordinal(), testing);
        String player1 = player(Player.PLAYER1.ordinal(), onState);
        String player2 = player(Player.PLAYER2.ordinal(), onState);
        String observe = player0 + player1 + player2;
        String players = "players=[player0,player1,player2];";
        String iFrames = getElements("iframe") + ";";
        return function("onYouTubeIframeAPIReady()", observe + players + iFrames);
    }

    private String onPlayerStateChange() {
        return function("onPlayerStateChange(yt)", ""); // TODO
    }

    private String onTesterStateChange() {
        return function("onTesterStateChange(yt)", ""); // TODO
    }

    private String getElements(String tag) {
        return "document.getElementsByTagName('" + tag + "')";
    }

    private String function(String name, String command) {
        return "function " + name + "{'use strict';" + command + "}";
    }

    private String player(int ordinal, String events) {
        String ytParams = "{playerVars:{showinfo:'0',modestbranding:'1'},events:{" + events + "}}";
        String ytPlayer = "new YT.Player('player_" + ordinal + "'," + ytParams + ");";
        return "var player" + ordinal + "=" + ytPlayer;
    }

    private String test = "alert('WORKS!');"; // </script><script>

    private enum Player {
        TESTING,
        PLAYER1,
        PLAYER2
    }
}
