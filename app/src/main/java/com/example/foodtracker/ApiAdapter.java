package com.example.foodtracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ApiItem> mList;
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setItemOnCLickListener(onItemClickListener listener){
        mListener = listener;
    }

    public ApiAdapter(Context context, ArrayList<ApiItem> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ApiItem currentItem = mList.get(position);

        String imageURL = currentItem.getImageURL();
        String nameOfTheFood = currentItem.getNameOfTheFood();
        String creator = currentItem.getCreator();
        String rank = currentItem.getRank();

        viewHolder.title.setText(nameOfTheFood);
        viewHolder.creator.setText(creator);
        Picasso.get().load(imageURL).fit().centerInside().into(viewHolder.imageView);
        viewHolder.rank.setText(rank);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView title, creator, rank;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.nameTextView);
            creator = itemView.findViewById(R.id.creatorTextView);
            rank = itemView.findViewById(R.id.rank);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                int position = getAdapterPosition();
                                if(position != RecyclerView.NO_POSITION){
                                    mListener.onItemClick(position);
                                }
                            }
                        }
                    }
            );

        }
    }
}
