package com.rizkywrdhana.finregards.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rizkywrdhana.finregards.R;
import com.rizkywrdhana.finregards.Regards;
import com.squareup.picasso.Picasso;

public class RegardsAdapter extends  FirestoreRecyclerAdapter<Regards, RegardsAdapter.RegardsHolder>{

    private RegardsAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RegardsAdapter(@NonNull FirestoreRecyclerOptions<Regards> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final RegardsAdapter.RegardsHolder holder, int i, @NonNull final Regards regards) {

        holder.namaTv.setText(regards.getName());
        holder.isiTv.setText(regards.getMessage());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("regards");
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
    public RegardsAdapter.RegardsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_regards,parent,false);
        return new RegardsAdapter.RegardsHolder(v);
    }


    @Override
    public int getItemCount(){
        return getSnapshots().size();
    }

    class RegardsHolder extends RecyclerView.ViewHolder{

        TextView namaTv, isiTv;
        ImageView imgProfile;

        public RegardsHolder(@NonNull View itemView) {
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

    public void setOnClickListener(RegardsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


}
