package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "v_appointment_summary")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VAppointmentSummary {

    @Id
    @Column(name = "id")
    private Integer id;

	@Column(name = "date_type_id", nullable = false)
	private LocalDate dateTypeId;

	@Column(name = "hour", nullable = false)
	private LocalTime hour;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride( name = "id", column = @Column(name = "appointment_state_id")),
		@AttributeOverride( name = "description", column = @Column(name = "status_description"))
	})
	private AppointmentStatusSummary status;

	@Column(name = "is_overturn", nullable = false)
	private boolean overturn;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "phone_prefix", length = 10)
	private String phonePrefix;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "id", column = @Column(name = "patient_id")),
			@AttributeOverride( name = "firstName", column = @Column(name = "patient_first_name")),
			@AttributeOverride( name = "lastName", column = @Column(name = "patient_last_name")),
			@AttributeOverride( name = "identificationNumber", column = @Column(name = "patient_identification_number")),
			@AttributeOverride( name = "genderId", column = @Column(name = "patient_gender_id"))
	})
	private PatientInfoSummary patient;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride( name = "licenseNumber", column = @Column(name = "license_number")),
		@AttributeOverride( name = "firstName", column = @Column(name = "doctor_name")),
		@AttributeOverride( name = "lastName", column = @Column(name = "doctor_last_name")),
		@AttributeOverride( name = "identificationNumber", column = @Column(name = "doctor_identification_number"))
	})
	private DoctorInfoSummary doctor;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride( name = "name", column = @Column(name = "medical_coverage_name")),
		@AttributeOverride( name = "cuit", column = @Column(name = "cuit")),
		@AttributeOverride( name = "affiliateNumber", column = @Column(name = "affiliate_number")),
	})
	private MedicalCoverageInfoSummary medicalCoverage;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "sctid", column = @Column(name = "sctid_code")),
			@AttributeOverride( name = "name", column = @Column(name = "clinical_specialty_name")),
	})
	private ClinicalSpecialtySummary clinicalSpecialty;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride( name = "id", column = @Column(name = "institution_id")),
			@AttributeOverride( name = "cuit", column = @Column(name = "institution_cuit")),
	})
	private InstitutionSummary institution;
}
