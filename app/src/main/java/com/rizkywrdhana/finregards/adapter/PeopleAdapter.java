package com.rizkywrdhana.finregards.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rizkywrdhana.finregards.R;
import com.rizkywrdhana.finregards.Regards;
import com.rizkywrdhana.finregards.Users;
import com.squareup.picasso.Picasso;

public class PeopleAdapter extends FirestoreRecyclerAdapter<Users, PeopleAdapter.PeopleHolder> {

    private PeopleAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PeopleAdapter(@NonNull FirestoreRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PeopleAdapter.PeopleHolder holder, int i, @NonNull final Users users) {

        holder.fullnameTv.setText(users.getFullname());
        holder.usernameTv.setText(users.getUsername());
        holder.statusTv.setText(users.getStatus());
        holder.accessTv.setText(users.getAccess());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users");
        String documentId = getSnapshots().getSnapshot(i).getId();

        final StorageReference Ref = storageReference.child(users.getUsername()  + "/profile.jpg");
        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                        .fit().centerCrop().into(holder.imgProfile);

            }
        });



    }

    @NonNull
    @Override
    public PeopleAdapter.PeopleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users,parent,false);
        return new PeopleAdapter.PeopleHolder(v);
    }


    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class PeopleHolder extends RecyclerView.ViewHolder{

        TextView fullnameTv, usernameTv, statusTv, accessTv;
        ImageView imgProfile;

        public PeopleHolder(@NonNull View itemView) {
            super(itemView);
            fullnameTv = itemView.findViewById(R.id.r_fullname);
            usernameTv = itemView.findViewById(R.id.r_username);
            statusTv = itemView.findViewById(R.id.r_status);
            accessTv = itemView.findViewById(R.id.r_access);
            imgProfile = itemView.findViewById(R.id.r_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClickListener(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClickListener(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(PeopleAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


}
