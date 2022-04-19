package net.pladema.booking.repository.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@Table(name = "health_insurance_practice")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeHealthInsurancePractice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="clinical_specialty_mandatory_medical_practice_id")
    private Integer clinicalSpecialtyMandatoryMedicalPracticeId;

    @Column(name="medical_coverage_id")
    private Integer medicalCoverageId;

    @Column(name="coverage_information")
    private String coverageInformation;
}

