import { Injectable } from '@angular/core';
import { ResponseAnamnesisDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary.service';
import { AnthropometricData, ClinicalEvaluationData, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';

@Injectable({
    providedIn: 'root'
})
export class AnamnesisDocumentSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    getAnamnesisAsViewFormat(anamnesis: ResponseAnamnesisDto): AnamnesisAsViewFormat {
        return {
            mainDiagnosis: anamnesis.mainDiagnosis ? [{ description: this.documentsSummaryService.getDescriptionAndStatus(anamnesis.mainDiagnosis) }] : null,
            diagnosis: anamnesis.diagnosis.length ? this.documentsSummaryService.getDiagnosisAsStringArray(anamnesis.diagnosis) : null,
            clinicalEvaluation: this.documentsSummaryService.hasClinicalEvaluations(anamnesis.notes) ? this.documentsSummaryService.getClinicalEvaluationAsStringArray(anamnesis.notes) : null,
            procedures: anamnesis.procedures.length ? this.documentsSummaryService.getProceduresAsStringArray(anamnesis.procedures) : null,
            anthropometricData: anamnesis.anthropometricData ? this.documentsSummaryService.getAnthropometricDataAsStrings(anamnesis.anthropometricData) : null,
            vitalSignsAndRiskFactors: anamnesis.riskFactors ? this.documentsSummaryService.getVitalSignsAndRiskFactors(anamnesis.riskFactors) : null,
            allergies: anamnesis.allergies.length ? this.documentsSummaryService.getAllergiesAsStringArray(anamnesis.allergies) : null,
            vaccines: anamnesis.immunizations.length ? this.documentsSummaryService.getVaccinesAsStringArray(anamnesis.immunizations) : null,
            personalHistories: anamnesis.personalHistories.length ? this.documentsSummaryService.getHistoriesAsStringArray(anamnesis.personalHistories) : null,
            familyHistories: anamnesis.familyHistories.length ? this.documentsSummaryService.getHistoriesAsStringArray(anamnesis.familyHistories) : null,
            medications: anamnesis.medications.length ? this.documentsSummaryService.getMedicationsAsStringArray(anamnesis.medications) : null,
        }
    }
}

export interface AnamnesisAsViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    clinicalEvaluation: ClinicalEvaluationData,
    procedures: DescriptionItemData[],
    anthropometricData: AnthropometricData,
    vitalSignsAndRiskFactors: VitalSignsAndRiskFactorsData,
    allergies: DescriptionItemData[],
    vaccines: DescriptionItemData[],
    personalHistories: DescriptionItemData[],
    familyHistories: DescriptionItemData[],
    medications: DescriptionItemData[],
}