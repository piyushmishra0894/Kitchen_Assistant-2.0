package com.example.zaheenkhan.kitchenassistant;
import com.loopj.android.http.*;

/**
 * Created by aksha on 2/16/2018.
 */

public class HttpUtils {
    private static final String BASE_URL = "http://kitchenassistant.us-east-2.elasticbeanstalk.com";
    //private static final String BASE_URL = "http://192.168.0.21:8000";

    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
