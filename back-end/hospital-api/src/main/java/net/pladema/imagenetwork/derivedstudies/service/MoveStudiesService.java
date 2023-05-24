package net.pladema.imagenetwork.derivedstudies.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import net.pladema.imagenetwork.derivedstudies.service.domain.MoveStudiesBO;

import java.util.List;

public interface MoveStudiesService extends SharedLoadStudiesResultPort {

	Integer save(MoveStudiesBO appointmentOrderImageBo);

	Integer create(Integer appointmentId);

	List<MoveStudiesBO> getMoveStudies();

	void updateStatus(Integer idMove, String status);

	void updateAttemps(Integer idMove,Integer attemps);

	void getSizeFromOrchestrator( Integer idMove);
}
