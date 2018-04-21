package com.waypoints1.Faq;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waypoints1.R;

import java.util.List;

public class FaqAdpter extends RecyclerView.Adapter<FaqAdpter.MyViewHolder> {
    List<DatumFaques> data;
    Context context;

    public FaqAdpter(List<DatumFaques> data, Context context) {
        this.data = data;
        this.context = context;

    }

    private void updateItem(int pos, DatumFaques item) {
        data.set(pos, item);
        notifyItemChanged(pos);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listof_faqs, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.faqsqustion.setText(data.get(position).getQuestion());
        holder.faqanswer.setText(data.get(position).getAnswer());
        holder.imdrop.setImageResource(R.drawable.ic_down);

        // holder.imdropup.setImageResource(R.drawable.dropup);

       final DatumFaques item=data.get(position);
        holder.position = position;
        holder.faqsqustion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int   statusst=item.getId();
                final int status=statusst;
                Log.d("adpaterstatus", String.valueOf(status));
                if(status==item.getId()){
                    //   updateItem(position,item);
                    holder.faqanswer.setVisibility(View.VISIBLE);
                    holder.imdrop.setRotation(180);
                    holder.faqsqustion.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
//
                          if(status==item.getId() && holder.faqanswer.getVisibility()==View.VISIBLE){
                    holder.faqanswer.setVisibility(View.GONE);
                    holder.imdrop.setRotation(0);
                              notifyItemChanged(position);
                }
                       }
                   });



               }else{
                }
//                if(status==1 && holder.faqanswer.getVisibility()==View.VISIBLE){
//                    holder.faqanswer.setVisibility(View.GONE);
//                    holder.imdrop.setRotation(0);
//                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView faqsqustion, faqanswer;
        ImageView imdrop, imdropup;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);


            faqsqustion = (TextView) itemView.findViewById(R.id.faqsqustion);
            faqanswer = (TextView) itemView.findViewById(R.id.faqsanswer);

            imdrop = (ImageView) itemView.findViewById(R.id.drpdown);
            // imdropup=(ImageView)itemView.findViewById(R.id.drpup);
//            imdropup.setVisibility(View.GONE);



//              imdropup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   if(imdropup.getVisibility()==View.VISIBLE){
//                       imdropup.setVisibility(View.GONE);
//                       imdrop.setVisibility(View.VISIBLE);
//                       faqanswer.setVisibility(View.GONE);
//                   }
//                }
//            });
        }
    }
}