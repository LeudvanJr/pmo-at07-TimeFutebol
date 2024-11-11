package br.edu.fateczl.timefutebol.controller;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.timefutebol.dao.JogadorDao;
import br.edu.fateczl.timefutebol.model.Jogador;

public class JogadorController implements IController<Jogador>{
    
    private final JogadorDao jDao;
    
    public JogadorController(JogadorDao jogadorDao){
        this.jDao = jogadorDao;
    }
    
    @Override
    public void inserir(Jogador jogador) throws SQLException {
        if(jDao.open() == null){
            jDao.open();
        }
        jDao.insert(jogador);
        jDao.close();
    }

    @Override
    public void modificar(Jogador jogador) throws SQLException {
        if(jDao.open() == null){
            jDao.open();
        }
        jDao.update(jogador);
        jDao.close();
    }

    @Override
    public void deletar(Jogador jogador) throws SQLException {
        if(jDao.open() == null){
            jDao.open();
        }
        jDao.delete(jogador);
        jDao.close();
    }

    @Override
    public Jogador buscar(Jogador jogador) throws SQLException {
        if(jDao.open() == null){
            jDao.open();
        }
        jogador = jDao.findOne(jogador);
        jDao.close();
        return jogador;
    }

    @Override
    public List<Jogador> listar() throws SQLException {
        if(jDao.open() == null){
            jDao.open();
        }
        List<Jogador> jogadores = jDao.findAll();
        jDao.close();
        return jogadores;
    }
}
