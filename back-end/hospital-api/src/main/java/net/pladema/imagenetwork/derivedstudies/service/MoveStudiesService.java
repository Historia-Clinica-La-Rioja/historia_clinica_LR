package net.pladema.imagenetwork.derivedstudies.service;

import net.pladema.imagenetwork.derivedstudies.service.domain.MoveStudiesBO;

import java.util.List;

public interface MoveStudiesService {

	Integer save(MoveStudiesBO appointmentOrderImageBo);
	Integer create(Integer appointmentId);
	List<MoveStudiesBO> getMoveStudies();

	void updateSize(Integer idMove, Integer size);

	void updateStatus(Integer idMove, String status);

	void updateStatusAndResult(Integer idMove, String status, String result);

	void updateAttemps(Integer idMove,Integer attemps);

	void getSizeFromOrchestrator( Integer idMove);
}
