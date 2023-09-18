import { Injectable } from '@angular/core';
import { AppFeature } from "@api-rest/api-model";
import { FeatureFlagService } from "@core/services/feature-flag.service";

@Injectable({
	providedIn: 'root'
})
export class PatientNameService {

	private nameSelfDeterminationFF = false;

	constructor(private readonly featureFlagService: FeatureFlagService) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	getPatientName(patientName: string, patientNameSelfDetermination: string): string {
		return this.nameSelfDeterminationFF && patientNameSelfDetermination ? patientNameSelfDetermination : patientName;
	}

	getFullName(patientFirstName: string, patientNameSelfDetermination: string, patientSecondsName?: string): string {
		const secondName = patientSecondsName ? ' ' + patientSecondsName : ' ';
		return this.nameSelfDeterminationFF && patientNameSelfDetermination ? patientNameSelfDetermination : patientFirstName + secondName;
	}

	completeName(patientFirstName: string, patientNameSelfDetermination: string, lastName: string, patientSecondsName?: string, otherLastNames?: string): string {
		const name = this.getFullName(patientFirstName, patientNameSelfDetermination, patientSecondsName);
		const completeLastName = otherLastNames ? `${lastName} ${otherLastNames}` : lastName
		return `${name} ${completeLastName}`
	}
}
