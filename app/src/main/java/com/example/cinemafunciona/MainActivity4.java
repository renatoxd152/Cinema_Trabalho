package com.example.cinemafunciona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemafunciona.model.Filme;
import com.example.cinemafunciona.model.Sessao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {

    private Spinner spinner;
    private EditText sala;
    private EditText quantidade;
    private int lastAssignedId;

    private DatabaseReference sessoesRef = FirebaseDatabase.getInstance().getReference("sessoes");


    private DatabaseReference filmesRef = FirebaseDatabase.getInstance().getReference("filmes");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        spinner = findViewById(R.id.filmeSpinner);
        sala = findViewById(R.id.sala);
        quantidade = findViewById(R.id.quantidade);

        carregarFilmesDoFirebase();

    }

    public void primeiraTela(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private List<Filme> carregarFilmesDoFirebase() {
        final List<Filme> listaFilmes = new ArrayList<>();

        filmesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Filme filme = snapshot.getValue(Filme.class);

                    listaFilmes.add(filme);

                }

                atualizarSpinnerAtletas(listaFilmes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                exibirMensagem("Falha ao carregar atletas do Firebase");
            }
        });

        return listaFilmes;
    }

    private void atualizarSpinnerAtletas(List<Filme> listaFilmes) {
        Spinner spinner = findViewById(R.id.filmeSpinner);

        ArrayAdapter<Filme> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaFilmes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }



    public void cadastrarSessao(View v)
    {
        String salaTexto = sala.getText().toString();
        String quantidadeTexto = quantidade.getText().toString();

        Filme filmeSelecionado = (Filme) spinner.getSelectedItem();

        if(filmeSelecionado == null || salaTexto.isEmpty() || quantidadeTexto.isEmpty())
        {
            exibirMensagem("Preencha todos os campos antes de cadastrar a sessão!");
            return;
        }

        int quantidadeInt = Integer.parseInt(quantidadeTexto);
        Sessao sessao = new Sessao(filmeSelecionado.getId(),quantidadeInt,salaTexto);
        inserirSessaoNoFirebase(sessao);
    }


    private void inserirSessaoNoFirebase(Sessao sessao) {
        sessoesRef.orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lastAssignedId = snapshot.child("id").getValue(Integer.class);
                    }
                }

                lastAssignedId++;

                sessao.setId(lastAssignedId);

                DatabaseReference novaSessaoRef = sessoesRef.child(String.valueOf(lastAssignedId));
                novaSessaoRef.setValue(sessao);

                sala.getText().clear();
                quantidade.getText().clear();

                exibirMensagem("Sessão cadastrada com sucesso!");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                exibirMensagem("Erro ao obter o último ID.");
            }
        });
    }

    private void exibirMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }


}