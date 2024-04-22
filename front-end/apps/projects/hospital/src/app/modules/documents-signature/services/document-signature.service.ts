import { Injectable } from '@angular/core';
import { AllergyConditionDto, AnthropometricDataDto, DiagnosisDto, DocumentDto, DocumentObservationsDto, HealthConditionDto, HealthHistoryConditionDto, MedicationDto, PersonalHistoryDto, ProcedureDto, ReasonDto, RiskFactorDto } from '@api-rest/api-model';
import { getDocumentType } from '@core/constants/summaries';
import { dateToViewDate } from '@core/utils/date.utils';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';

@Injectable({
	providedIn: 'root'
})
export class DocumentSignatureService {

	constructor() { }

	buildDetailedInformation(document: DocumentDto): DetailedInformation {
        return {
			title: getDocumentType(document.documentType).title.toLocaleUpperCase(),
            id: document.id,
            oneColumn: [
                {
                    icon: 'medical_services',
                    title: 'digital-signature.detailed-documents.SPECIALTY',
                    value: document.clinicalSpecialtyName ? [document.clinicalSpecialtyName] : []
                },
                {
                    icon: 'sms_failed',
                    title: 'digital-signature.detailed-documents.REASON',
                    value: this.buildReasons(document.reasons)
                },
                {
                    icon: 'chat',
                    title: 'digital-signature.detailed-documents.CLINICAL_EVALUATION',
                    value: this.buildObservations(document.notes)
                },
                {
                    icon: 'error_outlined',
                    title: 'digital-signature.detailed-documents.DIAGNOSIS',
                    value: this.buildDiagnosis(document.diagnosis, document.mainDiagnosis)
                }
            ],
            twoColumns: [
                {
                    icon: 'cancel',
                    title: 'digital-signature.detailed-documents.ALLERGIES',
                    value: this.buildAllergies(document.allergies)
                },
                {
                    icon: 'report',
                    title: 'digital-signature.detailed-documents.FAMILY_HISTORIES',
                    value: this.buildFamilyHistories(document.familyHistories)
                },
                {
                    icon: 'event_available',
                    title: 'digital-signature.detailed-documents.MEDICATION',
                    value: this.buildMedications(document.medications)
                },
                {
                    icon: 'report_outlined',
                    title: 'digital-signature.detailed-documents.PERSONAL_HISTORIES',
                    value: this.buildPersonalHistories(document.personalHistories)
                },
                {
                    icon: 'library_add',
                    title: 'digital-signature.detailed-documents.PROCEDURES',
                    value: this.buildProcedures(document.procedures)
                },
                {
                    icon: 'favorite_outlined',
                    title: 'digital-signature.detailed-documents.RISK_FACTORS',
                    value: this.buildRiskFactors(document.riskFactors)
                },
            ],
            threeColumns: [
                {
                    icon: 'accessibility',
                    title: 'digital-signature.detailed-documents.ANTHROPOMETRIC_DATA',
                    value: this.buildAnthropometricData(document.anthropometricData)
                }
            ]
        }
    }

	private buildRiskFactors(riskFactors: RiskFactorDto): string[] {
		const riskFactorsFiltered: string[] = [];
		if (riskFactors?.bloodGlucose)
			riskFactorsFiltered.push(`Glucemia: ${riskFactors.bloodGlucose.value}mg/dl`);
		if (riskFactors?.bloodOxygenSaturation)
			riskFactorsFiltered.push(`Saturación de oxígeno: ${riskFactors.bloodOxygenSaturation.value}%`);
		if (riskFactors?.cardiovascularRisk)
			riskFactorsFiltered.push(`Riesgo cardivascular: ${riskFactors.cardiovascularRisk.value}%`);
		if (riskFactors?.diastolicBloodPressure)
			riskFactorsFiltered.push(`Tension arterial diastólica: ${riskFactors.diastolicBloodPressure.value}mm`);
		if (riskFactors?.glycosylatedHemoglobin)
			riskFactorsFiltered.push(`Hemoglobina glicosilada: ${riskFactors.glycosylatedHemoglobin.value}%`);
		if (riskFactors?.heartRate)
			riskFactorsFiltered.push(`Frecuencia cardíaca: ${riskFactors.heartRate.value}/min`);
		if (riskFactors?.respiratoryRate)
			riskFactorsFiltered.push(`Frecuencia respiratoria: ${riskFactors.respiratoryRate.value}/min`);
		if (riskFactors?.systolicBloodPressure)
			riskFactorsFiltered.push(`Tension arterial sistólica: ${riskFactors.systolicBloodPressure.value}mm`);
		if (riskFactors?.temperature)
			riskFactorsFiltered.push(`Temperatura: ${riskFactors.temperature.value}°`);
		return riskFactorsFiltered;
	}

