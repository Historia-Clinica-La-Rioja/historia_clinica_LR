package net.pladema.reports.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.reports.infrastructure.input.AnnexIIProfessionalDto;

@Getter
@Setter
@ToString
public class AnnexIIDto {

    private String establishment;

    private String completePatientName;

	private String formalPatientName;

    private String documentType;

    private String documentNumber;

    private String patientGender;

    private Short patientAge;

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;

    private Boolean existsConsultation;

    private Boolean hasProcedures;

    private String specialty;

    private LocalDateTime consultationDate;

    private String sisaCode;

    private String problems;

	private Integer rnos;

	private List<AnnexIIProcedureDto> procedures;

	private LocalDateTime proceduresIngressDate;

	private LocalDateTime proceduresEgressDate;

	private Float proceduresTotal;

	private Boolean showProcedures;

	private Integer missingProcedures;

	private Short patientIdentityAccreditationStatusId;

	private AnnexIIProfessionalDto professional;

}
