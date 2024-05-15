package ar.lamansys.sgh.publicapi.apisumar.service.impl;

import ar.lamansys.sgh.publicapi.apisumar.repository.SumarApiQueryFactory;
import ar.lamansys.sgh.publicapi.apisumar.repository.model.ConsultationDetailData;
import ar.lamansys.sgh.publicapi.apisumar.service.SumarApiService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SumarApiServiceImpl implements SumarApiService {

	private final SumarApiQueryFactory sumarApiQueryFactory;

	public SumarApiServiceImpl(SumarApiQueryFactory sumarApiQueryFactory) {
		this.sumarApiQueryFactory = sumarApiQueryFactory;
	}

	@Override
	public List<ConsultationDetailData> getAllConsultationsDetailData() {
		return sumarApiQueryFactory.getAllConsultationsData();
	}

}
