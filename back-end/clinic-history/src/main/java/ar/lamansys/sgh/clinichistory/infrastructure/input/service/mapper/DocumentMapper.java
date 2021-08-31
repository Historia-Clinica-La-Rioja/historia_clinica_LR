package ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AllergyConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.HealthConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.ImmunizationMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.MedicationMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.VitalSignMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {AllergyConditionMapper.class, HealthConditionMapper.class, ImmunizationMapper.class, MedicationMapper.class,
        VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class, SnomedMapper.class})
public interface DocumentMapper {

    @Named("fromDocumentDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromAnthropometricDataDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromListAnthropometricDataDto")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toMedicationBo")
    @Mapping(target = "immunizations", source = "immunizations", qualifiedByName = "toImmunizationBo")
    @Mapping(target = "allergies", source = "allergies", qualifiedByName = "toAllergyConditionBo")
    @Mapping(target = "personalHistories", source = "personalHistories", qualifiedByName = "toHealthHistoryConditionBo")
    @Mapping(target = "familyHistories", source = "familyHistories", qualifiedByName = "toHealthHistoryConditionBo")
    DocumentBo from(DocumentDto documentDto);

}
