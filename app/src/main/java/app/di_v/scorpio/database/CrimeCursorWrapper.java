package app.di_v.scorpio.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.Date;
import java.util.UUID;
import app.di_v.scorpio.crime.Crime;
import app.di_v.scorpio.crime.CrimeMedia;

import static  app.di_v.scorpio.database.CrimeDbSchema.*;


public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String description = getString(getColumnIndex(CrimeTable.Cols.DESCRIPTION));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDescription(description);
        crime.setDate(new Date(date));

        return crime;
    }

    public CrimeMedia getCrimePhoto() {
        String uuidString = getString(getColumnIndex(CrimeMediaTable.Cols.UUID));
        String photo = getString(getColumnIndex(CrimeMediaTable.Cols.PHOTO));

        CrimeMedia photos = new CrimeMedia(UUID.fromString(uuidString));
        photos.setPhoto(photo);

        return photos;
    }
}
