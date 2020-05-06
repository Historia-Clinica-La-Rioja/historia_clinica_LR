package net.pladema.internation.service.internment;

import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.repository.core.entity.EvolutionNoteDocument;

import java.util.Optional;

public interface InternmentEpisodeService {

    void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId);

    Optional<InternmentSummary> getIntermentSummary(Integer internmentEpisodeId);

    Optional<Integer> getPatient(Integer internmentEpisodeId);

    void updateEpicrisisDocumentId(Integer intermentEpisodeId, Long id);

    EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId);
}
