package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Post post = mPost.get(position);

        //To display growth
        if (post.getGrowth() != null) {
            holder.growth.setVisibility(View.VISIBLE);
            holder.growth_holder.setVisibility(View.VISIBLE);
            holder.growth.setText(post.getGrowth());
        } else {
            holder.growth_holder.setVisibility(View.GONE);
        }

        //to display tag
        if (post.getTag() != null) {
            holder.tag_holder.setVisibility(View.VISIBLE);
            holder.tag.setVisibility(View.VISIBLE);
            holder.tag.setText(post.getTag());
        } else {
            holder.tag_holder.setVisibility(View.GONE);
            holder.tag.setVisibility(View.GONE);
        }

        //set post image
        if (post.getPostImages() == null) {
            holder.post_image.setVisibility(View.GONE);
        } else {
            holder.post_image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(post.getPostImages()).override(400, 400).fitCenter().into(holder.post_image);
        }

        //set description
        if (post.getDescription() == null) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }

        //change userid(in post) to username(in database)
        publisherInfo(post.getPublisher(), holder.publisher);
        holder.publish_time.setText(post.getTime());

        //get like status and numbers
        /*
        isLiked(post.getPostid(),holder.like);
        nrLikes(holder.likes, post.getPostid());
        */


        //update like number
        /*
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });*/



    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image, like, comment;
        public TextView likes, publisher, description, comments, growth, tag, publish_time;
        public CardView tag_holder;
        public LinearLayout growth_holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);

            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);
            growth = itemView.findViewById(R.id.growth);
            tag = itemView.findViewById(R.id.tag);
            publish_time = itemView.findViewById(R.id.time);

            tag_holder = itemView.findViewById(R.id.tag_holder);
            growth_holder = itemView.findViewById(R.id.growth_holder);

        }

    }

    private void isLiked(String postid, ImageView imageView) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrLikes(TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() + "likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void publisherInfo(final String userid, final TextView publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
