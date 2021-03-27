package com.epsi.covidmanager.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vial;

import java.util.ArrayList;

public class SlotAdaptater extends RecyclerView.Adapter<SlotAdaptater.ViewHolder> {

    private ArrayList<Slot> slots;
    private ArrayList<Vial> vials;
    private  OnSlotListener onSlotListener;



    public SlotAdaptater(ArrayList<Slot> slots, ArrayList<Vial> vials, OnSlotListener onSlotLister) {
        this.slots = slots;
        this.onSlotListener = onSlotLister;
        this.vials = vials;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_slot, parent, false);

        return new SlotAdaptater.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Slot slot = slots.get(position);

        Log.w("slot", slot.getStartTime().toString());
        holder.tv_slot_date.setText(slot.getDates());
        holder.tv_nb_doses.setText(Integer.toString(slot.getNbInitialPlaces()));
        holder.tv_doses_restantes.setText(Integer.toString(slot.getNbInitialPlaces() - slot.getNbReservedPlaces()));

        /* for (vial: vials) {
            if (vial.getId() == Slot.getId()){
                holder.tv_vaccin.setText(vial.getVaccin());
                break;
            }
        } */

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

        public TextView tv_slot_date, tv_nb_doses, tv_doses_restantes, tv_vaccin;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_slot_date = itemView.findViewById(R.id.tv_card_vaccine_name);
            tv_nb_doses = itemView.findViewById(R.id.tv_card_vaccine_nb);
            tv_vaccin = itemView.findViewById(R.id.tv_vaccin);
            tv_doses_restantes = itemView.findViewById(R.id.tv_doses_restantes);
            root = itemView.findViewById(R.id.root);
        }


    }

    public interface OnSlotListener{
        void onClick(Slot slot);

    }
}
