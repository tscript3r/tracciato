package pl.tscript3r.tracciato.route.day;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class DayEntity extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private DayAvailability availability;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TimeRangeEntity> timeRangeList;

}
