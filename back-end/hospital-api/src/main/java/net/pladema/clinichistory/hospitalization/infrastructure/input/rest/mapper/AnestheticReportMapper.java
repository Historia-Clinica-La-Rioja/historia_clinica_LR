package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnestheticHistoryMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.HealthConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.FoodIntakeMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.MedicationMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnestheticSubstanceMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.ProcedureDescriptionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.AnestheticReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, HealthConditionMapper.class, AnthropometricDataMapper.class, MedicationMapper.class, AnestheticSubstanceMapper.class, FoodIntakeMapper.class, RiskFactorMapper.class, AnestheticHistoryMapper.class, ProcedureDescriptionMapper.class})
public interface AnestheticReportMapper {

    @Named("fromAnestheticReportDto")
    @Mapping(target = "mainDiagnosis", source = "mainDiagnosis", qualifiedByName = "toHealthConditionBoFromDiagnosisDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisBo")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataDto")
    @Mapping(target = "riskFactors", source = "riskFactors", qualifiedByName = "fromRiskFactorDto")
    @Mapping(target = "anestheticHistory", source = "anestheticHistory", qualifiedByName = "toAnestheticHistoryBo")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationBo")
    @Mapping(target = "preMedications", source = "preMedications", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "foodIntake", source = "foodIntake", qualifiedByName = "toFoodIntakeBo")
    @Mapping(target = "histories", source = "histories", qualifiedByName = "toListHealthConditionBo")
    @Mapping(target = "procedureDescription", source = "procedureDescription", qualifiedByName = "toProcedureDescriptionBo")
    AnestheticReportBo fromAnestheticReportDto(AnestheticReportDto anestheticReport);

    @Named("fromAnestheticReportBo")
    @Mapping(target = "mainDiagnosis", source = "mainDiagnosis", qualifiedByName = "toDiagnosisDtoFromHealthConditionBo")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "riskFactors", source = "riskFactors", qualifiedByName = "fromRiskFactorBo")
    @Mapping(target = "anestheticHistory", source = "anestheticHistory", qualifiedByName = "toAnestheticHistoryDto")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationDto")
    @Mapping(target = "preMedications", source = "preMedications", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "foodIntake", source = "foodIntake", qualifiedByName = "toFoodIntakeDto")
    @Mapping(target = "histories", source = "histories", qualifiedByName = "toListHealthConditionDto")
    @Mapping(target = "procedureDescription", source = "procedureDescription", qualifiedByName = "toProcedureDescriptionDto")
    AnestheticReportDto fromAnestheticReportBo(AnestheticReportBo anestheticReport);
}