	private buildProcedures(procedures: ProcedureDto[]): string[] {
		const proceduresFiltered: string[] = [];
		procedures.forEach(p => {
			proceduresFiltered.push(p.snomed.pt);
			if (p.performedDate)
				proceduresFiltered.push(dateToViewDate(new Date(p.performedDate)));
		})
		return proceduresFiltered;
	}

	private buildDiagnosis(diagnosis: DiagnosisDto[], mainDiagnosis: HealthConditionDto): string[] {
		const diagnosisFiltered: string[] = [];
		if (mainDiagnosis)
			diagnosisFiltered.push(`${mainDiagnosis.snomed.pt} - ${this.getVerification(mainDiagnosis.verificationId)} (Principal)`);
		diagnosis.forEach(d => {
			diagnosisFiltered.push(`${d.snomed.pt} - ${this.getVerification(d.verificationId)}`);
		})
		return diagnosisFiltered;
	}

	private getVerification(verificationId: string): string {
		let verification = 'Descartado';
		if (verificationId === HEALTH_VERIFICATIONS.CONFIRMADO)
			verification = 'Confirmado'
		if (verificationId === HEALTH_VERIFICATIONS.PRESUNTIVO)
			verification = 'Presuntivo';
		return verification;
	}

	private buildReasons(reasons: ReasonDto[]): string[] {
		return reasons.map(r => r.snomed.pt);
	}

	private buildPersonalHistories(personalHistories: PersonalHistoryDto[]): string[] {
		const personalHistoriesFiltered: string[] = [];
		personalHistories.forEach(ph => {
			personalHistoriesFiltered.push(ph.snomed.pt);
			if (ph.startDate)
				personalHistoriesFiltered.push(`Desde ${dateToViewDate(new Date(ph.startDate))}`);
		})
		return personalHistoriesFiltered;
	}

	private buildObservations(observation: DocumentObservationsDto): string[] {
		const observationsFiltered: string[] = [];
		if (observation?.clinicalImpressionNote)
			observationsFiltered.push(`Impresión clínica y plan: ${observation.clinicalImpressionNote}`);
		if (observation?.currentIllnessNote)
			observationsFiltered.push(`Enfermedad actual: ${observation.clinicalImpressionNote}`);
		if (observation?.evolutionNote)
			observationsFiltered.push(`Evolución: ${observation.evolutionNote}`);
		if (observation?.indicationsNote)
			observationsFiltered.push(`Indicaciones: ${observation.indicationsNote}`);
		if (observation?.otherNote)
			observationsFiltered.push(`Otras observaciones: ${observation.otherNote}`);
		if (observation?.physicalExamNote)
			observationsFiltered.push(`Examen físico: ${observation.physicalExamNote}`);
		if (observation?.studiesSummaryNote)
			observationsFiltered.push(`Resumen de estudios y procedimientos realizados: ${observation.studiesSummaryNote}`);
		return observationsFiltered;
	}

	private buildMedications(medications: MedicationDto[]): string[] {
		const medicationsFiltered: string[] = [];
		medications.forEach(m => {
			medicationsFiltered.push(m.snomed.pt);
			medicationsFiltered.push(m.note);
		})
		return medicationsFiltered;
	}

	private buildFamilyHistories(familyHistories: HealthHistoryConditionDto[]): string[] {
		const familyHistoriesFiltered: string[] = [];
		familyHistories.forEach(fh => {
			familyHistoriesFiltered.push(fh.snomed.pt);
			if (fh.startDate)
				familyHistoriesFiltered.push(`Desde ${dateToViewDate(new Date(fh.startDate))}`);
		})
		return familyHistoriesFiltered;
	}

	private buildAnthropometricData(anthropometricData: AnthropometricDataDto): string[] {
		const anthropometricDataFiltered: string[] = [];
		if (anthropometricData?.bloodType)
			anthropometricDataFiltered.push(`Grupo sanguineo: ${anthropometricData.bloodType.value}`);
		if (anthropometricData?.height)
			anthropometricDataFiltered.push(`Altura: ${anthropometricData.height.value}`);
		if (anthropometricData?.weight)
			anthropometricDataFiltered.push(`Peso: ${anthropometricData.weight.value}`);
		return anthropometricDataFiltered;
	}

	private buildAllergies(allergies: AllergyConditionDto[]): string[] {
		return allergies.map(data => data.snomed.pt);
	}
}
