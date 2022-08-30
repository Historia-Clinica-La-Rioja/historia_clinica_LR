import { Injectable } from '@angular/core';
import {FeatureFlagService} from "@core/services/feature-flag.service";
import {AppFeature} from "@api-rest/api-model";

@Injectable({
  providedIn: 'root'
})
export class PatientGenderService {

	private selfPerceivedGenderEnabled = false;

	constructor(private readonly featureFlagService: FeatureFlagService) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{
			this.selfPerceivedGenderEnabled = isOn});
	}

	getPatientGender(patientGender: string, patientSelfPerceivedGender: string): string {
		return this.selfPerceivedGenderEnabled && patientSelfPerceivedGender ? patientSelfPerceivedGender : patientGender
	}
}
