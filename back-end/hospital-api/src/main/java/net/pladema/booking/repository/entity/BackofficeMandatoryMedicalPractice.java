package net.pladema.booking.repository.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Entity
@Table(name = "mandatory_medical_practice")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeMandatoryMedicalPractice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mandatory_medical_practice_id")
    private Integer id;

    @Column(name="description")
    private String description;

    @Column(name="mmp_code")
    private String mmpCode;

    @Column(name="snomed_id")
    private Integer snomedId;
}
