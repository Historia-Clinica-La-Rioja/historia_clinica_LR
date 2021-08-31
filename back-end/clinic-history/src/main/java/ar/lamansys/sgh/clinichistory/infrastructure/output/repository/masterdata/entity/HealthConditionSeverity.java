package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "health_condition_severity")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthConditionSeverity implements Serializable {

    @Id
    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "display", nullable = false, length = 10)
    private String display;
}
