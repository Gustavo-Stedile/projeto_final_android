package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class AdmPage extends AppCompatActivity {


    private ArrayList<Usuario> usuarioArray;
    private ArrayAdapter<Usuario> usuarioArrayAdapter;
    private ListView lvUsuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_page);
        lvUsuario = findViewById(R.id.lv_usuarios);

        usuarioArray = new ArrayList<>();
        usuarioArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, usuarioArray);
        lvUsuario.setAdapter(usuarioArrayAdapter);

        listaUsuario();
        lvUsuario.setOnItemClickListener((AdapterView<?> adapterView, View v, int i, long l) -> {
            Usuario usuario = usuarioArray.get(i);
            Intent intent = new Intent(this, Home.class);
            intent.putExtra("id", "" + usuario.getId());
            startActivity(intent);
        });

    }

    public void gotoInfo(View v) {
        startActivity(new Intent(this, About.class));
    }

    public void gotoLogin(View v) {
        startActivity(new Intent(this, Login.class));
    }

    private void listaUsuario() {


        Ion.with(this).load(Server.HOST+"/read.php")
                .asJsonArray().setCallback((Exception e, JsonArray jsonArray) -> {
                    if (e != null) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject json = jsonArray.get(i).getAsJsonObject();

                        Usuario usuario = new Usuario();
                        usuario.setId(json.get("id").getAsInt());
                        usuario.setNick(json.get("nick").getAsString());
                        usuarioArray.add(usuario);
                    }

                    usuarioArrayAdapter.notifyDataSetChanged();
                });
    }
}