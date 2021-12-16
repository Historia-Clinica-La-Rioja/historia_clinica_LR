package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "care_line_clinical_specialty")
@Getter
@Setter
@ToString
@EntityListeners(SGXAuditListener.class)
public class ClinicalSpecialtyCareLine extends SGXAuditableEntity<Integer> implements Serializable {

    private static final long serialVersionUID = -1832876231321092835L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "care_line_id", nullable = false)
    private Integer careLineId;

    @Column(name = "clinical_specialty_id", nullable = false)
    private Integer clinicalSpecialtyId;

    @Override
    public Integer getId() {
        return this.id;
    }
    
}
