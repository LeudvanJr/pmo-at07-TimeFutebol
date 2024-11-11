package br.edu.fateczl.timefutebol.controller;

import androidx.annotation.NonNull;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.timefutebol.dao.TimeDao;
import br.edu.fateczl.timefutebol.model.Time;

public class TimeController implements IController<Time> {

    private final TimeDao tDao;

    public TimeController(TimeDao timeDao){
        this.tDao = timeDao;
    }

    @Override
    public void inserir(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        tDao.insert(time);
        tDao.close();
    }

    @Override
    public void modificar(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        tDao.update(time);
        tDao.close();
    }

    @Override
    public void deletar(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        tDao.delete(time);
        tDao.close();
    }

    @Override
    public Time buscar(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        time = tDao.findOne(time);
        tDao.close();
        return time;
    }

    @Override
    public List<Time> listar() throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        List<Time> times = tDao.findAll();
        tDao.close();
        return times;
    }
}
