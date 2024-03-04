import { Component, Input } from '@angular/core';
import { PersonPhotoDto } from '@api-rest/api-model';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { PatientGenderService } from "@core/services/patient-gender.service";
import { Observable } from 'rxjs';
import { PatientBasicData, getAge } from '@presentation/utils/patient.utils';

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
	@Input() internmentEpisodeInProgress: boolean;
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

	public setGender(): string {
		let gender = this.patientGenderService.getPatientGender(this.patient?.gender, this.patient?.selfPerceivedGender);
		gender = gender ? gender : 'presentation.patient.gender.NO_GENDER';
		return gender;
	}

	public getAge(): string{
		return getAge(this.patient);
	}

	public viewPatientName(): string {
		let name = this.patientNameService.getPatientName(this.patient?.firstName, this.patient?.nameSelfDetermination)
		if (name == this.patient?.firstName && (this.patient?.middleNames !== null && this.patient?.middleNames !== undefined))
			name = this.patient?.firstName + " " + this.patient?.middleNames
		return name;
	}

}
