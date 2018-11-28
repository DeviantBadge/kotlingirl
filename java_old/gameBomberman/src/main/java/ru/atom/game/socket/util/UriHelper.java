package ru.atom.game.socket.util;

import java.nio.charset.Charset;
import java.util.HashMap;
import org.springframework.web.util.UriUtils;
import java.util.Map;

public class UriHelper {
    public static Map<String, String> getParamsFromUri(String uriQuery) {
        HashMap<String, String> params = new HashMap<>();
        String[] parameterValues;
        String[] currentParameter;
        uriQuery = UriUtils.decode(uriQuery, Charset.defaultCharset());
        parameterValues = uriQuery.split("&");
        for(String string : parameterValues) {
            currentParameter = string.split("=");
            params.put(currentParameter[0], currentParameter[1]);
        }
        return params;
    }
}
