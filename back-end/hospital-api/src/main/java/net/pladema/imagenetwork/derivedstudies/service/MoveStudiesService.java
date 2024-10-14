package net.pladema.imagenetwork.derivedstudies.service;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import net.pladema.imagenetwork.derivedstudies.service.domain.MoveStudiesBO;

public interface MoveStudiesService extends SharedLoadStudiesResultPort {

	Integer save(MoveStudiesBO appointmentOrderImageBo);

	Integer create(Integer appointmentId, Integer institutionId);

	List<MoveStudiesBO> getMoveStudies();

	void updateStatus(Integer idMove, String status);

	void updateAttemps(Integer idMove,Integer attemps);

	void getSizeFromOrchestrator( Integer idMove);

	Integer getInstitutionByAppointmetId(Integer appointmentId);

	Optional<Integer> getSizeImageByAppointmentId(Integer appointmentId);

	void updateFailedCurrentDate(Integer institutionId);
}
