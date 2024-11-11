package br.edu.fateczl.timefutebol.dao;

import java.sql.SQLException;

public interface ITimeDao {
    TimeDao open() throws SQLException;
    void close();
}
