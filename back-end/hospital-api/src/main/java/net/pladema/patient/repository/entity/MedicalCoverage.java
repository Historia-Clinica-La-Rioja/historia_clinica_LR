package net.pladema.patient.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "medical_coverage")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(SGXAuditListener.class)
public class MedicalCoverage extends SGXAuditableEntity<Integer> implements Serializable {
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

    @Getter
    @Setter
    @Column(name = "cuit", length = 20)
    private String cuit;

	@Getter
	@Setter
	@Column(name = "type")
	private Short type;

    public MedicalCoverage(String name, String cuit, Short type){
        this.name = name;
        this.cuit = cuit;
		this.type = type;
    }
}
