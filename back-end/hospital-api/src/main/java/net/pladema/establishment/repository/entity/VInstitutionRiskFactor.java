package net.pladema.establishment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "v_institution_vital_sign")
@Getter
@Setter
@ToString
public class VInstitutionRiskFactor {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "internment_episode_id", nullable = false)
    private Integer internmentEpisodeId;

    @Column(name = "effective_time", nullable = false)
    private LocalDateTime effectiveTime;

    @Column(name = "load_days", nullable = false)
    private Integer loadDays;

    @Column(name = "sctid_code", length = 20, nullable = false)
    private String sctidCode;
}
