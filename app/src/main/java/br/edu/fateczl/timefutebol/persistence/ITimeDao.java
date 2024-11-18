package br.edu.fateczl.timefutebol.persistence;

import java.sql.SQLException;

public interface ITimeDao {
    TimeDao open() throws SQLException;
    void close();
}
