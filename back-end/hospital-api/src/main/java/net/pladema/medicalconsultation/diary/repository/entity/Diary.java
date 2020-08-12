package net.pladema.medicalconsultation.diary.repository.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.DeleteableEntity;
import net.pladema.sgx.auditable.entity.Deleteable;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

@Entity
@Table(name = "diary")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
public class Diary extends SGXAuditableEntity implements DeleteableEntity<Integer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7771492167471325392L;

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "healthcare_professional_id", nullable = false)
    private Integer healthcareProfessionalId;

    @Column(name = "doctors_office_id", nullable = false)
    private Integer doctorsOfficeId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "appointment_duration", nullable = false)
    private Short appointmentDuration;

    @Column(name = "automatic_renewal", columnDefinition = "boolean default false", nullable = false)
    private boolean automaticRenewal = false;

    @Column(name = "days_before_renew", columnDefinition = "smallint default 0", nullable = false)
    private Short daysBeforeRenew = 0;

    @Column(name = "professional_asign_shift", columnDefinition = "boolean default false", nullable = false)
    private boolean professionalAsignShift = false;

    @Column(name = "include_holiday", columnDefinition = "boolean default false", nullable = false)
    private boolean includeHoliday = false;

    @Column(name = "active", columnDefinition = "boolean default true", nullable = false)
    private boolean active = true;
    
	@Embedded
	private Deleteable deleteable = new Deleteable();

	@Override
	public Integer getDeleteBy() {
		if (deleteable != null)
			return deleteable.getDeletedBy();
		return null;
	}

	@Override
	public void setDeleteBy(Integer user) {
		if (deleteable == null)
			deleteable = new Deleteable();
		deleteable.setDeletedBy(user);
		
	}

	@Override
	public LocalDateTime getDeletedOn() {
		if (deleteable != null)
			return deleteable.getDeletedOn();
		return null;
	}

	@Override
	public void setDeletedOn(LocalDateTime dateTime) {
		if (deleteable == null)
			deleteable = new Deleteable();
		deleteable.setDeletedOn(dateTime);
		
	}

	@Override
	public boolean isDeleted() {
		if (deleteable != null)
			return deleteable.isDeleted();
		return false;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		if (deleteable == null)
			deleteable = new Deleteable();
		deleteable.setDeleted(deleted);
	}

}
