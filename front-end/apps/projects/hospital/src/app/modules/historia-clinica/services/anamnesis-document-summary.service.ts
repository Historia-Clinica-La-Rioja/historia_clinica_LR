import { Injectable } from '@angular/core';
import { ResponseAnamnesisDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { AnthropometricData, ClinicalEvaluationData, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';

@Injectable({
    providedIn: 'root'
})
export class AnamnesisDocumentSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapToAnamnesisAsViewFormat(anamnesis: ResponseAnamnesisDto): AnamnesisAsViewFormat {
        return {
            ...(anamnesis.mainDiagnosis && { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(anamnesis.mainDiagnosis) }] }),
            ...(anamnesis.diagnosis.length && { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(anamnesis.diagnosis)} ),
            ...(this.documentsSummaryService.hasClinicalEvaluations(anamnesis.notes) && { clinicalEvaluation: this.documentsSummaryService.mapClinicalEvaluationToDescriptionItemData(anamnesis.notes)} ),
            ...(anamnesis.procedures.length && { procedures: this.documentsSummaryService.mapProceduresToDescriptionItemDataSummary(anamnesis.procedures)} ),
            ...(anamnesis.anthropometricData && { anthropometricData: this.documentsSummaryService.mapToAnthropometricData(anamnesis.anthropometricData)} ),
            ...(anamnesis.riskFactors && { vitalSignsAndRiskFactors: this.documentsSummaryService.mapToVitalSignsAndRiskFactors(anamnesis.riskFactors)} ),
            ...(anamnesis.allergies.content?.length && { allergies: this.documentsSummaryService.mapAllergiesToDescriptionItemDataSummary(anamnesis.allergies.content)} ),
            ...(anamnesis.immunizations.length && { vaccines: this.documentsSummaryService.mapVaccinesToDescriptionItemDataSummary(anamnesis.immunizations)} ),
            ...(anamnesis.personalHistories.content?.length && { personalHistories: this.documentsSummaryService.mapPersonalHistoriesToDescriptionItemDataSummary(anamnesis.personalHistories.content)} ),
            ...(anamnesis.familyHistories.content?.length && { familyHistories: this.documentsSummaryService.mapFamilyHistoriesToDescriptionItemDataSummary(anamnesis.familyHistories.content)} ),
            ...(anamnesis.medications.length && { medications: this.documentsSummaryService.mapMedicationsToDescriptionItemDataSummary(anamnesis.medications)} ),
        }
    }
}

export interface AnamnesisAsViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    clinicalEvaluation: ClinicalEvaluationData,
    procedures: DescriptionItemDataSummary,
    anthropometricData: AnthropometricData,
    vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData,
    allergies: DescriptionItemDataSummary,
    vaccines: DescriptionItemDataSummary,
    personalHistories: DescriptionItemDataSummary,
    familyHistories: DescriptionItemDataSummary,
    medications: DescriptionItemDataSummary,
}