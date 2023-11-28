package com.example.cinemafunciona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cinemafunciona.model.Sessao;
import com.example.cinemafunciona.model.Sessao_Pessoa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Filmes_Pessoa extends AppCompatActivity {

    private ListView listaFilmes;
    private ArrayList<String> listaFilmesPessoa = new ArrayList<>();
    private ArrayAdapter<String> adapterFilmes;

    private DatabaseReference sessoesPessoasRef = FirebaseDatabase.getInstance().getReference("sessoes_pessoas");
    private DatabaseReference sessoesRef = FirebaseDatabase.getInstance().getReference("sessoes");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmes_pessoa);


        listaFilmes = findViewById(R.id.listaFilmesPessoa);
        adapterFilmes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaFilmesPessoa);
        listaFilmes.setAdapter(adapterFilmes);


        int idPessoaSelecionada = getIntent().getIntExtra("idPessoa", -1);
        carregarFilmesdaPessoa(idPessoaSelecionada);


    }

    private void carregarFilmesdaPessoa(int idPessoa) {
        listaFilmesPessoa.clear();

        sessoesPessoasRef.orderByChild("id_pessoa").equalTo(idPessoa).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sessao_Pessoa sessaoPessoa = snapshot.getValue(Sessao_Pessoa.class);

                    if (sessaoPessoa != null) {
                        int idSessaoString = sessaoPessoa.getId_sessao();

                        obterIdFilmePorIdSessao(idSessaoString);
                    }
                }
                adapterFilmes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obterIdFilmePorIdSessao(int idSessao) {
        sessoesRef.orderByChild("id").equalTo(idSessao).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    Sessao sessao = sessionSnapshot.getValue(Sessao.class);
                    if (sessao != null) {

                        int idFilme = sessao.getId_filme();


                        obterNomeFilmePorId(idFilme);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void obterNomeFilmePorId(int idFilme) {
        DatabaseReference filmesRef = FirebaseDatabase.getInstance().getReference("filmes");
        filmesRef.orderByChild("id").equalTo(idFilme).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot filmeSnapshot : dataSnapshot.getChildren()) {

                    String nomeFilme = filmeSnapshot.child("nome").getValue(String.class);


                    listaFilmesPessoa.add(nomeFilme);


                    adapterFilmes.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }




    public void primeiraTela(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}