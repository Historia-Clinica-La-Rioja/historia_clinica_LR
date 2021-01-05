import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { PersonPhotoDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';

@Component({
	selector: 'app-patient-card',
	templateUrl: './patient-card.component.html',
	styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent {

	@Input() patient: PatientBasicData;
	@Input() set personPhoto(personPhotoDto: PersonPhotoDto) {
		if (personPhotoDto?.imageData) {
			this.decodedPhoto$ = this.imageDecoderService.decode(personPhotoDto.imageData);
		}
	}
	@Input() size = 'default';
	decodedPhoto$: Observable<string>;

	constructor(private readonly imageDecoderService: ImageDecoderService) { }


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
