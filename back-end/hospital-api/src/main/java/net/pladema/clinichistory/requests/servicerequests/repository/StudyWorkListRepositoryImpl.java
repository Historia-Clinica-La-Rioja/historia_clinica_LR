package net.pladema.clinichistory.requests.servicerequests.repository;

import lombok.AllArgsConstructor;

import net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class StudyWorkListRepositoryImpl implements StudyWorkListRepository {

	@Override
	public List<StudyOrderWorkListVo> execute(Integer institutionId, List<String> categories) {
		return new ArrayList<>();
	}

}
