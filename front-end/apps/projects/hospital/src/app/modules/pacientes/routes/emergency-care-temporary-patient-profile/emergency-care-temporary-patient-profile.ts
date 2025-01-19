import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientToMergeDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeSummaryService } from '@api-rest/services/emergency-care-episode-summary.service';
import { PatientToMergeService } from '@api-rest/services/patient-to-merge.service';
import { ContextService } from '@core/services/context.service';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-emergency-care-temporary-patient-profile',
	templateUrl: './emergency-care-temporary-patient-profile.html',
	styleUrls: ['./emergency-care-temporary-patient-profile.scss']
})
export class EmergencyCareTemporaryPatientProfile {


	episodeId: number;
	patientId: number;
	constructor(
		private readonly route: ActivatedRoute,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService,
		private readonly patientToMergeService: PatientToMergeService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly router: Router,
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
				this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgressInTheInstitution(this.patientId)
					.pipe(map(r => r.id)).subscribe(rr => this.episodeId = rr)
			}
		)

	}

	patientSelected(patient: Patient) {

		const patientToMergeDto: PatientToMergeDto = {
			activePatientId: patient.basicData.id,
			oldPatientsIds: [this.patientId],
			registrationDataPerson: {
				middleNames: patient.basicData.middleName,
				otherLastNames: patient.basicData.person.otherLastNames,
				birthDate: patient.basicData.person.birthDate,
				firstName: patient.basicData.firstName,
				genderId: patient.basicData.person.gender.id,
				identificationNumber: patient.basicData.identificationNumber,
				identificationTypeId: patient.basicData.person.identificationTypeId,
				lastName: patient.basicData.person.lastName,
				nameSelfDetermination: patient.basicData.person.nameSelfDetermination,
				phoneNumber: null,
				phonePrefix: null
			}
		}

		this.patientToMergeService.merge(patientToMergeDto).subscribe(
			_ => {
				this.snackBarService.showSuccess('Se ha asignado correctamente el paciente');
				const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/pacientes/profile/${patient.basicData.id}`;
				this.router.navigate([url]);
			},
			error => this.snackBarService.showError(error.text || 'No cuenta con los roles suficientes para realizar esta accion'),
		)

	}
}
