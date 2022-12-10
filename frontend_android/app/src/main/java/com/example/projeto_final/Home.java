package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

public class Home extends AppCompatActivity {

    private String userID;
    private TextView tvNick, tvNome, tvTelefone, tvUF, tvGeneros;

    private Usuario usuario;
    private String idade;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userID = getIntent().getStringExtra("id");

        tvNick = findViewById(R.id.tv_nick);
        tvNome = findViewById(R.id.tv_nome);
        tvTelefone = findViewById(R.id.tv_telefone);
        tvUF = findViewById(R.id.tv_uf);
        tvGeneros = findViewById(R.id.tv_generos);

        Ion.with(this).load(Server.HOST+"/readID.php?id=" + userID)
                .asJsonObject().setCallback((Exception e, JsonObject json) -> {
                    if (e != null) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    usuario = new Usuario();

                    tvNick.setText(json.get("nick").getAsString());
                    usuario.setNick(json.get("nick").getAsString());

                    tvNome.setText(json.get("nome").getAsString());
                    usuario.setNome(json.get("nome").getAsString());



                    tvTelefone.setText(json.get("telefone").getAsString());
                    usuario.setTelefone(json.get("telefone").getAsString());

                    tvUF.setText(json.get("uf").getAsString());
                    usuario.setUf(json.get("uf").getAsString());

                    tvGeneros.setText(json.get("generos").getAsString().replace(",", " | "));
                    usuario.setGeneros(json.get("generos").getAsString());

                    idade = json.get("idade").getAsString();
                });
    }

    public void gotoInfo(View v) {
        startActivity(new Intent(this, About.class));
    }

    public void remover(View v) {
        Ion.with(this).load(Server.HOST+"/delete.php")
                .setBodyParameter("id", userID)
                .asJsonObject().setCallback((Exception e, JsonObject json) -> {
                    if (e != null) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (json.get("delete").getAsString().equals("ok")) {
                        Intent intent = new Intent(this, Login.class);
                        Toast.makeText(this, "conta removida com sucesso", Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(() -> startActivity(intent), 1000);
                    }
                });
    }

    public void alterar(View v) {
        Intent intent = new Intent(this, CadastroUsuario.class);
        intent.putExtra("id", userID);
        intent.putExtra("nick", usuario.getNick());
        intent.putExtra("nome", usuario.getNome());
        intent.putExtra("telefone", usuario.getTelefone());
        intent.putExtra("generos", usuario.getGeneros());
        intent.putExtra("idade",  idade);
        intent.putExtra("uf",  usuario.getUf());

        startActivity(intent);
    }

    public void sair(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}