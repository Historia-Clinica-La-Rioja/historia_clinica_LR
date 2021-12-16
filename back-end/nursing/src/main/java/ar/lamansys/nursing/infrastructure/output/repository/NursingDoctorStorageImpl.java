package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.nursing.application.port.NursingDoctorStorage;
import ar.lamansys.nursing.domain.doctor.ClinicalSpecialtyBo;
import ar.lamansys.nursing.domain.doctor.DoctorInfoBo;
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
public class NursingDoctorStorageImpl implements NursingDoctorStorage {

    private static final Logger LOG = LoggerFactory.getLogger(NursingDoctorStorageImpl.class);

    private final SharedStaffPort sharedStaffPort;

    public NursingDoctorStorageImpl(SharedStaffPort sharedStaffPort) {
        this.sharedStaffPort = sharedStaffPort;
    }

    @Override
    public Optional<DoctorInfoBo> getDoctorInfo() {
        LOG.debug("Fetch external doctor information");
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
