import { Injectable } from '@angular/core';

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
