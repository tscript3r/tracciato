package pl.tscript3r.tracciato.scheduled;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ScheduledResultsEntity extends AbstractEntity {

    private UUID requestUuid;
    private UUID routeUuid;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @OneToOne(cascade = CascadeType.ALL)
    private ScheduledVariationEntity tuned;

    @OneToOne(cascade = CascadeType.ALL)
    private ScheduledVariationEntity optimal;

}
