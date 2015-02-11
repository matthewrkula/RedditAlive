package com.mattkula.redditalive.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mattkula.redditalive.Comment;
import com.mattkula.redditalive.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListAdapter extends BaseAdapter {

    private List<Comment> mCommentList;
    private Map<String, Comment> mCommentMap;
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
        comment.setDepth(depth);
        if (!mCommentMap.keySet().contains(comment.getId())) {

            if (mCommentMap.keySet().contains(comment.getParentId())) {
                Comment parent = mCommentMap.get(comment.getParentId());
                comment.setDepth(parent.getDepth() + 1);
                mCommentList.add(mCommentList.indexOf(parent) + 1, comment);
            } else {
                mCommentList.add(0, comment);
            }

            mCommentMap.put(comment.getId(), comment);
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
        return mCommentList.get(position).getId().hashCode();
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

        Comment comment = (Comment)getItem(position);
        TextView author = (TextView)view.findViewById(R.id.tv_comment_author);
        TextView body = (TextView)view.findViewById(R.id.tv_comment_body);
        author.setText(comment.getAuthor());
        body.setText(comment.getBody());
        view.setPadding(10*comment.getDepth(), 0, 0, 0);
        return view;
    }
}
