package com.mattkula.redditalive.listadapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.jreddit.entity.Comment;
import com.mattkula.redditalive.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListAdapter extends BaseAdapter {

    private List<CommentWithDepth> mCommentList;
    private Map<String, CommentWithDepth> mCommentMap;
    private Context mContext;

    public CommentListAdapter(Context c) {
        mContext = c;
        mCommentList = new ArrayList<>();
        mCommentMap = new HashMap<>();
    }

    public void addNewComments(List<Comment> comments) {
        Collections.reverse(comments);
        for (Comment comment : comments) {
            addDepthProperty(comment, 0);
        }
        this.notifyDataSetChanged();
    }

    private void addDepthProperty(Comment comment, int depth) {
        CommentWithDepth c = new CommentWithDepth(comment, depth);
        if (!mCommentMap.keySet().contains(comment.getFullName())) {

            if (mCommentMap.keySet().contains(comment.getParentId())) {
                CommentWithDepth parent = mCommentMap.get(comment.getParentId());
                c.depth = parent.depth + 1;
                mCommentList.add(mCommentList.indexOf(parent) + 1, c);
            } else {
                mCommentList.add(0, c);
            }

            mCommentMap.put(c.comment.getFullName(), c);
        }
        for (Comment reply : comment.getReplies()) {
            addDepthProperty(reply, depth + 1);
        }
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCommentList.get(position).comment.getIdentifier().hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_comment, parent, false);
        }

        CommentWithDepth commentHolder = (CommentWithDepth)getItem(position);
        TextView author = (TextView)view.findViewById(R.id.tv_comment_author);
        TextView body = (TextView)view.findViewById(R.id.tv_comment_body);
        author.setText(commentHolder.comment.getAuthor());
        body.setText(commentHolder.comment.getBody());
        view.setPadding(10*commentHolder.depth, 0, 0, 0);
        return view;
    }

    private class CommentWithDepth {
        public Comment comment;
        public int depth;
        public CommentWithDepth(Comment comment, int depth) {
            this.comment = comment;
            this.depth = depth;
        }
    }
}
