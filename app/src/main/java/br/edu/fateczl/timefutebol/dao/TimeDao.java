package br.edu.fateczl.timefutebol.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.timefutebol.model.Time;

public class TimeDao implements ICRUDDao<Time>, ITimeDao {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public TimeDao(Context context){
        this.context = context;
    }

    @Override
    public TimeDao open() throws SQLException {
        gDao = new GenericDao(this.context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Time time) throws SQLException {
        ContentValues contentValues = getContentValues(time);
        database.insert("time",null,contentValues);
    }

    @Override
    public int update(Time time) throws SQLException {
        ContentValues contentValues = getContentValues(time);
        return database.update("time", contentValues,
                "codigo="+time.getCodigo(),null);
    }

    @Override
    public void delete(Time time) throws SQLException {
        database.delete("time",
                "codigo="+time.getCodigo(),null);
    }

    @SuppressLint("Range")
    @Override
    public Time findOne(Time time) throws SQLException {
        String querry = "SELECT * FROM time WHERE codigo="+time.getCodigo();
        Cursor cursor = database.rawQuery(querry, null);
        if(cursor != null)
            cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            time.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
        }
        cursor.close();
        return time;
    }

    @SuppressLint("Range")
    @Override
    public List<Time> findAll() throws SQLException {
        List<Time> times = new ArrayList<>();
        String query = "SELECT * FROM time";
        Cursor cursor = database.rawQuery(query, null);

        if(cursor != null)
            cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));

            times.add(time);
            cursor.moveToNext();
        }
        cursor.close();
        return times;
    }

    private ContentValues getContentValues(Time time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("codigo", time.getCodigo());
        contentValues.put("nome", time.getNome());
        contentValues.put("cidade", time.getCidade());
        return contentValues;
    }
}
