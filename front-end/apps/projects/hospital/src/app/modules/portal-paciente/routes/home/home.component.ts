import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PersonPhotoDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientPortalService } from '@api-rest/services/patient-portal.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	patient$: Observable<PatientBasicData>;
	personPhoto: PersonPhotoDto;
	bloodType: string;

	constructor(
		private readonly patientPortalService: PatientPortalService,
		private readonly mapperService: MapperService,
	) { }

	ngOnInit(): void {
		this.patient$ = this.patientPortalService.getBasicDataPatient().pipe(
			map(patient => this.mapperService.toPatientBasicData(patient))
		);

		this.patientPortalService.getPatientPhoto()
			.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });

		this.patientPortalService.getLast2AnthropometricData().subscribe(
			list => {
				if (list.length && list[0].bloodType?.value) {
					this.bloodType = list[0].bloodType.value
				}
			}
		);

	}

}
