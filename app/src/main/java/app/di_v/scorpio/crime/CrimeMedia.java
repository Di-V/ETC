package app.di_v.scorpio.crime;

import java.util.UUID;

public class CrimeMedia {
    private UUID mId;
    private String mFileName;

    public CrimeMedia(UUID id) {
        mId = id;
        mFileName = "IMG_" + getId().toString() + "&" + UUID.randomUUID().toString() + ".jpg";
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getFile() {
        return mFileName;
    }

    public void setFile(String file) {
        mFileName = file;
    }
}
