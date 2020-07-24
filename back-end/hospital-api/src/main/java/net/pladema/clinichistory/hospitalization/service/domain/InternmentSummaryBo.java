package net.pladema.clinichistory.hospitalization.service.domain;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentSummaryBo {

    private Integer id;

    private DocumentsSummaryBo documents;

    private net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo doctor;

    private Integer bedId;

    private String bedNumber;

    private Integer roomId;

    private String roomNumber;
    
    private String sectorDescription;
    
    private String sectorSpecialty;

    private Integer clinicalSpecialtyId;

    private String specialty;

    private LocalDate entryDate;

    private int totalInternmentDays;

    private ResponsibleContactBo responsibleContact;

    private LocalDateTime probableDischargeDate;

    public InternmentSummaryBo(InternmentSummaryVo internmentSummaryVo) {
        this.id = internmentSummaryVo.getId();
        this.documents = new DocumentsSummaryBo(internmentSummaryVo.getDocuments());
        this.bedId = internmentSummaryVo.getBedId();
        this.bedNumber = internmentSummaryVo.getBedNumber();
        this.roomId = internmentSummaryVo.getRoomId();
        this.roomNumber = internmentSummaryVo.getRoomNumber();
        this.sectorDescription = internmentSummaryVo.getSectorDescription();
        this.sectorSpecialty = internmentSummaryVo.getSectorSpecialty();
        this.clinicalSpecialtyId = internmentSummaryVo.getClinicalSpecialtyId();
        this.specialty = internmentSummaryVo.getSpecialty();
        this.entryDate = internmentSummaryVo.getEntryDate();
        this.totalInternmentDays = totalInternmentDays();
        if (internmentSummaryVo.getDoctor() != null)
            this.doctor = new ResponsibleDoctorBo(internmentSummaryVo.getDoctor());
        if (internmentSummaryVo.getResponsibleContact() != null)
            this.responsibleContact = new ResponsibleContactBo(internmentSummaryVo.getResponsibleContact());
        this.probableDischargeDate = internmentSummaryVo.getProbableDischargeDate();
    }

    private int totalInternmentDays(){
        LocalDate today = LocalDate.now();
        Period p = Period.between(getEntryDate(), today);
        return p.getDays();
    }
}
