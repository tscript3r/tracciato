package pl.tscript3r.tracciato.route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.availability.AvailabilityConst;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.location.LocationEntity;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.stop.StopConst;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Route DAO")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RouteDaoTest {

    RouteDao routeDao;
    RouteDto routeDto;

    @BeforeEach
    void setUp() {
        routeDao = new RouteDao(new ModelMapper(),
                new RouteInMemoryRepositoryAdapter(),
                GlobalFailureResponse.NOT_FOUND);
        routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    void save_Should_ReturnRouteWithScheduledAsFalse_When_Called() {
        // when
        var resultsDto = routeDao.save(routeDto);

        // then
        assertFalse(resultsDto.get().getScheduled());
    }

    @Test
    void save_Should_ReturnRouteWithLastScheduledAsNull_When_Called() {
        // when
        var resultsDto = routeDao.save(routeDto);

        // then
        assertNull(resultsDto.get().getLastSchedule());
    }

    @Test
    void save_Should_ReturnRouteWithLastUpdateAsNull_When_Called() {
        // when
        var resultsDto = routeDao.save(routeDto);

        // then
        assertNull(resultsDto.get().getLastUpdate());
    }

    @Test
    void setAsScheduled_Should_ReturnRouteWithLastScheduleUpdateAndAsScheduled_When_Called() {
        // given
        var savedRoute = routeDao.save(routeDto);

        // when
        var resultRoute = routeDao.setAsScheduled(savedRoute.get().getUuid());

        // then
        assertNotNull(resultRoute.get().getLastSchedule());
    }

    @Test
    void addStop_Should_SetScheduledAsFalse_When_BeforeScheduledWasAsTrue() {
        // given
        routeDto.setScheduled(true);
        routeDto.setLastSchedule(LocalDateTime.now());
        var savedRoute = routeDao.save(routeDto).get();
        assert savedRoute.getScheduled();

        // when
        var results = routeDao.addStop(savedRoute.getUuid(), StopConst.getValidStopEntity()).get();

        // then
        assertFalse(results.getScheduled());
    }

    @Test
    void setStartLocation_Should_SetScheduledAsFalse_When_BeforeScheduledWasAsTrue() {
        // given
        routeDto.setScheduled(true);
        routeDto.setLastSchedule(LocalDateTime.now());
        var savedRoute = routeDao.save(routeDto).get();
        assert savedRoute.getScheduled();
        var locationEntity = new LocationEntity();
        locationEntity.setCity("test");

        // when
        var results = routeDao.setStartLocation(savedRoute.getUuid(), locationEntity).get();

        // then
        assertFalse(results.getScheduled());
    }

    @Test
    void setEndLocation_Should_SetScheduledAsFalse_When_BeforeScheduledWasAsTrue() {
        // given
        routeDto.setScheduled(true);
        routeDto.setLastSchedule(LocalDateTime.now());
        var savedRoute = routeDao.save(routeDto).get();
        assert savedRoute.getScheduled();
        var locationEntity = new LocationEntity();
        locationEntity.setCity("test");

        // when
        var results = routeDao.setEndLocation(savedRoute.getUuid(), locationEntity).get();

        // then
        assertFalse(results.getScheduled());
    }

    @Test
    void addAvailability_Should_SetScheduledAsFalse_When_BeforeScheduledWasAsTrue() {
        // given
        routeDto.setScheduled(true);
        routeDto.setLastSchedule(LocalDateTime.now());
        var savedRoute = routeDao.save(routeDto).get();
        assert savedRoute.getScheduled();
        var locationEntity = AvailabilityConst.getValidAvailabilityEntity();

        // when
        var results = routeDao.addAvailability(savedRoute.getUuid(), locationEntity).get();

        // then
        assertFalse(results.getScheduled());
    }

}