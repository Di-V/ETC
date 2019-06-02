package app.di_v.scorpio.database;

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String NUM = "num";
            public static final String DESCRIPTION = "description";
            public static final String DATE = "date";
        }
    }

    public static final class CrimeMediaTable {
        public static final String NAME = "crime_photos";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PHOTO = "photo";
        }
    }
}