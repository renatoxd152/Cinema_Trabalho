package com.example.cinemafunciona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cinemafunciona.model.Pessoa;
import com.example.cinemafunciona.model.Sessao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mostrarSessoes extends AppCompatActivity {

    private ListView listView;
    private AdapterSessoes adapterSessoes;
    private ArrayList<Sessao> listaSessoes = new ArrayList<>();
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_sessoes);

        listView = findViewById(R.id.sessoesLista);
        adapterSessoes = new AdapterSessoes(this,listaSessoes);
        listView.setAdapter(adapterSessoes);

        EscutadorLista el = new EscutadorLista();
        listView.setOnItemClickListener( el );
        listView.setOnItemLongClickListener( el );

        carregarDadosDoFirebase();
    }


    private void carregarDadosDoFirebase() {
        DatabaseReference sessoesRef = BD.child("sessoes");

        sessoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaSessoes.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Sessao sessao = snapshot.getValue(Sessao.class);

                    listaSessoes.add(sessao);
                }
                adapterSessoes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Trata o erro
                Toast.makeText(mostrarSessoes.this, "Falha ao carregar dados do Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void telaAnterior(View v)
    {
        Intent intent = new Intent(this, MainActivity5.class);
        startActivity(intent);
    }




    private class EscutadorLista implements AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int idSessao = listaSessoes.get(i).getId();


            Intent intent = new Intent(mostrarSessoes.this, Filmes_Sala.class);

            intent.putExtra("idSessao", idSessao);
            startActivity(intent);
        }


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    }


}