package net.pladema.booking.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Entity
@Table(name = "mandatory_medical_practice")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeMandatoryMedicalPractice {

    @Id
    @Column(name="mandatory_medical_practice_id")
    private Integer id;

    @Column(name="description")
    private String description;

    @Column(name="mmp_code")
    private String mmpCode;

    @Column(name="snomed_id")
    private Integer snomedId;
}
