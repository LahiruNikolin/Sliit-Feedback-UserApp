package com.example.UserApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.UserApp.Models.Teacher;
import com.example.UserApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Teacher> dataList;

    // handle interface for item listener
    private ListItemClickListener itemClickListener;


    public TeacherAdapter(Context mContext, ArrayList<Teacher> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView teacherImage;
        private TextView teacherName;
        private Button rate_teacher;

        // handle interface for item listener
        private ListItemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, final ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);

            teacherImage = (ImageView) itemView.findViewById(R.id.iVTeacherPic);
            teacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);

            rate_teacher = (Button) itemView.findViewById(R.id.btTeacherRate);

            rate_teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null){
                        itemClickListener.onItemClick(getLayoutPosition(),view);
                    }
                }
            });


        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_teacher, parent, false);
        return new ViewHolder(view, viewType, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = dataList.get(position).getFname() + " " + dataList.get(position).getLname();
        String imageUrl = dataList.get(position).getImage();

        holder.teacherName.setText(name);
        Picasso.get().load(imageUrl).into(holder.teacherImage);
    }


    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ListItemClickListener {
        public void onItemClick(int position, View view);
    }
}
