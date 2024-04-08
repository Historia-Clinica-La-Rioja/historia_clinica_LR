package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import org.springframework.stereotype.Service;

@Service
public class DoctorsOfficeFinder {

	private final SharedStaffPort sharedStaffPort;

	public DoctorsOfficeFinder(SharedStaffPort sharedStaffPort) {
		this.sharedStaffPort = sharedStaffPort;
	}


	public String getDoctorsOfficeDescription(Integer id) {
		return sharedStaffPort.getDoctorsOfficeDescription(id);
	}

}
