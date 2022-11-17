package ar.lamansys.sgh.publicapi.application.fetchdocumentsinfobyactivity;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;

import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class FetchDocumentsInfoByActivity {
	private final ActivityInfoStorage activityInfoStorage;

	public FetchDocumentsInfoByActivity(ActivityInfoStorage activityInfoStorage) {
		this.activityInfoStorage = activityInfoStorage;
	}

	public List<DocumentInfoBo> run(String refsetCode, Long activityId) {
		log.debug("Input parameters -> refsetCode {}, activityId {}", refsetCode, activityId);
		List<DocumentInfoBo> result = activityInfoStorage.getDocumentsByActivity(refsetCode, activityId);
		log.debug("Output -> {}", result);
		return result;
	}
}
