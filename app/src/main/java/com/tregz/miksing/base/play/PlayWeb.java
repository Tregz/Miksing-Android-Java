package com.tregz.miksing.base.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class PlayWeb extends WebView {

    //private final String TAG = PlayWeb.class.getSimpleName();
    private final String ABSOLUTE = "position:absolute;";
    private final String WIDTH = "width:100%;";
    private final String HEIGHT = "height:100%;";

    private final String FRAMES = "iFrames";
    private final String LOADING = "loading";
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

    private void init() {
        setWebChromeClient(new WebChromeClient()); // enable alert dialog
        //setWebViewClient(new WebViewClient());
        getSettings().setMediaPlaybackRequiresUserGesture(false);
        getSettings().setJavaScriptEnabled(true);

        final String CURRENT = "current", ENSUING = "ensuing";
        String var0 = "var " + CURRENT + "=0," + ENSUING + "=0," + FRAMES + "," + PLAYERS + ";";
        final String FADE_IN = "fadeIn", FADE_OUT = "fadeOut", LOOP = "loop", LOOP_OUT = "loopOut";
        String var1 = "var " + FADE_IN + "=0," + FADE_OUT + "=0,"+ LOOP + "," + LOOP_OUT + ";";
        final String STARTED = "started";
        String var2 = "var " + STARTED + "=true," + LOADING + "=false;";
        final String DURATION = "duration", PRESENT = "present";
        String var3 = "var " + DURATION + "=0," + PRESENT + "=0;";
        final String STEP_UP = "stepper", POSITION = "position";
        String var4 = "var data;" + POSITION + "=0," + STEP_UP + "=10;";
        String var5 = "var " + VIDEO_ID + "='';" + PREPARE + "=['5-q3meXJ6W4'];";
        String vars = var0 + var1 + var2 + var3 + var4 + var5;

        String onReady = function("onPlayerReady(event)", "event.target.mute();");
        final String DELAYED = "delayed", RISE_UP = "riseUp";
        String hide = visibility(getElements("progress") + "[0]","hidden");
        String ifProgress = ifVisible(getElements("progress") + "[0]") +"{" + hide + "}";
        String timeOut = "setTimeout(" + DELAYED + ", 100);";
        String playing = setInterval(200, RISE_UP, LOOP, FADE_IN) + timeOut + ifProgress;
        String ifPlaying = ifState(playing, State.PLAYING.name());
        String onPlayerStateChange = function("onPlayerStateChange(yt)", ifPlaying);
        final String SKIPPER = "skipper", MIKSING = "miksing";
        String isStart = "if(" + STARTED + "){" + STARTED + "=false;}"; // fired once on load
        String unStart = "else if(!" + LOADING + "){" + SKIPPER + "();" + STARTED + "=true;}";
        String ifStopped = ifState(isStart + unStart, State.UNSTARTED.name());
        String mix = MIKSING + "();" + PLAYERS + "[" + Player.TESTING.ordinal() + "].stopVideo();";
        String ifTested = ifState(LOADING + "=true;" + mix, State.PLAYING.name());
        String onTested = ifTested + "else " + ifStopped;
        String onTesterStateChange = function("onTesterStateChange(yt)", onTested);

        final String NEXT = "next";
        String setIndex = "if(" + LOADING + " && " + PREPARE + " !==undefined) {";
        String ifLength = "if(" + PREPARE + ".length===" + POSITION + ") " + POSITION + " =0;";
        String newIndex = setIndex + POSITION + "+=1;" + ifLength;
        String setVideo = newIndex + VIDEO_ID + "=" + PREPARE + "[" + POSITION + "];";
        String playNext = function(NEXT + "()", setVideo + testVideo() + "};");
        String api0 = onReady + onPlayerStateChange + onTesterStateChange + playNext;

        String testing = "'onReady':onPlayerReady,'onStateChange':onTesterStateChange";
        String onState = "'onStateChange':onPlayerStateChange";
        String player0 = player(Player.TESTING.ordinal(), testing);
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

        String ifIndex = "if(index==" + PREPARE + ".length){index=0;}";
        String index = "var index=" + PREPARE + ".indexOf(" + VIDEO_ID + ")+1;" + ifIndex;
        String setId = VIDEO_ID + "=" + PREPARE + "[index];";
        String api3 = function(SKIPPER + "()", index + setId + testVideo());

        String hiddenPlayer = PLAYERS + "[" + positionPlayerHidden() + "]";
        String mixYouTube = function(MIKSING + "()", loadVideoById(hiddenPlayer, VIDEO_ID));
        // Continuous mix
        final String COUNTDOWN = "countdown";
        String visible = PLAYERS + "[" + positionPlayerVisible() + "]";
        String currentTime = PRESENT + "=" + visible + ".getCurrentTime();";
        String duration = DURATION + "=" + visible + ".getDuration();";
        String time = currentTime + duration;
        String ending = "(" + DURATION + "-" + PRESENT + ")<10";
        String ifEnding = "if(" + PRESENT + ">0 && " + DURATION + ">0 && " + ending + "){";
        String reset = DURATION + "=0;" + PRESENT + "=0;";
        String next = reset + NEXT + "();" + clearInterval(COUNTDOWN) + ";}";
        String countdown = function(COUNTDOWN + "()", time + ifEnding + next);
        // Volume down fading
        final String GO_DOWN = "goDown";
        String stopping = "var stopping = " + PLAYERS + "[" + positionPlayerHidden() + "];";
        String stop = clearInterval(LOOP_OUT) + ";" + FADE_OUT + "=0;" + "stopping.stopVideo();";
        String ifFadedDown = "if(" + FADE_OUT + "==100){" + stop + "}";
        String stepDown = ifFadedDown + "else{" + FADE_OUT + "+=" + STEP_UP + ";";
        String goDown = stepDown + "stopping.setVolume(100-" + FADE_OUT + ")}";
        String volumeDown = function(GO_DOWN + "()", stopping + goDown);
        // Volume up fading
        final String INTERVAL_COUNTDOWN = "interval_countdown";
        String starting = "var starting = " + PLAYERS + "[" + positionPlayerVisible() + "];";
        String clear = "{" + clearInterval(LOOP) + ";" + FADE_IN + "=0;}";
        String ifFadedUp = "if(" + FADE_IN + "==100)" + clear;
        String stepUp = FADE_IN + "+=" + STEP_UP + ";starting.setVolume(" + FADE_IN + ");";
        String setCountdown = INTERVAL_COUNTDOWN + "=setInterval(" + COUNTDOWN + "," + 1000 + ");";
                //setInterval(1000, COUNTDOWN, INTERVAL_COUNTDOWN, null); // TODO
        String goUp = starting + stepUp + ifFadedUp + setCountdown;
        String volumeUp = function(RISE_UP + "()", goUp);
        // Delayed volume down fading
        String getHidden = ENSUING + "=" + positionPlayerHidden() + ";";
        String getVisible = CURRENT + "=" + positionPlayerVisible() + ";";
        String setHidden = visibility(FRAMES + "[" + CURRENT + "]", "hidden");
        String setVisible = visibility(FRAMES + "[" + ENSUING + "]", "visible");
        String setDelayedFadeOut = setInterval(400, GO_DOWN, LOOP_OUT, FADE_OUT);
        String delayedFadeOut = getHidden + getVisible + setHidden + setVisible + setDelayedFadeOut;
        String delayedVolumeDown = function(DELAYED + "()", delayedFadeOut);
        String api4 = mixYouTube + countdown + volumeDown + volumeUp + delayedVolumeDown;

        String api = api0 + api1 + api2 + api3 + api4;
        String script = "<script type='text/javascript'>" + vars + api + "</script>";
        String frames = div(0) + div(1) + div(2) + progress();
        String body = "<body style='margin:0;padding:0;'>" + frames + script + "</body>";
        String html = "<html>" + body + "</html>";
        loadData(html, "text/html", null);
    }

    public void load(String id) {
        loadUrl("javascript:(function(){" + loadVideoById("players[2]", id) + ";})()");
    }

    public void mix(String id) {
        String setId = VIDEO_ID + "='" + id + "';";
        loadUrl("javascript:(function(){" + setId + testVideo() + PREPARE + "=['" + id + "'];})()");
    }

    public void setListing(String playlist) {
        loadUrl("javascript:(function(){" + PREPARE + "=[" + playlist + "];})()");
    }

    private String clearInterval(String interval) {
        return "if(" + interval + "!=='null'){clearInterval(" + interval + ");}";
    }

    private String div(int ordinal) {
        String style = "style='" + WIDTH + HEIGHT + ABSOLUTE + "visibility:hidden;'";
        return "<div id='player_" + ordinal + "'" + style + "></div>";
    }

    private String function(String name, String command) {
        return "function " + name + "{'use strict';" + command + "}";
    }

    private String getElements(String tag) {
        return "document.getElementsByTagName('" + tag + "')";
    }

    private String ifState(String command, String state) {
        return "if(yt.data===YT.PlayerState." + state + "){" + command + "}";
    }

    private String ifVisible(int player) {
        String container = FRAMES + "[" + player + "]";
        return ifVisible(container) + "{return " + player + ";}";
    }

    private String ifVisible(String view) {
        String notNull = "'null'!=" + view;
        String visible = view + ".style.visibility==='visible'";
        return "if(" + notNull + " && " + visible + ")";
    }

    private String loadVideoById(String player, String videoId) {
        return player + ".loadVideoById(" + videoId + ");";
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
        String style = "style='" + WIDTH + HEIGHT + ABSOLUTE + "visibility:visible;'";
        return "<progress id='progress'" + style + "></progress>";
    }

    private String setInterval(int delay, String function, String interval, String value) {
        String equal = "=setInterval(" + function + "," + delay + ");";
        String setValue = value != null? value + "=0;" : "";
        return setValue + clearInterval(interval) + ";" + interval + equal;
    }

    private String testVideo() {
        String player = PLAYERS + "[" + Player.TESTING.ordinal() + "]";
        return LOADING + "=false;" + player + ".loadVideoById(" + VIDEO_ID + ");";
    }

    private String visibility(String view, String visibility) {
        return view + ".style.visibility='" + visibility + "';";
    }

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
