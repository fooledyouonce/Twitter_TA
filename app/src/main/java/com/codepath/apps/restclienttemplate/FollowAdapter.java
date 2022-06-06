package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ItemProfileBinding;
import com.codepath.apps.restclienttemplate.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    Context context;
    List<User> userList;

    public FollowAdapter(Context activity, List<User> users) {
        context = activity;
        userList = users;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemProfileBinding binding = ItemProfileBinding.inflate(LayoutInflater.from(context), parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) { holder.bind(userList.get(position)); }

    @Override
    public int getItemCount() { return userList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemProfileBinding binding;
        public ViewHolder(@NonNull @NotNull ItemProfileBinding Binding) {
            super(Binding.getRoot());
            binding = Binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(User user) {
            binding.tvUserNameFollow.setText(user.name);
            binding.tvScreenFollow.setText("@" + user.screenName);
            Glide.with(context).load(user.profileImageUrl)
                    .transform(new RoundedCorners(90))
                    .into(binding.ivProfileFollow);
        }
    }
}
