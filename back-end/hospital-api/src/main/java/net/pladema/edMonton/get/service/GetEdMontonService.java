package net.pladema.edMonton.get.service;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;
import net.pladema.edMonton.repository.domain.Answer;

import java.util.List;

public interface GetEdMontonService {

	public List<Answer> findPatientEdMonton(Integer patientId);

	public EdMontonSummary findEdMontonSummary(Integer edMontonId);
}
