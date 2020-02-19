package pl.tscript3r.tracciato.duration.provider;

import pl.tscript3r.tracciato.location.api.LocationDto;

public final class LocationDto2StringLocation {

    public static String map(LocationDto locationDto) {
        final var results = new StringBuilder();
        if (append(false, false, results, locationDto.getStreet()))
            append(false, true, results, locationDto.getNumber());
        append(true, true, results, locationDto.getZip());
        append(false, true, results, locationDto.getCity());
        if (!append(true, true, results, locationDto.getCountry()))
            results.deleteCharAt(results.length() - 1); // "," at the ending when country is not specified
        return results.toString();
    }

    private static boolean append(boolean comma, boolean space, StringBuilder results, String str) {
        var strValid = (str != null && str.length() > 0);
        if (results.length() > 0) {
            if (comma)
                results.append(",");
            if (space && strValid)
                results.append(" ");
        }
        if (strValid)
            results.append(str);
        return strValid;
    }

}
