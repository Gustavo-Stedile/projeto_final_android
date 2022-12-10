package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

public class Login extends AppCompatActivity {

    private EditText etNick, etSenha;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNick = findViewById(R.id.et_nick);
        etSenha = findViewById(R.id.et_senha);


    }

    public void login(View v) {
        String nick = etNick.getText().toString();
        String senha = etSenha.getText().toString();


        if (nick.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "ambos campos devem ser preenchidos", Toast.LENGTH_LONG).show();
            return;
        }

        if (nick.equals("rovilson") && senha.equals("liberalismo")) {
            startActivity(new Intent(this, AdmPage.class));
            return;
        }


        Ion.with(this).load(Server.HOST+"/login.php")
                .setBodyParameter("nick", nick).setBodyParameter("senha", senha)
                .asJsonObject().setCallback((Exception e, JsonObject json) -> {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    boolean logged = json.get("logged").getAsBoolean();
                    if (!logged) {
                        Toast.makeText(this, json.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String id = json.get("id").getAsString();

                    Intent intent = new Intent(this, Home.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                });
    }

    public void gotoCadastro(View v) {
        startActivity(new Intent(this, CadastroUsuario.class));

    }
}