package br.edu.fateczl.timefutebol.dao;

import java.sql.SQLException;

public interface IJogadorDao {
    JogadorDao open()throws SQLException;
    void close();
}
