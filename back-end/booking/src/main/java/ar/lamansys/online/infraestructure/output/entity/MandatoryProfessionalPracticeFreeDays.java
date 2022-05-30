package ar.lamansys.online.infraestructure.output.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name="mandatory_professional_practice_free_days")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MandatoryProfessionalPracticeFreeDays implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="healthcare_professional_id")
    private Integer healthcareProfessionalId;

    @Column(name="clinical_specialty_mandatory_medical_practice_id")
    private Integer clinicalSpecialtyMandatoryMedicalPracticeId;

    @Column(name="day")
    private Short day;

}
