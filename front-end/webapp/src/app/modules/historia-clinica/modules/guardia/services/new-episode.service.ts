import { Injectable } from '@angular/core';
import { NewECAdministrativeDto } from "@api-rest/api-model";

@Injectable()
export class NewEpisodeService {

	private administrativeAdmission: NewECAdministrativeDto;

	constructor() { }

	destroy(): void {
		this.administrativeAdmission = undefined;
	}

	setAdministrativeAdmission(data: NewECAdministrativeDto): void {
		this.administrativeAdmission = data;
	}

	getAdministrativeAdmission(): NewECAdministrativeDto{
		return this.administrativeAdmission;
	}
}

