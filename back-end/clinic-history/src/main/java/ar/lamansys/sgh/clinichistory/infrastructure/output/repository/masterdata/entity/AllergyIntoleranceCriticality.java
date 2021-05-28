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
@Table(name = "allergy_intolerance_criticality")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyIntoleranceCriticality implements Serializable {

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "display", nullable = false, length = 20)
    private String display;
}
