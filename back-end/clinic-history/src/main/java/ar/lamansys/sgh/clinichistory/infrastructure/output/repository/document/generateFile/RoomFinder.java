package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;

import org.springframework.stereotype.Service;

@Service
public class RoomFinder {

	private final SharedStaffPort sharedStaffPort;

	public RoomFinder(SharedStaffPort sharedStaffPort) {
		this.sharedStaffPort = sharedStaffPort;
	}


	public String getRoomNumber(Integer id) {
		return sharedStaffPort.getRoomNumber(id);
	}

}
