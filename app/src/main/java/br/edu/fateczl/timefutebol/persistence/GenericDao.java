package br.edu.fateczl.timefutebol.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TIMEFUTEBOL.DB";
    private static final int DATABASE_VER = 1;
    private static final String CREATE_TABLE_TIME =
            "CREATE TABLE time ( " +
                    "codigo INT NOT NULL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "cidade VARCHAR(32) NOT NULL" +
            ");";
    private static final String CREATE_TABLE_JOG =
            "CREATE TABLE jogador ( " +
                    "id INT NOT NULL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "data_nasc DATE NOT NULL," +
                    "altura NUMERIC(4,2) NOT NULL," +
                    "peso NUMERIC(5,2) NOT NULL," +
                    "cod_time INT," +
                    "FOREIGN KEY (cod_time) REFERENCES time(codigo)" +
            ");";

    public GenericDao(Context context){
        super(context,DATABASE_NAME, null,DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TIME);
        sqLiteDatabase.execSQL(CREATE_TABLE_JOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int antigaVersao, int novaVersao) {
        if(antigaVersao < novaVersao){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS jogador");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS time");
            this.onCreate(sqLiteDatabase);
        }
    }
}
