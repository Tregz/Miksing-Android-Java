package com.tregz.miksing.core.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class PlayWeb extends WebView {
    //private final String TAG = PlayWeb.class.getSimpleName();
    private final String ATTRIBUTES = "width:100%;height:100%;position:absolute;";
    private final String DURATION = "duration", PLAYING = "playingId", PRESENT = "present";
    private final String FRAMES = "iFrames";
    private final String LOADING = "loading";
    private final String MIKSING = "miksing";
    private final String NEXT = "next";
    private final String PLAYERS = "players";
    private final String PREPARE = "prepare";
    private final String VIDEO_ID = "videoId";

    public PlayWeb(Context context) {
        super(context);
        init();
    }

    public PlayWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayWeb(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        setMeasuredDimension(width, width * 9 / 16);
    }

    private void init() {
        setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                // Create empty bitmap, to prevent weird crash, when loading YouTube Player
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }
        });
        //setWebViewClient(new WebViewClient());
        getSettings().setMediaPlaybackRequiresUserGesture(false);
        getSettings().setJavaScriptEnabled(true);

        final String CURRENT = "current", ENSUING = "ensuing";
        String var0 = "var " + CURRENT + "=0," + ENSUING + "=0," + FRAMES + "," + PLAYERS + ";";
        final String FADE_IN = "fadeIn", FADE_OUT = "fadeOut", LOOP = "loop", LOOP_OUT = "loopOut";
        String var1 = "var " + FADE_IN + "=0," + FADE_OUT + "=0," + LOOP + "," + LOOP_OUT + ";";
        String var2 = "var " + LOADING + "=false;";
        String var3 = "var " + DURATION + "=0," + PRESENT + "=0," + PLAYING + "='';";
        final String STEP_UP = "stepper", POSITION = "position";
        String var4 = "var data," + POSITION + "=0;const " + STEP_UP + "=10;";
        String var5 = "var " + VIDEO_ID + "=''," + PREPARE + "=['5-q3meXJ6W4'];";
        String vars = var0 + var1 + var2 + var3 + var4 + var5;

        String onReady = function("onPlayerReady(event)", "event.target.mute();");
        final String DELAYED = "delayed", RISE_UP = "riseUp";
        String hide = visibility(getElements("progress") + "[0]", "hidden");
        String ifProgress = ifVisible(getElements("progress") + "[0]") + "{" + hide + "}";
        String timeOut = "setTimeout(" + DELAYED + ", 100);";
        String started = LOADING + "=true;" + timeOut + PLAYING + "=" + VIDEO_ID + ";";
        String playing = started + setInterval(200, RISE_UP, LOOP, FADE_IN) + ifProgress;
        String ifPlaying = ifState(playing, State.BUFFERING.name());
        String onPlayerStateChange = function("onPlayerStateChange(yt)", ifPlaying);

        // unused
        String mix = PLAYERS + "[" + Player.TESTING.ordinal() + "].pauseVideo();" + MIKSING + "();";
        String start = LOADING + "=true;" + mix;
        String onTested = ifState(start, State.PLAYING.name());
        String onTesterStateChange = function("onTesterStateChange(yt)", onTested);

        String setIndex = "if(" + PREPARE + " !==undefined) {";
        String ifLength = "if(" + PREPARE + ".length===" + POSITION + ") " + POSITION + " =0;";
        String newIndex = setIndex + POSITION + "+=1;" + ifLength;
        String setVideo = newIndex + VIDEO_ID + "=" + PREPARE + "[" + POSITION + "];";
        String playNext = function(NEXT + "()", setVideo + testVideo() + "};");
        String api0 = onReady + onPlayerStateChange + onTesterStateChange + playNext;

        String testing = "'onReady':onPlayerReady,'onStateChange':onTesterStateChange";
        String onState = "'onStateChange':onPlayerStateChange";
        String player0 = player(Player.TESTING.ordinal(), testing); // unused.
        String player1 = player(Player.PLAYER1.ordinal(), onState);
        String player2 = player(Player.PLAYER2.ordinal(), onState);
        String players = player0 + player1 + player2 + PLAYERS + "=[player0,player1,player2];";
        String iFrames = FRAMES + "=" + getElements("iframe") + ";";
        String api1 = function("onYouTubeIframeAPIReady()", players + iFrames);

        String src = "https://www.youtube.com/iframe_api";
        String create = "var tag = document.createElement('script');tag.src='" + src + "';";
        String element = "var script = " + getElements("script") + "[0];";
        String insert = "script.parentNode.insertBefore(tag,script);";
        String api2 = create + element + insert;

        String getHidden = ENSUING + "=" + positionPlayerHidden() + ";";
        String getShown = "if (" + ENSUING + "==1) {" + CURRENT + "=2 } else {" + CURRENT + "=1 };";
        String loadVideo = PLAYERS + "[" + ENSUING + "]" + ".loadVideoById(" + VIDEO_ID + ");";
        String notShown = "if(" + FRAMES + "[" + ENSUING + "].style.visibility==='hidden')";
        String setSwitchTimeOut = "setTimeout(function(){" + notShown + NEXT + "();}, 2000);";
        String mixer = getHidden + getShown + loadVideo + setSwitchTimeOut;
        String mixYouTube = function(MIKSING + "()", mixer);
        // Volume down fading
        final String GO_DOWN = "goDown";
        String stopping = "var stopping = " + PLAYERS + "[" + CURRENT + "];";
        String stop = clearInterval(LOOP_OUT) + ";" + FADE_OUT + "=0;" + "stopping.stopVideo();";
        String ifFadedDown = "if(" + FADE_OUT + "==100){" + stop + "}";
        String stepDown = ifFadedDown + "else{" + FADE_OUT + "+=" + STEP_UP + ";";
        String goDown = stepDown + "stopping.setVolume(100-" + FADE_OUT + ")}";
        String volumeDown = function(GO_DOWN + "()", stopping + goDown);
        // Volume up fading
        String starting = "var starting = " + PLAYERS + "[" + ENSUING + "];";
        String clear = "{" + clearInterval(LOOP) + ";" + FADE_IN + "=0;}";
        String ifFadedUp = "if(" + FADE_IN + "==100)" + clear;
        String stepUp = FADE_IN + "+=" + STEP_UP + ";starting.setVolume(" + FADE_IN + ");";
        String goUp = starting + stepUp + ifFadedUp;
        String volumeUp = function(RISE_UP + "()", goUp);
        // Delayed volume down fading
        String setHidden = visibility(FRAMES + "[" + CURRENT + "]", "hidden");
        String setVisible = visibility(FRAMES + "[" + ENSUING + "]", "visible");
        String setDelayedFadeOut = setInterval(400, GO_DOWN, LOOP_OUT, FADE_OUT);
        String delayedFadeOut = setHidden + setVisible + setDelayedFadeOut;
        String delayedVolumeDown = function(DELAYED + "()", delayedFadeOut);
        String api3 = mixYouTube + volumeDown + volumeUp + delayedVolumeDown;

        String api = api0 + api1 + api2 + api3;
        String script = "<script type='text/javascript'>" + vars + api + "</script>";
        String frames = divTest() + div(1) + div(2) + progress();
        String body = "<body style='margin:0;padding:0;'>" + frames + script + "</body>";
        String html = "<html>" + body + "</html>";
        loadData(html, "text/html", null);
    }

    public void countdown() {
        // Continuous mix
        final String TIMER = "timer";
        final int SECONDS_BEFORE_END = 10;
        String visible = PLAYERS + "[" + positionPlayerVisible() + "]";
        String currentTime = visible + ".getCurrentTime";
        String getTime = PRESENT + "=!" + currentTime + " ? 0.0 :" + currentTime + "();";
        String duration = visible + ".getDuration";
        String getDuration = DURATION + "=!" + duration + " ? 0.0 :" + duration + "();";
        String isEnding = "(" + DURATION + "-" + PRESENT + ")<" + SECONDS_BEFORE_END;
        String isPlaying = PLAYING + "===" + VIDEO_ID;
        String isStarted = PRESENT + ">0 && " + DURATION + ">0";
        String ifPlaying = "if(" + isPlaying + "){" + getTime + getDuration;
        String ifStarted = "if(" + isStarted + "){";
        String time = "else {" + getTime + getDuration + "}";
        String reset = DURATION + "=0;" + PRESENT + "=0;" + PLAYING + "='';";
        String next = reset + NEXT + "();";
        String ifEnding = "if(" + isEnding + "){" + next + "}" + time + "} else {" + NEXT + "();}";
        String command = ifPlaying + ifStarted + ifEnding + "}";
        String countdown = function(TIMER + "()", command);
        loadUrl("javascript:(" + countdown + ")()");
    }

    public void mix(String id) {
        String setId = VIDEO_ID + "='" + id + "';";
        loadUrl("javascript:(function(){" + setId + testVideo() + "})()");
    }

    public void setListing(String playlist) {
        loadUrl("javascript:(function(){" + PREPARE + "=" + playlist + ";})()");
    }

    private String clearInterval(String interval) {
        return "if(" + interval + "){clearInterval(" + interval + ");}";
    }

    private String div(int ordinal) {
        String style = "style='" + ATTRIBUTES + "visibility:hidden;'";
        return "<div id='player_" + ordinal + "' " + style + "></div>";
    }

    private String divTest() {
        String style = "style='" + ATTRIBUTES + "visibility:hidden;'";
        return "<div id='player_" + 0 + "' " + style + "></div>";
    }

    private String function(String name, String command) {
        return "function " + name + "{'use strict';" + command + "}";
    }

    private String getElements(String tag) {
        return "document.getElementsByTagName('" + tag + "')";
    }

    // when player is known
    private String ifState(String command, String state) {
        return "if(yt.data===YT.PlayerState." + state + "){" + command + "}";
    }

    private String ifVisible(int player) {
        String container = FRAMES + "[" + player + "]";
        return ifVisible(container) + "{return " + player + ";}";
    }

    private String ifVisible(String view) {
        String visible = view + ".style.visibility==='visible'";
        return "if(" + view + " && " + visible + ")";
    }

    private String player(int ordinal, String events) {
        String ytParams = "{playerVars:{showinfo:'0',modestbranding:'1'},events:{" + events + "}}";
        String ytPlayer = "new YT.Player('player_" + ordinal + "'," + ytParams + ");";
        return "var player" + ordinal + "=" + ytPlayer;
    }

    private String positionPlayerVisible() {
        String ifPlayer1 = ifVisible(Player.PLAYER1.ordinal());
        String ifPlayer2 = ifVisible(Player.PLAYER2.ordinal());
        String isTesting = " else{return " + Player.TESTING.ordinal() + ";};";
        return "(function(){'use strict';" + ifPlayer1 + "else " + ifPlayer2 + isTesting + "})()";
    }

    private String positionPlayerHidden() {
        String command = "if(" + positionPlayerVisible() + "===2){return 1;}else{return 2;}";
        return "(function(){'use strict';" + command + "})()";
    }

    private String progress() {
        String style = "style='" + ATTRIBUTES + "visibility:visible;'";
        return "<progress id='progress'" + style + "></progress>";
    }

    private String setInterval(int delay, String function, String interval, String value) {
        String equal = "=setInterval(" + function + "," + delay + ");";
        String setValue = value != null ? value + "=0;" : "";
        return setValue + clearInterval(interval) + ";" + interval + equal;
    }

    private String testVideo() {
        return LOADING + "=false;" + MIKSING + "()";
    }

    private String visibility(String view, String visibility) {
        return view + ".style.visibility='" + visibility + "';";
    }

    private enum Player {
        TESTING,
        PLAYER1,
        PLAYER2
    }

    /** https://developers.google.com/youtube/iframe_api_reference?hl=fr */
    private enum State {
        //UNSTARTED(-1),
        //ENDED(0),
        PLAYING, //(1)
        //PAUSED(2),
        BUFFERING,
        //CUED(5);

        /* private int value;

        public int getValue() {
            return value;
        }

        State(int value) {
            this.value = value;
        } */
    }
}
