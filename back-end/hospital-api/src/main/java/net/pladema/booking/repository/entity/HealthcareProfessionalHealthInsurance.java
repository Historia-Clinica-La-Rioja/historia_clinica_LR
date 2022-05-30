package net.pladema.booking.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Getter
@Entity
@Table(name = "healthcare_professional_health_insurance")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProfessionalHealthInsurance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="healthcare_professional_id")
    private Integer healthcareProfessionalId;

    @Column(name="medical_coverage_id")
    private Integer medicalCoverageId;

}