package com.siz_kimsiz.inson_psixologiyasi;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;


public class RecyclerViewAdapterquizText extends RecyclerView.Adapter<RecyclerViewAdapterquizText.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public RecyclerViewAdapterquizText(Context context, ArrayList<DataModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;

        public RelativeLayout relativeLayout;
        DataModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);

            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);

        }

        public void setData(DataModel item) {
            this.item = item;

            textView.setText(item.text);



        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {

            }
        }
    }

    @Override
    public RecyclerViewAdapterquizText.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_quiz_text, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}