import { Injectable } from '@angular/core';
import { ResponseEpicrisisDto } from '@api-rest/api-model';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { ClinicalEvaluationData, DescriptionItemDataInfo } from '@historia-clinica/utils/document-summary.model';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';

@Injectable({
    providedIn: 'root'
})
export class EpicrisisDocumentSummaryService {

    constructor(
        private readonly documentsSummaryService: DocumentsSummaryMapperService,
    ) { }

    mapEpicrisisAsViewFormat(epicrisis: ResponseEpicrisisDto): EpicrisisViewFormat {
        return {
            ...(epicrisis.mainDiagnosis && { mainDiagnosis: [{ description: this.documentsSummaryService.mapDescriptionAndStatusToString(epicrisis.mainDiagnosis) }] }),
            ...(epicrisis.diagnosis.length && { diagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(epicrisis.diagnosis)} ),
            ...(this.documentsSummaryService.hasClinicalEvaluations(epicrisis.notes) && { clinicalEvaluation: this.documentsSummaryService.mapClinicalEvaluationToDescriptionItemData(epicrisis.notes)} ),
            ...(epicrisis.procedures.length && { procedures: this.documentsSummaryService.mapProceduresToDescriptionItemDataSummary(epicrisis.procedures)} ),
            ...(epicrisis.personalHistories?.length && { personalHistories: this.documentsSummaryService.mapPersonalHistoriesToDescriptionItemDataSummary(epicrisis.personalHistories)} ),
            ...(epicrisis.familyHistories?.length && { familyHistories: this.documentsSummaryService.mapFamilyHistoriesToDescriptionItemDataSummary(epicrisis.familyHistories)} ),
            ...(epicrisis.allergies?.length && { allergies: this.documentsSummaryService.mapAllergiesToDescriptionItemDataSummary(epicrisis.allergies)} ),
            ...(epicrisis.medications.length && { medications: this.documentsSummaryService.mapMedicationsToDescriptionItemDataSummary(epicrisis.medications)} ),
            ...(epicrisis.immunizations.length && { vaccines: this.documentsSummaryService.mapVaccinesToDescriptionItemDataSummary(epicrisis.immunizations)} ),
            ...(epicrisis.otherProblems.length && { otherProblems: this.documentsSummaryService.mapOtherProblemsToDescriptionItemDataSummary(epicrisis.otherProblems)} ),
            ...(epicrisis.externalCause && { externalCause: this.documentsSummaryService.mapExternalCauseToDescriptionItemDataInfo(epicrisis.externalCause)} ),
        }
    }
}

export interface EpicrisisViewFormat {
    mainDiagnosis: DescriptionItemData[],
    diagnosis: DescriptionItemData[],
    clinicalEvaluation: ClinicalEvaluationData,
    procedures: DescriptionItemDataSummary,
    personalHistories: DescriptionItemDataSummary,
    familyHistories: DescriptionItemDataSummary,
    allergies: DescriptionItemDataSummary,
    medications: DescriptionItemDataSummary,
    otherProblems: DescriptionItemDataSummary,
    externalCause: DescriptionItemDataInfo[],
}