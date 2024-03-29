package app.di_v.scorpio.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import  app.di_v.scorpio.database.CrimeDbSchema.*;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + ", " +
                CrimeTable.Cols.TITLE + ", " +
                CrimeTable.Cols.NUM + ", " +
                CrimeTable.Cols.DESCRIPTION + ", " +
                CrimeTable.Cols.DATE +
                ")"
        );

        db.execSQL("create table " + CrimeMediaTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeMediaTable.Cols.UUID + ", " +
                CrimeMediaTable.Cols.PHOTO +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}