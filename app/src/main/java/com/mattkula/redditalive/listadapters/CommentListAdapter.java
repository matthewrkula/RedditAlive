package com.mattkula.redditalive.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.jreddit.entity.Comment;
import com.mattkula.redditalive.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by matt on 2/4/15.
 */
public class CommentListAdapter extends BaseAdapter {

    private List<Comment> mComments;
    private Context mContext;

    public CommentListAdapter(Context c, List<Comment> comments) {
        mContext = c;
        mComments = comments;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
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

        Comment comment = (Comment)getItem(position);
        TextView author = (TextView)view.findViewById(R.id.tv_comment_author);
        TextView body = (TextView)view.findViewById(R.id.tv_comment_body);
        author.setText(comment.getAuthor());
        body.setText(comment.getBody());
        return view;
    }
}
