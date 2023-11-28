package com.example.cinemafunciona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cinemafunciona.model.Filme;
import com.example.cinemafunciona.model.Pessoa;
import com.example.cinemafunciona.model.Sessao;
import com.example.cinemafunciona.model.Sessao_Pessoa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity5 extends AppCompatActivity {
    private int lastAssignedId;
    private Spinner sessao;
    private Spinner pessoa;
    private DatabaseReference sessoesRef = FirebaseDatabase.getInstance().getReference("sessoes");


    private DatabaseReference pessoasRef = FirebaseDatabase.getInstance().getReference("pessoas");

    private DatabaseReference sessoes_pessoas = FirebaseDatabase.getInstance().getReference("sessoes_pessoas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        sessao = findViewById(R.id.sessaoSpinnerIngresso);
        pessoa = findViewById(R.id.pessoaIngresso);

        carregarSessoesDoFirebase();
        carregarPessoasDoFirebase();
    }

    public void primeiraTela(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void mostrarSessoes(View v)
    {
        Intent intent = new Intent(this, mostrarSessoes.class);
        startActivity(intent);
    }



    private List<Sessao> carregarSessoesDoFirebase() {
        final List<Sessao> listaSessoes = new ArrayList<>();

        sessoesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sessao sessao = snapshot.getValue(Sessao.class);
                    listaSessoes.add(sessao);

                }

                atualizarSpinnerSessao(listaSessoes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                exibirMensagem("Falha ao carregar atletas do Firebase");
            }
        });

        return listaSessoes;
    }

    private void atualizarSpinnerSessao(List<Sessao> listaSessao) {
        Spinner spinner = findViewById(R.id.sessaoSpinnerIngresso);

        ArrayAdapter<Sessao> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaSessao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    private List<Pessoa> carregarPessoasDoFirebase() {
        final List<Pessoa> listaPessoas = new ArrayList<>();

        pessoasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pessoa pessoa = snapshot.getValue(Pessoa.class);
                    listaPessoas.add(pessoa);

                }

                atualizarSpinnerPessoa(listaPessoas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                exibirMensagem("Falha ao carregar atletas do Firebase");
            }
        });

        return listaPessoas;
    }

    private void atualizarSpinnerPessoa(List<Pessoa> listaPessoas) {
        Spinner spinner = findViewById(R.id.pessoaIngresso);

        ArrayAdapter<Pessoa> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaPessoas );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    public void cadastrarIngresso(View v)
    {

        Pessoa pessoaSelecionada = (Pessoa) pessoa.getSelectedItem();

        Sessao sessaoSelecionada = (Sessao) sessao.getSelectedItem();


        if(pessoaSelecionada == null || sessaoSelecionada == null)
        {
            exibirMensagem("Preencha todos os campos antes de cadastrar o ingresso!");
            return;
        }
        Sessao_Pessoa sessaoPessoa = new Sessao_Pessoa(pessoaSelecionada.getId(),sessaoSelecionada.getId());
        inserirIngressoNoFirebase(sessaoPessoa);
    }


    private void inserirIngressoNoFirebase(Sessao_Pessoa sessaoPessoa) {
        sessoes_pessoas.orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lastAssignedId = snapshot.child("id").getValue(Integer.class);
                    }
                }

                lastAssignedId++;

                sessaoPessoa.setId(lastAssignedId);

                DatabaseReference novaIngressoRef = sessoes_pessoas.child(String.valueOf(lastAssignedId));
                novaIngressoRef.setValue(sessaoPessoa);


                exibirMensagem("Ingresso cadastrado com sucesso!");


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