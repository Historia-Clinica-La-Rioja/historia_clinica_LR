package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InternmentEpisodeBo implements Serializable {

    private Integer id;

    private PatientBo patient;

    private BedBo bed;

    private ResponsibleDoctorBo doctor;

    private HospitalizationSpecialtyBo specialty;

	private boolean hasPhysicalDischarge;

	private DocumentsSummaryBo documentsSummary;

    public InternmentEpisodeBo(Integer internmentEpisodeId, Integer patientId, String firstName, String lastName,
							   String nameSelfDetermination, Short identificationTypeId, String identificationNumber, LocalDate birthDate, Integer bedId, String bedNumber, Integer roomId,
							   String roomNumber , Integer sectorId, String sector, boolean hasPhysicalDischarge){
        this.id = internmentEpisodeId;
        this.patient = new PatientBo(patientId, firstName, lastName, nameSelfDetermination, identificationTypeId, identificationNumber, birthDate);
        this.bed = new BedBo(bedId, bedNumber, new RoomBo(
                roomId, roomNumber, new SectorBo(sectorId, sector)));
		this.hasPhysicalDischarge = hasPhysicalDischarge;
    }
}
