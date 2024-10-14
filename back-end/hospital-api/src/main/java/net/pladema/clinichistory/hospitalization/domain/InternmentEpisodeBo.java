package net.pladema.clinichistory.hospitalization.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.service.domain.BedBo;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentsSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.HospitalizationSpecialtyBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientBo;
import net.pladema.clinichistory.hospitalization.service.domain.ResponsibleContactBo;
import net.pladema.clinichistory.hospitalization.service.domain.RoomBo;
import net.pladema.clinichistory.hospitalization.service.domain.SectorBo;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InternmentEpisodeBo {

    private Integer id;
    private PatientBo patient;
    private BedBo bed;
    private ResponsibleDoctorBo doctor;
    private HospitalizationSpecialtyBo specialty;
	private boolean hasPhysicalDischarge;
	private DocumentsSummaryBo documentsSummary;
    private Integer institutionId;
    private Long noteId;
    private LocalDateTime entryDate;
    private LocalDate dischargeDate;
    private Integer patientMedicalCoverageId;
    private ResponsibleContactBo responsibleContact;
    private Short statusId;

    public InternmentEpisodeBo(Integer internmentEpisodeId, Integer patientId, String firstName, String lastName,
							   String nameSelfDetermination, Short identificationTypeId, String identificationNumber, LocalDate birthDate, Integer bedId, String bedNumber, Integer roomId,
							   String roomNumber , Integer sectorId, String sector, boolean hasPhysicalDischarge){
        this.id = internmentEpisodeId;
        this.patient = new PatientBo(patientId, firstName, lastName, nameSelfDetermination, identificationTypeId, identificationNumber, birthDate);
        this.bed = new BedBo(bedId, bedNumber, new RoomBo(
                roomId, roomNumber, new SectorBo(sectorId, sector)));
		this.hasPhysicalDischarge = hasPhysicalDischarge;
    }

    public Integer getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    public Integer getBedId() {
        return bed != null ? bed.getId() : null;
    }

    public Integer getResponsibleDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }
}
