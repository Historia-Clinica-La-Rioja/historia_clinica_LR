import { Injectable } from '@angular/core';
import { AllergyConditionDto, AnthropometricDataDto, DiagnosisDto, DocumentObservationsDto, HealthHistoryConditionDto, HospitalizationDocumentHeaderDto, HospitalizationProcedureDto, ImmunizationDto, MedicationDto, RiskFactorDto } from '@api-rest/api-model';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { TranslateService } from '@ngx-translate/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { AnthropometricData, ClinicalEvaluationData, HeaderDescription, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';
import { fromStringToDateByDelimeter } from '@core/utils/date.utils';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';

const CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
const PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;
const INFO_DIVIDER = ' | ';

@Injectable({
    providedIn: 'root'
})
export class DocumentsSummaryMapperService {

    private confirmedStatus: string = '';
    private presumptiveStatus: string = '';

    constructor(
        private readonly translateService: TranslateService,
        private readonly dateFormatPipe: DateFormatPipe
    ) {
        this.confirmedStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.CONFIRMED')
        this.presumptiveStatus = this.translateService.instant('internaciones.anesthesic-report.diagnosis.PRESUMPTIVE')
    }

    mapToHeaderDescription(header: HospitalizationDocumentHeaderDto, title: string, activeDocument: DocumentSearch): HeaderDescription {
        return {
            title,
            edit: activeDocument.canDoAction.edit,
            delete: activeDocument.canDoAction.delete,
            headerDescriptionData: {
                scope: header.sourceTypeName,
                specialty: header.clinicalSpecialtyName,
                dateTime: this.dateFormatPipe.transform(dateTimeDtotoLocalDate(header.createdOn), 'datetime'),
                professional: header.professionalName,
                institution: header.institutionName,
                sector: header.bed?.room.sector.description,
                room: header.bed?.room.description,
                bed: header.bed?.bedNumber,
            },
        }
    }

    getDiagnosisAsStringArray(diagnosis: DiagnosisDto[]): DescriptionItemData[] {
        return diagnosis.map(diag => {
            return { description: this.getDescriptionAndStatus(diag) };
        })
    }

    getDescriptionAndStatus(diagnosis: DiagnosisDto): string {
        if (diagnosis.verificationId === CONFIRMED) {
            return diagnosis.snomed.pt + ' ' + this.confirmedStatus
        }
        if (diagnosis.verificationId === PRESUMPTIVE) {
            return diagnosis.snomed.pt + ' ' + this.presumptiveStatus
        }
        return diagnosis.snomed.pt
    }

    getProceduresAsStringArray(procedures: HospitalizationProcedureDto[]): DescriptionItemData[] {
        return procedures.map(procedure => {
            return { 
                description: procedure.snomed.pt ,
                dateOrTime: procedure.performedDate ? { date: fromStringToDateByDelimeter(procedure.performedDate, '-') } : null,
            }
        })
    }

    getAnthropometricDataAsStrings(antropometricData: AnthropometricDataDto): AnthropometricData {
        return {
            bloodType: antropometricData.bloodType ? [{ description: antropometricData.bloodType.value }] : null,
            height: antropometricData.height ? [{ description: antropometricData.height.value }] : null,
            weight: antropometricData.weight ? [{ description: antropometricData.weight.value + 'Kg' }] : null,
        }
    }

    getVitalSignsAndRiskFactors(riskFactors: RiskFactorDto): VitalSignsAndRiskFactorsData {
        return {
            bloodGlucose: riskFactors.bloodGlucose ? [{ description: riskFactors.bloodGlucose.value + 'mg/dl'}] : null,
            bloodOxygenSaturation: riskFactors.bloodOxygenSaturation ? [{ description: riskFactors.bloodOxygenSaturation.value + '%'}] : null,
            cardiovascularRisk: riskFactors.cardiovascularRisk ? [{ description: riskFactors.cardiovascularRisk.value + '%' }] : null,
            diastolicBloodPressure: riskFactors.diastolicBloodPressure ? [{ description: riskFactors.diastolicBloodPressure.value + 'mm' }] : null,
            glycosylatedHemoglobin: riskFactors.glycosylatedHemoglobin ? [{ description: riskFactors.glycosylatedHemoglobin.value + '%' }] : null,
            heartRate: riskFactors.heartRate ? [{ description: riskFactors.heartRate.value + '/min'  }] : null,
            hematocrit: riskFactors.hematocrit ? [{ description: riskFactors.hematocrit.value + '%'}] : null,
            respiratoryRate: riskFactors.respiratoryRate ? [{ description: riskFactors.respiratoryRate.value + '/min' }] : null,
            systolicBloodPressure: riskFactors.systolicBloodPressure ? [{ description: riskFactors.systolicBloodPressure.value + 'mm' }] : null,
            temperature: riskFactors.temperature ? [{ description: riskFactors.temperature.value + '°'}] : null,
        }
    }

    getAllergiesAsStringArray(allergies: AllergyConditionDto[]): DescriptionItemData[] {
        return allergies.map(allergie => {
            return { description: allergie.snomed.pt };
        })
    }

    getVaccinesAsStringArray(vaccines: ImmunizationDto[]): DescriptionItemData[] {
        return vaccines.map(vaccine => {
            return {
                description: vaccine.snomed.pt,
                dateOrTime: vaccine.administrationDate ? { date: fromStringToDateByDelimeter(vaccine.administrationDate, '-') } : null,
            };
        })
    }

    getHistoriesAsStringArray(histories: HealthHistoryConditionDto[]): DescriptionItemData[] {
        return histories.map(history => {
            return {
                description: history.snomed.pt,
                dateOrTime: history.startDate ? { date: fromStringToDateByDelimeter(history.startDate, '-') } : null,
            };
        })
    }    
    
    getMedicationsAsStringArray(medications: MedicationDto[]): DescriptionItemData[] {
        return medications.map(medication => {
            return { description: medication.note ? medication.snomed.pt + this.isSuspended(medication) + INFO_DIVIDER + medication.note : medication.snomed.pt + this.isSuspended(medication) };
        })
    }

    private isSuspended(medication: MedicationDto): string {
        return medication.suspended ? ' (Suspendida)' : ''
    }

    hasClinicalEvaluations(notes: DocumentObservationsDto): boolean {
        return (
            !!notes.currentIllnessNote?.length 
            || !!notes.physicalExamNote?.length 
            || !!notes.studiesSummaryNote?.length 
            || !!notes.evolutionNote?.length 
            || !!notes.clinicalImpressionNote?.length 
            || !!notes.otherNote?.length)
    }

    getClinicalEvaluationAsStringArray(notes: DocumentObservationsDto): ClinicalEvaluationData {
        return {
            clinicalImpressionNote: notes.clinicalImpressionNote ? [{ description: notes.clinicalImpressionNote }] : null,
            currentIllnessNote: notes.currentIllnessNote ? [{ description: notes.currentIllnessNote }] : null,
            evolutionNote: notes.evolutionNote ? [{ description: notes.evolutionNote }] : null,
            indicationsNote: notes.indicationsNote ? [{ description: notes.indicationsNote }] : null,
            otherNote: notes.otherNote ? [{ description: notes.otherNote }] : null,
            physicalExamNote: notes.physicalExamNote ? [{ description: notes.physicalExamNote }] : null,
            studiesSummaryNote: notes.studiesSummaryNote ? [{ description: notes.studiesSummaryNote }] : null,
        }
    }
}