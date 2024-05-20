import { Injectable } from '@angular/core';
import { NewEmergencyCareDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';
import { GuardiaMapperService } from './guardia-mapper.service';

@Injectable()
export class NewEpisodeService {

	private administrativeAdmission: AdministrativeAdmission;

	private subject: BehaviorSubject<AdministrativeAdmission> = new BehaviorSubject<AdministrativeAdmission>(null);
	constructor() { }

	clearData(): void {
		this.administrativeAdmission = undefined;
	}

	getSubject(): BehaviorSubject<AdministrativeAdmission> {
		return this.subject;
	}

	nextSubject(param: AdministrativeAdmission): void {
		this.subject.next(param);
	}

	setAdministrativeAdmission(data: AdministrativeAdmission): void {
		this.administrativeAdmission = data;
	}

	getAdministrativeAdmissionDto(): NewEmergencyCareDto {
		return GuardiaMapperService._toECAdministrativeDto(this.administrativeAdmission);
	}

	getAdministrativeAdmission(): AdministrativeAdmission {
		return this.administrativeAdmission;
	}

}

export interface AdministrativeAdmission {
	patientId: number;
	reason: string;
	patientMedicalCoverageId: number;
	emergencyCareTypeId: number;
	doctorsOfficeId: number;
	emergencyCareEntranceTypeId: number;
	ambulanceCompanyId: string;
	hasPoliceIntervention: boolean;
	callDate: Date;
	callTime: string;
	plateNumber: string;
	firstName: string;
	lastName: string;
}

