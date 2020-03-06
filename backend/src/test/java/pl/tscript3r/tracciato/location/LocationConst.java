package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.Arrays;
import java.util.List;

public final class LocationConst {

    public static final String LOCATION_CITY = "Berlin";
    public static final String EXISTING_LOCATION_CITY = "Warsaw";

    public static LocationDto getValidLocationDto() {
        var results = new LocationDto();
        results.setCity(LOCATION_CITY);
        return results;
    }

    public static LocationDto getBerlinLocationDto() {
        var results = new LocationDto();
        results.setCity("Berlin");
        results.setStreet("Mollstrasse");
        results.setNumber("50");
        results.setZip("10178");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getHamburgLocationDto() {
        var results = new LocationDto();
        results.setCity("Hamburg");
        results.setStreet("Ballindamm");
        results.setNumber("25");
        results.setZip("20095");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getEssenLocationDto() {
        var results = new LocationDto();
        results.setCity("Essen");
        results.setStreet("Rollandstrasse");
        results.setNumber("25");
        results.setZip("45128");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getStuttgartLocationDto() {
        var results = new LocationDto();
        results.setCity("Stuttgart");
        results.setStreet("Hegelstrasse");
        results.setNumber("50");
        results.setZip("70174");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getStartLocationDto() {
        var results = new LocationDto();
        results.setCity("Bielany Wroclawskie");
        results.setStreet("Logistyczna");
        results.setNumber("1");
        results.setZip("52-007");
        results.setCountry("Poland");
        return results;
    }

    public static LocationDto getEndLocationDto() {
        var results = new LocationDto();
        results.setCity("Bielany Wroclawskie");
        results.setStreet("Logistyczna");
        results.setNumber("1");
        results.setZip("52-007");
        results.setCountry("Poland");
        return results;
    }

    public static LocationDto getLuneburgLocationDto() {
        var results = new LocationDto();
        results.setCity("Luneburg");
        results.setStreet("Volgerstrasse");
        results.setNumber("5");
        results.setZip("21335");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getGetyngaLocationDto() {
        var results = new LocationDto();
        results.setCity("Getynga");
        results.setStreet("Humboldtallee");
        results.setNumber("5");
        results.setZip("37073");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getBremaLocationDto() {
        var results = new LocationDto();
        results.setCity("Brema");
        results.setStreet("Hansator");
        results.setNumber("6");
        results.setZip("28217");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getBrunszwikLocationDto() {
        var results = new LocationDto();
        results.setCity("Braunschweig");
        results.setStreet("Waggumer Weg");
        results.setNumber("5");
        results.setZip("38108");
        results.setCountry("Germany");
        return results;
    }

    public static LocationDto getWarsawLocationDto() {
        var results = new LocationDto();
        results.setCity("Warsaw");
        results.setStreet("Piekna");
        results.setNumber("2");
        results.setZip("05-075");
        results.setCountry("Poland");
        return results;
    }

    public static List<LocationDto> getLocationsList() {
        return Arrays.asList(getBerlinLocationDto(), getBremaLocationDto(), getBrunszwikLocationDto());
    }

}
