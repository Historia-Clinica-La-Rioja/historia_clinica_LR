import { Component, OnInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Moment } from 'moment';
import * as moment from 'moment';
import {
	ERole
} from '@api-rest/api-model';
import {
	APatientDto,
	BMPatientDto,
	GenderDto,
	IdentificationTypeDto,
	CompletePatientDto,
	BMPersonDto,
	PatientMedicalCoverageDto,
	EthnicityDto,
	PersonOccupationDto,
	EducationLevelDto,
	SelfPerceivedGenderDto
} from '@api-rest/api-model';

import { AppFeature, } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { scrollIntoError, hasError, VALIDATIONS, DEFAULT_COUNTRY_ID, updateControlValidator } from '@core/utils/form.utils';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { PersonService } from '@api-rest/services/person.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PATIENT_TYPE } from '@core/utils/patient.utils';
import { MatDialog } from '@angular/material/dialog';
import { MedicalCoverageComponent, PatientMedicalCoverage, } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { map } from 'rxjs/operators';
import { MapperService } from '@core/services/mapper.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PERSON } from '@core/constants/validation-constants';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PermissionsService } from '@core/services/permissions.service';


const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
	selector: 'app-edit-patient',
	templateUrl: './edit-patient.component.html',
	styleUrls: ['./edit-patient.component.scss']
})
export class EditPatientComponent implements OnInit {

	readonly PERSON_MAX_LENGTH = PERSON.MAX_LENGTH;
	readonly GENDER_MAX_LENGTH = VALIDATIONS.MAX_LENGTH.gender;
	private readonly NONE_SELF_PERCEIVED_GENDER_SELECTED_ID = 10; // Dato Maestro proveniente de género autopercibido "Ninguna de las anteriores"

	public form: FormGroup;
	public personResponse: BMPatientDto;
	public formSubmitted = false;
	public today: Moment = moment();
	public hasError = hasError;
	public genders: GenderDto[];
	public selfPerceivedGenders: SelfPerceivedGenderDto[];
	public showOtherGender: boolean;
	public countries: any[];
	public provinces: any[];
	public departments: any[];
	public cities: any[];
	public identificationTypeList: IdentificationTypeDto[];
	private readonly routePrefix;
	public patientType;
	public completeDataPatient: CompletePatientDto;
	public patientId: any;

