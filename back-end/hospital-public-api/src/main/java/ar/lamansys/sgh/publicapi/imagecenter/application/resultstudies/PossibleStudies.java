package ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies;

import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions.InsertResultStudiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions.ResultStudiesException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.StudyDto;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.service.ImageCenterPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedResultStudiesPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PossibleStudies {

	public final ImageCenterPublicApiPermissions imageCenterPublicApiPermissions;
	public final SharedResultStudiesPort resultStudiesService;
	public final SharedLoadStudiesResultPort moveStudiesService;

	public Boolean run(Integer idMove, Integer appointmentId, List<StudyDto> studies) throws ResultStudiesException {
		var studyInstitutionId = moveStudiesService.findInstitutionId(idMove)
				.orElseThrow(InsertResultStudiesAccessDeniedException::new);

		if (!imageCenterPublicApiPermissions.canUpdate(studyInstitutionId)) {
			throw new InsertResultStudiesAccessDeniedException();
		}

		try {
			if(resultStudiesService.existsResult(idMove)){
				resultStudiesService.deleteResult(idMove);
			}
			for (StudyDto study: studies){
				resultStudiesService.insertPossibleStudy(idMove,
						appointmentId,
						study.getPatientId(),
						study.getPatientName(),
						study.getStudyDate(),
						study.getStudyTime(),
						study.getModality(),
						study.getStudyInstanceUid());
			}
			return true;
		} catch (Exception e) {
			throw new ResultStudiesException(e.getMessage(), e);
		}
	}
}
