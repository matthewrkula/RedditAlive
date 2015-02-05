package com.mattkula.redditalive.networking;

import android.os.AsyncTask;

import com.github.jreddit.entity.Comment;
import com.github.jreddit.retrieval.Comments;
import com.github.jreddit.retrieval.params.CommentSort;
import com.github.jreddit.utils.restclient.HttpRestClient;

import java.util.List;

/**
 * Created by matt on 2/4/15.
 */
public class CommentGetter extends AsyncTask<Void, Void, List<Comment>> {

    CommentGetterCallback mCallback;
    String mThreadID;

    public CommentGetter(String threadID, CommentGetterCallback callback) {
        mThreadID = threadID;
        mCallback = callback;
    }

    @Override
    protected List<Comment> doInBackground(Void... params) {
        HttpRestClient client = new HttpRestClient();
        client.setUserAgent("android:com.mattkula.redditalive:v0.1 (by /u/kuler51)");
        Comments commentFetcher = new Comments(client);
        final List<Comment> comments = commentFetcher.ofSubmission(mThreadID, null, 0, 3, 10, CommentSort.NEW);
        return comments;
    }

    @Override
    protected void onPostExecute(List<Comment> comments) {
        super.onPostExecute(comments);
        if (mCallback != null) {
            mCallback.onCommentsBeenGot(comments);
        }
    }

    public void setCallback(CommentGetterCallback callback) {
        this.mCallback = callback;
    }

    public interface CommentGetterCallback {
        public void onCommentsBeenGot(List<Comment> comments);
    }
}

