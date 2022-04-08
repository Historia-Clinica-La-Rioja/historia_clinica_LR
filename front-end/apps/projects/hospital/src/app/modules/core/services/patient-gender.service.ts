import { Injectable } from '@angular/core';
import {FeatureFlagService} from "@core/services/feature-flag.service";
import {AppFeature} from "@api-rest/api-model";

@Injectable({
  providedIn: 'root'
})
export class PatientGenderService {

	private selfPerceivedGenderEnabled;

	constructor(private readonly featureFlagService: FeatureFlagService) {

	}

	private selfPerceivedGenderIsEnabled(): boolean {
		if ((this.selfPerceivedGenderEnabled === null) || (this.selfPerceivedGenderEnabled === undefined))
			this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{
				this.selfPerceivedGenderEnabled = isOn});
		return this.selfPerceivedGenderEnabled;
	}

	getPatientGender(patientGender: string, patientSelfPerceivedGender: string): string {
		const nameSelfDetermination = patientSelfPerceivedGender ? patientSelfPerceivedGender : null;
		if (this.selfPerceivedGenderIsEnabled() && (patientSelfPerceivedGender != undefined && nameSelfDetermination != null))
			return nameSelfDetermination;
		return patientGender;
	}
}
