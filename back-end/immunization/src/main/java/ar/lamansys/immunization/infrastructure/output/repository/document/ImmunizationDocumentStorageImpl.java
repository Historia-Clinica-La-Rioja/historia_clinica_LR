package ar.lamansys.immunization.infrastructure.output.repository.document;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentStorage;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

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
    public Long save(ImmunizationDocumentBo immunizationDocumentBo) {
        logger.debug("Save new immunization document -> {}", immunizationDocumentBo);
        return documentExternalFactory.run(mapTo(immunizationDocumentBo), true);
    }

    private DocumentDto mapTo(ImmunizationDocumentBo immunizationDocumentBo) {
        DocumentDto result = new DocumentDto();
        result.setPatientId(immunizationDocumentBo.getPatientId());
        result.setEncounterId(immunizationDocumentBo.getEncounterId());
        result.setInstitutionId(immunizationDocumentBo.getInstitutionId());
        result.setClinicalSpecialtyId(immunizationDocumentBo.getClinicalSpecialtyId());
        result.setImmunizations(mapTo(immunizationDocumentBo.getImmunizations()));
        result.setPerformedDate(localDateMapper.toDateDto(immunizationDocumentBo.getPerformedDate()));
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
        result.setInstitutionInfo(immunizationInfoBo.getInstitutionInfo());
        result.setDoctorInfo(immunizationInfoBo.getDoctorInfo());
        result.setNote(immunizationInfoBo.getNote());
        result.setDose(mapDose(immunizationInfoBo.getDose()));
        result.setConditionId(immunizationInfoBo.getConditionId());
        result.setSchemeId(immunizationInfoBo.getSchemeId());
        result.setSnomed(new SnomedDto(
                immunizationInfoBo.getVaccine().getSctid(),
                immunizationInfoBo.getVaccine().getPt()));
        result.setLotNumber(immunizationInfoBo.getLotNumber());
        result.setBillable(immunizationInfoBo.isBillable());
        return result;
    }

    private VaccineDoseInfoDto mapDose(VaccineDoseBo dose) {
        if (dose == null)
            return null;
        return new VaccineDoseInfoDto(dose.getDescription(), dose.getOrder());
    }
}
