package app.di_v.scorpio.crime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.di_v.scorpio.database.CrimeBaseHelper;
import app.di_v.scorpio.database.CrimeCursorWrapper;
import app.di_v.scorpio.database.CrimeDbSchema.CrimeMediaTable;
import app.di_v.scorpio.database.CrimeDbSchema.CrimeTable;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(Crime c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
        mDatabase.delete(CrimeMediaTable.NAME,
                CrimeMediaTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DESCRIPTION, crime.getDescription());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());

        return values;
    }

    /**
     * Media File
     */
    public void addMedia(CrimeMedia media) {
        ContentValues values = getContentMediaValues(media);
        mDatabase.insert(CrimeMediaTable.NAME, null, values);
    }

    public File getMediaFile(CrimeMedia media) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, media.getPhoto());
    }

    public List<CrimeMedia> getMedia(UUID id) {
        List<CrimeMedia> mediaList = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimeMedia(CrimeMediaTable.Cols.UUID + " = ?",
                new String[]{id.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mediaList.add(cursor.getCrimePhoto());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mediaList;
    }

    public void updateMediaCrime(CrimeMedia media) {
        String uuidString = media.getId().toString();
        ContentValues values = getContentMediaValues(media);
        mDatabase.update(CrimeMediaTable.NAME, values,
                CrimeMediaTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }



    private CrimeCursorWrapper queryCrimeMedia(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeMediaTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getContentMediaValues(CrimeMedia media) {
        ContentValues values = new ContentValues();
        values.put(CrimeMediaTable.Cols.UUID, media.getId().toString());
        values.put(CrimeMediaTable.Cols.PHOTO, media.getPhoto());

        return values;
    }
}
