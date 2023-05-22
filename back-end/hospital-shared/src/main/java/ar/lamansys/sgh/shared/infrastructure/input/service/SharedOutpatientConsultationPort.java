package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;
import java.util.Map;

public interface SharedOutpatientConsultationPort {

	Map<Integer, List<OutpatientConsultationDto>> getOutpatientConsultationsToCipres();
}
