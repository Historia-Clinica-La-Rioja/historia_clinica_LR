package net.pladema.reports.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class AnnexIIOutpatientVo extends AnnexIIVo{

    private Boolean existsConsultation;

    private LocalDate consultationDate;

    private Boolean hasProcedures;

    private String specialty;

    private String problems;

	private String medicalCoverage;
	private String medicalCoverageCuit;

	private Integer rnos;

    public AnnexIIOutpatientVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender, LocalDate patientBirthDate,
                               String documentType, String documentNumber, LocalDate consultationDate, Boolean hasProcedures, String specialty, String sisaCode, String problems){
        super(establishment, firstName, middleNames, lastName, otherLastNames, patientGender, patientBirthDate, documentType, documentNumber, sisaCode);
        this.consultationDate = consultationDate;
        this.hasProcedures = hasProcedures == null ? false : hasProcedures;
        this.existsConsultation = true;
        this.specialty = specialty;
        this.problems = problems;
    }

	public AnnexIIOutpatientVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender, LocalDate patientBirthDate,
							   String documentType, String documentNumber, LocalDate consultationDate, Boolean hasProcedures, String specialty, String sisaCode, String problems,
							   String medicalCoverageName, String medicalCoverageCuit, Integer rnos){
		super(establishment, firstName, middleNames, lastName, otherLastNames, patientGender, patientBirthDate, documentType, documentNumber, sisaCode);
		this.consultationDate = consultationDate;
		this.hasProcedures = hasProcedures == null ? false : hasProcedures;
		this.existsConsultation = true;
		this.specialty = specialty;
		this.problems = problems;
		this.medicalCoverage = medicalCoverageName;
		this.medicalCoverageCuit = medicalCoverageCuit;
		this.rnos = rnos;
	}

    @JsonIgnore
    public Short getAge(){
        if (super.getPatientBirthDate() == null)
            return null;
        Period p = Period.between(super.getPatientBirthDate(), consultationDate);
        return (short) p.getYears();
    }
}
