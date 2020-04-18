package net.thumbtack.hospital.utils.json;

import com.google.gson.Gson;

public class JsonProcessor {
    private static final String emptyJson = "{}";

    public static String toJson(Object o) {
        return new Gson().toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> c) {
        return new Gson().fromJson(json, c);
    }

    public static String getEmptyJson() {
        return emptyJson;
    }
}