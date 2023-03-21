package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedDiaryCareLinePort {

	List<Integer> getCareLineIdsByDiaryId(Integer diaryId);
}
