package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import org.springframework.stereotype.Service;

@Service
public class ClinicalSpecialtyFinder {

    private final SharedStaffPort sharedStaffPort;

    public ClinicalSpecialtyFinder(SharedStaffPort sharedStaffPort) {
        this.sharedStaffPort = sharedStaffPort;
    }


    public ClinicalSpecialtyDto getClinicalSpecialty(Integer id) {
        return sharedStaffPort.getClinicalSpecialty(id).orElse(null);
    }
}
