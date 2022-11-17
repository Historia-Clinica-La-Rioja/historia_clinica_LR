package net.pladema.medicalconsultation.diary.repository.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "diary")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
public class Diary extends SGXAuditableEntity<Integer> {

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

    @Column(name = "automatic_renewal", nullable = false)
	@ColumnDefault("false")
    private boolean automaticRenewal = false;

    @Column(name = "days_before_renew", columnDefinition = "smallint default 0", nullable = false)
    private Short daysBeforeRenew = 0;

    @Column(name = "professional_asign_shift", nullable = false)
	@ColumnDefault("false")
	private boolean professionalAsignShift = false;

    @Column(name = "include_holiday", nullable = false)
	@ColumnDefault("false")
    private boolean includeHoliday = false;

    @Column(name = "active", nullable = false)
	@ColumnDefault("true")
    private boolean active = true;

	@Column(name = "clinical_specialty_id", nullable = false)
	private Integer clinicalSpecialtyId;

	@Column(name = "alias", length = 100)
	private String alias;

	@Column(name = "protected_appointments_percentage")
	private Short protectedAppointmentsPercentage;
}
