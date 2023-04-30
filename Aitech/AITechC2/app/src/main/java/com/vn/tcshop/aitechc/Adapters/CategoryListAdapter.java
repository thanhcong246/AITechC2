package com.vn.tcshop.aitechc.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vn.tcshop.aitechc.Models.Category;
import com.vn.tcshop.aitechc.R;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private final Activity context;
    private final int resource;
    private final List<Category> categoryList;

    public CategoryListAdapter(@NonNull Activity context, int resource, @NonNull List<Category> categoryList) {
        super(context, resource, categoryList);
        this.context = context;
        this.resource = resource;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View row = inflater.inflate(resource, null);

        TextView tvCategoryName = row.findViewById(R.id.textViewCategory);
        TextView tvIdCategoryName = row.findViewById(R.id.textViewIdCategory);

        Category category = categoryList.get(position);

        tvCategoryName.setText(category.getName());
        tvIdCategoryName.setText(String.valueOf(category.getId()));

        return row;
    }
}