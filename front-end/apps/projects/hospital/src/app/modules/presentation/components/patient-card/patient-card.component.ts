import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import {
	ExternalPatientCoverageDto,
	InternmentEpisodeProcessDto,
	PersonPhotoDto
} from '@api-rest/api-model';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientNameService } from '@core/services/patient-name.service';

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
	@Input() bloodType: string;
	@Input() internmentEpisodeProcess: InternmentEpisodeProcessDto;

	constructor(private readonly imageDecoderService: ImageDecoderService, private readonly patientNameService: PatientNameService) { }

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
		this.personalAdditionalInformation.map(info => {
			identificationNumber = info.data;
			identificationType = info.description;
		})
		if ((identificationNumber === undefined) && ((identificationType === undefined) || (identificationType === NO_DOCUMENT_TYPE))) {
			return ('');
		}
		else {
			const idType = ((identificationType === undefined) || (identificationType === NO_DOCUMENT_TYPE)) ? (' · Documento: ') : (' · ' + identificationType + ': ');
			const idNumber = (identificationNumber === undefined) ? ('Sin Información') : (identificationNumber);
			return (idType + idNumber);
		}
	}

	public viewIDAndIdentificationTypeAndNumber() {
		if (this.showAdditionalInformation) {
			return (this.showID() + this.showIdentificationTypeAndNumber());
		}
		return (this.showID());
	}

	public viewGenderAge() {
		const gender = (this.patient?.gender) ? (this.patient.gender + ' · ') : '';
		const age = (this.patient?.age) || (this.patient?.age === 0) ? (this.patient.age + ' años') : '';
		return gender + age;
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
}
