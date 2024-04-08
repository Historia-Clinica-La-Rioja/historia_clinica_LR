package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import org.springframework.stereotype.Service;

@Service
public class SectorFinder {

	private final SharedStaffPort sharedStaffPort;

	public SectorFinder(SharedStaffPort sharedStaffPort) {
		this.sharedStaffPort = sharedStaffPort;
	}


	public String getSectorName(Integer id) {
		return sharedStaffPort.getSectorName(id);
	}

}
