package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personal_history")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonalHistory {

    @Id
    @Column(name = "health_condition_id")
    private Integer healthConditionId;

    @Column(name = "type_id", nullable = false)
    private Short typeId;
}
