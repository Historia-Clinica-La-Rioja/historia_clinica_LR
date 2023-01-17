import { Component, Input, OnInit } from '@angular/core';
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
import { PatientService } from '@api-rest/services/patient.service';
import { CompletePatientDto, PersonalInformationDto, TriageListDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators, FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { PersonService } from '@api-rest/services/person.service';
import { BMPersonDto } from '@api-rest/api-model';

const NO_DOCUMENT_TYPE = 'No posee';
@Component({
	selector: 'app-patient-card',
	templateUrl: './patient-card.component.html',
	styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent implements OnInit{

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
	
	public patientId: any;
	public form: FormGroup;
	public completeDataPatient: CompletePatientDto;
	public patientType;

	constructor(
		private readonly imageDecoderService: ImageDecoderService,
		private readonly patientNameService: PatientNameService,
		private readonly patientGenderService: PatientGenderService,
		private route: ActivatedRoute,
		private formBuilder: FormBuilder,
		private patientService: PatientService,
		private personService: PersonService,
	) { }

	ngOnInit(): void {
		this.route.queryParams
			.subscribe(params => {
				this.formBuild();
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patient?.id)
					.subscribe(completeData => {
						this.completeDataPatient = completeData;
						this.patientType = completeData.patientType.id;
						this.personService.getCompletePerson<BMPersonDto>(completeData.person.id)
							.subscribe(personInformationData => {
								this.form.setControl('birthDate', new FormControl(new Date(personInformationData.birthDate), Validators.required));
							});
					});
			});
	}

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

	formBuild() {
		this.form = this.formBuilder.group({
			birthDate: [null, [Validators.required]]
		});
	}

	public viewGenderAge(): string {
		let gender = this.patientGenderService.getPatientGender(this.patient?.gender, this.patient?.selfPerceivedGender);
		gender = gender ? gender : 'Sin género';
		var age = 'Sin edad';
		if ((this.patient?.age < 2) && (this.form.valid)){
			let fecha1 = new Date();
			let fecha2 = this.form.get('birthDate').value;
			console.log(fecha2)
			let milisegundosXdia = 24 * 60 * 60 * 1000;
			let milisegundosTranscurridos = Math.abs((fecha1.getTime())-(fecha2.getTime()));
			let diasTranscurridos = Math.round(milisegundosTranscurridos/milisegundosXdia);
			let meses = Math.round(diasTranscurridos/30);
			age = (this.patient?.age) || (this.patient?.age === 0) ? (meses + ' meses') : 'Sin edad';
		}
		else{
			age = (this.patient?.age) || (this.patient?.age === 0) ? (this.patient.age + ' años') : 'Sin edad';
		}
		console.log(age);
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