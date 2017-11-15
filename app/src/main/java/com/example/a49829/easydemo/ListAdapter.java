package com.example.a49829.easydemo;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luck.easyrecyclerview.adapter.BaseViewHolder;
import com.luck.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

/**
 * Created by 49829 on 2017/10/25.
 */

public class ListAdapter extends RecyclerArrayAdapter<Person> {

    private Context context;
    public ListAdapter(Context context) {
        super(context);
        this.context=context;
    }

    public ListAdapter(Context context, Person[] objects) {
        super(context, objects);
    }

    public ListAdapter(Context context, List<Person> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponViewHolder(parent);
    }

    public class CouponViewHolder extends BaseViewHolder<Person>{

        TextView name,age;

        public CouponViewHolder(ViewGroup parent) {
            super(parent,R.layout.view_rc_item);
            name=$(R.id.name);
            age=$(R.id.age);
        }

        @Override
        public void setData(Person data, int position) {
            Log.d("marj", "setData: "+data.toString());
            Log.d("marj", "setData: "+data.getName());
            Log.d("marj", "setData: "+data.getAge());
            name.setText(data.getName());
            age.setText(String.valueOf(data.getAge()));
        }
    }

    @Override
    public int getPosition(Person item) {
        return super.getPosition(item);
    }
}
