package com.epsi.covidmanager.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epsi.covidmanager.R;
import com.epsi.covidmanager.model.beans.Slot;
import com.epsi.covidmanager.model.beans.Vaccine;
import com.epsi.covidmanager.model.beans.Vial;

import java.util.ArrayList;

public class VaccineAdaptater extends RecyclerView.Adapter<VaccineAdaptater.ViewHolder> {

    private ArrayList<Vaccine> vaccines;
    private VaccineAdaptater.OnVaccineListener onVaccineListener;
    private ArrayList<Vial> vials;



    public VaccineAdaptater(ArrayList<Vaccine> vaccines, ArrayList<Vial> vials, VaccineAdaptater.OnVaccineListener onVaccineListenerLister) {
        this.vaccines = vaccines;
        this.onVaccineListener = onVaccineListenerLister;
        this.vials = vials;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_card, parent, false);

        return new VaccineAdaptater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Vaccine vaccine = vaccines.get(position);

        holder.tv_card_vaccine_name.setText(vaccine.getName());
        holder.tv_card_vaccine_nb.setText(quantityRemainToAllow(vaccine) + "");
        holder.tv_card_vaccine_nb_prev.setText(quantityAllTime(vaccine) + "");


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onVaccineListener != null){
                    onVaccineListener.onClick(vaccine);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_card_vaccine_nb, tv_card_vaccine_name, tv_card_vaccine_nb_prev;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_card_vaccine_name = itemView.findViewById(R.id.tv_card_vaccine_name);
            tv_card_vaccine_nb = itemView.findViewById(R.id.tv_card_vaccine_nb);
            tv_card_vaccine_nb_prev = itemView.findViewById(R.id.tv_vaccin);
            root = itemView.findViewById(R.id.root);
        }


    }

    public interface OnVaccineListener{
        void onClick(Vaccine vaccine);

    }

    public int quantityAllTime(Vaccine vaccine){
        int nb = 0;
        int i = 0;
        for(Vial vial : vials){
            if(vial.getVaccine().getId().equals(vaccine.getId())){
                nb += vial.getShotNumber();
                i++;
            }
        }
        Log.d("I", Integer.toString(i));
        return nb;
    }

    public int quantityRemainToAllow(Vaccine vaccine){
        int nb = 0;
        for(Vial vial : vials){
            if(vial.getVaccine().getId().equals(vaccine.getId()) && vial.getSlot() == null){
                nb += vial.getShotNumber();
            }
        }
        return nb;
    }
}
