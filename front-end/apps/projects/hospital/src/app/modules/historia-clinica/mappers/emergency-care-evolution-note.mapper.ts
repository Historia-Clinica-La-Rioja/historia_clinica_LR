import { FormGroup } from "@angular/forms";
import { ClinicalTermDto, DiagnosisDto, EEmergencyCareEvolutionNoteType, EffectiveClinicalObservationDto, EmergencyCareEvolutionNoteDto, HealthConditionDto, IsolationAlertDto, OutpatientAllergyConditionDto, OutpatientAnthropometricDataDto, OutpatientFamilyHistoryDto, OutpatientMedicationDto, OutpatientProcedureDto, OutpatientReasonDto, OutpatientRiskFactorDto, ReferableItemDto } from "@api-rest/api-model";
import { dateDtoToDate, dateToDateDto } from "@api-rest/mapper/date-dto.mapper";
import { toApiFormat } from "@api-rest/mapper/date.mapper";
import { fixDate } from "@core/utils/date/format";
import { dateISOParseDate } from "@core/utils/moment.utils";
import { EmergencyCareDiagnosis, EmergencyCareMainDiagnosis } from "@historia-clinica/components/emergency-care-diagnoses/emergency-care-diagnoses.component";
import { IsolationAlert } from "@historia-clinica/components/isolation-alert-form/isolation-alert-form.component";
import { Alergia } from "@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service";
import { AntecedenteFamiliar } from "@historia-clinica/modules/ambulatoria/services/antecedentes-familiares-nueva-consulta.service";
import { AnthropometricDataValues } from "@historia-clinica/modules/ambulatoria/services/datos-antropometricos-nueva-consulta.service";
import { Medicacion } from "@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service";
import { MotivoConsulta } from "@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service";
import { EffectiveObservation, RiskFactorsValue } from "@historia-clinica/services/factores-de-riesgo-form.service";
import { Procedimiento } from "@historia-clinica/services/procedimientos.service";

export const toOutpatientRiskFactorDto = (riskFactors: OutpatientRiskFactorDto): OutpatientRiskFactorDto => {
	if (riskFactors) {
		let result;
		Object.keys(riskFactors).forEach(
			key => {
				if (riskFactors[key]?.value) {
					result = { ...result, [key]: riskFactors[key] }
				}
			}
		)
		return result;
	}
	return null;
}


export const buildEmergencyCareEvolutionNoteDto = (form: FormGroup, isFamilyHistoriesNoRefer: boolean, isAllergyNoRefer: boolean, patientId: number, evolutionNoteType: EEmergencyCareEvolutionNoteType): EmergencyCareEvolutionNoteDto => {
	const value = form.value;
	const allDiagnosis = evolutionNoteType === EEmergencyCareEvolutionNoteType.DOCTOR ? toAllDiagnosis(value.diagnosis) : toAllNursingDiagnosis(value.diagnosis);
	const medications = toOutpatientMedicationDto(value.medications?.data);
	const anthropometricData = toOutpatientAnthropometricDataDto(value.anthropometricData);
	const familyHistories = toOutpatientFamilyHistoryDto(value.familyHistories?.data);
	const riskFactors = toOutpatientRiskFactorDto(value.riskFactors);
	const isolationAlerts = value.isolationAlerts?.isolationAlerts.length ? toIsolationAlertsDto(value.isolationAlerts?.isolationAlerts) : [];
	return {
		clinicalSpecialtyId: value.clinicalSpecialty?.clinicalSpecialty.id,
		reasons: value.reasons?.motivo || [],
		diagnosis: allDiagnosis.diagnosis,
		mainDiagnosis: allDiagnosis.mainDiagnosis,
		evolutionNote: value.evolutionNote?.evolucion,
		anthropometricData,
		familyHistories: {
			isReferred: (isFamilyHistoriesNoRefer && (value.familyHistories?.data || []).length === 0) ? null : isFamilyHistoriesNoRefer,
			content: familyHistories,
		},
		procedures: value.procedures?.data.map(p => { return { ...p, performedDate: p.performedDate ? toApiFormat(p.performedDate) : null } }) || [],
		medications,
		riskFactors,
		allergies: {
			isReferred: (isAllergyNoRefer && (value.allergies?.data || []).length === 0) ? null : isAllergyNoRefer,
			content: value.allergies?.data || []
		},
		patientId,
		type: evolutionNoteType,
		isolationAlerts,
	}
}

