import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MasterDataDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { DEFAULT_COUNTRY_ID, hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';

interface BasicOption {
	text: string,
	value: boolean
}

enum FormOption {
	YES = 'Sí',
	NO = 'No',
	IN_PROCESS = "En trámite",
	WITHOUT_DATA = 'Sin información',
}

enum RelationOption {
	MOTHER = 'Madre',
	FATHER = 'Padre',
	GRANDFATHERMOTHER = 'Abuelo/a',
	AUNT = 'Tío/a',
	SIBLING = 'Hermano/a',
	REFERRER = 'Referente',
	OTHER = 'Otro'
}

enum SectorOption {
	FORMAL = 'Formal',
	INFORMAL = 'Informal'
}

@Component({
	selector: 'app-violence-situation-person-information',
	templateUrl: './violence-situation-person-information.component.html',
	styleUrls: ['./violence-situation-person-information.component.scss']
})
export class ViolenceSituationPersonInformationComponent implements OnInit {

	basicOptions: BasicOption[] = [
		{
			text: FormOption.YES,
			value: true
		},
		{
			text: FormOption.NO,
			value: false
		},
		{
			text: FormOption.WITHOUT_DATA,
			value: null
		}
	]

	basicOptionsExtended: string[] = [FormOption.YES, FormOption.NO, FormOption.IN_PROCESS, FormOption.WITHOUT_DATA];

	relations: string[] = [RelationOption.MOTHER, RelationOption.FATHER, RelationOption.GRANDFATHERMOTHER, RelationOption.AUNT, RelationOption.SIBLING, RelationOption.REFERRER, RelationOption.OTHER];

	sectors: string[] = [SectorOption.FORMAL, SectorOption.INFORMAL];

	formOption = FormOption;

	relationOption = RelationOption;

	provinces$: Observable<MasterDataDto[]>;
	departments$: Observable<MasterDataDto[]>;
	cities$: Observable<MasterDataDto[]>;

	hasError = hasError;

	form: FormGroup<{
		knowHowToReadWrite: FormControl<boolean>
		receiveIncome: FormControl<boolean>
		whichSector: FormControl<string>
		receivePlanAssistance: FormControl<boolean>
		haveDisability: FormControl<boolean>
		haveDisabilityCertificate: FormControl<FormOption>
		isPersonInstitutionalized: FormControl<boolean>
		inWhichInstitution: FormControl<string>
		personTypeAge: FormControl<boolean>
		lastname: FormControl<string>
		name: FormControl<string>
		age: FormControl<string>
		address: FormControl<string>,
		addressProvinceId: FormControl<number>,
		addressDepartmentId: FormControl<number>,
		addressCityId: FormControl<number>,
		relationPersonViolenceSituation: FormControl<string>,
		whichTypeRelation: FormControl<string>,
	}>;

	constructor(private addressMasterDataService: AddressMasterDataService) { }

	ngOnInit(): void {
		this.form = new FormGroup({
			knowHowToReadWrite: new FormControl(null, Validators.required),
			receiveIncome: new FormControl(null, Validators.required),
			whichSector: new FormControl(null, Validators.required),
			receivePlanAssistance: new FormControl(null, Validators.required),
			haveDisability: new FormControl(null, Validators.required),
			haveDisabilityCertificate: new FormControl(null, Validators.required),
			isPersonInstitutionalized: new FormControl(null, Validators.required),
			inWhichInstitution: new FormControl('', Validators.required),
			personTypeAge: new FormControl(null, Validators.required),
			lastname: new FormControl(null, Validators.required),
			name: new FormControl(null, Validators.required),
			age: new FormControl(null, Validators.required),
			address: new FormControl(null, Validators.required),
			addressProvinceId: new FormControl(null, Validators.required),
			addressDepartmentId: new FormControl(null, Validators.required),
			addressCityId: new FormControl(null, Validators.required),
			relationPersonViolenceSituation: new FormControl(null, Validators.required),
			whichTypeRelation: new FormControl(null, Validators.required),
		});
		this.provinces$ = this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID);
	}

	get haveDisability() {
		return this.form.value.haveDisability;
	}

	get isPersonInstitutionalized() {
		return this.form.value.isPersonInstitutionalized;
	}

	get personTypeAge() {
		return this.form.value.personTypeAge;
	}

	get relationPersonViolenceSituation() {
		return this.form.value.relationPersonViolenceSituation;
	}

	get receiveIncome() {
		return this.form.value.receiveIncome;
	}

	setDepartments() {
		this.provinces$ = this.addressMasterDataService.getDepartmentsByProvince(this.form.value.addressProvinceId);
	}

	setCities() {
		this.cities$ = this.addressMasterDataService.getCitiesByDepartment(this.form.value.addressDepartmentId);
	}

	resetAllLocaltyControls(event: Event) {
		this.resetDepartmentAndCityControl(event);
		this.form.controls.addressProvinceId.reset();
	}

	resetDepartmentAndCityControl(event: Event) {
		event.stopPropagation();
		this.form.controls.addressDepartmentId.reset();
		this.form.controls.addressCityId.reset();
	}
}