package net.pladema.internation.service.internment;

import net.pladema.internation.repository.documents.entity.EvolutionNoteDocument;
import net.pladema.internation.repository.documents.entity.InternmentEpisode;
import net.pladema.internation.repository.documents.entity.PatientDischarge;
import net.pladema.internation.service.internment.summary.domain.InternmentSummaryBo;
import net.pladema.internation.service.internment.summary.domain.PatientDischargeBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InternmentEpisodeService {

    void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId);

    Optional<InternmentSummaryBo> getIntermentSummary(Integer internmentEpisodeId);

    Optional<Integer> getPatient(Integer internmentEpisodeId);

    void updateEpicrisisDocumentId(Integer intermentEpisodeId, Long id);

    EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId);

    InternmentEpisode addInternmentEpisode(InternmentEpisode internmentEpisode, Integer institutionId);

    boolean haveAnamnesis(Integer internmentEpisodeId);

    boolean haveEpicrisis(Integer internmentEpisodeId);

    LocalDate getEntryDate(Integer internmentEpisodeId);

    boolean haveAnamnesisAndEvolutionNote(Integer internmentEpisodeId);
 
    PatientDischargeBo savePatientDischarge(PatientDischargeBo patientDischarge);
    
    void updateInternmentEpisodeSatus(Integer internmentEpisodeId, Short statusId);

    InternmentEpisode getInternmentEpisode(Integer internmentEpisodeId, Integer institutionId);

	List<InternmentEpisode> findByBedId(Integer bedId);

	Boolean existsActiveForBedId(Integer bedId);

	LocalDate getLastUpdateDateOfInternmentEpisode(Integer internmentEpisode);
}
