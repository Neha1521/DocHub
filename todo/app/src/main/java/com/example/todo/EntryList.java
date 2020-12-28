package com.example.todo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntryList extends RecyclerView.Adapter<EntryList.MyViewHolder> {

    private List<todoEntry> list;
    private Context context;
    private DatabaseHelper db;

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,date,time;
        Button del;
        CheckBox check;
        MyViewHolder(View v){
            super(v);
            name=v.findViewById(R.id.enName);
            date=v.findViewById(R.id.enDate);
            time=v.findViewById(R.id.enTime);
            check=v.findViewById(R.id.check);
            del=v.findViewById(R.id.del);
        }

    }
    EntryList(List<todoEntry> list,Context context) {
        this.list=list;
        this.context=context;
        db=new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final todoEntry entry=list.get(position);
        holder.name.setText(entry.getName());
        holder.date.setText(entry.getDate());
        holder.time.setText(entry.getTime());
        if(entry.getCheck()==1)
            holder.check.setChecked(true);
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.check(entry.getId(),entry.getCheck());
                if(entry.getCheck()==1)
                    holder.check.setChecked(true);
                notifyItemChanged(position);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(entry.getId());
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
