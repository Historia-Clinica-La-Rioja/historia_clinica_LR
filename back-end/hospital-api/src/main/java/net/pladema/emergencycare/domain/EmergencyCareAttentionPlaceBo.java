package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;
import net.pladema.establishment.domain.bed.EmergencyCareBedBo;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;
import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareAttentionPlaceBo {

	private Integer id;
	private String description;
	private Short sectorOrganizationId;
	private Integer sectorId;
	private List<DoctorsOfficeBo> doctorsOffices;
	private List<ShockRoomBo> shockRooms;
	private List<EmergencyCareBedBo> beds;
	private List<ClinicalSpecialtySectorBo> clinicalSpecialtySectors;

	public EmergencyCareAttentionPlaceBo(Integer id, String description, Short sectorOrganizationId, Integer sectorId) {
		this.id = id;
		this.description = description;
		this.sectorOrganizationId = sectorOrganizationId;
		this.sectorId = sectorId;
	}
}
