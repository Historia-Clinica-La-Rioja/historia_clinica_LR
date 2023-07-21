package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedOrderReportInfoBo;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.TranscribedOrderReportInfoDto;

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

	@Named("parseToDto")
	public TranscribedOrderReportInfoDto parseToDto(TranscribedOrderReportInfoBo transcribedOrderReportInfoBo){
		LOG.debug("input -> transcribedOrderReportInfoBo{}", transcribedOrderReportInfoBo);
		TranscribedOrderReportInfoDto result = new TranscribedOrderReportInfoDto();
		result.setSnomed(transcribedOrderReportInfoBo.getSnomed());
		result.setCreationDate(transcribedOrderReportInfoBo.getCreationDate());
		result.setStatus(transcribedOrderReportInfoBo.getStatus());
		result.setImageId(transcribedOrderReportInfoBo.getImageId());
		if (transcribedOrderReportInfoBo.getDocumentId() != null) {
			HCEDocumentDataDto hceDocumentDataDto = new HCEDocumentDataDto();
			hceDocumentDataDto.setId(transcribedOrderReportInfoBo.getDocumentId().longValue());
			hceDocumentDataDto.setFilename(transcribedOrderReportInfoBo.getFileName());
			result.setHceDocumentDataDto(hceDocumentDataDto);
		}
		result.setHealthCondition(transcribedOrderReportInfoBo.getHealthCondition());
		result.setProfessionalName(transcribedOrderReportInfoBo.getProfessionalName());
		Boolean existsDocument = result.getHceDocumentDataDto() == null? false : true;
		String documentStatus = transcribedOrderReportInfoBo.getDocumentStatus();
		result.setViewReport(existsDocument && DocumentStatus.FINAL.equals(documentStatus));
		result.setSeeStudy(result.getStatus());


		LOG.debug("Output: {}", result);
		return result;
	}

}
