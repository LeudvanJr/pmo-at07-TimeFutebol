/*
 *@author:Leudvan Guedes
 */

package br.edu.fateczl.timefutebol;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.edu.fateczl.timefutebol.controller.JogadorController;
import br.edu.fateczl.timefutebol.controller.TimeController;
import br.edu.fateczl.timefutebol.model.Jogador;
import br.edu.fateczl.timefutebol.model.Time;
import br.edu.fateczl.timefutebol.persistence.JogadorDao;
import br.edu.fateczl.timefutebol.persistence.TimeDao;

public class JogadorFragment extends Fragment {

    private View view;
    private JogadorController jController;
    private Spinner spTime;

    private TextView tvListJog;
    private EditText etID;
    private EditText etDataNasc;
    private EditText etNomeJogador;
    private EditText etAltura;
    private EditText etPeso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_jogador, container, false);

        tvListJog = view.findViewById(R.id.tvListarJog);
        etID = view.findViewById(R.id.etID);
        etDataNasc = view.findViewById(R.id.etDataNasc);
        etNomeJogador = view.findViewById(R.id.etNomeJogador);
        etAltura = view.findViewById(R.id.etAltura);
        etPeso = view.findViewById(R.id.etPeso);
        spTime = view.findViewById(R.id.spTime);

        Button btnInserirJog = view.findViewById(R.id.btnInserirJog);
        Button btnAtualizarJog = view.findViewById(R.id.btnAtualizarJog);
        Button btnEncontrarJog = view.findViewById(R.id.btnEncontrarJog);
        Button btnDeletarJog = view.findViewById(R.id.btnDeletarJog);
        Button btnListarJog = view.findViewById(R.id.btnListarJog);

        JogadorDao jDao = new JogadorDao(view.getContext());
        jController = new JogadorController(jDao);
        preencherSpinner();

        btnInserirJog.setOnClickListener(e -> inserirJogador());
        btnAtualizarJog.setOnClickListener(e -> atualizarJogador());
        btnEncontrarJog.setOnClickListener(e -> encontrarJogador());
        btnDeletarJog.setOnClickListener(e -> deletarJogador());
        btnListarJog.setOnClickListener(e -> listarJogadores());

        return view;
    }


    private void inserirJogador() {
        try {
            jController.inserir(montarJogador());
            limparCampos();
        } catch (SQLException e) {
            toaster("Falha ao inserir o Jogador!");
        }
    }

    private void atualizarJogador() {
        try {
            jController.modificar(montarJogador());
            limparCampos();
        } catch (SQLException e) {
            toaster("Falha ao atualizar o Jogador");
        }
    }

    private void deletarJogador() {
        try {
            jController.deletar(montarJogador());
            limparCampos();
        } catch (SQLException e) {
            toaster("Falha ao remover o Jogador");
        }
    }

    private void encontrarJogador() {
        try {
            Jogador jogador = jController.buscar(montarJogador());
            if(jogador.getNome() == null){
                toaster("Jogador nao encontrado");
                return;
            }

            etNomeJogador.setText(jogador.getNome());
            etDataNasc.setText(jogador.getDataNasc().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")).toString());
            etAltura.setText(String.valueOf(jogador.getAltura()));
            etPeso.setText(String.valueOf(jogador.getPeso()));
            spTime.setSelection(jogador.getTime().getCodigo()-1,true);
        } catch (SQLException e) {
            toaster("Falha ao encontrar o Jogador");
        }
    }

    private void listarJogadores() {
        try {
            List<Jogador> listaJogador = jController.listar();
            StringBuffer sb = new StringBuffer();
            for (Jogador t:listaJogador) {
                sb.append(t.toString()+"\n");
            }
            tvListJog.setText(sb.toString());
        } catch (SQLException e) {
            toaster("Falha ao lista os Jogadores");
        }
    }

    private void preencherSpinner() {
        TimeController timeController = new TimeController(new TimeDao(this.getContext()));

        try {
            List<Time> listaTimes = timeController.listar();
            ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter(
                    this.getContext(),
                    android.R.layout.simple_spinner_item,
                    listaTimes);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTime.setAdapter(arrayAdapter);
        }catch (SQLException e){
            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private Jogador montarJogador(){
        Jogador jogador = new Jogador();
        try {
            jogador.setId(Integer.parseInt(etID.getText().toString()));
            jogador.setNome(etNomeJogador.getText().toString());
            jogador.setDataNasc(
                    LocalDate.parse(etDataNasc.getText().toString(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
            jogador.setAltura(Float.parseFloat(etAltura.getText().toString()));
            jogador.setPeso(Float.parseFloat(etPeso.getText().toString()));
            jogador.setTime((Time) spTime.getSelectedItem());
        } catch (Exception e){
            Log.i("Campos Incompletos", "montarJogador: "+e.getMessage());
        }

        return jogador;
    }

    private void toaster(String m){
        Toast.makeText(this.getContext(),m,Toast.LENGTH_LONG).show();
    }

    private void limparCampos(){
        etID.setText("");
        etNomeJogador.setText("");
        etDataNasc.setText("");
        etPeso.setText("");
        etAltura.setText("");
        spTime.setSelection(0);
    }
}
