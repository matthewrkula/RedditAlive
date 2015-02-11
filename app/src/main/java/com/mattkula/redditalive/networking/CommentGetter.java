package com.mattkula.redditalive.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mattkula.redditalive.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matt on 2/10/15.
 */
public class CommentGetter {

//    static String url = "http://www.reddit.com/r/nba/comments/2vh5jw/game_thread_detroit_pistons_2032_charlotte.json?sort=new";
    static String baseURL = "http://www.reddit.com/comments/";

    static int cacheBreaker = 1;

    public static void getComments(Context context, String threadID, final CommentListener listener) {
        String url = baseURL + threadID + ".json?sort=new&asdf=" + cacheBreaker++;
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {
                try {

                    ArrayList<Comment> commentList = new ArrayList<>();

                    JSONObject obj = jsonArray.getJSONObject(1);
                    JSONArray array = obj.getJSONObject("data")
                            .getJSONArray("children");

                    for (int i=0; i < array.length(); i++) {
                        JSONObject replyObj = array.getJSONObject(i);
                        Comment comment = Comment.fromJsonObject(replyObj);
                        commentList.add(comment);
                    }

                    listener.gotComments(commentList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(CommentGetter.class.getName(), volleyError.toString());
            }

        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "com.mattkula.redditalive v0.1 (by /u/kuler51)");
                return headers;
            }
        };

        NetworkManager.getInstance(context)
                .addJsonArrayRequest(request);
    }

    public interface CommentListener {
        public void gotComments(List<Comment> comments);
    }
}
