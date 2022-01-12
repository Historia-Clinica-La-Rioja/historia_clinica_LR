package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import java.io.Serializable;

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

    public InternmentEpisodeBo(Integer internmentEpisodeId, Integer patientId, String firstName, String lastName,
                               String nameSelfDetermination,Integer bedId, String bedNumber, Integer roomId,
                               String roomNumber, ClinicalSpecialty clinicalSpecialty, Integer sectorId, String sector){
        this.id = internmentEpisodeId;
        this.patient = new PatientBo(patientId, firstName, lastName, nameSelfDetermination);
        this.bed = new BedBo(bedId, bedNumber, new RoomBo(
                roomId, roomNumber, new SectorBo(sectorId, sector)));
        clinicalSpecialty.fixSpecialtyType();
        this.specialty = new HospitalizationSpecialtyBo(clinicalSpecialty.getId(), clinicalSpecialty.getName());
    }
}
