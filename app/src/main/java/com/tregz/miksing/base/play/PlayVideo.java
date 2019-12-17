package com.tregz.miksing.base.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class PlayVideo extends WebView {

    private final String TAG = PlayVideo.class.getSimpleName();
    private final String POSITION = "position:absolute;";
    private final String WIDTH = "width:100%;";
    private final String HEIGHT = "height:100%;";

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
        //setWebChromeClient(new WebChromeClient());
        //setWebViewClient(new WebViewClient());
        getSettings().setMediaPlaybackRequiresUserGesture(false);
        getSettings().setJavaScriptEnabled(true);
        String frames = div(0) + div(1) + div(2) + progress();
        String script = "<script type='text/javascript'>" + vars() + api() + "</script>";
        String body = "<body style='margin:0;padding:0;'>" + frames + script + "</body>";
        String html = "<html>" + body + "</html>";
        loadData(html, "text/html", null);
    }

    public void load(String id) {
        loadUrl("javascript:(function(){players[2].loadVideoById('" + id + "');})()");
    }

    public void mix(String id) {
        String setId = VIDEO_ID + "='" + id + "';";
        loadUrl("javascript:(function(){" + setId + testVideo() + LISTING + "=['" + id + "'];})()");
    }

    public void setListing(String playlist) {
        loadUrl("javascript:(function(){" + LISTING + "=[" + playlist + "];})()");
    }

    private String progress() {
        String style = "style='" + WIDTH + HEIGHT + POSITION + "visibility:" + VISIBLE + ";'";
        return "<progress id='progress'" + style + "></progress>";
    }

    private String div(int ordinal) {
        String style = "style='" + WIDTH + HEIGHT + POSITION + "visibility:hidden;'";
        return "<div id='player_" + ordinal + "'" + style + "></div>";
    }

    private String api() {
        String src = "https://www.youtube.com/iframe_api";
        String tag = "var tag = document.createElement('script');tag.src='" + src + "';";
        String script = "var script = " + getElements("script") + "[0];";
        String insert = "script.parentNode.insertBefore(tag,script);";
        String states = onPlayerReady() + onPlayerStateChange() + onTesterStateChange();
        String mixer = mixYouTube() + volumeDown() + volumeUp() + delayedFadeOut();
        return states + onYouTubeIframeAPIReady() + tag + script + insert + skipper() + mixer;
    }

    private String ifState(String command, String state) {
        Log.d(TAG, "STATE: " + state);
        return "if(yt.data===YT.PlayerState." + state + "){" + command + "}";
    }

    private String clearInterval(String interval) {
        return "if(" + interval + "!=='null'){clearInterval(" + interval + ");}";
    }

    private String setInterval(int delay, String function, String interval, String value) {
        String equal = "=setInterval(" + function + "," + delay + ");";
        return value + "=0;" + clearInterval(interval) + interval + equal;
    }

    private String mixYouTube() {
        String pl = PLAYERS + "[" + hiddenPlayerPosition() + "]";
        return function(MIKSING + "()", pl + ".loadVideoById(" + VIDEO_ID + ");");
    }

    private String onYouTubeIframeAPIReady() {
        String testing = "'onReady':onPlayerReady,'onStateChange':onTesterStateChange";
        String onState = "'onStateChange':onPlayerStateChange";
        String player0 = player(Player.TESTING.ordinal(), testing);
        String player1 = player(Player.PLAYER1.ordinal(), onState);
        String player2 = player(Player.PLAYER2.ordinal(), onState);
        String observe = player0 + player1 + player2;
        String players = PLAYERS + "=[player0,player1,player2];";
        String iFrames = IFRAMES + "=" + getElements("iframe") + ";";
        return function("onYouTubeIframeAPIReady()", observe + players + iFrames);
    }

    private String onPlayerReady() {
        String command = "event.target.mute();";
        return function("onPlayerReady(event)", command);
    }

    private String onPlayerStateChange() {
        String hide = visibility(getElements("progress") + "[0]","hidden");
        String ifProgress = ifVisible(getElements("progress") + "[0]") +"{" + hide + "}";
        String timeOut = "setTimeout(" + DELAYED + ", 100);";
        String playing = setInterval(200, RISE_UP, LOOPING, FADE_IN) + timeOut + ifProgress;
        String ifPlaying = ifState(playing, State.PLAYING.name());
        return function("onPlayerStateChange(yt)", ifPlaying);
    }

    private String onTesterStateChange() {
        String isStart = "if(" + STARTED + "){" + STARTED + "=false;}"; // fired once on load
        String unStart = "else if(!" + LOADING + "){" + SKIPPER + "();" + STARTED + "=true;}";
        String stopped = ifState(isStart + unStart, State.UNSTARTED.name());
        String mix = MIKSING + "();" + PLAYERS + "[" + Player.TESTING.ordinal() + "].stopVideo();";
        String playing = ifState(LOADING + "=true;" + mix, State.PLAYING.name());
        return function("onTesterStateChange(yt)", playing + "else " + stopped);
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

    private String delayedFadeOut() {
        String ensuing = ENSUING + "=" + hiddenPlayerPosition() + ";";
        String current = CURRENT + "=" + visiblePlayerPosition() + ";";
        String hide = visibility(IFRAMES + "[" + CURRENT + "]", "hidden");
        String show = visibility(IFRAMES + "[" + ENSUING + "]", VISIBLE);
        String fadeOut = setInterval(400, GO_DOWN, LOOPOUT, FADE_OUT);
        return function(DELAYED + "()", ensuing + current + hide + show + fadeOut);
    }

    private String skipper() {
        String ifLength = "if(index==" + LISTING + ".length){index=0;}";
        String index = "var index=" + LISTING + ".indexOf(" + VIDEO_ID + ")+1;" + ifLength;
        String setId = VIDEO_ID + "=" + LISTING + "[index];";
        return function(SKIPPER + "()", index + setId + testVideo());
    }

    private String testVideo() {
        String player = PLAYERS + "[" + Player.TESTING.ordinal() + "]";
        return LOADING + "=false;" + player + ".loadVideoById(" + VIDEO_ID + ");";
    }

    private String vars() {
        String frames = "var " + CURRENT + "=0," + ENSUING + "=0," + IFRAMES + "," + PLAYERS + ";";
        String faders = "var " + FADE_IN + "=0," + FADE_OUT + "=0,"+ LOOPING + "," + LOOPOUT + ";";
        String states = "var " + STARTED + "=true," + LOADING + "=false;";
        String videos = "var " + VIDEO_ID + "=''; var " + LISTING + "=['5-q3meXJ6W4'];";
        return "var data;" + frames + faders + states + videos + "var " + STEP_UP + "=10;";
    }

    private String volumeDown() {
        String player = PLAYERS + "[" + CURRENT + "]";
        String clear = clearInterval(LOOPOUT) + ";" + FADE_OUT + "=0;" + player + ".stopVideo();";
        String ifFaded = "if(" + FADE_OUT + "==100){" + clear + "}";
        String stepper = ifFaded + "else{" + FADE_OUT + "+=" + STEP_UP + ";";
        String command = stepper + player + ".setVolume(100-" + FADE_OUT + ")}";
        return function(GO_DOWN + "()", command);
    }

    private String volumeUp() {
        String clear = "{" + clearInterval(LOOPING) + ";" + FADE_IN + "=0;}";
        String ifFaded = "if(" + FADE_IN + "==100)" + clear;
        String stepper = FADE_IN + "+=" + STEP_UP + ";" + PLAYERS + "[" + ENSUING + "]";
        String command = stepper + ".setVolume(" + FADE_IN + ");" + ifFaded;
        return function(RISE_UP + "()", command);
    }

    private String visiblePlayerPosition() {
        String ifPlayer1 = ifVisible(Player.PLAYER1.ordinal());
        String ifPlayer2 = ifVisible(Player.PLAYER2.ordinal());
        String isTesting = " else{return " + Player.TESTING.ordinal() + ";};";
        return "(function(){'use strict';" + ifPlayer1 + "else " + ifPlayer2 + isTesting + "})()";
    }

    private String hiddenPlayerPosition() {
        String command = "if(" + visiblePlayerPosition() + "===2){return 1;}else{return 2;}";
        return "(function(){'use strict';" + command + "})()";
    }

    private String visibility(String view, String visibility) {
        return view + ".style.visibility='" + visibility + "';";
    }

    private String ifVisible(int player) {
        String container = IFRAMES + "[" + player + "]";
        return ifVisible(container) + "{return " + player + ";}";
    }

    private String ifVisible(String view) {
        String notNull = "'null'!=" + view;
        String visible = view + ".style.visibility==='" + VISIBLE + "'";
        return "if(" + notNull + " && " + visible + ")";
    }

    /* private String alert(String message) {
        return "alert(" + message + ");";
    } */

    private final String CURRENT = "current";
    private final String STEP_UP = "stepper";
    private final String DELAYED = "delayed";

    private final String ENSUING = "ensuing";
    private final String FADE_IN = "faderIn";
    private final String FADE_OUT = "fadeOut";
    private final String GO_DOWN = "goDown";
    private final String IFRAMES = "iFrames";
    private final String LOADING = "loading";
    private final String LISTING = "prepare"; // TODO
    private final String LOOPING = "looping";
    private final String LOOPOUT = "loopOut";
    private final String MIKSING = "miksing";
    private final String PLAYERS = "players";
    private final String RISE_UP = "riseUp";
    private final String SKIPPER = "skipper";
    private final String STARTED = "started";
    private final String VIDEO_ID = "videoId";
    private final String VISIBLE = "visible";

    private enum Player {
        TESTING,
        PLAYER1,
        PLAYER2
    }

    private enum State {
        UNSTARTED,
        PLAYING
    }
}
