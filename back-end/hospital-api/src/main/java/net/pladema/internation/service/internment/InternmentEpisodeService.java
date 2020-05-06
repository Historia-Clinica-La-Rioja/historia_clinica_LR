package net.pladema.internation.service.internment;

import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.repository.core.entity.EvolutionNoteDocument;

import java.util.Optional;

public interface InternmentEpisodeService {

    void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId);

    Optional<InternmentSummaryVo> getIntermentSummary(Integer internmentEpisodeId);

    Optional<Integer> getPatient(Integer internmentEpisodeId);

    void updateEpicrisisDocumentId(Integer intermentEpisodeId, Long id);

    EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId);
}
