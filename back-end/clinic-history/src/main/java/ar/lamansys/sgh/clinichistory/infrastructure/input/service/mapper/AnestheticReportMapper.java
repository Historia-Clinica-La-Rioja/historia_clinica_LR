package ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnalgesicTechniqueMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnestheticHistoryMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnestheticTechniqueMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.HealthConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.FoodIntakeMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.MeasuringPointMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.MedicationMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnestheticSubstanceMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.PostAnesthesiaStatusMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.ProcedureDescriptionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.AnestheticReportDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, HealthConditionMapper.class, AnthropometricDataMapper.class,
        MedicationMapper.class, AnestheticSubstanceMapper.class, FoodIntakeMapper.class, RiskFactorMapper.class,
        AnestheticHistoryMapper.class, ProcedureDescriptionMapper.class, AnalgesicTechniqueMapper.class,
        AnestheticTechniqueMapper.class, MeasuringPointMapper.class, PostAnesthesiaStatusMapper.class},
        builder = @Builder(disableBuilder = true))
public interface AnestheticReportMapper {

    @Named("fromAnestheticReportDto")
    @Mapping(target = "mainDiagnosis", source = "mainDiagnosis", qualifiedByName = "toHealthConditionBoFromDiagnosisDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisBo")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataDto")
    @Mapping(target = "riskFactors", source = "riskFactors", qualifiedByName = "fromRiskFactorDto")
    @Mapping(target = "anestheticHistory", source = "anestheticHistory", qualifiedByName = "toAnestheticHistoryBo")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationBo")
    @Mapping(target = "preMedications", source = "preMedications", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "histories", source = "histories", qualifiedByName = "toListHealthConditionBo")
    @Mapping(target = "procedureDescription", source = "procedureDescription", qualifiedByName = "toProcedureDescriptionBo")
    @Mapping(target = "anestheticPlans", source = "anestheticPlans", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "analgesicTechniques", source = "analgesicTechniques", qualifiedByName = "toListAnalgesicTechniqueBo")
    @Mapping(target = "anestheticTechniques", source = "anestheticTechniques", qualifiedByName = "toListAnestheticTechniqueBo")
    @Mapping(target = "fluidAdministrations", source = "fluidAdministrations", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "anestheticAgents", source = "anestheticAgents", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "nonAnestheticDrugs", source = "nonAnestheticDrugs", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "antibioticProphylaxis", source = "antibioticProphylaxis", qualifiedByName = "toListAnestheticSubstanceBo")
    @Mapping(target = "procedureDescription.foodIntake", source = "foodIntake.clockTime")
    @Mapping(target = "measuringPoints", source = "measuringPoints", qualifiedByName = "toListMeasuringPointBo")
    @Mapping(target = "postAnesthesiaStatus", source = "postAnesthesiaStatus", qualifiedByName = "toPostAnesthesiaStatusBo")
    @Mapping(target = "documentSource", ignore = true)
    @Mapping(target = "documentType", ignore = true)
    AnestheticReportBo fromAnestheticReportDto(AnestheticReportDto anestheticReport);

    @Named("fromAnestheticReportBo")
    @Mapping(target = "mainDiagnosis", source = "mainDiagnosis", qualifiedByName = "toDiagnosisDtoFromHealthConditionBo")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "riskFactors", source = "riskFactors", qualifiedByName = "fromRiskFactorBo")
    @Mapping(target = "anestheticHistory", source = "anestheticHistory", qualifiedByName = "toAnestheticHistoryDto")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationDto")
    @Mapping(target = "preMedications", source = "preMedications", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "foodIntake", source = "procedureDescription", qualifiedByName = "toFoodIntakeDto")
    @Mapping(target = "histories", source = "histories", qualifiedByName = "toListHealthConditionDto")
    @Mapping(target = "procedureDescription", source = "procedureDescription", qualifiedByName = "toProcedureDescriptionDto")
    @Mapping(target = "anestheticPlans", source = "anestheticPlans", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "analgesicTechniques", source = "analgesicTechniques", qualifiedByName = "toListAnalgesicTechniqueDto")
    @Mapping(target = "anestheticTechniques", source = "anestheticTechniques", qualifiedByName = "toListAnestheticTechniqueDto")
    @Mapping(target = "fluidAdministrations", source = "fluidAdministrations", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "anestheticAgents", source = "anestheticAgents", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "nonAnestheticDrugs", source = "nonAnestheticDrugs", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "antibioticProphylaxis", source = "antibioticProphylaxis", qualifiedByName = "toListAnestheticSubstanceDto")
    @Mapping(target = "measuringPoints", source = "measuringPoints", qualifiedByName = "toListMeasuringPointDto")
    @Mapping(target = "postAnesthesiaStatus", source = "postAnesthesiaStatus", qualifiedByName = "toPostAnesthesiaStatusDto")
    AnestheticReportDto fromAnestheticReportBo(AnestheticReportBo anestheticReport);
}
