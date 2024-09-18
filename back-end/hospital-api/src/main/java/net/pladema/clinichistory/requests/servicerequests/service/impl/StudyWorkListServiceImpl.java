package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyWorkListServiceImpl implements StudyWorkListService {

	@Override
	public List<Object> execute(){
		return new ArrayList<>();
	};

}
