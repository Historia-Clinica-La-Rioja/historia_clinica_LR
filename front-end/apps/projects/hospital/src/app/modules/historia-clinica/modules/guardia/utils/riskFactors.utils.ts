import { RiskFactorDto } from "@api-rest/api-model";
import { RiskFactorFull } from "../components/triage-details/triage-details.component";
import { dateISOParseDate } from "@core/utils/moment.utils";

export const LABELS_RISK_FACTORS_PEDIATRIC = {
	respiratoryRate: { description: 'Frecuencia respiratoria', id: 'respiratory_rate' },
	bloodOxygenSaturation: { description: 'Saturación de oxígeno', id: 'blood_oxygen_saturation' },
	heartRate: { description: 'Frecuencia cardíaca', id: 'heart_rate' },
}

export const LABELS_RISK_FACTORS = {
	systolicBloodPressure: { description: 'Tensión arterial sistólica', id: 'systolic_blood_pressure' },
	diastolicBloodPressure: { description: 'Tensión arterial diastólica', id: 'diastolic_blood_pressure' },
	heartRate: { description: 'Frecuencia cardíaca', id: 'heart_rate' },
	respiratoryRate: { description: 'Frecuencia respiratoria', id: 'respiratory_rate' },
	temperature: { description: 'Temperatura', id: 'temperature' },
	bloodOxygenSaturation: { description: 'Saturación de oxígeno', id: 'blood_oxygen_saturation' },
	bloodGlucose: { description: 'Glucemia (mg/dl)', id: 'blood_glucose' },
	glycosylatedHemoglobin: { description: 'Hemoglobina glicosilada (%)', id: 'glycosylated_hemoglobin' },
	cardiovascularRisk: { description: 'Riesgo cardiovascular (%)', id: 'cardiovascular_risk' },
};

export function mapToRiskFactorFull(riskDto: RiskFactorDto): RiskFactorFull[] {
	const labels = LABELS_RISK_FACTORS;

	const riskFactors: RiskFactorFull[] = Object.keys(labels).map(key => {
		const riskFactor = riskDto ? riskDto[key] : null;
		return {
			description: labels[key].description,
			id: labels[key].id,
			value: {
				value: riskFactor?.value || undefined,
				effectiveTime: riskFactor?.effectiveTime ? dateISOParseDate(riskFactor.effectiveTime) : undefined
			}
		};
	});
	return riskFactors;
}