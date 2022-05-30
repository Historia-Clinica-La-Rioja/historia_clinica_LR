package ar.lamansys.online.infraestructure.output.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Table(name = "clinical_specialty_mandatory_medical_practice")
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalSpecialtyMandatoryMedicalPractice implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "clinical_specialty_id")
    private Integer clinicalSpecialtyId;

    @Column(name = "mandatory_medical_practice_id")
    private Integer mandatoryMedicalPracticeId;

    @Column(name = "practice_recommendations")
    private String practiceRecommendations;

}
