package br.edu.fateczl.timefutebol;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.timefutebol.controller.TimeController;
import br.edu.fateczl.timefutebol.dao.TimeDao;
import br.edu.fateczl.timefutebol.model.Time;

public class TimeFragment extends Fragment {

    private View view;
    private EditText etCodigo;
    private EditText etNomeTime;
    private EditText etCidade;
    private TextView tvListarTim;
    private TimeController timeController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time, container, false);

        etCodigo = view.findViewById(R.id.etCodigo);
        etNomeTime = view.findViewById(R.id.etNomeTime);
        etCidade = view.findViewById(R.id.etCidade);
        tvListarTim = view.findViewById(R.id.tvListarTim);
        Button btnEncontrarTim = view.findViewById(R.id.btnEncontrarTim);
        Button btnInserirTim = view.findViewById(R.id.btnInserirTim);
        Button btnAtualizarTim = view.findViewById(R.id.btnAtualizarTim);
        Button btnDeletarTim = view.findViewById(R.id.btnDeletarTim);
        Button btnListarTim = view.findViewById(R.id.btnListarTim);

        TimeDao timeDao = new TimeDao(view.getContext());
        timeController = new TimeController(timeDao);

        btnEncontrarTim.setOnClickListener(e -> encontrarTime());
        btnInserirTim.setOnClickListener(e -> inserirTime());
        btnAtualizarTim.setOnClickListener(e -> atualizarTime());
        btnDeletarTim.setOnClickListener(e -> deletarTime());
        btnListarTim.setOnClickListener(e -> listarTimes());

        return view;
    }

    private void encontrarTime() {
        Time time = null;
        try {
            time = timeController.buscar(montarTime());
            etNomeTime.setText(time.getNome());
            etCidade.setText((time.getCidade()));
        } catch (SQLException e) {
            Log.e("CRUD", "Falha ao encontrar o Time - " + e.getMessage());
        }
    }

    private void inserirTime() {
        try {
            timeController.inserir(montarTime());
            limparCampos();
        } catch (SQLException e) {
            Log.e("CRUD", "Falha ao Inserir o Time - " + e.getMessage());
        }
    }

    private void atualizarTime() {
        try {
            timeController.modificar(montarTime());
            limparCampos();
        } catch (SQLException e) {
            Log.e("CRUD", "Falha ao Modificar o Time - " + e.getMessage());
        }
    }

    private void deletarTime() {
        try {
            timeController.deletar(montarTime());
            limparCampos();
        } catch (SQLException e) {
            Log.e("CRUD", "Falha ao Remover o Time - " + e.getMessage());
        }
    }

    private void listarTimes() {
        try {
            List<Time> times = timeController.listar();
            StringBuffer sb = new StringBuffer();

            for (Time time: times) {
                sb.append(time.toString());
                sb.append("\n");
            }

            tvListarTim.setText(sb.toString());
        } catch (SQLException e) {
            Log.e("CRUD", "Falha ao Listar os Time - " + e.getMessage());
        }
    }

    private Time montarTime(){
        Time time = new Time();
        try {
            time.setCodigo(Integer.parseInt(etCodigo.getText().toString()));
            time.setNome(etNomeTime.getText().toString());
            time.setCidade(etCidade.getText().toString());
        } catch (Exception e){
            Toast.makeText(this.getContext(),
                "Preencha os campos Corretamente!",
                Toast.LENGTH_LONG).show();
        }
        return time;
    }

    private void limparCampos(){
        etCodigo.setText("");
        etNomeTime.setText("");
        etCidade.setText("");
    }

}