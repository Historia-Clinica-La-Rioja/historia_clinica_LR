package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

@Getter
@Setter
public class EmergencyCareDoctorsOfficeDetailBo extends EmergencyCareAttentionPlaceDetailBo{

	private DoctorsOfficeBo doctorsOffice;

	public EmergencyCareDoctorsOfficeDetailBo(DoctorsOfficeBo doctorsOffice){
		super();
		this.doctorsOffice = doctorsOffice;
	}
}
