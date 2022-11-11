package net.pladema.reports.repository.entity;

import java.time.LocalDate;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnexIIAppointmentVo extends AnnexIIVo{

    private String appointmentState;

    private LocalDate attentionDate;

    private String medicalCoverage;

    private String affiliateNumber;

	private Integer rnos;

    public AnnexIIAppointmentVo(String establishment, String firstName, String middleNames, String lastName, String otherLastNames, String patientGender, LocalDate patientBirthDate,
                                String documentType, String documentNumber, String appointmentState, LocalDate attentionDate, String medicalCoverage, String affiliateNumber, String sisaCode, Integer rnos){
        super(establishment, firstName, middleNames, lastName, otherLastNames, patientGender, patientBirthDate, documentType, documentNumber, sisaCode);
        this.appointmentState = appointmentState;
        this.attentionDate = attentionDate;
        this.medicalCoverage = medicalCoverage;
        this.affiliateNumber = affiliateNumber;
		this.rnos = rnos;
    }

    @JsonIgnore
    public Short getAge(){
        if (super.getPatientBirthDate() == null)
            return null;
        Period p = Period.between(super.getPatientBirthDate(), attentionDate);
        return (short) p.getYears();
    }
}
