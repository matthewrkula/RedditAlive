package com.mattkula.redditalive.networking;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

/**
 * Created by matt on 2/10/15.
 */
public class NetworkManager {

    private static NetworkManager instance;
    private RequestQueue requestQueue;

    private NetworkManager(Context context) {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 0);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public void addJsonArrayRequest(JsonArrayRequest request) {
        requestQueue.add(request);
    }
}
