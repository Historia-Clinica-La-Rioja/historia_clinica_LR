package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnalgesicTechniqueDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnestheticHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnestheticTechniqueDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.FoodIntakeDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.MeasuringPointDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.MedicationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnestheticSubstanceDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.PostAnesthesiaStatusDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ProcedureDescriptionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HospitalizationProcedureDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnestheticReportDto {

    @Nullable
    private @Valid DiagnosisDto mainDiagnosis;

    @Nullable
    private @Valid List<DiagnosisDto> diagnosis = new ArrayList<>();

    @Nullable
    private @Valid List<HospitalizationProcedureDto> surgeryProcedures = new ArrayList<>();

    @Nullable
    private @Valid AnthropometricDataDto anthropometricData;

    @Nullable
    private @Valid RiskFactorDto riskFactors;

    @Nullable
    private @Valid AnestheticHistoryDto anestheticHistory;

    @Nullable
    private @Valid List<MedicationDto> medications = new ArrayList<>();

    @Nullable
    private @Valid List<AnestheticSubstanceDto> preMedications = new ArrayList<>();

    @Nullable
    private @Valid FoodIntakeDto foodIntake;

    @Nullable
    private @Valid List<HealthConditionDto> histories = new ArrayList<>();

    @Nullable
    private @Valid ProcedureDescriptionDto procedureDescription;

    @Nullable
    private @Valid List<AnestheticSubstanceDto> anestheticPlans = new ArrayList<>();

    @Nullable
    private @Valid List<AnalgesicTechniqueDto> analgesicTechniques = new ArrayList<>();

    @Nullable
    private @Valid List<AnestheticTechniqueDto> anestheticTechniques = new ArrayList<>();

    @Nullable
    private @Valid List<AnestheticSubstanceDto> fluidAdministrations = new ArrayList<>();

    @Nullable
    private @Valid List<AnestheticSubstanceDto> anestheticAgents = new ArrayList<>();

    @Nullable
    private @Valid List<AnestheticSubstanceDto> nonAnestheticDrugs = new ArrayList<>();

    @Nullable
    private @Valid List<AnestheticSubstanceDto> antibioticProphylaxis = new ArrayList<>();

    @Nullable
    private @Valid List<MeasuringPointDto> measuringPoints = new ArrayList<>();

    @Nullable
    private @Valid PostAnesthesiaStatusDto postAnesthesiaStatus;

    @Nullable
    private String anestheticChart;

    private boolean confirmed = false;
}
