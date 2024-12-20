package br.edu.fateczl.timefutebol.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.timefutebol.model.Jogador;
import br.edu.fateczl.timefutebol.model.Time;

public class JogadorDao implements ICRUDDao<Jogador>, IJogadorDao {

    private GenericDao gDao;
    private final Context context;
    private SQLiteDatabase database;

    public JogadorDao(Context context){
        this.context = context;
    }

    @Override
    public JogadorDao open() throws SQLException {
        gDao = new GenericDao(this.context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        database.insert("jogador",null,contentValues);
    }

    @Override
    public int update(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        return database.update("jogador",contentValues,
                "id = " + jogador.getId(), null);
    }

    @Override
    public void delete(Jogador jogador) throws SQLException {
        database.delete("jogador",
                "id = " + jogador.getId(),null);
    }

    @SuppressLint("Range")
    @Override
    public Jogador findOne(Jogador jogador) throws SQLException {
        String query = "SELECT j.id AS id, j.nome AS nome, date(j.data_nasc) AS data_nasc," +
                "j.altura AS altura, j.peso AS peso, t.codigo AS codigoTime, " +
                "t.nome AS nomeTime, t.cidade AS cidadeTime " +
                "FROM jogador j, time t " +
                "WHERE j.cod_time = t.codigo " +
                "AND j.id=" + jogador.getId();
        Cursor cursor = database.rawQuery(query, null);
        jogador = new Jogador();

        if(cursor != null)
            cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoTime")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nomeTime")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cidadeTime")));

            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("data_nasc"))
            ));
            jogador.setAltura(cursor.getFloat(cursor.getColumnIndex("altura")));
            jogador.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));
            jogador.setTime(time);
        }
        cursor.close();
        return jogador;
    }

    @SuppressLint("Range")
    @Override
    public List<Jogador> findAll() throws SQLException {
        List<Jogador> jogadores = new ArrayList<>();
        String query = "SELECT j.id AS id, j.nome AS nome, date(j.data_nasc) AS data_nasc," +
                "j.altura AS altura, j.peso AS peso, t.codigo AS codigoTime, " +
                "t.nome AS nomeTime, t.cidade AS cidadeTime " +
                "FROM jogador j " +
                "INNER JOIN time t " +
                "ON j.cod_time = t.codigo";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor != null)
            cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoTime")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nomeTime")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cidadeTime")));

            Jogador jogador = new Jogador();
            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("data_nasc"))
            ));
            jogador.setAltura(cursor.getFloat(cursor.getColumnIndex("altura")));
            jogador.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));
            jogador.setTime(time);
            jogadores.add(jogador);
            cursor.moveToNext();
        }
        cursor.close();
        return jogadores;
    }

    private ContentValues getContentValues(Jogador jogador){
        ContentValues contentValues = new ContentValues();

        contentValues.put("id",jogador.getId());
        contentValues.put("nome",jogador.getNome());
        contentValues.put("data_nasc",jogador.getDataNasc().toString());
        contentValues.put("altura",jogador.getAltura());
        contentValues.put("peso",jogador.getPeso());
        contentValues.put("cod_time",jogador.getTime().getCodigo());

        return contentValues;
    }
}