export const toOutpatientFamilyHistoryDto = (familyHistories: any[]): OutpatientFamilyHistoryDto[] => {
	return familyHistories?.map(f => {
		return {
			snomed: f.snomed,
			startDate: f.fecha ? toApiFormat(fixDate(f.fecha)) : null
		}
	}) || []
}

export const toOutpatientAnthropometricDataDto = (anthropometricData): OutpatientAnthropometricDataDto => {
	if (anthropometricData) {
		return {
			height: {
				value: anthropometricData.height?.toString()
			},
			weight: {
				value: anthropometricData.weight?.toString()
			},
			bloodType: {
				value: anthropometricData.bloodType?.description
			},
			headCircumference: {
				value: anthropometricData.headCircumference?.toString()
			},
			bmi: {
				value: null
			},
		}
	}
	return null;
}

export const toOutpatientMedicationDto = (formData: any[]): OutpatientMedicationDto[] => {
	return formData?.map(r => {
		return {
			note: r.observaciones,
			snomed: r.snomed,
			suspended: r.suspendido
		}
	}) || []
}

const toAllergy = (allergy: OutpatientAllergyConditionDto): Alergia => {
	return { snomed: allergy.snomed, criticalityId: allergy.criticalityId }
}

export const toAllergies = (allergies: ReferableItemDto<OutpatientAllergyConditionDto>): Alergia[] => {
	return allergies.content?.map(allergy => toAllergy(allergy));
}

export const toClinicalTermDto = (id: number, pt: string, sctid: string): ClinicalTermDto => {
	return {
		id, snomed: { pt, sctid }
	}
}

const toIsolationAlert = (isolationAlert: IsolationAlertDto): IsolationAlert => {
	const { healthConditionId, healthConditionPt, healthConditionSctid } = isolationAlert;
	return {
		id: isolationAlert.id,
		statusId: isolationAlert.statusId,
		diagnosis: toClinicalTermDto(healthConditionId, healthConditionPt, healthConditionSctid),
		types: isolationAlert.types,
		criticality: isolationAlert.criticality,
		endDate: dateDtoToDate(isolationAlert.endDate),
		observations: isolationAlert.observations,
	}
}

export const toIsolationAlerts = (isolationAlerts: IsolationAlertDto[]): IsolationAlert[] => {
	return isolationAlerts.map(isolationAlert => toIsolationAlert(isolationAlert));
}

const toProcedure = (procedure: OutpatientProcedureDto): Procedimiento => {
	return { snomed: procedure.snomed, ...(procedure.performedDate && { performedDate: dateISOParseDate(procedure.performedDate) }) }
}

export const toProcedures = (procedures: OutpatientProcedureDto[]): Procedimiento[] => {
	return procedures.map(procedure => toProcedure(procedure));
}

const toMedication = (medication: OutpatientMedicationDto): Medicacion => {
	return { snomed: medication.snomed, observaciones: medication.note, suspendido: medication.suspended }
}

export const toMedications = (medications: OutpatientMedicationDto[]): Medicacion[] => {
	return medications.map(medication => toMedication(medication));
}

const toMotive = (motive: OutpatientReasonDto): MotivoConsulta => {
	return { snomed: motive.snomed }
}

export const toMotives = (motives: OutpatientReasonDto[]): MotivoConsulta[] => {
	return motives.map(motive => toMotive(motive));
}

