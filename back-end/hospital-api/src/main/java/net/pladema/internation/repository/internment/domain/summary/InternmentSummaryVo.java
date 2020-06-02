package net.pladema.internation.repository.internment.domain.summary;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentSummaryVo {

    private Integer id;

    private DocumentsSummaryVo documents;

    private ResponsibleDoctorVo doctor;

    private Integer bedId;

    private String bedNumber;

    private Integer roomId;

    private String roomNumber;

    private Integer clinicalSpecialtyId;

    private String specialty;

    private LocalDate entryDate;

    public InternmentSummaryVo(Integer id, LocalDate entryDate, Long anamnesisDocId, String anamnesisStatusId,
                               Long epicrisisDocId, String epicrisisStatusId,
                               Integer bedId, String bedNumber, Integer roomId, String roomNumber,
                               Integer clinicalSpecialtyId, String specialty,
                               Integer healthcareProfessionalId, String licenseNumber, String firstName, String lastName) {
        this.id = id;
        this.documents = new DocumentsSummaryVo();
        this.documents.setAnamnesis(new AnamnesisSummaryVo(anamnesisDocId, anamnesisStatusId));
        this.documents.setEpicrisis(new EpicrisisSummaryVo(epicrisisDocId, epicrisisStatusId));
        this.bedId = bedId;
        this.bedNumber = bedNumber;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
        this.specialty = specialty;
        this.entryDate = entryDate;
        if (healthcareProfessionalId != null)
            this.doctor = new ResponsibleDoctorVo(healthcareProfessionalId, firstName, lastName, licenseNumber);
    }

}
