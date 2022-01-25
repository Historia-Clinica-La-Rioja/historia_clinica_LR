import {Injectable} from '@angular/core';
import {AppFeature} from "@api-rest/api-model";
import {FeatureFlagService} from "@core/services/feature-flag.service";

@Injectable({
	providedIn: 'root'
})
export class PatientNameService {

	private nameSelfDeterminationFF;

	constructor(private readonly featureFlagService: FeatureFlagService) {

	}

	nameSelfDeterminationFFIsEnabled(): boolean {
		if ((this.nameSelfDeterminationFF === null) || (this.nameSelfDeterminationFF === undefined))
			this.featureFlagService.isActive(AppFeature.HABILITAR_NOMBRE_AUTOPERCIBIDO).subscribe(isOn => this.nameSelfDeterminationFF = isOn);
		return this.nameSelfDeterminationFF;
	}

	getPatientName(patientName: string, patientNameSelfDetermination: string): string {
		const nameSelfDetermination = patientNameSelfDetermination ? patientNameSelfDetermination : null;
		if (this.nameSelfDeterminationFFIsEnabled() && (patientNameSelfDetermination != undefined && nameSelfDetermination != null))
			return nameSelfDetermination;
		return patientName;
	}
}