const toFamilyHistory = (familyHistory: OutpatientFamilyHistoryDto): AntecedenteFamiliar => {
	return { snomed: familyHistory.snomed, fecha: familyHistory.startDate ? dateISOParseDate(familyHistory.startDate) : null }
}

export const toFamilyHistories = (familyHistories: ReferableItemDto<OutpatientFamilyHistoryDto>): AntecedenteFamiliar[] => {
	return familyHistories.content?.map(familyHistory => toFamilyHistory(familyHistory));
}

export const toEffectiveObservation = (factorDeRiesgo: EffectiveClinicalObservationDto): EffectiveObservation => {
	return { value: factorDeRiesgo?.value || null, effectiveTime: factorDeRiesgo?.effectiveTime ? dateISOParseDate(factorDeRiesgo.effectiveTime) : new Date() }
}

export const toRiskFactorsValue = (riskFactor: OutpatientRiskFactorDto): RiskFactorsValue => {
	return {
		bloodOxygenSaturation: toEffectiveObservation(riskFactor?.bloodOxygenSaturation),
		diastolicBloodPressure: toEffectiveObservation(riskFactor?.diastolicBloodPressure),
		heartRate: toEffectiveObservation(riskFactor?.heartRate),
		respiratoryRate: toEffectiveObservation(riskFactor?.respiratoryRate),
		systolicBloodPressure: toEffectiveObservation(riskFactor?.systolicBloodPressure),
		temperature: toEffectiveObservation(riskFactor?.temperature),
		bloodGlucose: toEffectiveObservation(riskFactor?.bloodGlucose),
		glycosylatedHemoglobin: toEffectiveObservation(riskFactor?.glycosylatedHemoglobin),
		cardiovascularRisk: toEffectiveObservation(riskFactor?.cardiovascularRisk),
	}
}

export const toAnthropometricDataValues = (antropometricData: OutpatientAnthropometricDataDto): AnthropometricDataValues => {
	return {
		bloodType: antropometricData?.bloodType?.value || null,
		height: antropometricData?.height?.value || null,
		weight: antropometricData?.weight?.value || null,
		headCircumference: antropometricData?.headCircumference?.value || null,
	}
}

export const toAllDiagnosis = (diagnosisFormValue): { diagnosis: DiagnosisDto[], mainDiagnosis: HealthConditionDto } => {
	const mainDiagnosis: EmergencyCareMainDiagnosis = diagnosisFormValue?.mainDiagnostico;
	const others: EmergencyCareDiagnosis[] = diagnosisFormValue?.otrosDiagnosticos;
	return {
		diagnosis: others?.filter(otherDiagnosis => otherDiagnosis.diagnosis.isAdded).map(otherDiagnosis => otherDiagnosis.diagnosis) || [],
		mainDiagnosis: mainDiagnosis?.main || null
	}
}

export const toAllNursingDiagnosis = (diagnosisFormValue): { diagnosis: DiagnosisDto[], mainDiagnosis: HealthConditionDto } => {
	return {
		diagnosis: diagnosisFormValue.otrosDiagnosticos,
		mainDiagnosis: diagnosisFormValue.mainDiagnostico
	}
}

const toIsolationAlertDto = (isolationAlert: IsolationAlert): IsolationAlertDto => {
	return {
		criticality: isolationAlert.criticality,
		endDate: dateToDateDto(isolationAlert.endDate),
		healthConditionId: isolationAlert.diagnosis.id,
		healthConditionPt: isolationAlert.diagnosis.snomed.pt,
		healthConditionSctid: isolationAlert.diagnosis.snomed.sctid,
		types: isolationAlert.types,
		...(isolationAlert.observations && { observations: isolationAlert.observations }),
		id: isolationAlert.id || null,
		statusId: isolationAlert.statusId || null,
	}
}

export const toIsolationAlertsDto = (isolationAlerts: IsolationAlert[]): IsolationAlertDto[] => {
	return isolationAlerts.map(isolationAlert => toIsolationAlertDto(isolationAlert));
}