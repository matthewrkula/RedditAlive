package com.mattkula.redditalive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matt on 2/10/15.
 */
public class Comment {

    String id;
    String parentId;
    String author;
    String authorFlairText;
    String body;
    int score;
    ArrayList<Comment> replies;
    int depth = 0;

    public static Comment fromJsonObject(JSONObject obj) {
        Comment comment = new Comment();

        try {

            JSONObject data = obj.getJSONObject("data");
            comment.id = "t1_" + data.getString("id");
            comment.parentId = data.getString("parent_id");
            comment.author = data.getString("author");
            comment.authorFlairText = data.getString("author_flair_text");
            comment.body = data.getString("body");
            comment.score = data.getInt("score");

            comment.replies = new ArrayList<>();
            JSONArray replyChildren = data.getJSONObject("replies")
                    .getJSONObject("data")
                    .getJSONArray("children");

            for (int i=0; i < replyChildren.length(); i++) {
                JSONObject replyObj = replyChildren.getJSONObject(i);
                Comment reply = Comment.fromJsonObject(replyObj);
                comment.replies.add(reply);
            }

            return comment;

        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return comment;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorFlairText() {
        return authorFlairText;
    }

    public String getBody() {
        return body;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Comment> getReplies() {
        //TODO fix the reason this is needed
        if (replies == null) {
            replies = new ArrayList<>();
        }
        return replies;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
