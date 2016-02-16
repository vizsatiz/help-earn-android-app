package com.earnapp.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by vizsatiz on 14-02-2016.
 */
public class CustomJsonObjectRequest extends CustomRequest<JSONObject>{


    public CustomJsonObjectRequest(int method, String url, Map<String, String> headers,
                                   Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, headers, responseListener, errorListener);
    }

    public CustomJsonObjectRequest(String url, Map<String, String> headers,
                                   Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(url, headers, responseListener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
