package net.pladema.staff.application.getallprofessioinalsandtechniciansbyinstitution;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class GetAllProfessionalsAndTechniciansByInstitution {

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;

    public List<HealthcareProfessionalBo> run(Integer institutionId) {
        log.debug("Input parameters -> institutionId {}",institutionId);
        List<Short> professionalERoleIds = getRolesId() ;
        List<HealthcareProfessionalVo> queryResults = healthcareProfessionalRepository.findAllByInstitution(institutionId,professionalERoleIds);
        List<HealthcareProfessionalBo> result = queryResults.stream().map(HealthcareProfessionalBo::new).collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private List<Short> getRolesId() {
        List<Short> result = RoleUtils.getProfessionalERoleIds();
        result.add(ERole.TECNICO.getId());
        return result;
    }
}