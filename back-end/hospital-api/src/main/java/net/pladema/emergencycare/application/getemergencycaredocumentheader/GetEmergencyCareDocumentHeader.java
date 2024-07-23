package net.pladema.emergencycare.application.getemergencycaredocumentheader;


import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentHeaderPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.domain.BedBo;
import net.pladema.clinichistory.hospitalization.service.domain.RoomBo;
import net.pladema.clinichistory.hospitalization.service.domain.SectorBo;
import net.pladema.emergencycare.application.getemergencycaredocumentheader.exceptions.GetEmergencyCareDocumentHeaderException;
import net.pladema.emergencycare.application.port.output.GetUserCompleteNameByUserIdPort;
import net.pladema.emergencycare.domain.EmergencyCareDocumentHeaderBo;
import net.pladema.emergencycare.domain.exceptions.GetEmergencyCareDocumentHeaderEnumException;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.service.BedService;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetEmergencyCareDocumentHeader {

    private final InstitutionService institutionService;
    private final DocumentHeaderPort documentHeaderPort;
    private final SharedStaffPort sharedStaffPort;
    private final BedService bedService;
    private final EmergencyCareEpisodeService emergencyCareEpisodeService;
    private final HistoricEmergencyEpisodeService historicEmergencyEpisodeService;
    private final GetUserCompleteNameByUserIdPort getUserCompleteNameByUserIdPort;

    public EmergencyCareDocumentHeaderBo run(Integer institutionId,Integer emergencyCareEpisodeId,Long documentId) {
        log.debug("Input parameters -> institutionId {}, emergencyCareEpisodeId {}, documentId {}", institutionId, emergencyCareEpisodeId, documentId);

        var result = new EmergencyCareDocumentHeaderBo();

        var base = documentHeaderPort.getDocumentHeader(documentId);
        this.validateInputParameters(institutionId,emergencyCareEpisodeId,base);
        result.setBaseDocumentHeader(base);
        this.setInstitutionName(result);
        this.setProfessionalInformation(result);
        this.setBedInformation(result);

        log.debug("Output -> {}", result);
        return result;
    }

    private void setBedInformation(EmergencyCareDocumentHeaderBo documentHeaderBo) {
        var episode = emergencyCareEpisodeService.get(
                documentHeaderBo.getEncounterId(),
                documentHeaderBo.getInstitutionId()
        );
        Optional<HistoricEmergencyEpisodeBo> lastEpisodeVersion =
                findLastEpisodeVersionBeforeDocuemtCreateDate(episode.getId(),documentHeaderBo.getCreatedOn());

        if (lastEpisodeVersion.isEmpty()) {
            Optional.ofNullable(episode.getBed())
                    .flatMap(bedBo-> bedService.getBedInfo(bedBo.getId())
                    .map(this::toBedBo))
                    .ifPresent(documentHeaderBo::setBed);
        } else {
            Optional.ofNullable(lastEpisodeVersion.get().getBedId())
                    .flatMap(bedId -> bedService.getBedInfo(bedId)
                    .map(this::toBedBo))
                    .ifPresent(documentHeaderBo::setBed);
        }
    }

    private Optional<HistoricEmergencyEpisodeBo> findLastEpisodeVersionBeforeDocuemtCreateDate(Integer episodeId, LocalDateTime documentCreatedOn) {
        return historicEmergencyEpisodeService.getAllHistoricByEmergecyEpisodeId(episodeId)
                .stream()
                .filter(he -> he.getChangeStateDate().isBefore(documentCreatedOn))
                .max(Comparator.comparing(HistoricEmergencyEpisodeBo::getChangeStateDate));
    }

    private void setClinicalSpecialtyName(EmergencyCareDocumentHeaderBo documentHeaderBo) {
        List<ClinicalSpecialtyBo> specialties = this.getClinicalSpecialtyFromInternmentInformation(documentHeaderBo.getCreatedBy());
        var clinicalSpecialtyName = this.buildClinicalSpecialtyName(specialties);
        documentHeaderBo.setClinicalSpecialtyName(clinicalSpecialtyName);
    }

    private void setInstitutionName(EmergencyCareDocumentHeaderBo documentHeaderBo) {
        String institutionName = institutionService.get(documentHeaderBo.getInstitutionId()).getName();
        documentHeaderBo.setInstitutionName(institutionName);
    }

    private void setProfessionalInformation(EmergencyCareDocumentHeaderBo result) {
        String userCompleteName;
        try {
            userCompleteName = sharedStaffPort.getProfessionalCompleteNameByUserId(result.getCreatedBy())
                    .orElseThrow(() -> new NotFoundException(null, null));
            this.setClinicalSpecialtyName(result);

        } catch (NotFoundException e) {
            userCompleteName = this.getAdministrativeName(result.getCreatedBy());
        }
        result.setProfessionalName(userCompleteName);
    }

    private String getAdministrativeName(Integer userId) {
        return getUserCompleteNameByUserIdPort.run(userId);
    }

    private List<ClinicalSpecialtyBo> getClinicalSpecialtyFromInternmentInformation(Integer createdBy) {
        var professional = sharedStaffPort.getProfessionalCompleteInfo(createdBy);
        return this.mapTo(professional.getClinicalSpecialties());
    }

    private void validateInputParameters(Integer institutionId, Integer emergencyCareEpisodeId, IDocumentHeaderBo base) {
        if (!Objects.equals(base.getEncounterId(), emergencyCareEpisodeId)){
            throw new GetEmergencyCareDocumentHeaderException(
                    GetEmergencyCareDocumentHeaderEnumException.INPUT_PARAMETERS_NOT_VALID,
                    String.format(
                            "El documento con id %s no se corresponde con el episodio de guardia %s",
                            base.getId(),
                            emergencyCareEpisodeId
                    )
            );
        }
        if (!Objects.equals(base.getInstitutionId(), institutionId)) {
            throw new GetEmergencyCareDocumentHeaderException(
                    GetEmergencyCareDocumentHeaderEnumException.INPUT_PARAMETERS_NOT_VALID,
                    String.format(
                            "El documento con id %s no pertenece a la institución con id %s",
                            base.getId(),
                            institutionId
                    )
            );
        }
    }

    private String buildClinicalSpecialtyName(List<ClinicalSpecialtyBo> specialties) {
        if (specialties.isEmpty())
            return "";
        String firstClinicalSpecialty = specialties.get(0).getName();
        int size = specialties.size();
        if (size == 1)
            return firstClinicalSpecialty;

        int cantMoreSpecialties = size - 1;
        return String.format("%s (+%d)", firstClinicalSpecialty, cantMoreSpecialties);
    }

    private List<ClinicalSpecialtyBo> mapTo(List<ClinicalSpecialtyDto> clinicalSpecialties) {
        return clinicalSpecialties.stream()
                .map(clinicalSpecialtyDto -> new ClinicalSpecialtyBo(clinicalSpecialtyDto.getId(), clinicalSpecialtyDto.getName()))
                .collect(Collectors.toList());
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
}
