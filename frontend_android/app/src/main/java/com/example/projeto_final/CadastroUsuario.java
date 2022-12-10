package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroUsuario extends AppCompatActivity {

    private TextView tvTitleForm;
    private EditText etNome, etNick, etSenha, etConfirmarSenha, etTelefone, etIdade;
    private CheckBox cbMasc, cbFem, cbNaoBin, cbGenFluido;
    private Spinner spnUf;

    private String userID = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        tvTitleForm = findViewById(R.id.tv_titulo_form);

        etNome = findViewById(R.id.et_nome);
        etNick = findViewById(R.id.et_nick);
        etSenha = findViewById(R.id.et_senha);
        etConfirmarSenha = findViewById(R.id.et_confimar_senha);
        etTelefone = findViewById(R.id.et_telefone);
        etIdade = findViewById(R.id.et_idade);

        cbMasc = findViewById(R.id.cb_masc);
        cbFem = findViewById(R.id.cb_fem);
        cbNaoBin = findViewById(R.id.cb_nao_bin);
        cbGenFluido = findViewById(R.id.cb_gen_fluido);

        spnUf = findViewById(R.id.spn_uf);

        if (getIntent().getStringExtra("id") != null) {
            etNome.setText(getIntent().getStringExtra("nome"));
            etNick.setText(getIntent().getStringExtra("nick"));
            etNick.setEnabled(false);
            etTelefone.setText(getIntent().getStringExtra("telefone"));
            etIdade.setText(getIntent().getStringExtra("idade"));

            List<String> generos = Arrays.asList(getIntent().getStringExtra("generos").split(","));
            if (generos.contains("masculino")) cbMasc.setChecked(true);
            if (generos.contains("feminino")) cbFem.setChecked(true);
            if (generos.contains("não binárie")) cbNaoBin.setChecked(true);
            if (generos.contains("gênero flúido")) cbGenFluido.setChecked(true);
            tvTitleForm.setText("alterar");
        }
    }

    public void gotoLogin(View v) {
        Intent intent = new Intent(this, getIntent().getStringExtra("id") == null ? Login.class : Home.class);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
    }

    public void cadastrar(View v) {
        String nick = etNick.getText().toString();
        if (nick.isEmpty()) {
            Toast.makeText(this, "nick deve ser preenchido", Toast.LENGTH_LONG).show();
            return;
        }

        String nome = etNome.getText().toString();
        if (nome.isEmpty()) {
            Toast.makeText(this, "nome deve ser preenchido", Toast.LENGTH_LONG).show();
            return;
        }

        String telefone = etTelefone.getText().toString();
        if (telefone.isEmpty()) {
            Toast.makeText(this, "telefone deve ser preenchido", Toast.LENGTH_LONG).show();
            return;
        }

        String idade = etIdade.getText().toString();
        if (idade.isEmpty()) {
            Toast.makeText(this, "idade deve ser preenchida", Toast.LENGTH_LONG).show();
            return;
        }

        String senha = etSenha.getText().toString();
        if (senha.isEmpty()) {
            Toast.makeText(this, "senha deve ser preenchida", Toast.LENGTH_LONG).show();
            return;
        }

        String regex = "^(?=(.*[0-9]){2,})(?=(.*[!@#$%^&*()\\-__+.]){1,}).{8,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(senha);
        if (!matcher.matches())  {
            Toast.makeText(this, "senha deve conter 8 caracteres; caracter especial e números", Toast.LENGTH_SHORT).show();
            return;
        }


        String confirmarSenha = etConfirmarSenha.getText().toString();
        if (confirmarSenha.isEmpty()) {
            Toast.makeText(this, "confirmação de senha deve ser preenchida", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(idade) < 18) {
            Toast.makeText(this, "usuário deve ser maior de idade", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "senhas não conferem", Toast.LENGTH_LONG).show();
            return;
        }

        String generos = "";
        if (cbMasc.isChecked()) generos += "masculino,";
        if (cbFem.isChecked()) generos += "feminino,";
        if (cbNaoBin.isChecked()) generos += "não binárie,";
        if (cbGenFluido.isChecked()) generos += "gênero flúido,";
        if (generos.length() > 0) generos = generos.substring(0, generos.length()-1);

        generos = generos.trim();
        if (generos.isEmpty()) {
            Toast.makeText(this, "selecione pelo menos um gênero", Toast.LENGTH_LONG).show();
            return;
        }

        if (spnUf.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "selecione uma UF", Toast.LENGTH_LONG).show();
            return;
        }

        String uf = spnUf.getSelectedItem().toString();

        if (getIntent().getStringExtra("id") != null) {
            String id = getIntent().getStringExtra("id");
            Ion.with(this).load(Server.HOST + "/update.php")
                    .setBodyParameter("id", id)
                    .setBodyParameter("nick", nick).setBodyParameter("senha", senha)
                    .setBodyParameter("uf", uf).setBodyParameter("telefone", telefone)
                    .setBodyParameter("idade", idade).setBodyParameter("generos", generos)
                    .setBodyParameter("nome", nome)
                    .asJsonObject().setCallback((Exception e, JsonObject json) -> {
                        if (e != null) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (json.get("update").getAsString().equals("error")) {
                            Toast.makeText(this, json.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent intent = new Intent(this, Home.class);
                        intent.putExtra("id", id);
                        startActivity(intent);

                    });
            return;
        }

        Ion.with(this).load(Server.HOST + "/create.php")
                .setBodyParameter("nick", nick).setBodyParameter("senha", senha)
                .setBodyParameter("uf", uf).setBodyParameter("telefone", telefone)
                .setBodyParameter("idade", idade).setBodyParameter("uf", uf)
                .setBodyParameter("generos", generos).setBodyParameter("nome", nome)
                .asJsonObject().setCallback((Exception e, JsonObject json) -> {
                    if (e != null) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (json.get("create").getAsString().equals("error")) {
                        Toast.makeText(this, json.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(this, "usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(() -> startActivity(new Intent(this, CadastroSucesso.class)), 1000);
                });
        }
    }
