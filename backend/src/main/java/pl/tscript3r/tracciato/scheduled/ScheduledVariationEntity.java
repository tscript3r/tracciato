package pl.tscript3r.tracciato.scheduled;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;
import pl.tscript3r.tracciato.scheduled.util.EventsConverter;
import pl.tscript3r.tracciato.scheduled.util.UuidListConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
class ScheduledVariationEntity extends AbstractEntity {

    @Lob
    @Convert(converter = UuidListConverter.class)
    @Column(name = "_order")
    private List<UUID> order;

    @Lob
    @Convert(converter = EventsConverter.class)
    private List<Map<String, String>> timeline;

    @Lob
    @Convert(converter = UuidListConverter.class)
    private List<UUID> missedAppointments;

    private Long travelledMeters;
    private Integer missedAppointmentsCount;
    private LocalDateTime endingDate;

}