	private medicalCoverages: PatientMedicalCoverage[];
	public ethnicities: EthnicityDto[];
	public occupations: PersonOccupationDto[];
	public educationLevels: EducationLevelDto[];
	currentEducationLevelDescription: string;
	currentOccupationDescription: string;
	hasInstitutionalAdministratorRole = false;
	constructor(
		private formBuilder: FormBuilder,
		private router: Router,
		private el: ElementRef,
		private patientService: PatientService,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		private addressMasterDataService: AddressMasterDataService,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private personService: PersonService,
		private featureFlagService: FeatureFlagService,
		private dialog: MatDialog,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private permissionsService: PermissionsService,

	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.route.queryParams
			.subscribe(params => {
				this.patientId = params.id;
				this.formBuild();
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						this.completeDataPatient = completeData;
						this.patientType = completeData.patientType.id;
						this.personService.getCompletePerson<BMPersonDto>(completeData.person.id)
							.subscribe(personInformationData => {
								if (personInformationData.identificationTypeId) {
									this.form.setControl('identificationTypeId', new FormControl(Number(personInformationData.identificationTypeId), Validators.required));
								}
								this.form.setControl('identificationNumber', new FormControl(personInformationData.identificationNumber, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]));
								// person
								this.form.setControl('firstName', new FormControl(completeData.person.firstName, Validators.required));
								this.form.setControl('middleNames', new FormControl(personInformationData.middleNames));
								this.form.setControl('lastName', new FormControl(completeData.person.lastName, Validators.required));
								this.form.setControl('otherLastNames', new FormControl(personInformationData.otherLastNames));
								this.form.setControl('mothersLastName', new FormControl(personInformationData.mothersLastName));
								if (completeData.person.gender.id) {
									this.form.setControl('genderId', new FormControl(Number(completeData.person.gender.id), Validators.required));
								}
								this.form.setControl('genderSelfDeterminationId', new FormControl(Number(personInformationData.genderSelfDeterminationId)));

								const OTHER_GENDER_VALUE = personInformationData.otherGenderSelfDetermination ? personInformationData.otherGenderSelfDetermination : null;
								this.form.setControl('otherGenderSelfDetermination', new FormControl(OTHER_GENDER_VALUE, [Validators.required, Validators.maxLength(this.GENDER_MAX_LENGTH)]));
								if (personInformationData.genderSelfDeterminationId !== this.NONE_SELF_PERCEIVED_GENDER_SELECTED_ID) {
									this.form.get('otherGenderSelfDetermination').disable();
									this.showOtherGender = false;
								}
								else
									this.showOtherGender = true;

								this.form.setControl('nameSelfDetermination', new FormControl(personInformationData.nameSelfDetermination));
								this.form.setControl('birthDate', new FormControl(new Date(personInformationData.birthDate), Validators.required));
								this.form.setControl('cuil', new FormControl(personInformationData.cuil, Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)));
								this.form.setControl('email', new FormControl(personInformationData.email, Validators.email));
								this.form.setControl('phonePrefix', new FormControl(personInformationData.phonePrefix));
								this.form.setControl('phoneNumber', new FormControl(personInformationData.phoneNumber));
								if (personInformationData.phoneNumber) {
									updateControlValidator(this.form, 'phoneNumber', [Validators.required]);
									updateControlValidator(this.form, 'phonePrefix', [Validators.required]);
								}
								this.form.setControl('religion', new FormControl(personInformationData.religion));
								this.form.setControl('ethnicityId', new FormControl(personInformationData.ethnicityId));
								this.form.setControl('occupationId', new FormControl(personInformationData.occupationId));
								this.form.setControl('educationLevelId', new FormControl(personInformationData.educationLevelId));
								// address
								if (personInformationData.countryId) {
									this.form.setControl('addressCountryId', new FormControl(personInformationData.countryId));
									this.setProvinces();
								}
								if (personInformationData.provinceId) {
									this.form.setControl('addressProvinceId', new FormControl(personInformationData.provinceId));
									this.setDepartments();
								}
								if (personInformationData.departmentId) {
									this.form.setControl('addressDepartmentId', new FormControl(personInformationData.departmentId));
									this.setCities();
								}
								this.form.setControl('addressCityId', new FormControl(personInformationData.cityId));
								this.form.setControl('addressStreet', new FormControl(personInformationData.street));
								this.form.setControl('addressNumber', new FormControl(personInformationData.number));
								this.form.setControl('addressFloor', new FormControl(personInformationData.floor));
								this.form.setControl('addressApartment', new FormControl(personInformationData.apartment));
								this.form.setControl('addressQuarter', new FormControl(personInformationData.quarter));
								this.form.setControl('addressPostcode', new FormControl(personInformationData.postcode));
								// doctors
								this.form.setControl('generalPractitioner', new FormControl(completeData.generalPractitioner?.fullName));
								this.form.setControl('generalPractitionerPhoneNumber', new FormControl(completeData.generalPractitioner?.phoneNumber));
								this.form.setControl('pamiDoctor', new FormControl(completeData.pamiDoctor?.fullName));
								this.form.setControl('pamiDoctorPhoneNumber', new FormControl(completeData.pamiDoctor?.phoneNumber));
								this.restrictFormEdit();

								this.form.get("addressCountryId").valueChanges.subscribe(
									countryId => {
										this.clear(this.form.controls.addressProvinceId);
										delete this.provinces;
										if (countryId) {
											this.addressMasterDataService.getByCountry(countryId)
												.subscribe(provinces => {
													this.provinces = provinces;
												});
										}
									}
								);

								this.form.get("addressProvinceId").valueChanges.subscribe(
									provinceId => {
										this.clear(this.form.controls.addressDepartmentId);
										delete this.departments;
										if (provinceId) {
											this.addressMasterDataService.getDepartmentsByProvince(provinceId)
												.subscribe(departments => {
													this.departments = departments;
												});
										}
									}
								);

								this.form.get("addressDepartmentId").valueChanges.subscribe(
									departmentId => {
										this.clear(this.form.controls.addressCityId);
										delete this.cities;
										if (departmentId) {
											this.addressMasterDataService.getCitiesByDepartment(departmentId)
												.subscribe(cities => {
													this.cities = cities;
												});
										}
									}
								);

								this.form.get("addressCityId").valueChanges.subscribe(
									_ => {
											this.clear(this.form.controls.addressNumber);
											this.clear(this.form.controls.addressFloor);
											this.clear(this.form.controls.addressApartment);
											this.clear(this.form.controls.addressQuarter);
											this.clear(this.form.controls.addressStreet);
											this.clear(this.form.controls.addressPostcode);
									}
								);

								//Tooltips
								this.currentOccupationDescription = this.occupations.find(occupation => occupation.id === personInformationData.occupationId)?.description;
								this.currentEducationLevelDescription = this.educationLevels.find(educationLevel => educationLevel.id === personInformationData.educationLevelId)?.description;
							});
					});
			});

		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});

		this.personMasterDataService.getSelfPerceivedGenders()
			.subscribe(
				genders => this.selfPerceivedGenders = genders
			);

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});

		this.personMasterDataService.getEthnicities()
			.subscribe(ethnicities => {
				this.ethnicities = ethnicities;
			});

		this.personMasterDataService.getOccupations()
			.subscribe(occupations => {
				this.occupations = occupations;
			});

		this.personMasterDataService.getEducationLevels()
			.subscribe(educationLevels => {
				this.educationLevels = educationLevels;
			});

		this.addressMasterDataService.getAllCountries()
			.subscribe(countries => {
				this.countries = countries;
			});

			this.permissionsService.hasContextAssignments$([ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE]).subscribe(hasInstitutionalAdministratorRole => this.hasInstitutionalAdministratorRole = hasInstitutionalAdministratorRole);
	}

	updatePhoneValidators() {
		if (this.form.controls.phoneNumber.value || this.form.controls.phonePrefix.value) {
			updateControlValidator(this.form, 'phoneNumber', [Validators.required]);
			updateControlValidator(this.form, 'phonePrefix', [Validators.required]);
		} else {
			updateControlValidator(this.form, 'phoneNumber', []);
			updateControlValidator(this.form, 'phonePrefix', []);
		}

	}
	formBuild() {
		this.form = this.formBuilder.group({
			firstName: [null, [Validators.required]],
			middleNames: [null],
			lastName: [null, [Validators.required]],
			otherLastNames: [null],
			genderId: [null, [Validators.required]],
			identificationNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			identificationTypeId: [null, [Validators.required]],
			birthDate: [null, [Validators.required]],

			// Person extended
			cuil: [null, [Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)]],
			mothersLastName: [],
			phonePrefix: [],
			phoneNumber: [],
			email: [null, Validators.email],
			ethnicityId: [],
			occupationId: [],
			educationLevelId: [],
			religion: [],
			nameSelfDetermination: [],
			genderSelfDeterminationId: [],

			// Address
			addressStreet: [],
			addressNumber: [],
			addressFloor: [],
			addressApartment: [],
			addressQuarter: [],
			addressCityId: [],
			addressPostcode: [],
			addressProvinceId: [],
			addressCountryId: [],
			addressDepartmentId: [],

			// doctors
			generalPractitioner: [],
			generalPractitionerPhoneNumber: [],
			pamiDoctor: [],
			pamiDoctorPhoneNumber: []
		});
	}

	save(): void {
		this.formSubmitted = true;
		if (this.form.valid) {
			const personRequest: APatientDto = this.mapToPersonRequest();
			this.patientService.editPatient(personRequest, this.patientId)
				.subscribe(patientId => {
					if (this.medicalCoverages) {
						const patientMedicalCoveragesDto: PatientMedicalCoverageDto[] =
							this.medicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));
						this.patientMedicalCoverageService.addPatientMedicalCoverages(this.patientId, patientMedicalCoveragesDto)
							.subscribe();
					}
					this.router.navigate([this.routePrefix + ROUTE_PROFILE + patientId]);
					this.snackBarService.showSuccess(this.getMessagesSuccess());
				}, _ => this.snackBarService.showError(this.getMessagesError()));
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	private getMessagesSuccess(): string {
		return this.hasInstitutionalAdministratorRole ? 'pacientes.edit.messages.SUCCESS_PERSON' : 'pacientes.edit.messages.SUCCESS_PATIENT' ;
	}

	private getMessagesError(): string {
		return this.hasInstitutionalAdministratorRole ? 'pacientes.edit.messages.ERROR_PERSON' : 'pacientes.edit.messages.ERROR_PATIENT' ;
	}

	private mapToPersonRequest(): APatientDto {
		const patient: APatientDto = {
			birthDate: this.form.controls.birthDate.value,
			firstName: this.form.controls.firstName.value,
			genderId: this.form.controls.genderId.value,
			identificationTypeId: this.form.controls.identificationTypeId.value,
			identificationNumber: this.form.controls.identificationNumber.value,
			lastName: this.form.controls.lastName.value,
			middleNames: this.form.controls.middleNames.value && this.form.controls.middleNames.value.length ? this.form.controls.middleNames.value : null,
			otherLastNames: this.form.controls.otherLastNames.value && this.form.controls.otherLastNames.value.length ? this.form.controls.otherLastNames.value : null,
			// Person extended
			cuil: this.form.controls.cuil.value,
			email: this.form.controls.email.value && this.form.controls.email.value.length ? this.form.controls.email.value : null,
			ethnicityId: this.form.controls.ethnicityId.value,
			occupationId: this.form.controls.occupationId.value,
			educationLevelId: this.form.controls.educationLevelId.value,
			genderSelfDeterminationId: this.form.controls.genderSelfDeterminationId.value,
			mothersLastName: this.form.controls.mothersLastName.value,
			nameSelfDetermination: this.form.controls.nameSelfDetermination.value ? this.form.controls.nameSelfDetermination.value : null,
			phonePrefix: this.form.controls.phonePrefix.value,
			phoneNumber: this.form.controls.phoneNumber.value,
			religion: this.form.controls.religion.value,
			// Address
			apartment: this.form.controls.addressApartment.value,
			cityId: this.form.controls.addressCityId.value,
			floor: this.form.controls.addressFloor.value,
			number: this.form.controls.addressNumber.value,
			postcode: this.form.controls.addressPostcode.value,
			quarter: this.form.controls.addressQuarter.value,
			street: this.form.controls.addressStreet.value,
			countryId: this.form.controls.addressCountryId.value,
			departmentId: this.form.controls.addressDepartmentId.value,
			provinceId: this.form.controls.addressProvinceId.value,
			// Patient
			typeId: this.patientType,
			comments: null,
			identityVerificationStatusId: null,
			// Doctors
			generalPractitioner: {
				id: this.completeDataPatient.generalPractitioner?.id,
				fullName: this.form.controls.generalPractitioner.value,
				phoneNumber: this.form.controls.generalPractitionerPhoneNumber.value,
				generalPractitioner: true
			},
			pamiDoctor: {
				id: this.completeDataPatient.pamiDoctor?.id,
				fullName: this.form.controls.pamiDoctor.value,
				phoneNumber: this.form.controls.pamiDoctorPhoneNumber.value,
				generalPractitioner: false

			}
		};

		if (patient.genderSelfDeterminationId === this.NONE_SELF_PERCEIVED_GENDER_SELECTED_ID)
			patient.otherGenderSelfDetermination = this.form.value.otherGenderSelfDetermination;

		return patient;

	}

	setProvinces() {
		const countryId: number = this.form.controls.addressCountryId.value;
		this.addressMasterDataService.getByCountry(countryId)
			.subscribe(provinces => {
				this.provinces = provinces;
			});
	}

	setDepartments() {
		const provinceId: number = this.form.controls.addressProvinceId.value;
		this.addressMasterDataService.getDepartmentsByProvince(provinceId)
			.subscribe(departments => {
				this.departments = departments;
			});
	}

	setCities() {
		const departmentId: number = this.form.controls.addressDepartmentId.value;
		this.addressMasterDataService.getCitiesByDepartment(departmentId)
			.subscribe(cities => {
				this.cities = cities;
			});
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.form.getRawValue().genderId,
				identificationNumber: this.form.getRawValue().identificationNumber,
				identificationTypeId: this.form.getRawValue().identificationTypeId,
				initValues: this.medicalCoverages,
				patientId: this.patientId,
			}
		});
		dialogRef.afterClosed().subscribe(medicalCoverages => {
			if (medicalCoverages) {
				this.medicalCoverages = medicalCoverages.patientMedicalCoverages;
			}
		});
	}

	goBack(): void {
		this.formSubmitted = false;
		this.router.navigate([this.routePrefix + ROUTE_PROFILE + `${this.patientId}`]);
	}

	private disableFormField() {
		this.form.controls.identificationNumber.disable();
		this.form.controls.identificationTypeId.disable();
		this.form.controls.genderId.disable();
		this.form.controls.firstName.disable();
		this.form.controls.middleNames.disable();
		this.form.controls.lastName.disable();
		this.form.controls.otherLastNames.disable();
		this.form.controls.birthDate.disable();
	}

	private isLockablePatientType(): boolean {
		return (this.patientType === PATIENT_TYPE.PERMANENT_INVALID || this.patientType === PATIENT_TYPE.VALID || this.patientType === PATIENT_TYPE.PERMANENT);
	}

	private restrictFormEdit(): void {
		this.featureFlagService.isActive(AppFeature.RESTRINGIR_DATOS_EDITAR_PACIENTE)
			.subscribe(result => {
				if (result && this.isLockablePatientType()) {
					this.disableFormField();
				}
			});
	}

	public showOtherSelfPerceivedGender(): void {
		this.showOtherGender = (this.form.value.genderSelfDeterminationId === this.NONE_SELF_PERCEIVED_GENDER_SELECTED_ID);
		this.form.get('otherGenderSelfDetermination').setValue(null);
		if (this.showOtherGender)
			this.form.get('otherGenderSelfDetermination').enable();
		else
			this.form.get('otherGenderSelfDetermination').disable();
	}

	public clearGenderSelfDetermination(): void {
		this.form.controls.genderSelfDeterminationId.reset();
		this.showOtherSelfPerceivedGender();
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}
