package com.example.cinemafunciona;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cinemafunciona.model.Pessoa;

import java.util.ArrayList;

public class AdapterTexto extends ArrayAdapter<Pessoa>
{
    private Context context;

    private ArrayList<Pessoa> pessoas;

    public AdapterTexto(Context context, ArrayList<Pessoa> pessoas) {
        super(context, R.layout.item_lista, pessoas);
        this.context = context;
        this.pessoas = pessoas;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        View itemView = li.inflate(R.layout.item_lista, parent, false);

        TextView lblName = itemView.findViewById(R.id.textoItem);

        lblName.setText(pessoas.get(position).getNome());

        return itemView;
    }

}
