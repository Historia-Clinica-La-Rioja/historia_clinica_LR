package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.auditable.entity.AuditableEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 7559172653664261066L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "type_id", nullable = false)
    private Short typeId;

    @Column(name = "possible_duplicate", nullable = false)
    private Boolean possibleDuplicate;

    @Column(name = "national_id")
    private BigInteger nationalId;
}

