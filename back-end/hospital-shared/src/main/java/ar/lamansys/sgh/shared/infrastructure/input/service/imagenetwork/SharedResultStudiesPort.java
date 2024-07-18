package ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork;

import org.springframework.transaction.annotation.Transactional;

public interface SharedResultStudiesPort {

	public void insertPossibleStudy(Integer idMove,Integer appointmentId, String patientId, String patientName, String studyDate, String studyTime, String modality, String studyInstanceUid);
	public  Boolean existsResult(Integer idMove);
	public void deleteResult(Integer idMove);
}
