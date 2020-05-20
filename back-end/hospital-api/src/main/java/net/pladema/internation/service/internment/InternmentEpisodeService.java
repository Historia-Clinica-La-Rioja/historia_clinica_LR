package net.pladema.internation.service.internment;

import java.time.LocalDate;
import java.util.Optional;

import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.repository.core.entity.EvolutionNoteDocument;
import net.pladema.internation.repository.core.entity.InternmentEpisode;
import net.pladema.internation.repository.core.entity.PatientDischarge;

public interface InternmentEpisodeService {

    void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId);

    Optional<InternmentSummaryVo> getIntermentSummary(Integer internmentEpisodeId);

    Optional<Integer> getPatient(Integer internmentEpisodeId);

    void updateEpicrisisDocumentId(Integer intermentEpisodeId, Long id);

    EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId);

    InternmentEpisode addInternmentEpisode(InternmentEpisode internmentEpisode, Integer institutionId);

    boolean haveAnamnesis(Integer internmentEpisodeId);

    LocalDate getEntryDate(Integer internmentEpisodeId);

    boolean haveAnamnesisAndEvolutionNote(Integer internmentEpisodeId);
 
    public PatientDischarge addPatientDischarge(PatientDischarge patientDischarge);
    
    void updateInternmentEpisodeSatus(Integer internmentEpisodeId, Short statusId);
    
}
