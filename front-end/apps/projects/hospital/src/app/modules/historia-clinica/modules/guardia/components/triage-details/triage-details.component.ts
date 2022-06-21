import { Component, Input, OnChanges } from '@angular/core';
import { EmergencyCareTypes, Triages } from '../../constants/masterdata';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { RiskFactor } from '@presentation/components/factor-de-riesgo-current/factor-de-riesgo.component';
import {PatientNameService} from "@core/services/patient-name.service";

@Component({
	selector: 'app-triage-details',
	templateUrl: './triage-details.component.html',
	styleUrls: ['./triage-details.component.scss']
})

export class TriageDetailsComponent implements OnChanges {

	@Input() triage: Triage;
	@Input() emergencyCareType: EmergencyCareTypes;

	readonly triages = Triages;
	readonly emergencyCareTypes = EmergencyCareTypes;

	riskFactors: { description: string, value: RiskFactor }[];

	constructor(private readonly patientNameService: PatientNameService,) {
	}

	ngOnChanges() {
		this.riskFactors = this.includesRiskFactors() ? this.mapToRiskFactor(this.triage) : undefined;
	}

	private includesRiskFactors(): boolean {
		return !!this.emergencyCareType && !!this.triage;
	}

	private mapToRiskFactor(triage: Triage): { description: string, value: RiskFactor }[] {
		const riskFactors = [];
		const LABELS = this.emergencyCareType === EmergencyCareTypes.PEDIATRIA ?
			{
				respiratoryRate: 'Frecuencia respiratoria',
				bloodOxygenSaturation: 'Saturación de oxígeno',
				heartRate: 'Frecuencia cardíaca',
			} :
			{
				systolicBloodPressure: 'Tensión arterial sistólica',
				diastolicBloodPressure: 'Tensión arterial diastólica',
				heartRate: 'Frecuencia cardíaca',
				respiratoryRate: 'Frecuencia respiratoria',
				temperature: 'Temperatura',
				bloodOxygenSaturation: 'Saturación de oxígeno',
				bloodGlucose: 'Glucemia (mg/dl)',
				glycosylatedHemoglobin: 'Hemoglobina glicosilada (%)',
				cardiovascularRisk: 'Riesgo cardiovascular (%)',
			};

		Object.keys(LABELS).forEach(key => {
			const riskFactor = getRiskFactor(key);
			riskFactors.push({
				description: LABELS[key],
				value: {
					value: Number(riskFactor?.value) || undefined,
					effectiveTime: riskFactor?.effectiveTime || undefined
				}
			});
		}
		);

		return riskFactors;

		function getRiskFactor(key): { value: number, effectiveTime: Date } {
			if (triage.riskFactors && triage.riskFactors[key]) {
				return triage.riskFactors[key];
			}
			if (triage.breathing && (key === 'respiratoryRate' || key === 'bloodOxygenSaturation')) {
				return triage.breathing[key];
			}
			if (triage.circulation && key === 'heartRate') {
				return triage.circulation[key];
			}
			return undefined;
		}
	}

	getFullName(triage: Triage): string {
		return `${this.patientNameService.getPatientName(triage.createdBy.firstName, triage.createdBy.nameSelfDetermination)}, ${triage.createdBy.lastName}`;
	}

}

export interface Triage {
	creationDate: Date;
	category: TriageCategory;
	createdBy: {
		firstName: string,
		lastName: string,
		nameSelfDetermination: string
	};
	doctorsOfficeDescription?: string;
	riskFactors?: {
		bloodOxygenSaturation: {
			value: string,
			effectiveTime: Date
		},
		diastolicBloodPressure?: {
			value: string,
			effectiveTime: Date
		},
		heartRate: {
			value: string,
			effectiveTime: Date
		},
		respiratoryRate: {
			value: string,
			effectiveTime: Date
		},
		systolicBloodPressure?: {
			value: string,
			effectiveTime: Date
		},
		temperature?: {
			value: string,
			effectiveTime: Date
		},
		bloodGlucose?: {
			value: string,
			effectiveTime: Date
		},
		glycosylatedHemoglobin?: {
			value: string,
			effectiveTime: Date
		},
		cardiovascularRisk?: {
			value: string,
			effectiveTime: Date
		},
	};
	appearance?: {
		bodyTemperatureDescription?: string,
		cryingExcessive?: boolean,
		muscleHypertoniaDescription?: string
	};
	breathing?: {
		respiratoryRetractionDescription: string,
		stridor: boolean,
		respiratoryRate: {
			value: string
			effectiveTime: Date,
		},
		bloodOxygenSaturation: {
			value: string
			effectiveTime: Date,
		}
	};
	circulation?: {
		perfusionDescription: string,
		heartRate: {
			value: string
			effectiveTime: Date,
		}
	};
	notes?: string;
}
