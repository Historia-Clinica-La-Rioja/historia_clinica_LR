import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { AdditionalInfo, PersonPhotoDto } from '@api-rest/api-model';

const NO_DOCUMENT_TYPE = 'No posee';
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
	@Input() personalAdditionalInformation: AdditionalInfo[];
	@Input() showAdditionalInformation: boolean;
	@Input() size = 'default';
	decodedPhoto$: Observable<string>;

	constructor(private readonly imageDecoderService: ImageDecoderService) { }

	public viewIDAndIdentificationTypeAndNumber(){
		if(this.showAdditionalInformation){
			let identificationType: string;
			let identificationNumber: string;
			this.personalAdditionalInformation.map(info => {
				identificationNumber = info.data;
				identificationType = info.description;
			})
			if ((identificationNumber === undefined) && ((identificationType === undefined) || (identificationType ===  NO_DOCUMENT_TYPE))){
				return ('ID ' + this.patient?.id);
			}
			else{
				const idType = ((identificationType === undefined) || (identificationType ===  NO_DOCUMENT_TYPE)) ? (' · Documento: ') : (' · ' + identificationType + ': ');
				const idNumber = (identificationNumber === undefined) ? ('Sin Información') : (identificationNumber);
				return ('ID ' + this.patient?.id + idType + idNumber);
			}
		}
		return ('ID ' + this.patient?.id);
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
