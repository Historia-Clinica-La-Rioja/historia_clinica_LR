package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "care_line")
@Getter
@Setter
@ToString
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
public class CareLine extends SGXAuditableEntity<Integer> implements Serializable {

    private static final long serialVersionUID = -1734756036190169609L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "consultation")
    private Boolean consultation;

    @Column(name = "procedure")
    private Boolean procedure;

}
