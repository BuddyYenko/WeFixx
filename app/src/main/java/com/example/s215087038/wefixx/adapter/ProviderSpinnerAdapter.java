package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.R;

import java.util.List;

/**
 * Created by s215087038 on 2017/07/31.
 */

public class ProviderSpinnerAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private List<ProviderDataObject> listData;
    private Context context;
    public ProviderSpinnerAdapter(Context context, List<ProviderDataObject> listData) {
        this.context = context;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = listData;
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return (ProviderDataObject)listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder spinnerHolder;
        if(convertView == null){
            spinnerHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_list, parent, false);
            spinnerHolder.spinnerItemList = (TextView)convertView.findViewById(R.id.spinner_list_item);
            convertView.setTag(spinnerHolder);
        }else{
            spinnerHolder = (ViewHolder)convertView.getTag();
        }
        spinnerHolder.spinnerItemList.setText(listData.get(position).getName());
        return convertView;
    }
    class ViewHolder{
        TextView spinnerItemList;
    }
}


