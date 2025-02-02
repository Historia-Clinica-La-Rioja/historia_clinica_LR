package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ClinicalSpecialtyBo;
import ar.lamansys.odontology.domain.consultation.DoctorInfoBo;
import ar.lamansys.odontology.application.odontogram.ports.OdontologyDoctorStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OdontologyDoctorStorageImpl implements OdontologyDoctorStorage {

    private final Logger logger;

    private final SharedStaffPort sharedStaffPort;

    public OdontologyDoctorStorageImpl(SharedStaffPort sharedStaffPort) {
        this.sharedStaffPort = sharedStaffPort;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Optional<DoctorInfoBo> getDoctorInfo() {
        logger.debug("Fetch external doctor information");
        return Optional
                .ofNullable(sharedStaffPort.getProfessionalCompleteInfo(UserInfo.getCurrentAuditor()))
                .map(professional -> new DoctorInfoBo(professional.getId(), mapTo(professional.getClinicalSpecialties())));
    }

    private List<ClinicalSpecialtyBo> mapTo(List<ClinicalSpecialtyDto> clinicalSpecialties) {
        return clinicalSpecialties
                .stream()
                .map(clinicalSpecialtyDto -> new ClinicalSpecialtyBo(clinicalSpecialtyDto.getId(), clinicalSpecialtyDto.getName()))
                .collect(Collectors.toList());
    }
}
