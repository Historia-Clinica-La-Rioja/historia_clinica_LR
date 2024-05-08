package net.pladema.clinichistory.hospitalization.application.getdocumentheader;

import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentHeaderPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.HospitalizationDocumentHeaderBo;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.BedBo;
import net.pladema.clinichistory.hospitalization.service.domain.RoomBo;
import net.pladema.clinichistory.hospitalization.service.domain.SectorBo;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.service.BedService;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.staff.service.ClinicalSpecialtyService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetHospitalizationDocumentHeader {

    private final InternmentEpisodeService internmentEpisodeService;
    private final InstitutionService institutionService;
    private final BedService bedService;
    private final DocumentHeaderPort documentHeaderPort;
    private final ClinicalSpecialtyService clinicalSpecialtyService;
    private final SharedStaffPort sharedStaffPort;

    public HospitalizationDocumentHeaderBo run(Integer institutionId, Integer internmentEpisodeId, Long documentId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, documentId {}", institutionId, internmentEpisodeId, documentId);

        var result = this.createHospitalizationDocumentHeader(documentId);
        this.setInternmentInformation(result);
        this.setInstitutionName(result);
        this.setProfessionalName(result);

        log.debug("Output -> {}", result);
        return result;
    }

    private HospitalizationDocumentHeaderBo createHospitalizationDocumentHeader(Long documentId) {
        var result = new HospitalizationDocumentHeaderBo();
        var base = documentHeaderPort.getDocumentHeader(documentId);
        result.setBaseDocumentHeader(base);
        return result;
    }

    private void setInternmentInformation(HospitalizationDocumentHeaderBo documentHeaderBo) {
        var episode = internmentEpisodeService.getInternmentEpisode(
                documentHeaderBo.getEncounterId(),
                documentHeaderBo.getInstitutionId());

        this.setClinicalSpecialtyName(documentHeaderBo, episode);
        this.setLastBed(episode, documentHeaderBo);
    }

    private void setClinicalSpecialtyName(HospitalizationDocumentHeaderBo documentHeaderBo, InternmentEpisode episode) {
        clinicalSpecialtyService.getClinicalSpecialty(episode.getClinicalSpecialtyId())
                .ifPresent(clinicalSpecialtyBo -> documentHeaderBo.setClinicalSpecialtyName(clinicalSpecialtyBo.getName()));
    }

    private void setLastBed(InternmentEpisode episode, HospitalizationDocumentHeaderBo documentHeaderBo) {
        bedService.getBedInfo(episode.getBedId())
                .map(this::toBedBo)
                .ifPresent(documentHeaderBo::setBed);
    }

    private BedBo toBedBo(BedInfoVo bedInfoVo) {
        var bed = bedInfoVo.getBed();
        var room = bedInfoVo.getRoom();
        var sector = bedInfoVo.getSector();

        return new BedBo(bed.getId(), bed.getBedNumber(),
                new RoomBo(
                        room.getId(),
                        new SectorBo(sector.getId(), sector.getDescription())
                        , room.getDescription()));
    }

    private void setInstitutionName(HospitalizationDocumentHeaderBo documentHeaderBo) {
        String institutionName = institutionService.get(documentHeaderBo.getInstitutionId()).getName();
        documentHeaderBo.setInstitutionName(institutionName);
    }

    private void setProfessionalName(HospitalizationDocumentHeaderBo result) {
        sharedStaffPort.getProfessionalCompleteNameByUserId(result.getCreatedBy())
                .ifPresent(result::setProfessionalName);
    }
}
