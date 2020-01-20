package pl.tscript3r.tracciato.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Getter
    @Setter
    private UUID uuid;

    public Boolean isNew() {
        return id == null;
    }

    private boolean identityEquals(final AbstractEntity other) {
        if (isNew())
            return false;
        return getId().equals(other.getId());
    }

    private int identityHashCode() {
        return new HashCodeBuilder().append(this.getId()).toHashCode();
    }

    @Override
    public final int hashCode() {
        return identityHashCode();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        return identityEquals((AbstractEntity) o);
    }

}
