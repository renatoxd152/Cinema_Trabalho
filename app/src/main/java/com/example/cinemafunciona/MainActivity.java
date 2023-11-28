package com.example.cinemafunciona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cinemafunciona.model.Pessoa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listaTextos;
    private ArrayList<Pessoa> lista = new ArrayList<>();
    private AdapterTexto adapterTexto;
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference pessoasRef;
    private int lastAssignedId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaTextos = findViewById(R.id.listaTextos);
        adapterTexto = new AdapterTexto(this,lista);
        listaTextos.setAdapter(adapterTexto);

        carregarDadosDoFirebase();

        EscutadorLista el = new EscutadorLista();
        listaTextos.setOnItemClickListener( el );
        listaTextos.setOnItemLongClickListener( el );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.itemCadastrarPessoa)
        {
            segundaTela(item.getActionView());
            return true;
        }

        if(id == R.id.itemCadastrarFilme)
        {
            terceiraTela(item.getActionView());
            return true;
        }

        if(id == R.id.itemCadastrarSessao)
        {
            sessaoTela(item.getActionView());
            return true;
        }

        if(id == R.id.itemCadastrarIngresso)
        {
            ingressoTela(item.getActionView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void segundaTela(View v)
    {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {

            String nome = data.getStringExtra("nome");
            String cpf = data.getStringExtra("cpf");
            String email = data.getStringExtra("email");
            String telefone = data.getStringExtra("telefone");

            Pessoa pessoa = new Pessoa(nome,cpf,email,telefone);
            inserirTextoNoBanco(pessoa);
            lista.add(pessoa);
            adapterTexto.notifyDataSetChanged();


        }

    }

    private void carregarDadosDoFirebase() {
        DatabaseReference pessoasRef = BD.child("pessoas");

        pessoasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lista.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Pessoa pessoa = snapshot.getValue(Pessoa.class);


                    lista.add(pessoa);
                }
                adapterTexto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Trata o erro
                Toast.makeText(MainActivity.this, "Falha ao carregar dados do Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class EscutadorLista implements AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int idPessoaSelecionada = lista.get(i).getId();


            Intent intent = new Intent(MainActivity.this, Filmes_Pessoa.class);

            intent.putExtra("idPessoa", idPessoaSelecionada);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            final int id = lista.get(i).getId();


            DatabaseReference pessoaRef = BD.child("pessoas").child(String.valueOf(id));
            pessoaRef.removeValue();

            DatabaseReference sessoesRef = BD.child("sessoes_pessoas");
            sessoesRef.orderByChild("id_pessoa").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            lista.remove(i);
            adapterTexto.notifyDataSetChanged();

            Toast.makeText(MainActivity.this, "Essa pessoa foi excluída junto com suas sessões!", Toast.LENGTH_LONG).show();
            return true;
        }



    }


    public void inserirTextoNoBanco(final Pessoa pessoa) {

        final DatabaseReference pessoasRef = BD.child("pessoas");

        pessoasRef.orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lastAssignedId = snapshot.child("id").getValue(Integer.class);
                    }
                }


                lastAssignedId++;

                pessoa.setId(lastAssignedId);


                DatabaseReference novaPessoaRef = pessoasRef.child(String.valueOf(lastAssignedId));
                novaPessoaRef.setValue(pessoa);

                Toast.makeText(MainActivity.this, "Pessoa adicionada com sucesso!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "Erro ao obter o último ID.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void terceiraTela(View v)
    {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    public void sessaoTela(View v)
    {
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }

    public void ingressoTela(View v)
    {
        Intent intent = new Intent(this, MainActivity5.class);
        startActivity(intent);
    }

}