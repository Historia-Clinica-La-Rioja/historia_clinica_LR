package ar.lamansys.online.infraestructure.output.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "health_insurance_practice")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthInsurancePractice implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="clinical_specialty_mandatory_medical_practice_id")
    private Integer clinicalSpecialtyMandatoryMedicalPracticeId;

    @Column(name="medical_coverage_id")
    private Integer medicalCoverageId;

    @Column(name="coverage_information")
    private String coverageInformation;

}