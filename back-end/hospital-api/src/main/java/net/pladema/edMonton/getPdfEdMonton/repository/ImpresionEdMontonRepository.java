package net.pladema.edMonton.getPdfEdMonton.repository;

import net.pladema.edMonton.repository.domain.Answer;

import java.util.List;
import java.util.Optional;

public interface ImpresionEdMontonRepository {

	Optional<List<Answer>> getEdMontonReportInfo(Long edMontonTestId);
}
