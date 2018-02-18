package com.example.zaheenkhan.kitchenassistant;
import com.loopj.android.http.*;

/**
 * Created by aksha on 2/16/2018.
 */

public class HttpUtils {
    private static final String BASE_URL = "https://1bf05016-3e44-4c89-90a3-14ea6e0b10a1.mock.pstmn.io";

    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(BASE_URL, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
