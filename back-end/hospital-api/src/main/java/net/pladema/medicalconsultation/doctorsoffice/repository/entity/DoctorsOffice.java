package net.pladema.medicalconsultation.doctorsoffice.repository.entity;

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
import java.time.LocalTime;

@Entity
@Table(name = "doctors_office")
@Getter
@Setter
@ToString
@EntityListeners(SGXAuditListener.class)
public class DoctorsOffice extends SGXAuditableEntity<Integer> implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "sector_id", nullable = false)
    private Integer sectorId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "topic")
    private String topic;

    @Column(name = "opening_time", columnDefinition = "time default '00:00:00'", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", columnDefinition = "time default '24:00:00'", nullable = false)
    private LocalTime closingTime;

	@Column(name = "status_id", nullable = true)
	private Integer statusId;
}
