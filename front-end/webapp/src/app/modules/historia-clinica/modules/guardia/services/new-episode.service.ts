import { Injectable } from '@angular/core';
import { NewEmergencyCareDto } from "@api-rest/api-model";

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

