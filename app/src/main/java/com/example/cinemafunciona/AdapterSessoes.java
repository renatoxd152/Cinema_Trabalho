package com.example.cinemafunciona;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cinemafunciona.model.Pessoa;
import com.example.cinemafunciona.model.Sessao;

import java.util.ArrayList;

public class AdapterSessoes extends ArrayAdapter<Sessao> {

    private Context context;

    private ArrayList<Sessao> sessoes;

    public AdapterSessoes(Context context, ArrayList<Sessao> sessoes) {
        super(context, R.layout.item_lista2, sessoes);
        this.context = context;
        this.sessoes = sessoes;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        View itemView = li.inflate(R.layout.item_lista2, parent, false);

        TextView lblName = itemView.findViewById(R.id.nome_sessao);

        lblName.setText(sessoes.get(position).getSala());

        return itemView;
    }
}
