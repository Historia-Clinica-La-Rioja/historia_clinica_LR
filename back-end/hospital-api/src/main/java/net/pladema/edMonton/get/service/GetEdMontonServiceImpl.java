package net.pladema.edMonton.get.service;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;
import net.pladema.edMonton.repository.EdMontonRepository;
import net.pladema.edMonton.repository.EdMontonSummaryRepository;
import net.pladema.edMonton.repository.domain.Answer;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetEdMontonServiceImpl implements GetEdMontonService{

	private final EdMontonRepository edMontonRepository;

	private final EdMontonSummaryRepository edMontonSummaryRepository;

	public GetEdMontonServiceImpl(EdMontonRepository edMontonRepository, EdMontonSummaryRepository edMontonSummaryRepository) {
		this.edMontonRepository = edMontonRepository;
		this.edMontonSummaryRepository = edMontonSummaryRepository;
	}

	public List<Answer> findPatientEdMonton(Integer patientId) {
		return this.edMontonRepository.findPatientEdMontonTest(patientId);
	}

	public Optional<EdMontonSummary> findEdMontonSummary(Integer edMontonId) {

		return this.edMontonSummaryRepository.getSummaryReportEdMonton(edMontonId);

	}
}
