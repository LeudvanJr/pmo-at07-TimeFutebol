package br.edu.fateczl.timefutebol.persistence;

import java.sql.SQLException;

public interface IJogadorDao {
    JogadorDao open()throws SQLException;
    void close();
}
