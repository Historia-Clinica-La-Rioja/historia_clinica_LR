import { Injectable } from '@angular/core';
import { DateTimeDto, NewEmergencyCareDto, TriageNoAdministrativeDto } from "@api-rest/api-model";

@Injectable()
export class NewEpisodeService {

	private administrativeAdmission: NewEmergencyCareDto;

	constructor() { }

	destroy(): void {
		this.administrativeAdmission = undefined;
	}

	setAdministrativeAdmission(data: NewEmergencyCareDto): void {
		this.administrativeAdmission = data;
	}

	getAdministrativeAdmission(): NewEmergencyCareDto{
		return this.administrativeAdmission;
	}
}

export interface TriageAdultGynecologicalDto {
	categoryId: number;
	doctorsOfficeId: number;
	notes: string;
	vitalSigns?: {
		bloodOxygenSaturation?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		diastolicBloodPressure?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		heartRate?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		respiratoryRate?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		systolicBloodPressure?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		temperature?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
	};
}
