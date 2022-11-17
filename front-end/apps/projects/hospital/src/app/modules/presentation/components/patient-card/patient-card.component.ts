import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import {
	InternmentEpisodeProcessDto,
	PersonPhotoDto
} from '@api-rest/api-model';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientNameService } from '@core/services/patient-name.service';
import { PatientGenderService } from "@core/services/patient-gender.service";
import { Color } from '@presentation/colored-label/colored-label.component';

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
	@Input() internmentEpisodeProcess: InternmentEpisodeProcessDto;
	@Input() emergencyCareEpisodeInProgress: boolean;
	Color = Color;

	constructor(
		private readonly imageDecoderService: ImageDecoderService,
		private readonly patientNameService: PatientNameService,
		private readonly patientGenderService: PatientGenderService
	) { }

	public showID(): string {
		if (this.patient?.id === undefined) {
			return ('ID');
		}
		else {
			return ('ID ' + this.patient?.id);
		}
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

	public viewGenderAge(): string {
		let gender = this.patientGenderService.getPatientGender(this.patient?.gender, this.patient?.selfPerceivedGender);
		gender = gender ? gender : 'Sin género';
		const age = (this.patient?.age) || (this.patient?.age === 0) ? (this.patient.age + ' años') : 'Sin edad';
		return gender + ' · ' + age;
	}

	public viewPatientName(): string {
		let name = this.patientNameService.getPatientName(this.patient?.firstName, this.patient?.nameSelfDetermination)
		if (name == this.patient?.firstName && (this.patient?.middleNames !== null && this.patient?.middleNames !== undefined))
			name = this.patient?.firstName + " " + this.patient?.middleNames
		return name;
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
	nameSelfDetermination?: string;
	selfPerceivedGender?: string;
}
