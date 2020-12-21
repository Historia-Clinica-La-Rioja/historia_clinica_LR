import { Component, Input, OnInit } from '@angular/core';
import { PersonPhotoDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { PatientPhotoService } from '@presentation/services/patient-photo.service';

@Component({
	selector: 'app-patient-card',
	templateUrl: './patient-card.component.html',
	styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent implements OnInit {

	@Input() patient: PatientBasicData;
	@Input() personPhoto: PersonPhotoDto;
	decodedPhoto$: Observable<string>;

	constructor(private readonly patientPhotoService: PatientPhotoService) { }

	ngOnInit(): void {
		if (this.personPhoto.imageData) {
				this.decodedPhoto$ = this.patientPhotoService.decodePhoto(this.personPhoto.imageData);
		}
	}

	public viewGenderAge() {
		const gender = (this.patient?.gender) ? (this.patient.gender + ' · ') : '';
		const age = (this.patient?.age) || (this.patient?.age === 0) ? (this.patient.age + ' años') : '';
		return gender + age;
	}
}

export class PatientBasicData {
	id: number;
	firstName: string;
	middleNames?: string;
	lastName: string;
	otherLastNames?: string;
	gender?: string;
	age?: number;
}
