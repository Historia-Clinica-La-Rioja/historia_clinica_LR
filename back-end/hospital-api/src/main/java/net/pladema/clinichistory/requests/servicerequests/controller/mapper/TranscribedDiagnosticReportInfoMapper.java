package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyTranscribedOrderReportInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.TranscribedDiagnosticReportInfoDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TranscribedDiagnosticReportInfoMapper {

    @Named("parseTo")
    public TranscribedDiagnosticReportInfoDto parseTo(TranscribedDiagnosticReportBo transcribedDiagnosticReportBo){
        log.trace("input -> transcribedDiagnosticReportBo{}", transcribedDiagnosticReportBo);
		TranscribedDiagnosticReportInfoDto result = new TranscribedDiagnosticReportInfoDto();
        result.setServiceRequestId(transcribedDiagnosticReportBo.getServiceRequestId());
		result.setStudyId(transcribedDiagnosticReportBo.getStudyId());
		result.setStudyName(transcribedDiagnosticReportBo.getStudyName());
        log.trace("Output: {}", result);
        return result;
    }

	@Named("parseToDto")
	public StudyTranscribedOrderReportInfoDto parseToDto(StudyTranscribedOrderReportInfoBo studyTranscribedOrderReportInfoBo){
		log.trace("input -> transcribedOrderReportInfoBo{}", studyTranscribedOrderReportInfoBo);
		StudyTranscribedOrderReportInfoDto result = new StudyTranscribedOrderReportInfoDto();
		result.setSnomed(studyTranscribedOrderReportInfoBo.getSnomed());
		result.setCreationDate(studyTranscribedOrderReportInfoBo.getCreationDate());
		result.setStatus(studyTranscribedOrderReportInfoBo.getStatus());
		result.setImageId(studyTranscribedOrderReportInfoBo.getImageId());
		if (studyTranscribedOrderReportInfoBo.getDocumentId() != null) {
			HCEDocumentDataDto hceDocumentDataDto = new HCEDocumentDataDto();
			hceDocumentDataDto.setId(studyTranscribedOrderReportInfoBo.getDocumentId().longValue());
			hceDocumentDataDto.setFilename(studyTranscribedOrderReportInfoBo.getFileName());
			result.setHceDocumentDataDto(hceDocumentDataDto);
		}
		result.setHealthCondition(studyTranscribedOrderReportInfoBo.getHealthCondition());
		result.setProfessionalName(studyTranscribedOrderReportInfoBo.getProfessionalName());
		boolean existsDocument = result.getHceDocumentDataDto() != null;
		String documentStatus = studyTranscribedOrderReportInfoBo.getDocumentStatus();
		result.setViewReport(existsDocument && DocumentStatus.FINAL.equals(documentStatus));
		result.setIsAvailableInPACS(studyTranscribedOrderReportInfoBo.getIsAvailableInPACS());


		log.trace("Output: {}", result);
		return result;
	}

}
