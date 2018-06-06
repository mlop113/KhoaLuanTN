package com.android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.API.APIFunction;
import com.android.Interface.IOnClickCategory;
import com.android.Models.Category;
import com.android.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    List<Category> listCategory = new ArrayList<>();
    IOnClickCategory iOnClickCategory;
    DatabaseReference databaseReference;
    APIFunction apiFunction;

    public CategoryAdapter(Context context, List<Category> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        apiFunction = new APIFunction();
    }

    public void setiOnClickCategory(IOnClickCategory iOnClickCategory){
        this.iOnClickCategory=iOnClickCategory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category category = listCategory.get(position);
        Glide.with(context)
                .load(apiFunction.getUrlImage(category.getImage()))
                .into(holder.imageViewCategory);
        holder.textViewCategory.setText(category.getName());
        holder.textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickCategory.onClickCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory==null?0:listCategory.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<Category> listData)
    {
        listCategory.clear();
        listCategory.addAll(listData);
        notifyDataSetChanged();
    }


    static  class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ImageView imageViewCategory;
        TextView textViewCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageViewCategory =  itemView.findViewById(R.id.imageViewCategory);
            textViewCategory =  itemView.findViewById(R.id.textViewCategory);

        }
    }
}
