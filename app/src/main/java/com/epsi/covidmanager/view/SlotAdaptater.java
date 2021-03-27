package com.epsi.covidmanager.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SlotAdaptater extends RecyclerView.Adapter<SlotAdaptater.ViewHolder> {

    private ArrayList<Slot> slots;
    private  OnSlotListener onSlotListener;



    public SlotAdaptater(ArrayList<Slot> slots, OnSlotListener onSlotLister) {
        this.slots = slots;
        this.onSlotListener = onSlotLister;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_slot, parent, false);

        return new SlotAdaptater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Slot slot = slots.get(position);

        //holder.tv_slot_date.setText(slot.getDate() + " ");
        //holder.tv_nb_doses.setText(slot.getNb()+"");

        slot.slotDuration();

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSlotListener != null){
                    onSlotListener.onClick(slot);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_slot_date, tv_nb_doses, tv_slot_vaccine;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_slot_date = itemView.findViewById(R.id.tv_slot_date);
            tv_nb_doses = itemView.findViewById(R.id.tv_nb_doses);
            tv_slot_vaccine = itemView.findViewById(R.id.tv_slot_vaccine);
            root = itemView.findViewById(R.id.root);
        }


    }

    public interface OnSlotListener{
        void onClick(Slot slot);

    }
}
