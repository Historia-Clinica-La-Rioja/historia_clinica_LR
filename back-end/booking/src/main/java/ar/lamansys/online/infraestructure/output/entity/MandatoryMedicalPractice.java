package ar.lamansys.online.infraestructure.output.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mandatory_medical_practice")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MandatoryMedicalPractice implements Serializable {
    @Id
    @Column(name = "mandatory_medical_practice_id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "mmp_code")
    private String mmpCode;

    @Column(name = "snomed_id")
    private Integer snomedId;

}

