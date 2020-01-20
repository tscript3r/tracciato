package pl.tscript3r.tracciato.route.availability;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class AvailabilityEntity extends AbstractEntity {

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "_from")
    private LocalTime from;

    private LocalTime to;

    @Column(nullable = false)
    private Boolean excluded = false;

}
