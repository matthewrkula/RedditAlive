package com.mattkula.redditalive.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.jreddit.entity.Comment;
import com.mattkula.redditalive.R;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends BaseAdapter {

    private List<CommentWithDepth> mCommentList;
    private Context mContext;

    public CommentListAdapter(Context c, List<Comment> comments) {
        mContext = c;
        mCommentList = new ArrayList<>();
        sortComments(comments);
    }

    private void sortComments(List<Comment> comments) {
        for (Comment comment : comments) {
            addDepthProperty(comment, 0);
        }
    }

    private void addDepthProperty(Comment comment, int depth) {
        mCommentList.add(new CommentWithDepth(comment, depth));
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
        return position;
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
        author.setPadding(20 * commentHolder.depth, 0, 0, 0);
        body.setPadding(20 * commentHolder.depth, 0, 0, 0);
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
