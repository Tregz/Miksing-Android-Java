package com.tregz.miksing.data.song;

import com.tregz.miksing.data.DataNotation;

import java.util.HashMap;

final class SongUtil {

    static HashMap<String, Object> map(Song song) {
        HashMap<String, Object> map = new HashMap<>();
        if (song.getReleasedAt() != null) map.put(DataNotation.BD, song.getReleasedAt().getTime());
        if (song.getFeaturing() != null) map.put(DataNotation.FS, song.getFeaturing());
        if (song.getArtist() != null) map.put(DataNotation.MS, song.getArtist());
        if (song.getMixedBy() != null) map.put(DataNotation.LS, song.getMixedBy());
        if (song.getTitle() != null) map.put(DataNotation.NS, song.getTitle());
        map.put(DataNotation.WI, song.getWhat());
        return map;
    }
}
