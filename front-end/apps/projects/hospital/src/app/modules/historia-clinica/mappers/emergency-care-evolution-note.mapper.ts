import { EffectiveClinicalObservationDto, OutpatientAllergyConditionDto, OutpatientAnthropometricDataDto, OutpatientFamilyHistoryDto, OutpatientMedicationDto, OutpatientProcedureDto, OutpatientReasonDto, OutpatientRiskFactorDto } from "@api-rest/api-model";
import { toApiFormat } from "@api-rest/mapper/date.mapper";
import { fixDate } from "@core/utils/date/format";
import { dateISOParseDate, momentParseDate } from "@core/utils/moment.utils";
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
			bloodType: anthropometricData.bloodType,
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

export const toAllergies = (allergies: OutpatientAllergyConditionDto[]): Alergia[] => {
	return allergies.map(allergy => toAllergy(allergy));
}

const toProcedure = (procedure: OutpatientProcedureDto): Procedimiento => {
	return { snomed: procedure.snomed, ...(procedure.performedDate && { performedDate: procedure.performedDate }) }
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
	return { snomed: familyHistory.snomed, fecha: familyHistory.startDate ? momentParseDate(familyHistory.startDate) : null }
}

export const toFamilyHistories = (familyHistories: OutpatientFamilyHistoryDto[]): AntecedenteFamiliar[] => {
	return familyHistories.map(familyHistory => toFamilyHistory(familyHistory));
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