package com.tregz.miksing.data.user;

import com.tregz.miksing.data.DataNotation;

import java.util.HashMap;

final class UserUtil {

    static HashMap<String, Object> map(User user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DataNotation.CD, user.getCreatedAt().getTime());
        if (user.getName() != null) map.put(DataNotation.NS, user.getName());
        if (user.getEmail() != null) map.put(DataNotation.ES, user.getEmail());
        return map;
    }
}
