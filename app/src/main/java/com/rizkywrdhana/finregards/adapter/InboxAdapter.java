package com.rizkywrdhana.finregards.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rizkywrdhana.finregards.Inbox;
import com.rizkywrdhana.finregards.R;
import com.rizkywrdhana.finregards.Regards;

public class InboxAdapter extends FirestoreRecyclerAdapter<Inbox, InboxAdapter.InboxHolder> {

    private InboxAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InboxAdapter(@NonNull FirestoreRecyclerOptions<Inbox> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final InboxAdapter.InboxHolder holder, int i, @NonNull final Inbox inbox) {

        holder.namaTv.setText(inbox.getName());
        holder.isiTv.setText(inbox.getMessage());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("inbox");
        String documentId = getSnapshots().getSnapshot(i).getId();

//        final StorageReference Ref = storageReference.child(regards.getName()  + "/Profile.jpg");
//        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
//                        .fit().centerCrop().into(holder.imgProfile);
//
//            }
//        });



    }

    @NonNull
    @Override
    public InboxAdapter.InboxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox,parent,false);
        return new InboxAdapter.InboxHolder(v);
    }


    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class InboxHolder extends RecyclerView.ViewHolder{

        TextView namaTv, isiTv;
        ImageView imgProfile;

        public InboxHolder(@NonNull View itemView) {
            super(itemView);
            namaTv = itemView.findViewById(R.id.r_username);
            isiTv = itemView.findViewById(R.id.r_msg);
//            imgProfile = itemView.findViewById(R.id.profileImageView);

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

    public void setOnClickListener(InboxAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
