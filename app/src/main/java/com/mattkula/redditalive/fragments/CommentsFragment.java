package com.mattkula.redditalive.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.jreddit.entity.Comment;
import com.mattkula.redditalive.R;
import com.mattkula.redditalive.listadapters.CommentListAdapter;
import com.mattkula.redditalive.networking.CommentGetter;

import java.util.List;

public class CommentsFragment extends Fragment {

    public static final String ARG_THREAD_ID = "thread_id";

    private boolean isActive = false;
    private boolean isFetching = false;
    private final int FETCH_DELAY = 1000 * 10;

    private ListView commentList;
    private ProgressBar progressBar;
    private String threadID;

    private Handler timer;
    private Runnable fetchComments;
    private CommentListAdapter adapter;

    public static CommentsFragment newInstance(String threadID) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle data = new Bundle();
        data.putString(ARG_THREAD_ID, threadID);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommentListAdapter(getActivity());
        threadID = getArguments().getString(ARG_THREAD_ID);
        timer = new Handler();
        fetchComments = new Runnable() {
            @Override
            public void run() {
                if (isFetching || !isActive) return;

                new CommentGetter(threadID, callback).execute();
                progressBar.setVisibility(View.VISIBLE);
                isFetching = true;
                timer.postDelayed(fetchComments, FETCH_DELAY);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, null);
        commentList = (ListView)view.findViewById(R.id.list_comments);
        commentList.setAdapter(adapter);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        timer.post(fetchComments);
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    CommentGetter.CommentGetterCallback callback = new CommentGetter.CommentGetterCallback() {
        @Override
        public void onCommentsBeenGot(List<Comment> comments) {
            isFetching = false;
            progressBar.setVisibility(View.GONE);
            if (isActive) {
                adapter.addNewComments(comments);
                Log.v(CommentsFragment.class.getName(), comments.get(0).getBody());
            }
        }
    };
}
