package net.pladema.edMonton.get.service;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;
import net.pladema.edMonton.repository.EdMontonRepository;
import net.pladema.edMonton.repository.domain.Answer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetEdMontonServiceImpl implements GetEdMontonService{

	private final EdMontonRepository edMontonRepository;

	public GetEdMontonServiceImpl(EdMontonRepository edMontonRepository) {
		this.edMontonRepository = edMontonRepository;
	}

	public List<Answer> findPatientEdMonton(Integer patientId) {
		return this.edMontonRepository.findPatientEdMontonTest(patientId);
	}

	public EdMontonSummary findEdMontonSummary(Integer edMontonId) {

		return this.edMontonRepository.findEdMontonSummary(edMontonId);

	}
}
