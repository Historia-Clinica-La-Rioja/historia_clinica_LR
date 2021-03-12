import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {PatientBasicData} from '@presentation/components/patient-card/patient-card.component';
import {PatientService} from '@api-rest/services/patient.service';
import {BasicPatientDto, PersonPhotoDto} from '@api-rest/api-model';
import {map} from 'rxjs/operators';
import {MapperService} from '@presentation/services/mapper.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	patient$: Observable<PatientBasicData>;
	private patientId: number;
	public personPhoto: PersonPhotoDto;

	constructor(
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
	) { }

	ngOnInit(): void {

		this.patientId = 1;

		this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
			map(patient => this.mapperService.toPatientBasicData(patient))
		);

		this.patientService.getPatientPhoto(this.patientId)
			.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto; });

	}

}
