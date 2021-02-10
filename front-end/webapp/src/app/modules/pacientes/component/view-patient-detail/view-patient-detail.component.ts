import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';

const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
	selector: 'app-view-patient-detail',
	templateUrl: './view-patient-detail.component.html',
	styleUrls: ['./view-patient-detail.component.scss']
})
export class ViewPatientDetailComponent implements OnInit {

	public personPhoto: PersonPhotoDto;

	private readonly routePrefix;

	constructor(
		public dialogRef: MatDialogRef<ViewPatientDetailComponent>,
		@Inject(MAT_DIALOG_DATA) public patient: ViewPatientBasicData,
		private router: Router,
		private contextService: ContextService,
		private patientService: PatientService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit() {
		this.patientService.getPatientPhoto(this.patient.id)
			.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
	}

	back() {
		this.dialogRef.close();
	}

	goToProfile() {
		const id = this.patient.id;
		this.router.navigate([this.routePrefix + ROUTE_PROFILE + `${id}`]);
		this.dialogRef.close();
	}

}

export class ViewPatientBasicData extends PatientBasicData {
	birthDate: number;
	identificationNumber: number;
	identificationTypeId: string;
}
