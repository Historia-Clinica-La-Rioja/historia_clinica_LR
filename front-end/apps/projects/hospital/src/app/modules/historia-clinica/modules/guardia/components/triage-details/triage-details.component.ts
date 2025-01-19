import { Component, Input, OnChanges } from '@angular/core';
import { EmergencyCareTypes, Triages } from '../../constants/masterdata';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { RiskFactor } from '@presentation/components/factor-de-riesgo-current/factor-de-riesgo.component';
import { PatientNameService } from "@core/services/patient-name.service";
import { LABELS_RISK_FACTORS, LABELS_RISK_FACTORS_PEDIATRIC } from '../../utils/riskFactors.utils';
import { EmergencyCareClinicalSpecialtySectorDto } from '@api-rest/api-model';

@Component({
	selector: 'app-triage-details',
	templateUrl: './triage-details.component.html',
	styleUrls: ['./triage-details.component.scss']
})

export class TriageDetailsComponent implements OnChanges {

	@Input() triage: TriageDetails;
	@Input() emergencyCareType: EmergencyCareTypes;
	@Input() showRiskFactors = true;

	readonly triages = Triages;
	readonly emergencyCareTypes = EmergencyCareTypes;

	riskFactors: RiskFactorFull[];

	constructor(private readonly patientNameService: PatientNameService,) {
	}

	ngOnChanges() {
		this.riskFactors = this.includesRiskFactors() ? this.mapToRiskFactor(this.triage) : undefined;
	}

	private includesRiskFactors(): boolean {
		return !!this.emergencyCareType && !!this.triage;
	}

	private mapToRiskFactor(triage: TriageDetails): RiskFactorFull[] {
		const riskFactors = [];
		const LABELS = this.emergencyCareType === EmergencyCareTypes.PEDIATRIA ? LABELS_RISK_FACTORS_PEDIATRIC : LABELS_RISK_FACTORS;

		Object.keys(LABELS).forEach(key => {
			const riskFactor = getRiskFactor(key);
			riskFactors.push({
				description: LABELS[key].description,
				id: LABELS[key].id,
				value: {
					value: Number(riskFactor?.value) || undefined,
					effectiveTime: riskFactor?.effectiveTime || undefined
				}
			});
		});

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

	getFullName(triage: TriageDetails): string {
		return `${this.patientNameService.getPatientName(triage.createdBy.firstName, triage.createdBy.nameSelfDetermination)} ${triage.createdBy.lastName}`;
	}

}

export interface TriageDetails {
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
	reasons?: string[];
	clinicalSpecialtySector?: EmergencyCareClinicalSpecialtySectorDto;
}

export interface RiskFactorFull {
	id: string,
	description: string,
	value: RiskFactor
}

export interface RiskFactorValue {
	value: string,
	effectiveTime: Date
}
