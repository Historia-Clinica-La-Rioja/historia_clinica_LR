package net.pladema.edMonton.repository;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;

import java.util.Optional;

public interface EdMontonSummaryRepository {

	Optional<EdMontonSummary> getSummaryReportEdMonton(Integer edMontonId);
}
