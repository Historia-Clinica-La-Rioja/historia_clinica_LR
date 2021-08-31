import { Component, Input, OnChanges } from '@angular/core';
import { EmergencyCareTypes, Triages } from '../../constants/masterdata';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { VitalSign } from '@presentation/components/signo-vital-current/signo-vital.component';

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

	vitalSigns: { description: string, value: VitalSign } [];

	constructor() {
	}

	ngOnChanges() {
		this.vitalSigns = this.includesVitalSigns() ? this.mapToVitalSign(this.triage) : undefined;
	}

	private includesVitalSigns(): boolean {
		return !!this.emergencyCareType && !!this.triage;
	}

	private mapToVitalSign(triage: Triage): { description: string, value: VitalSign }[] {
		const vitalSigns = [];
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
			};

		Object.keys(LABELS).forEach(key => {
				const vitalSign = getVitalSign(key);
				vitalSigns.push({
					description: LABELS[key],
					value: {
						value: Number(vitalSign?.value) || undefined,
						effectiveTime: vitalSign?.effectiveTime || undefined
					}
				});
			}
		);

		return vitalSigns;

		function getVitalSign(key): { value: number, effectiveTime: Date } {
			if (triage.vitalSigns && triage.vitalSigns[key]) {
				return triage.vitalSigns[key];
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

}

export interface Triage {
	creationDate: Date;
	category: TriageCategory;
	createdBy: {
		firstName: string,
		lastName: string
	};
	doctorsOfficeDescription?: string;
	vitalSigns?: {
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
		}
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
