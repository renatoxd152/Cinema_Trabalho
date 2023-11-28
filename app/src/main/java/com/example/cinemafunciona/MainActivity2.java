package com.example.cinemafunciona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private EditText nome;

    private EditText telefone;
    private EditText cpf;

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nome = findViewById(R.id.texto);
        cpf = findViewById(R.id.cpf);
        email = findViewById(R.id.email);
        telefone = findViewById(R.id.telefone);


    }

    public void primeiraTela(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void inserirDados(View view)
    {
        String nomeTexto = nome.getText().toString();
        String cpfTexto = cpf.getText().toString();
        String emailTexto = email.getText().toString();
        String telefoneTexto = telefone.getText().toString();


        if(nomeTexto.isEmpty())
        {
            Toast.makeText(this, "Digite um nome para a pessoa!", Toast.LENGTH_SHORT).show();
        }
        else if(cpfTexto.isEmpty())
        {
            Toast.makeText(this, "Digite o CPF!", Toast.LENGTH_SHORT).show();
        }
        else if(emailTexto.isEmpty())
        {
            Toast.makeText(this, "Digite o email!", Toast.LENGTH_SHORT).show();
        }
        else if(telefoneTexto.isEmpty())
        {
            Toast.makeText(this, "Digite um telefone!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i = new Intent(MainActivity2.this, MainActivity.class);

            i.putExtra("nome", nomeTexto);
            i.putExtra("cpf",cpfTexto);
            i.putExtra("email",emailTexto);
            i.putExtra("telefone",telefoneTexto);

            setResult(RESULT_OK, i);

            finish();

            nome.getText().clear();
            cpf.getText().clear();
            email.getText().clear();
            telefone.getText().clear();

        }


    }
}