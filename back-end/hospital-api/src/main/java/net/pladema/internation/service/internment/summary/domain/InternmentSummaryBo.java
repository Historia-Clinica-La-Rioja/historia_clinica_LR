package net.pladema.internation.service.internment.summary.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.internment.domain.summary.InternmentSummaryVo;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentSummaryBo {

    private Integer id;

    private DocumentsSummaryBo documents;

    private ResponsibleDoctorBo doctor;

    private Integer bedId;

    private String bedNumber;

    private Integer roomId;

    private String roomNumber;

    private Integer clinicalSpecialtyId;

    private String specialty;

    private LocalDate entryDate;

    private int totalInternmentDays;

    private ResponsibleContactBo responsibleContact;

    public InternmentSummaryBo(InternmentSummaryVo internmentSummaryVo) {
        this.id = internmentSummaryVo.getId();
        this.documents = new DocumentsSummaryBo(internmentSummaryVo.getDocuments());
        this.bedId = internmentSummaryVo.getBedId();
        this.bedNumber = internmentSummaryVo.getBedNumber();
        this.roomId = internmentSummaryVo.getRoomId();
        this.roomNumber = internmentSummaryVo.getRoomNumber();
        this.clinicalSpecialtyId = internmentSummaryVo.getClinicalSpecialtyId();
        this.specialty = internmentSummaryVo.getSpecialty();
        this.entryDate = internmentSummaryVo.getEntryDate();
        this.totalInternmentDays = totalInternmentDays();
        if (internmentSummaryVo.getDoctor() != null)
            this.doctor = new ResponsibleDoctorBo(internmentSummaryVo.getDoctor());
        if (internmentSummaryVo.getResponsibleContact() != null)
            this.responsibleContact = new ResponsibleContactBo(internmentSummaryVo.getResponsibleContact());

    }

    private int totalInternmentDays(){
        LocalDate today = LocalDate.now();
        Period p = Period.between(getEntryDate(), today);
        return p.getDays();
    }
}
