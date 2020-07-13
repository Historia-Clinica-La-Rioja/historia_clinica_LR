package net.pladema.clinichistory.hospitalization.service;

import net.pladema.clinichistory.documents.repository.entity.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    boolean canCreateEpicrisis(Integer internmentEpisodeId);
 
    PatientDischargeBo savePatientDischarge(PatientDischargeBo patientDischarge);
    
    void updateInternmentEpisodeSatus(Integer internmentEpisodeId, Short statusId);

    InternmentEpisode getInternmentEpisode(Integer internmentEpisodeId, Integer institutionId);

	List<InternmentEpisode> findByBedId(Integer bedId);

	Boolean existsActiveForBedId(Integer bedId);

	LocalDate getLastUpdateDateOfInternmentEpisode(Integer internmentEpisode);

	LocalDateTime updateInternmentEpisodeProbableDischargeDate(Integer internmentEpisode, LocalDateTime probableDischargeDate);
}
