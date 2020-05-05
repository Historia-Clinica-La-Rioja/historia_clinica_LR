package net.pladema.internation.service.internment;

import net.pladema.internation.repository.core.domain.InternmentSummary;

import java.util.Optional;

public interface InternmentEpisodeService {

    void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId);

    Optional<InternmentSummary> getIntermentSummary(Integer internmentEpisodeId);

    Optional<Integer> getPatient(Integer internmentEpisodeId);

    void updateEpicrisisDocumentId(Integer intermentEpisodeId, Long id);

    void addEvaluationNote(Integer intermentEpisodeId, Long id);
}
