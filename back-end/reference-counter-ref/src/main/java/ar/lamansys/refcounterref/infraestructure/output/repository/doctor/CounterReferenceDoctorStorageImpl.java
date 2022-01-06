package ar.lamansys.refcounterref.infraestructure.output.repository.doctor;

import ar.lamansys.refcounterref.application.port.CounterReferenceDoctorStorage;
import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;
import ar.lamansys.refcounterref.domain.doctor.CounterReferenceDoctorInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CounterReferenceDoctorStorageImpl implements CounterReferenceDoctorStorage {

    private final SharedStaffPort sharedStaffPort;

    @Override
    public Optional<CounterReferenceDoctorInfoBo> getDoctorInfo() {
        log.debug("Fetch external doctor information");
        return Optional
                .ofNullable(sharedStaffPort.getProfessionalCompleteInfo(UserInfo.getCurrentAuditor()))
                .map(professional -> new CounterReferenceDoctorInfoBo(professional.getId(), mapTo(professional.getClinicalSpecialties())));
    }

    private List<ClinicalSpecialtyBo> mapTo(List<ClinicalSpecialtyDto> clinicalSpecialties) {
        return clinicalSpecialties
                .stream()
                .map(clinicalSpecialtyDto -> new ClinicalSpecialtyBo(clinicalSpecialtyDto.getId(), clinicalSpecialtyDto.getName()))
                .collect(Collectors.toList());
    }

}