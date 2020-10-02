package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "patient_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientType implements Serializable {

	public static final Short PERMANENT = 1;
    public static final Short VALIDATED = 2;
    public static final Short TEMPORARY = 3;
    public static final Short PERMANENT_NOT_VALIDATED = 7;

    /*
     */
    private static final long serialVersionUID = -2509033551090730514L;
    

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", length = 10, nullable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "audit", nullable = false)
    private Boolean audit;
}
