package pl.tscript3r.tracciato.route.day;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class TimeRangeEntity extends AbstractEntity {

    @Column(name = "_from")
    private LocalTime from;
    private LocalTime till;

}
