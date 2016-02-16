package com.earnapp.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.Map;

/**
 * Created by vizsatiz on 12-02-2016.
 */
public abstract class CustomRequest<T> extends Request<T>{

    private Response.Listener<T> listener;
    private Map<String,String> headers;


    public CustomRequest(String url,Map<String,String> headers,Listener<T> responseListener,ErrorListener errorListener){
        super(Request.Method.GET, url, errorListener);
        this.listener = responseListener;
        this.headers = headers;
    }

    public CustomRequest(int method, String url, Map<String, String> headers,
                         Listener<T> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.headers = headers;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return headers;
    };

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    protected void deliverResponse(T response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

}
