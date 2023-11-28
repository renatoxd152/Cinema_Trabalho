package com.example.cinemafunciona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemafunciona.model.Filme;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

   private EditText nome;
   private EditText duracao;

    private DatabaseReference filmesRef = FirebaseDatabase.getInstance().getReference("filmes");
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        nome = findViewById(R.id.nome);
        duracao = findViewById(R.id.duracao);
    }

    public void primeiraTela(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cadastrarFilme(View v) {

        String nomeTexto = nome.getText().toString();
        String duracaoTexto = duracao.getText().toString();

        if(nomeTexto.isEmpty())
        {
            exibirMensagem("Preencha o campo nome!");
        }
        else if(duracaoTexto.isEmpty())
        {
            exibirMensagem("Preencha o duração!");
        }
        else if((nomeTexto.isEmpty()) && (duracaoTexto.isEmpty()))
        {
            exibirMensagem("Preencha os campos corretamente!");
        }
        else
        {
            filmesRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Obtém o último ID cadastrado
                        id = Integer.parseInt(snapshot.getKey());
                    }

                    // Incrementa o ID para obter o próximo
                    id++;

                    // Restante do seu código para cadastrar o filme

                    int duracaoInt = Integer.parseInt(duracaoTexto);


                    Filme filme = new Filme(id, nomeTexto, duracaoInt);
                    inserirFilmeNoFirebase(filme);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    exibirMensagem("Erro ao obter o último ID. Tente novamente.");
                }
            });
        }
    }



    private void inserirFilmeNoFirebase(Filme filme) {
        filmesRef.child(String.valueOf(filme.getId())).setValue(filme)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        exibirMensagem("Filme cadastrado com sucesso!");
                        nome.getText().clear();
                        duracao.getText().clear();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        exibirMensagem("Erro ao cadastrar o filme. Tente novamente.");
                    }
                });
    }

    private void exibirMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

}