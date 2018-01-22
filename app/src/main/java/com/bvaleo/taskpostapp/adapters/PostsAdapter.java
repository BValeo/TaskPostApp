package com.bvaleo.taskpostapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bvaleo.taskpostapp.R;
import com.bvaleo.taskpostapp.model.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<Post> mData;

    public PostsAdapter(List<Post> data){
        this.mData = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView twId;
        public TextView twTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            twId = itemView.findViewById(R.id.tw_id_post);
            twTitle = itemView.findViewById(R.id.tw_title_post);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mData.get(position);

        String id = Integer.toString(post.getId());
        String title = post.getTitle().substring(0, 4) + "...";

        holder.twId.setText(id);
        holder.twTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<Post> data){
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public Post getPost(int id){
        return mData.get(id);
    }
}
