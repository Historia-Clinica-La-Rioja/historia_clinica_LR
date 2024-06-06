package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import org.springframework.stereotype.Service;

@Service
public class ShockRoomFinder {

	private final SharedStaffPort sharedStaffPort;

	public ShockRoomFinder(SharedStaffPort sharedStaffPort) {
		this.sharedStaffPort = sharedStaffPort;
	}


	public String getShockRoomDescription(Integer id) {
		return sharedStaffPort.getShockRoomDescription(id);
	}

}
