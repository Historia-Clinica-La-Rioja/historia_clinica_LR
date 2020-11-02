package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "medical_coverage")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MedicalCoverage implements Serializable {
    /*
     */
    private static final long serialVersionUID = 2873716268832417941L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    public MedicalCoverage(String name){
        this.name = name;
    }
}
