import { Injectable } from '@angular/core';
import { SnomedDto } from "@api-rest/api-model";

@Injectable()
export class NewEpisodeService {


	private administrativeAdmission: AdministrativeAdmissionDto;
	constructor() { }

	setAdministrativeAdmission(data: AdministrativeAdmissionDto): void {
		this.administrativeAdmission = data;
	}

	getAdministrativeAdmission(): AdministrativeAdmissionDto{
		return this.administrativeAdmission;
	}
}

export class AdministrativeAdmissionDto {
	patient: {
		id: number;
		patientMedicalCoverageId: number;
	};
	reasons: SnomedDto[];
	typeId: number;
	entranceTypeId: number;
	ambulanceCompanyId: number;
	policeIntervention: {
		dateCall: string;
		timeCall: string;
		plateNumber: string;
		firstName: string;
		lastName: string;
	}
}
