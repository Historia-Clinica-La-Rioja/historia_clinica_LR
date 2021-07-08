package ar.lamansys.immunization.infrastructure.output.repository.document;

import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentStorage;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImmunizationDocumentStorageImpl implements ImmunizationDocumentStorage {

    private final Logger logger;

    private final DocumentExternalFactory documentExternalFactory;

    private final LocalDateMapper localDateMapper;

    public ImmunizationDocumentStorageImpl(DocumentExternalFactory documentExternalFactory, LocalDateMapper localDateMapper) {
        this.documentExternalFactory = documentExternalFactory;
        this.localDateMapper = localDateMapper;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void save(ImmunizationDocumentBo immunizationDocumentBo) {
        logger.debug("Save new immunization document -> {}", immunizationDocumentBo);
        documentExternalFactory.run(mapTo(immunizationDocumentBo), true);
    }

    private DocumentDto mapTo(ImmunizationDocumentBo immunizationDocumentBo) {
        DocumentDto result = new DocumentDto();
        result.setPatientId(immunizationDocumentBo.getPatientId());
        result.setEncounterId(immunizationDocumentBo.getEncounterId());
        result.setInstitutionId(immunizationDocumentBo.getInstitutionId());
        result.setClinicalSpecialtyId(immunizationDocumentBo.getClinicalSpecialtyId());
        result.setImmunizations(mapTo(immunizationDocumentBo.getImmunizations()));
        result.setDocumentType(DocumentType.IMMUNIZATION);
        result.setDocumentSource(SourceType.IMMUNIZATION);
        return result;
    }

    private List<ImmunizationDto> mapTo(List<ImmunizationInfoBo> immunizations) {
        return immunizations.stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    private ImmunizationDto mapTo(ImmunizationInfoBo immunizationInfoBo) {
        ImmunizationDto result = new ImmunizationDto();
        result.setAdministrationDate(localDateMapper.fromLocalDateToString(immunizationInfoBo.getAdministrationDate()));
        result.setInstitutionId(immunizationInfoBo.getInstitutionId());
        result.setNote(immunizationInfoBo.getNote());
        result.setDoseId(immunizationInfoBo.getDose().getId());
        result.setConditionId(immunizationInfoBo.getCondition().getId());
        result.setSchemeId(immunizationInfoBo.getSchemeId());
        result.setSnomed(new SnomedDto(
                immunizationInfoBo.getVaccine().getSctid(),
                immunizationInfoBo.getVaccine().getPt()));
        result.setLotNumber(immunizationInfoBo.getLotNumber());
        return result;
    }
}
