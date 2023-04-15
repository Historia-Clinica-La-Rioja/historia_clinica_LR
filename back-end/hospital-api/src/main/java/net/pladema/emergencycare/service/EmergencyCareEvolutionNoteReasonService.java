package net.pladema.emergencycare.service;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import java.util.List;

public interface EmergencyCareEvolutionNoteReasonService {

	List<ReasonBo> addReasons(Integer emergencyCareEvolutionNoteId, List<ReasonBo> reasons);

}
