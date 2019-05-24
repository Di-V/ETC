package app.di_v.scorpio.crime;

import java.util.UUID;

public class CrimeMedia {
    private UUID mId;
    private String mPhoto;

    public CrimeMedia(UUID id) {
        mId = id;
        mPhoto = "IMG_" + getId().toString() + "&" + UUID.randomUUID().toString() + ".jpg";
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }
}
