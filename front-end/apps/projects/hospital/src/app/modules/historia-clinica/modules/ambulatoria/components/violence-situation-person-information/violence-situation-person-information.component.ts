import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EDisabilityCertificateStatus, EKeeperRelationship, MasterDataDto, ViolenceReportDto, ViolenceReportVictimDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { DEFAULT_COUNTRY_ID, hasError, updateControlValidator } from '@core/utils/form.utils';
import { Observable, Subscription } from 'rxjs';
import { BasicOptions, BasicTwoOptions, DisabilityCertificateStatus, FormOption, RelationOption, Sectors } from '../../constants/violence-masterdata';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';

const idNoInfo = 99;
@Component({
	selector: 'app-violence-situation-person-information',
	templateUrl: './violence-situation-person-information.component.html',
	styleUrls: ['./violence-situation-person-information.component.scss']
})
export class ViolenceSituationPersonInformationComponent implements OnInit, OnDestroy {
	@Output() personInformation = new EventEmitter<any>();
	@Input() confirmForm: Observable<boolean>;

	disabilityCertificateStatus = DisabilityCertificateStatus;

	relations = RelationOption;

	sectors = Sectors;

	formOption = FormOption;

	relationOptionOther = EKeeperRelationship.OTHER;

	basicOptions = BasicOptions;

	basicTwoOptions = BasicTwoOptions;

	provinces$: Observable<MasterDataDto[]>;
	departments$: Observable<MasterDataDto[]>;

	violenceSituationSub: Subscription;

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

	constructor(private addressMasterDataService: AddressMasterDataService,
				private readonly violenceSituationFacadeService: ViolenceReportFacadeService) { }

