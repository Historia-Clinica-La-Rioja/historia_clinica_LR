package net.pladema.edMonton.getPdfEdMonton.repository;

import net.pladema.edMonton.repository.domain.Answer;

import java.util.List;
import java.util.Optional;

public class ImpresionEdMontonRepositoryImpl implements ImpresionEdMontonRepository{
	@Override
	public Optional<List<Answer>> getEdMontonReportInfo(Long edMontonTestId) {
		return Optional.empty();
	}
}
