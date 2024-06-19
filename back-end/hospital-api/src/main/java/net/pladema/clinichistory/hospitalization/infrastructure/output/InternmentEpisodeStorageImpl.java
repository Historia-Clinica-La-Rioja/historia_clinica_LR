package net.pladema.clinichistory.hospitalization.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.hospitalization.application.port.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.infrastructure.output.repository.InternmentEpisodeSummaryRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InternmentEpisodeStorageImpl implements InternmentEpisodeStorage {
    
    private final InternmentEpisodeRepository internmentEpisodeRepository;
    private final InternmentEpisodeSummaryRepository internmentEpisodeSummaryRepository;
    
    @Override
    public Integer save(InternmentEpisodeBo internmentEpisodeBo) {
        InternmentEpisode result = internmentEpisodeRepository.save(this.mapTo(internmentEpisodeBo));
        return result.getId();
    }

    @Override
    public boolean hasIntermentEpisodeActiveInInstitution(Integer patientId, Integer institutionId) {
        return internmentEpisodeRepository.hasIntermentEpisodeActiveInInstitution(patientId, institutionId);
    }

    @Override
    public Optional<InternmentSummaryVo> getSummary(Integer internmentEpisodeId) {
        return internmentEpisodeSummaryRepository.getSummary(internmentEpisodeId);
    }

    private InternmentEpisode mapTo(InternmentEpisodeBo internmentEpisodeBo) {
        InternmentEpisode internmentEpisode = new InternmentEpisode();

        internmentEpisode.setPatientId(internmentEpisodeBo.getPatientId());
        internmentEpisode.setBedId(internmentEpisodeBo.getBedId());
        internmentEpisode.setNoteId(internmentEpisodeBo.getNoteId());
        internmentEpisode.setEntryDate(internmentEpisodeBo.getEntryDate());
        internmentEpisode.setInstitutionId(internmentEpisodeBo.getInstitutionId());
        internmentEpisode.setPatientMedicalCoverageId(internmentEpisodeBo.getPatientMedicalCoverageId());
        internmentEpisode.setStatusId(internmentEpisodeBo.getStatusId());
        
        return internmentEpisode;
    }
    
}