	ngOnInit(): void {
		this.setForm();
		this.setViolenceSituation();
		this.provinces$ = this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID);
	}

	ngOnChanges(changes: SimpleChanges) {
		if (!changes.confirmForm.isFirstChange()) {
			if (this.form.valid) {
				this.personInformation.emit(this.mapPersonInformatio());
			}else{
				this.personInformation.emit(null);
				this.form.markAllAsTouched();
			}
		}
	}

	ngOnDestroy(): void {
		this.violenceSituationSub.unsubscribe();
	}

	private setViolenceSituation() {
		this.violenceSituationSub = this.violenceSituationFacadeService.violenceSituation$
			.subscribe((result: ViolenceReportDto) => {
				const {victimData} = result;
				this.form.controls.knowHowToReadWrite.setValue(victimData.canReadAndWrite);
				this.form.controls.receiveIncome.setValue(victimData.incomeData.hasIncome);
				this.form.controls.whichSector.setValue(victimData.incomeData.worksAtFormalSector);
				this.form.controls.receivePlanAssistance.setValue(victimData.hasSocialPlan);
				this.form.controls.haveDisability.setValue(victimData.disabilityData.hasDisability);
				this.form.controls.haveDisabilityCertificate.setValue(victimData.disabilityData.disabilityCertificateStatus);
				this.form.controls.isPersonInstitutionalized.setValue(victimData.institutionalizedData.isInstitutionalized);
				this.form.controls.inWhichInstitution.setValue(victimData.institutionalizedData.institutionalizedDetails);
				this.form.controls.personTypeAge.setValue(victimData.lackOfLegalCapacity);
				if (victimData.lackOfLegalCapacity) {
					this.form.controls.lastname.setValue(victimData.keeperData.actorPersonalData.lastName);
					this.form.controls.name.setValue(victimData.keeperData.actorPersonalData.firstName);
					this.form.controls.age.setValue(victimData.keeperData.actorPersonalData.age);
					this.form.controls.address.setValue(victimData.keeperData.actorPersonalData.address);
					this.form.controls.addressProvinceId.setValue(victimData.keeperData.actorPersonalData.municipality.provinceId);
					this.form.controls.addressDepartmentId.setValue(victimData.keeperData.actorPersonalData.municipality.id);
					this.setDepartments();
					this.form.controls.relationPersonViolenceSituation.setValue(victimData.keeperData.relationshipWithVictim);
					this.form.controls.whichTypeRelation.setValue(victimData.keeperData?.otherRelationshipWithVictim);
				}
			});
	}

	private setForm() {
		this.form = new FormGroup({
			knowHowToReadWrite: new FormControl(null),
			receiveIncome: new FormControl(null),
			whichSector: new FormControl(null),
			receivePlanAssistance: new FormControl(null),
			haveDisability: new FormControl(null),
			haveDisabilityCertificate: new FormControl(null),
			isPersonInstitutionalized: new FormControl(null),
			inWhichInstitution: new FormControl(null),
			personTypeAge: new FormControl(null, Validators.required),
			lastname: new FormControl(null),
			name: new FormControl(null),
			age: new FormControl(null),
			address: new FormControl(null),
			addressProvinceId: new FormControl(null),
			addressDepartmentId: new FormControl(null),
			relationPersonViolenceSituation: new FormControl(null),
			whichTypeRelation: new FormControl(null),
		});
	}

	mapPersonInformatio(): ViolenceReportVictimDto {
		return {
			canReadAndWrite: this.form.value.knowHowToReadWrite,
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
					municipality: {
						id: this.form.value.addressDepartmentId ? this.form.value.addressDepartmentId : null,
						provinceId: null,
						description: null
					},
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
		if(this.form.value.addressProvinceId === idNoInfo){
			updateControlValidator(this.form, 'addressDepartmentId', []);
		  }else{
			updateControlValidator(this.form, 'addressDepartmentId', Validators.required);
		  }
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

	updateValidationsPersonTypeAge() {
		if (this.form.value.personTypeAge) {
			updateControlValidator(this.form, 'lastname', Validators.required);
			updateControlValidator(this.form, 'name', Validators.required);
			updateControlValidator(this.form, 'age', Validators.required);
			updateControlValidator(this.form, 'address', Validators.required);
			updateControlValidator(this.form, 'addressDepartmentId', Validators.required);
			updateControlValidator(this.form, 'addressProvinceId', Validators.required);
			updateControlValidator(this.form, 'relationPersonViolenceSituation', Validators.required);
		} else {
			updateControlValidator(this.form, 'lastname', []);
			updateControlValidator(this.form, 'name', []);
			updateControlValidator(this.form, 'age', []);
			updateControlValidator(this.form, 'address', []);
			updateControlValidator(this.form, 'addressDepartmentId', []);
			updateControlValidator(this.form, 'addressProvinceId', []);
			updateControlValidator(this.form, 'relationPersonViolenceSituation', []);
			this.resetPersonTypeAgeControls();
		}
	}

	private resetPersonTypeAgeControls() {
		this.form.controls.lastname.setValue(null);
		this.form.controls.name.setValue(null);
		this.form.controls.age.setValue(null);
		this.form.controls.address.setValue(null);
		this.form.controls.addressDepartmentId.setValue(null);
		this.form.controls.addressProvinceId.setValue(null);
		this.form.controls.relationPersonViolenceSituation.setValue(null);
		this.form.controls.whichTypeRelation.setValue(null);
	}

	updateValidationOtherRelation(){
		if(this.form.value.relationPersonViolenceSituation === EKeeperRelationship.OTHER){
			updateControlValidator(this.form, 'whichTypeRelation', Validators.required);
		}else{
			updateControlValidator(this.form, 'whichTypeRelation', []);
			this.form.controls.whichTypeRelation.setValue(null);
		}
	}

	updateValidationsHaveDisability() {
		if (this.form.value.haveDisability) {
			updateControlValidator(this.form, 'haveDisabilityCertificate', Validators.required);
		} else {
			updateControlValidator(this.form, 'haveDisabilityCertificate', []);
			this.form.controls.haveDisabilityCertificate.setValue(null);
		}
	}

	updateValidationsReceiveIncome(){
		if (this.form.value.receiveIncome) {
			updateControlValidator(this.form, 'whichSector', Validators.required);
		} else {
			updateControlValidator(this.form, 'whichSector', []);
			this.form.controls.whichSector.setValue(null);
		}
	}

	updateValidationsIsPersonInstitutionalized() {
		if (this.form.value.isPersonInstitutionalized) {
			updateControlValidator(this.form, 'inWhichInstitution', Validators.required);
		} else {
			updateControlValidator(this.form, 'inWhichInstitution', []);
			this.form.controls.inWhichInstitution.setValue(null);
		}
	}
}
