package com.tregz.miksing.data.user;

import com.tregz.miksing.data.DataNotation;

import java.util.HashMap;

public final class UserUtil {

    static HashMap<String, Object> map(User user) {
        HashMap<String, Object> map = new HashMap<>();
        if (user.getName() != null) map.put(DataNotation.NS, user.getName());
        if (user.getEmail() != null) map.put(DataNotation.AS, user.getEmail());
        return map;
    }
}
