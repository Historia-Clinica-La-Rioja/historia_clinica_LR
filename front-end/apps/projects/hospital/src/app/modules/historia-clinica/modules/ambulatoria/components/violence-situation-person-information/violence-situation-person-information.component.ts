import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EDisabilityCertificateStatus, EKeeperRelationship, MasterDataDto, ViolenceReportVictimDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { DEFAULT_COUNTRY_ID, hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';
import { BasicOptions, BasicTwoOptions, DisabilityCertificateStatus, FormOption, RelationOption, Sectors } from '../../constants/violence-masterdata';
@Component({
	selector: 'app-violence-situation-person-information',
	templateUrl: './violence-situation-person-information.component.html',
	styleUrls: ['./violence-situation-person-information.component.scss']
})
export class ViolenceSituationPersonInformationComponent implements OnInit {
	@Output() personInformation = new EventEmitter<any>();
	@Input() confirmForm: Observable<boolean>;

	disabilityCertificateStatus = DisabilityCertificateStatus;

	relations =  RelationOption;

	sectors = Sectors;

	formOption = FormOption;

	relationOptionOther = EKeeperRelationship.OTHER;

	basicOptions = BasicOptions;

	basicTwoOptions = BasicTwoOptions;

	provinces$: Observable<MasterDataDto[]>;
	departments$: Observable<MasterDataDto[]>;

	hasError = hasError;

	form: FormGroup<{
		knowHowToReadWrite: FormControl<boolean>
		receiveIncome: FormControl<boolean>
		whichSector: FormControl<boolean>
		receivePlanAssistance: FormControl<boolean>
		haveDisability: FormControl<boolean>
		haveDisabilityCertificate: FormControl<EDisabilityCertificateStatus>
		isPersonInstitutionalized: FormControl<boolean>
		inWhichInstitution: FormControl<string>
		personTypeAge: FormControl<boolean>
		lastname: FormControl<string>
		name: FormControl<string>
		age: FormControl<number>
		address: FormControl<string>,
		addressProvinceId: FormControl<number>,
		addressDepartmentId: FormControl<number>,
		relationPersonViolenceSituation: FormControl<EKeeperRelationship>,
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
			relationPersonViolenceSituation: new FormControl(null, Validators.required),
			whichTypeRelation: new FormControl(null, Validators.required),
		});
		this.provinces$ = this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID);

	}

	ngOnChanges(changes: SimpleChanges) {
		if (!changes.confirmForm.isFirstChange()) {
			this.personInformation.emit(this.mapPersonInformatio());
		}
	}

	mapPersonInformatio(): ViolenceReportVictimDto {
		return {
			canReadAndWrite:this.form.value.knowHowToReadWrite,
			hasSocialPlan: this.form.value.receivePlanAssistance,
			lackOfLegalCapacity: this.form.value.personTypeAge,
			disabilityData: {
				disabilityCertificateStatus: this.form.value.haveDisabilityCertificate,
				hasDisability: this.form.value.haveDisability,
			},
			incomeData: {
				hasIncome: this.form.value.receiveIncome,
				worksAtFormalSector: this.form.value.whichSector,
			},
			institutionalizedData: {
				institutionalizedDetails: this.form.value.inWhichInstitution,
				isInstitutionalized: this.form.value.isPersonInstitutionalized,
			},
			keeperData: {
				actorPersonalData: {
					address: this.form.value.address,
					age: this.form.value.age,
					firstName: this.form.value.name,
					lastName: this.form.value.lastname,
					municipalityId: this.form.value.addressDepartmentId,
				},
				otherRelationshipWithVictim: this.form.value.whichTypeRelation,
				relationshipWithVictim: this.form.value.relationPersonViolenceSituation,
			}
		}
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
		this.departments$ = this.addressMasterDataService.getDepartmentsByProvince(this.form.value.addressProvinceId);
	}

	resetAllLocaltyControls(event: Event) {
		this.resetDepartmentControl(event);
		this.form.controls.addressProvinceId.reset();
	}

	resetDepartmentControl(event: Event) {
		event.stopPropagation();
		this.form.controls.addressDepartmentId.reset();
	}
}