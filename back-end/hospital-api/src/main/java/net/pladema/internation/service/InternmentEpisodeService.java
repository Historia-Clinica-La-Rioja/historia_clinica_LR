package net.pladema.internation.service;

import net.pladema.internation.repository.core.domain.InternmentSummary;

import java.util.Optional;

public interface InternmentEpisodeService {

    Optional<InternmentSummary> getIntermentSummary(Integer internmentEpisodeId);
}
