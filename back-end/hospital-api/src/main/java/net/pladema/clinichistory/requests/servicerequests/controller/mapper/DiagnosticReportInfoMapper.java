package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportInfoDto;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import net.pladema.staff.controller.dto.ProfessionalDto;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticReportInfoMapper {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportInfoMapper.class);

    @Named("parseTo")
    public DiagnosticReportInfoDto parseTo(DiagnosticReportBo diagnosticReportBo, ProfessionalDto professionalDto){
        LOG.debug("input -> diagnosticReportBo{},a professionalDto {}", diagnosticReportBo, professionalDto);
        DiagnosticReportInfoDto result = new DiagnosticReportInfoDto();
        result.setId(diagnosticReportBo.getId());
        result.setSnomed(SnomedDto.from(diagnosticReportBo.getSnomed()));
        result.setHealthCondition(HealthConditionInfoDto.from(diagnosticReportBo.getHealthCondition()));
        result.setObservations(diagnosticReportBo.getObservations());
        result.setStatusId(diagnosticReportBo.getStatusId());
        result.setDoctor(DoctorInfoDto.from(professionalDto));
        result.setServiceRequestId(diagnosticReportBo.getEncounterId());
        result.setCreationDate(diagnosticReportBo.getEffectiveTime());
		result.setCategory(diagnosticReportBo.getCategory());
		result.setSource(diagnosticReportBo.getSource());
        LOG.debug("Output: {}", result);
        return result;
    }
}
