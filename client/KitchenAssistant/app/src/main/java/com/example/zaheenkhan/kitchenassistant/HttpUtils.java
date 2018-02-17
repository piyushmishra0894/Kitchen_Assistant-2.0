package com.example.zaheenkhan.kitchenassistant;
import com.loopj.android.http.*;

/**
 * Created by aksha on 2/16/2018.
 */

public class HttpUtils {
    private static final String BASE_URL = "https://BaseURL";

    private static AsyncHttpClient client = new AsyncHttpClient();

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
