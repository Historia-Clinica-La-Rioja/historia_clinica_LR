import { Injectable } from '@angular/core';
import { AllergyConditionDto, AnthropometricDataDto, DateTimeDto, DiagnosisDto, DocumentObservationsDto, ExternalCauseDto, HealthConditionDto, HealthHistoryConditionDto, HospitalizationDocumentHeaderDto, HospitalizationProcedureDto, ImmunizationDto, IsolationAlertDto, MedicationDto, NewbornDto, ObstetricEventDto, OutpatientAllergyConditionDto, OutpatientFamilyHistoryDto, OutpatientReasonDto, ReferableItemDto, RiskFactorDto, TriageAppearanceDto, TriageBreathingDto, TriageCirculationDto } from '@api-rest/api-model';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { TranslateService } from '@ngx-translate/core';
import { DateFormat, DateToShow, DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { AnthropometricData, ClinicalEvaluationData, DescriptionItemDataInfo, ExternalCauseData, HeaderDescription, HeaderIdentifierData, IsolationAlertDescriptionItemData, NewBornsData, NewBornsSummary, ObstetricEventData, ObstetricEventInfo, ReferredDescriptionItemData, TitleDescriptionListItem, VitalSignsAndRiskFactorsData } from '@historia-clinica/utils/document-summary.model';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { convertDateTimeDtoToDate, dateDtoToDate, dateTimeDtoToDate, dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { PROCEDURES_DESCRIPTION_ITEM, ALLERGIES_DESCRIPTION_ITEM, VITAL_SIGNS_AND_RISK_FACTORS, VACCINES_DESCRIPTION_ITEM, PERSONAL_HISTORIES_DESCRIPTION_ITEM, FAMILY_HISTORIES_DESCRIPTION_ITEM, USUAL_MEDICATIONS_DESCRIPTION_ITEM, HEADER_DATA_BED, HEADER_DATA_DATE, HEADER_DATA_INSTITUTION, HEADER_DATA_PATIENT, HEADER_DATA_PROFESSIONAL, HEADER_DATA_ROOM, HEADER_DATA_SCOPE, HEADER_DATA_SECTOR, HEADER_DATA_SPECIALTY, OTHER_PROBLEMS_DESCRIPTION_ITEM, ExternalCauseType, EventLocation, PregnancyTerminationType, BirthConditionType, Gender, REASONS_DESCRIPTION_ITEM, CRITICITY_DESCRIPTION, PROPOSED_SURGERIES_DESCRIPTION_ITEM } from '@historia-clinica/constants/document-summary.constants';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { toRegisterEditorInfo } from '@historia-clinica/mappers/isolation-alerts.mapper';

const CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
const PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;

@Injectable({
    providedIn: 'root'
})
export class DocumentsSummaryMapperService {

    private confirmedStatus = '';
    private presumptiveStatus = '';

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
            edit: activeDocument.document.confirmed ? activeDocument.canDoAction.edit : false,
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

    mapEmergencyCareToHeaderDescription(header: HospitalizationDocumentHeaderDto, title: string, canEdit?: boolean, canDelete?: boolean, canDownload?: boolean): HeaderDescription {
        return {
            title,
            edit: canEdit,
            delete: canDelete,
            download: canDownload,
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

    toDescriptionItemData(description: string, dateToShow?: DateToShow): DescriptionItemData {
        return {
            description,
            ...(dateToShow && { dateToShow }),
        }
    }

    mapDiagnosisToDescriptionItemData(diagnosis: DiagnosisDto[]): DescriptionItemData[] {
        return diagnosis.map(diag => this.toDescriptionItemData(this.mapDescriptionAndStatusToString(diag)));
    }

    mapDescriptionAndStatusToString(diagnosis: DiagnosisDto): string {
        if (diagnosis.verificationId === CONFIRMED) {
            return `${diagnosis.snomed.pt} ${this.confirmedStatus}`
        }
        if (diagnosis.verificationId === PRESUMPTIVE) {
            return `${diagnosis.snomed.pt} ${this.presumptiveStatus}`
        }
        return diagnosis.snomed.pt
    }

    mapProceduresToDescriptionItemData(procedures: HospitalizationProcedureDto[]): DescriptionItemData[] {
        return procedures.map(procedure => this.toDescriptionItemData(procedure.snomed.pt, this.mapStringToDateToShow(procedure.performedDate)));
    }

    mapStringToDateToShow(date: string): DateToShow {
        return date ? { date: dateISOParseDate(date), format: DateFormat.DATE } : null;
    }

    mapDateTimeDtoToDateToShow(date: DateTimeDto): DateToShow {
        return date ? { date: dateTimeDtoToDate(date), format: DateFormat.DATE_TIME } : null;
    }

    mapToAnthropometricData(anthropometricData: AnthropometricDataDto): AnthropometricData {
        return {
            ...(anthropometricData.height && { height: [this.toDescriptionItemData(`${anthropometricData.height.value} ${VITAL_SIGNS_AND_RISK_FACTORS.CENTIMETERS}`)] }),
            ...(anthropometricData.bloodType && { bloodType: [this.toDescriptionItemData(anthropometricData.bloodType.value)] }),
            ...(anthropometricData.weight && { weight: [this.toDescriptionItemData(`${anthropometricData.weight.value} ${VITAL_SIGNS_AND_RISK_FACTORS.KG}`)] }),
            ...(anthropometricData.headCircumference && { headCircunference: [this.toDescriptionItemData(`${anthropometricData.headCircumference.value} ${VITAL_SIGNS_AND_RISK_FACTORS.CENTIMETERS}`)] }),
        }
    }

    mapToVitalSignsAndRiskFactors(riskFactors: RiskFactorDto): VitalSignsAndRiskFactorsData {
        return {
            ...(riskFactors.bloodGlucose && { bloodGlucose: [this.toDescriptionItemData(`${riskFactors.bloodGlucose.value} ${VITAL_SIGNS_AND_RISK_FACTORS.MG_DL}`)] }),
            ...(riskFactors.bloodOxygenSaturation && { bloodOxygenSaturation: [this.toDescriptionItemData(`${riskFactors.bloodOxygenSaturation.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.cardiovascularRisk && { cardiovascularRisk: [this.toDescriptionItemData(`${riskFactors.cardiovascularRisk.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.diastolicBloodPressure && { diastolicBloodPressure: [this.toDescriptionItemData(`${riskFactors.diastolicBloodPressure.value} ${VITAL_SIGNS_AND_RISK_FACTORS.MILIMITERS}`)] }),
            ...(riskFactors.glycosylatedHemoglobin && { glycosylatedHemoglobin: [this.toDescriptionItemData(`${riskFactors.glycosylatedHemoglobin.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.heartRate && { heartRate: [this.toDescriptionItemData(`${riskFactors.heartRate.value}${VITAL_SIGNS_AND_RISK_FACTORS.MINUTE}`)] }),
            ...(riskFactors.hematocrit && { hematocrit: [this.toDescriptionItemData(`${riskFactors.hematocrit.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)] }),
            ...(riskFactors.respiratoryRate && { respiratoryRate: [this.toDescriptionItemData(`${riskFactors.respiratoryRate.value}${VITAL_SIGNS_AND_RISK_FACTORS.MINUTE}`)] }),
            ...(riskFactors.systolicBloodPressure && { systolicBloodPressure: [this.toDescriptionItemData(`${riskFactors.systolicBloodPressure.value} ${VITAL_SIGNS_AND_RISK_FACTORS.MILIMITERS}`)] }),
            ...(riskFactors.temperature && { temperature: [this.toDescriptionItemData(`${riskFactors.temperature.value}${VITAL_SIGNS_AND_RISK_FACTORS.TEMPERATURE}`)] }),
        }
    }

    mapAllergiesToDescriptionItemData(allergies: AllergyConditionDto[]): DescriptionItemData[] {
        return allergies.map(allergy => this.toDescriptionItemData(allergy.snomed.pt));
    }

    mapOutpatientAllergiesToDescriptionItemData(allergies: OutpatientAllergyConditionDto[]): DescriptionItemData[] {
        return allergies.map(allergy => this.toDescriptionItemData(this.getAllergyDescription(allergy), this.mapStringToDateToShow(allergy.startDate)));
    }

    getAllergyDescription(allergy: OutpatientAllergyConditionDto): string {
        return `${allergy.snomed.pt} (${this.mapAllergiesCriticityIdToDescription(allergy.criticalityId)})`
    }

    mapAllergiesCriticityIdToDescription(criticityId: number): string {
        return CRITICITY_DESCRIPTION[criticityId];
    }

    mapVaccinesToDescriptionItemData(vaccines: ImmunizationDto[]): DescriptionItemData[] {
        return vaccines.map(vaccine => this.toDescriptionItemData(vaccine.snomed.pt, this.mapStringToDateToShow(vaccine.administrationDate)));
    }

    mapProposedSurgeriesToDescriptionItemData(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemData[] {
        return proposedSurgeries.map(proposedSurgery => this.toDescriptionItemData(proposedSurgery.snomed.pt))
    }

    mapHistoriesToDescriptionItemData(histories: HealthHistoryConditionDto[] | OutpatientFamilyHistoryDto[]): DescriptionItemData[] {
        return histories.map(history => this.toDescriptionItemData(history.snomed.pt, this.mapStringToDateToShow(history.startDate)));
    }

    mapMedicationsToDescriptionItemData(medications: MedicationDto[]): DescriptionItemData[] {
        return medications.map(medication => this.toDescriptionItemData(this.getMedicationDescription(medication)));
    }

    private getMedicationDescription(medication: MedicationDto): string {
        return medication.note ? `${medication.snomed.pt} ${this.isSuspended(medication)}
${medication.note}`
            : `${medication.snomed.pt} ${this.isSuspended(medication)}`;
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

    mapClinicalEvaluationToDescriptionItemData(notes: DocumentObservationsDto): ClinicalEvaluationData {
        return {
            ...(notes.clinicalImpressionNote && { clinicalImpressionNote: [this.toDescriptionItemData(notes.clinicalImpressionNote)] }),
            ...(notes.currentIllnessNote && { currentIllnessNote: [this.toDescriptionItemData(notes.currentIllnessNote)] }),
            ...(notes.evolutionNote && { evolutionNote: [this.toDescriptionItemData(notes.evolutionNote)] }),
            ...(notes.indicationsNote && { indicationsNote: [this.toDescriptionItemData(notes.indicationsNote)] }),
            ...(notes.otherNote && { otherNote: [this.toDescriptionItemData(notes.otherNote)] }),
            ...(notes.physicalExamNote && { physicalExamNote: [this.toDescriptionItemData(notes.physicalExamNote)] }),
            ...(notes.studiesSummaryNote && { studiesSummaryNote: [this.toDescriptionItemData(notes.studiesSummaryNote)] }),
        }
    }

    getNoInformationAsDescriptionItemData(): DescriptionItemData {
        return this.toDescriptionItemData(this.translateService.instant('historia-clinica.anesthetic-report.summary.NO_INFORMATION'));
    }

    mapProceduresToDescriptionItemDataSummary(procedures: HospitalizationProcedureDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapProceduresToDescriptionItemData(procedures),
            ...PROCEDURES_DESCRIPTION_ITEM,
        }
    }

    mapAllergiesToDescriptionItemDataSummary(allergies: AllergyConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapAllergiesToDescriptionItemData(allergies),
            ...ALLERGIES_DESCRIPTION_ITEM,
        }
    }

    mapOutpatientAllergiesToDescriptionItemDataSummary(allergies: OutpatientAllergyConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapOutpatientAllergiesToDescriptionItemData(allergies),
            ...ALLERGIES_DESCRIPTION_ITEM,
        }
    }

    mapVaccinesToDescriptionItemDataSummary(vaccines: ImmunizationDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapVaccinesToDescriptionItemData(vaccines),
            ...VACCINES_DESCRIPTION_ITEM,
        }
    }

    mapProposedSurgeriesToDescriptionItemDataSummary(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapProposedSurgeriesToDescriptionItemData(proposedSurgeries),
            ...PROPOSED_SURGERIES_DESCRIPTION_ITEM,
        }
    }

    mapPersonalHistoriesToDescriptionItemDataSummary(personalHistories: HealthHistoryConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapHistoriesToDescriptionItemData(personalHistories),
            ...PERSONAL_HISTORIES_DESCRIPTION_ITEM,
        }
    }

    mapFamilyHistoriesToDescriptionItemDataSummary(familyHistories: HealthHistoryConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapHistoriesToDescriptionItemData(familyHistories),
            ...FAMILY_HISTORIES_DESCRIPTION_ITEM,
        }
    }

    mapFamilyHistoriesToReferredDescriptionItemDataSummary(familyHistories: ReferableItemDto<OutpatientFamilyHistoryDto>): ReferredDescriptionItemData {
        return {
            isReferred: this.isReferred(familyHistories),
            notReferredText: this.translateService.instant('guardia.documents-summary.family-histories.NOT_REFERRED'),
            content: {
                summary: this.mapHistoriesToDescriptionItemData(familyHistories.content),
                ...FAMILY_HISTORIES_DESCRIPTION_ITEM,
            }
        }
    }

    mapAllergiesToReferredDescriptionItemDataSummary(allergies: ReferableItemDto<OutpatientAllergyConditionDto>): ReferredDescriptionItemData {
        return {
            isReferred: this.isReferred(allergies),
            notReferredText: this.translateService.instant('guardia.documents-summary.allergies.NOT_REFERRED'),
            content: this.mapOutpatientAllergiesToDescriptionItemDataSummary(allergies.content),
        }
    }

    mapMedicationsToDescriptionItemDataSummary(medications: MedicationDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapMedicationsToDescriptionItemData(medications),
            ...USUAL_MEDICATIONS_DESCRIPTION_ITEM,
        }
    }

    mapReasonsToDescriptionItemDataSummary(reasons: OutpatientReasonDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapProceduresToDescriptionItemData(reasons),
            ...REASONS_DESCRIPTION_ITEM,
        }
    }

    mapToHeaderIdentifierData(headerData: HeaderDescription): HeaderIdentifierData {
        return {
            ...headerData,
            headerDescriptionData: [{
                value: headerData.headerDescriptionData?.scope,
                ...HEADER_DATA_SCOPE,
            },
            {
                value: headerData.headerDescriptionData?.specialty,
                ...HEADER_DATA_SPECIALTY,
            },
            {
                value: headerData.headerDescriptionData?.dateTime,
                ...HEADER_DATA_DATE,
            },
            {
                value: headerData.headerDescriptionData?.patient,
                ...HEADER_DATA_PATIENT,
            },
            {
                value: headerData.headerDescriptionData?.professional,
                ...HEADER_DATA_PROFESSIONAL,
            },
            {
                value: headerData.headerDescriptionData?.institution,
                ...HEADER_DATA_INSTITUTION,
            },
            {
                value: headerData.headerDescriptionData?.sector,
                ...HEADER_DATA_SECTOR,
            },
            {
                value: headerData.headerDescriptionData?.room,
                ...HEADER_DATA_ROOM,
            },
            {
                value: headerData.headerDescriptionData?.bed,
                ...HEADER_DATA_BED,
            }]
        }
    }

    mapOtherProblemsToDescriptionItemDataSummary(otherProblems: HealthConditionDto[]): DescriptionItemDataSummary {
        return {
            summary: this.mapOtherProblemsToDescriptionItemData(otherProblems),
            ...OTHER_PROBLEMS_DESCRIPTION_ITEM,
        }
    }

    mapOtherProblemsToDescriptionItemData(otherProblems: HealthConditionDto[]): DescriptionItemData[] {
        return otherProblems.map(otherProblem => this.toDescriptionItemData(otherProblem.snomed.pt));
    }

    private mapExternalCauseToDescriptionItemDataSummary(externalCause: ExternalCauseDto): ExternalCauseData {
        return {
            ...(externalCause.externalCauseType && { producedBy: [this.toDescriptionItemData(ExternalCauseType[externalCause.externalCauseType])] }),
            ...(externalCause.eventLocation && { eventLocation: [this.toDescriptionItemData(EventLocation[externalCause.eventLocation])] }),
            ...(externalCause.snomed && { howItHappened: [this.toDescriptionItemData(externalCause.snomed.pt)] }),
        }
    }

    hasExternalCause(externalCause: ExternalCauseDto): boolean {
        return !!(externalCause && (externalCause.eventLocation || externalCause.externalCauseType || externalCause.snomed));
    }

    mapExternalCauseToDescriptionItemDataInfo(externalCause: ExternalCauseDto): DescriptionItemDataInfo[] {
        let externalCauseData: ExternalCauseData = this.mapExternalCauseToDescriptionItemDataSummary(externalCause);
        return [{
            title: "internaciones.documents-summary.external-cause.PRODUCED_BY",
            dataId: "produced-by-note",
            descriptionData: externalCauseData.producedBy
        },
        {
            title: "internaciones.documents-summary.external-cause.EVENT_LOCATION",
            dataId: "event-location-note",
            descriptionData: externalCauseData.eventLocation
        },
        {
            title: "internaciones.documents-summary.external-cause.HOW_IT_HAPPENED",
            dataId: "how-it-happened-note",
            descriptionData: externalCauseData.howItHappened
        }]
    }

    private mapToObstetricEventData(obstetricEvent: ObstetricEventDto): ObstetricEventData {
        return {
            ...(obstetricEvent.previousPregnancies && { previousPregnancies: [this.toDescriptionItemData(obstetricEvent.previousPregnancies?.toString())] }),
            ...(obstetricEvent.currentPregnancyEndDate && { currentPregnancyEndDate: [this.toDescriptionItemData(this.dateFormatPipe.transform(dateDtoToDate(obstetricEvent.currentPregnancyEndDate), 'date'))] }),
            ...(obstetricEvent.gestationalAge && { gestationalAge: [this.toDescriptionItemData(`${obstetricEvent.gestationalAge?.toString()} semanas`)] }),
            ...(obstetricEvent.pregnancyTerminationType && { pregnancyTerminationType: [this.toDescriptionItemData(PregnancyTerminationType[obstetricEvent.pregnancyTerminationType])] }),
        }
    }

    private mapToNewBornsData(newBorns: NewbornDto[]): NewBornsData[] {
        return newBorns.map(newBorn => {
            return {
                ...(newBorn.weight && { birthWeight: [this.toDescriptionItemData(`${newBorn.weight} gr`)] }),
                ...(newBorn.birthConditionType && { birthCondition: [this.toDescriptionItemData(BirthConditionType[newBorn.birthConditionType])] }),
                ...(newBorn.genderId && { gender: [this.toDescriptionItemData(Gender[newBorn.genderId])] }),
            }
        })
    }

    mapObstetricEventToDescriptionItemDataInfo(obstetricEventData: ObstetricEventDto): ObstetricEventInfo {
        let obstetricEvent: ObstetricEventData = this.mapToObstetricEventData(obstetricEventData);
        let newBorns: NewBornsData[] = this.mapToNewBornsData(obstetricEventData.newborns);
        return {
            obstetricEvent: this.mapToObstetricEventSummary(obstetricEvent),
            newBorns: this.mapToNewBornsInfo(newBorns),
        }
    }

    private mapToObstetricEventSummary(obstetricEvent: ObstetricEventData): DescriptionItemDataInfo[] {
        return [{
            title: "internaciones.documents-summary.obstetric-event.PREVIOUS_PREGNANCIES",
            dataId: "previous-pregnancies",
            descriptionData: obstetricEvent.previousPregnancies
        },
        {
            title: "internaciones.documents-summary.obstetric-event.CURRENT_FEAT",
            dataId: "current-feat",
            descriptionData: obstetricEvent.currentPregnancyEndDate
        },
        {
            title: "internaciones.documents-summary.obstetric-event.GESTATIONAL_AGE",
            dataId: "gestational-age",
            descriptionData: obstetricEvent.gestationalAge
        },
        {
            title: "internaciones.documents-summary.obstetric-event.TERMINATION",
            dataId: "termination",
            descriptionData: obstetricEvent.pregnancyTerminationType
        }]
    }

    private mapToNewBornsInfo(newBornsData: NewBornsData[]): NewBornsSummary[] {
        return newBornsData.map(newBorn => {
            return {
                newBornSummary: [{
                    title: "internaciones.documents-summary.obstetric-event.new-born.WEIGHT",
                    dataId: "weight",
                    descriptionData: newBorn.birthWeight
                },
                {
                    title: "internaciones.documents-summary.obstetric-event.new-born.CONDITION",
                    dataId: "condition",
                    descriptionData: newBorn.birthCondition
                },
                {
                    title: "internaciones.documents-summary.obstetric-event.new-born.GENDER",
                    dataId: "gender",
                    descriptionData: newBorn.gender
                }]
            }
        })
    }

    hasObstetricEvent(obstetricEvent: ObstetricEventDto): boolean {
        return !!(obstetricEvent && (obstetricEvent.newborns.length ||
            !!(obstetricEvent.currentPregnancyEndDate
                || obstetricEvent.gestationalAge
                || obstetricEvent.pregnancyTerminationType
                || obstetricEvent.previousPregnancies)));
    }

    hasReferredItemContent<T>(item: ReferableItemDto<T>): boolean {
        return !(item.isReferred == null)
    }

    isReferred<T>(item: ReferableItemDto<T>): boolean {
        return item.isReferred
    }

    mapAppearencesToTitleDescriptionListItem(appearance: TriageAppearanceDto): TitleDescriptionListItem {
        let appearanceDescription = [];

        appearance.bodyTemperature ? appearanceDescription.push({
            title: 'guardia.documents-summary.appearance.TEMPERATURE',
            dataId: 'temperature-section',
            descriptionData: [this.toDescriptionItemData(appearance.bodyTemperature.description)],
        }) : null;

        appearance.cryingExcessive ? appearanceDescription.push({
            title: 'guardia.documents-summary.appearance.CRYING_EXCESSIVE',
            dataId: 'cryingExcessive-section',
            descriptionData: appearance.cryingExcessive ? this.mapBooleanToDescription(appearance.cryingExcessive) : [],
        }) : null;

        appearance.muscleHypertonia ? appearanceDescription.push({
            title: 'guardia.documents-summary.appearance.MUSCULAR_TONE',
            dataId: 'muscular-tone-section',
            descriptionData: [this.toDescriptionItemData(appearance.muscleHypertonia?.description)],
        }) : null;

        return {
            title: 'guardia.documents-summary.appearance.TITLE',
            icon: 'monitor_heart',
            description: appearanceDescription,
        }
    }

    mapBreathingToTitleDescriptionListItem(breathing: TriageBreathingDto): TitleDescriptionListItem {
        let breathingDescription = [];

        breathing.respiratoryRetraction ? breathingDescription.push({
            title: 'guardia.documents-summary.breathing.RESPIRATORY_RETRACTION',
            dataId: 'respiratory-retraction-section',
            descriptionData: [this.toDescriptionItemData(breathing.respiratoryRetraction.description)],
        }) : null;

        breathing.stridor ? breathingDescription.push({
            title: 'guardia.documents-summary.breathing.STRIDOR',
            dataId: 'stridor-section',
            descriptionData: breathing.stridor ? this.mapBooleanToDescription(breathing.stridor) : [],
        }) : null;

        breathing.respiratoryRate ? breathingDescription.push({
            title: 'guardia.documents-summary.breathing.RESPIRATORY_RATE',
            dataId: 'respiratory-rate-section',
            descriptionData: [this.toDescriptionItemData(`${breathing.respiratoryRate?.value}${VITAL_SIGNS_AND_RISK_FACTORS.MINUTE}`)],
        }) : null;

        breathing.bloodOxygenSaturation ? breathingDescription.push({
            title: 'guardia.documents-summary.breathing.BLOOD_OXYGEN_SATURATION',
            dataId: 'saturation-section',
            descriptionData: [this.toDescriptionItemData(`${breathing.bloodOxygenSaturation?.value}${VITAL_SIGNS_AND_RISK_FACTORS.PERCENTAJE}`)],
        }) : null;

        return {
            title: 'guardia.documents-summary.breathing.TITLE',
            icon: 'monitor_heart',
            description: breathingDescription,
        }
    }

    mapCirculationToTitleDescriptionListItem(circulation: TriageCirculationDto): TitleDescriptionListItem {
        let circulationDescription = [];

        circulation.perfusion ? circulationDescription.push({
            title: 'guardia.documents-summary.circulation.PERFUSION',
            dataId: 'perfusion-section',
            descriptionData: [this.toDescriptionItemData(circulation.perfusion.description)],
        }) : null;

        circulation.heartRate ? circulationDescription.push({
            title: 'guardia.documents-summary.circulation.HEART_RATE',
            dataId: 'heart-rate-section',
            descriptionData: circulation.heartRate ? [this.toDescriptionItemData(`${circulation.heartRate.value}${VITAL_SIGNS_AND_RISK_FACTORS.MINUTE}`)] : [],
        }) : null;

        return {
            title: 'guardia.documents-summary.circulation.TITLE',
            icon: 'monitor_heart',
            description: circulationDescription,
        }
    }

    private mapBooleanToDescription(value: boolean): DescriptionItemData[] {
        return value ?
            [this.toDescriptionItemData(this.translateService.instant('historia-clinica.anesthetic-report.summary.YES'))]
            : [this.toDescriptionItemData(this.translateService.instant('historia-clinica.anesthetic-report.summary.NO'))]
    }

    mapIsolationAlertDescriptionItemDataList(isolationAlerts: IsolationAlertDto[]): IsolationAlertDescriptionItemData[] {
        return isolationAlerts.map(isolationAlert => this.mapIsolationAlertDescriptionItemData(isolationAlert));
    }

    mapIsolationAlertDescriptionItemData(isolationAlert: IsolationAlertDto): IsolationAlertDescriptionItemData {
        const dateFormatPipe = new DateFormatPipe();
        const endDate: string = dateFormatPipe.transform(dateDtoToDate(isolationAlert.endDate), 'date');
        return {
            diagnosis: [this.toDescriptionItemData(isolationAlert.healthConditionPt)],
            types: isolationAlert.types.map(type => this.toDescriptionItemData(type.description)),
            criticality: [this.toDescriptionItemData(isolationAlert.criticality.description)],
            endAlert: [this.toDescriptionItemData(endDate)],
            ...(isolationAlert.observations && { observations: [this.toDescriptionItemData(isolationAlert.observations)] }),
            ...(isolationAlert.isModified && { editor: toRegisterEditorInfo(isolationAlert.updatedBy.fullName, convertDateTimeDtoToDate(isolationAlert.updatedOn)) })
        }
    }

}
