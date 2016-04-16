package com.group.msci.puzzlegenerator.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by filipt on 15/04/2016.
 */
public class UrlBuilder {

    public static final String URI_ROOT = "http://webprojects.eecs.qmul.ac.uk/ma334/";

    private String prefix;
    private String script;
    private Map<String, String> params;

    public UrlBuilder() {
        params = new HashMap<>();
    }

    public UrlBuilder(String script) {
        this();
        this.prefix = "";
        this.script = script;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void addParam(String paramName, String paramValue) {
        params.put(paramName, paramValue);
    }

    @Override
    public String toString() {
        StringBuilder args = new StringBuilder();
        if (params.size() > 0) {
            args.append("?");
            for (Map.Entry<String, String> pair : params.entrySet()) {
                args.append(String.format("%s=%s&", pair.getKey(), pair.getValue()));
            }
            //Remove trailing '&'
            args.setLength(args.length() - 1);
        }

        return String.format("%s%s%s%s", URI_ROOT, prefix, script, args.toString());
    }

    public URL toURL() throws MalformedURLException{
        URL url = new URL(toString());
        return url;
    }

}
