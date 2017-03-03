package com.example.user.sqliteproj;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Product> mDataset;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView adaptId, adaptName, adaptPrice, adaptQt, adaptKind, adaptCost;

        public ViewHolder(View v) {
            super(v);
            adaptId = (TextView) v.findViewById(R.id.tvRecId);
            adaptName = (TextView) v.findViewById(R.id.tvRecName);
            adaptPrice = (TextView) v.findViewById(R.id.tvRecPrice);
            adaptQt = (TextView) v.findViewById(R.id.tvRecQuantity);
            adaptKind = (TextView) v.findViewById(R.id.tvRecKind);
            adaptCost = (TextView) v.findViewById(R.id.tvRecCost);
        }
    }


//  Bag in Product?

    MyAdapter(Context context, List<Product> mDataset) {
        this.mDataset = new ArrayList<>(mDataset);
        this.context = context;

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_layout, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//  Bag in Product?
//        Product product = mDataset.get(position);
        holder.adaptId.setText(String.valueOf(mDataset.get(position).getIdProd()));
        holder.adaptName.setText(mDataset.get(position).getNameProd());
        holder.adaptPrice.setText(String.valueOf(mDataset.get(position).getPriceProd()));
        holder.adaptQt.setText(String.valueOf(mDataset.get(position).getQtProd()));
        holder.adaptKind.setText(String.valueOf(mDataset.get(position).getKindProd()));
        holder.adaptCost.setText(String.valueOf(mDataset.get(position).getCostProd()));
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}


