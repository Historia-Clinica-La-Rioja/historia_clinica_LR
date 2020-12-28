import { Injectable } from '@angular/core';
import { SnomedDto } from "@api-rest/api-model";

@Injectable()
export class NewEpisodeService {


	private administrativeAdmision;
	constructor() { }

	setAdministrativeAdmision(data: any): void {
		this.administrativeAdmision = data;
	}

	getAdministrativeAdmision(): any{
		return this.administrativeAdmision;
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
