package pl.tscript3r.tracciato.scheduled;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;
import pl.tscript3r.tracciato.scheduled.util.StringListConverter;
import pl.tscript3r.tracciato.stop.StopEntity;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ScheduledVariationEntity extends AbstractEntity {

    @OneToMany(cascade = CascadeType.ALL)
    private List<StopEntity> order;

    @Convert(converter = StringListConverter.class)
    private List<String> timeline;

    @OneToMany(cascade = CascadeType.ALL)
    private List<StopEntity> missedAppointments;

    private Long travelledMeters;
    private Integer missedAppointmentsCount;
    private LocalDateTime endingDate;

}
