import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PERSON } from '@core/constants/validation-constants';
import { MIN_DATE } from '@core/utils/date.utils';
import { hasError } from '@core/utils/form.utils';
import { newMoment } from '@core/utils/moment.utils';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-empadronamiento',
	templateUrl: './empadronamiento.component.html',
	styleUrls: ['./empadronamiento.component.scss']
})
export class EmpadronamientoComponent implements OnInit {
	hasError = hasError;
	personalInformationForm: FormGroup;
	genders: GenderDto[];
	identificationTypeList: IdentificationTypeDto[];
	today: Moment = newMoment();
	minDate = MIN_DATE;
	patientIdForm:FormGroup;

	readonly validations = PERSON;
	constructor(private readonly formBuilder: FormBuilder,	private readonly personMasterDataService: PersonMasterDataService,) { }

	ngOnInit(): void {
		this.setMasterData();
		this.initForms();
	}

	private setMasterData(): void {

		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});
	}
	private initForms() {
		this.patientIdForm = this.formBuilder.group({
			patientId: [null,[Validators.pattern(PATTERN_INTEGER_NUMBER)]]
		})

		this.personalInformationForm = this.formBuilder.group({
			firstName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.firstName), Validators.pattern(/^\S*$/)]],
			middleNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.middleNames), Validators.pattern(/^\S*$/)]],
			lastName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.lastName), Validators.pattern(/^\S*$/)]],
			otherLastNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.otherLastNames), Validators.pattern(/^\S*$/)]],
			genderId: [null],
			identificationNumber: [null, [Validators.maxLength(PERSON.MAX_LENGTH.identificationNumber), Validators.pattern(PATTERN_INTEGER_NUMBER) ,Validators.pattern(/^\S*$/)]],
			identificationTypeId: [IDENTIFICATION_TYPE_IDS.DNI],
			birthDate: []
		});
	}
	save(){

	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}
