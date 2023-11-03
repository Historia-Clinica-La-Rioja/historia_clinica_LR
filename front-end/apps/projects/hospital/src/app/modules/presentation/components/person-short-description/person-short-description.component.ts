import { Component, Input, OnInit } from '@angular/core';
import { PersonPhotoDto } from '@api-rest/api-model';
import { PatientGenderService } from '@core/services/patient-gender.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { Observable } from 'rxjs';

const NO_DOCUMENT_TYPE = 'No posee';
@Component({
	selector: 'app-person-short-description',
	templateUrl: './person-short-description.component.html',
	styleUrls: ['./person-short-description.component.scss']
})
export class PersonShortDescriptionComponent implements OnInit {

	@Input() person: PersonShortDescription;
	@Input() set personPhoto(personPhotoDto: PersonPhotoDto) {
		if (personPhotoDto?.imageData) {
			this.decodedPhoto$ = this.imageDecoderService.decode(personPhotoDto.imageData);
		}
	}
	@Input() showAdditionalInformation: boolean;
	@Input() personalAdditionalInformation: AdditionalInfo[];

	decodedPhoto$: Observable<string>;

	constructor(
		private readonly imageDecoderService: ImageDecoderService,
		private readonly patientNameService: PatientNameService,
		private readonly patientGenderService: PatientGenderService
	) { }

	ngOnInit(): void {
	}

	public showID(): string {
		if (this.person?.id === undefined) {
			return ('ID');
		}
		else {
			return ('ID ' + this.person?.id);
		}
	}

	public viewGenderAge(): string {
		let gender = this.patientGenderService.getPatientGender(this.person?.gender, this.person?.selfPerceivedGender);
		gender = gender ? gender : 'Sin género';
		const age = (this.person?.age) || (this.person?.age === 0) ? (this.person.age + ' años') : 'Sin edad';
		return gender + ' · ' + age;
	}

	public viewPatientName(): string {
		let name = this.patientNameService.getPatientName(this.person?.firstName, this.person?.nameSelfDetermination)
		if (name == this.person?.firstName && (this.person?.middleNames !== null && this.person?.middleNames !== undefined))
			name = this.person?.firstName + " " + this.person?.middleNames
		return name;
	}

	public showIdentificationTypeAndNumber(): string {
		let identificationType: string;
		let identificationNumber: string;
		this.personalAdditionalInformation.forEach(info => {
			identificationNumber = info.data;
			identificationType = info.description;
		});
		if ((identificationNumber === undefined) && ((identificationType === undefined) || (identificationType === NO_DOCUMENT_TYPE))) {
			return ('');
		}
		else {
			const idType = ((identificationType === undefined) || (identificationType === NO_DOCUMENT_TYPE)) ? (' · Documento: ') : (' · ' + identificationType + ': ');
			const idNumber = (identificationNumber === undefined) ? ('Sin Información') : (identificationNumber);
			return (idType + idNumber);
		}
	}


	public viewIDAndIdentificationTypeAndNumber(): string {
		if (this.showAdditionalInformation) {
			return (this.showID() + this.showIdentificationTypeAndNumber());
		}
		return (this.showID());
	}

}

export interface PersonShortDescription {
	id: number;
	firstName: string;
	middleNames?: string;
	lastName: string;
	otherLastNames?: string;
	gender?: string;
	age?: number;
	nameSelfDetermination?: string;
	selfPerceivedGender?: string;
}
