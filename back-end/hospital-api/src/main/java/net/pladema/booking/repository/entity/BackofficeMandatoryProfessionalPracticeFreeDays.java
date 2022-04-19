package net.pladema.booking.repository.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Entity
@Table(name = "mandatory_professional_practice_free_days")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeMandatoryProfessionalPracticeFreeDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="clinical_specialty_mandatory_medical_practice_id")
    private Integer clinicalSpecialtyMandatoryMedicalPracticeId;

    @Column(name="healthcare_professional_id")
    private Integer healthcareProfessionalId;

    @Column(name="day")
    private Short day;

    public BackofficeMandatoryProfessionalPracticeFreeDays(Integer clinicalSpecialtyMandatoryMedicalPracticeId,
                                                           Integer healthcareProfessionalId,
                                                           Short day){
        this.clinicalSpecialtyMandatoryMedicalPracticeId = clinicalSpecialtyMandatoryMedicalPracticeId;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.day = day;
    }

}
