package pl.tscript3r.tracciato.infrastructure;

public final class DateTimeFormats {

    public final static String TIME_FORMAT = "kk:mm";
    public final static String DATE_FORMAT = "dd.MM.yyyy";
    public final static String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    public final static String DATE_TIME_FORMAT_WITH_SEC = DATE_TIME_FORMAT + ":ss";

    private DateTimeFormats() {
    }

}
