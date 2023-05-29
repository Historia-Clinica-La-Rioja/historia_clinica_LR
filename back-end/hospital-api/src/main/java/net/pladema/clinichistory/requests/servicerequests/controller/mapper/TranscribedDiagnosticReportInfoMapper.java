package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.TranscribedDiagnosticReportInfoDto;

@Component
public class TranscribedDiagnosticReportInfoMapper {

    private static final Logger LOG = LoggerFactory.getLogger(TranscribedDiagnosticReportInfoMapper.class);

    @Named("parseTo")
    public TranscribedDiagnosticReportInfoDto parseTo(TranscribedDiagnosticReportBo transcribedDiagnosticReportBo){
        LOG.debug("input -> transcribedDiagnosticReportBo{}", transcribedDiagnosticReportBo);
		TranscribedDiagnosticReportInfoDto result = new TranscribedDiagnosticReportInfoDto();
        result.setServiceRequestId(transcribedDiagnosticReportBo.getServiceRequestId());
		result.setStudyId(transcribedDiagnosticReportBo.getStudyId());
		result.setStudyName(transcribedDiagnosticReportBo.getStudyName());
        LOG.debug("Output: {}", result);
        return result;
    }
}
