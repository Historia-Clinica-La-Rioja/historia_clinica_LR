package net.pladema.booking.repository.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@Table(name = "clinical_specialty_mandatory_medical_practice")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeClinicalSpecialtyMandatoryMedicalPractice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="clinical_specialty_id")
    private Integer clinicalSpecialtyId;

    @Column(name="practice_recommendations")
    private String practiceRecommendations;

    @Column(name="mandatory_medical_practice_id")
    private Integer mandatoryMedicalPracticeId;

}