import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ERole } from '@api-rest/api-model';
import { APatientDto, BMPatientDto, EthnicityDto, PersonOccupationDto, EducationLevelDto, GenderDto, IdentificationTypeDto, PatientMedicalCoverageDto, SelfPerceivedGenderDto } from '@api-rest/api-model';
import { scrollIntoError, hasError, VALIDATIONS, DEFAULT_COUNTRY_ID, updateControlValidator } from '@core/utils/form.utils';
import { Router, ActivatedRoute } from '@angular/router';
import { PatientService } from '@api-rest/services/patient.service';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { Moment } from 'moment';
import * as moment from 'moment';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { momentParseDate } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PERSON } from '@core/constants/validation-constants';
import { PermissionsService } from '@core/services/permissions.service';

const TEMPORARY_PATIENT = 3;
const ROUTE_HOME = 'pacientes';
const ROUTE_PROFILE = 'pacientes/profile/';
const TIME_TO_PREVENT_SCROLL = 100;

@Component({
	selector: 'app-new-temporary-patient',
	templateUrl: './new-temporary-patient.component.html',
	styleUrls: ['./new-temporary-patient.component.scss']
})
export class NewTemporaryPatientComponent implements OnInit {
	readonly PERSON_MAX_LENGTH = PERSON.MAX_LENGTH;
	readonly GENDER_MAX_LENGTH = VALIDATIONS.MAX_LENGTH.gender;
	private readonly NONE_SELF_PERCEIVED_GENDER_SELECTED_ID = 10; // Dato Maestro proveniente de género autopercibido "Ninguna de las anteriores"

	hasInstitutionalAdministratorRole = false;
	public form: FormGroup;
	public personResponse: BMPatientDto;
	public formSubmitted = false;
	public isSubmitButtonDisabled = false;
	public today: Moment = moment();
	public hasError = hasError;
	public genders: GenderDto[];
	public selfPerceivedGenders: SelfPerceivedGenderDto[];
	public showOtherGender: boolean = false;
	public countries: any[];
	public provinces: any[];
	public departments: any[];
	public cities: any[];
	public identificationTypeList: IdentificationTypeDto[];
	patientMedicalCoveragesToAdd: PatientMedicalCoverage[];

	private identityVerificationStatus;
	private comments;
	private readonly routePrefix;
	public ethnicities: EthnicityDto[];
	public occupations: PersonOccupationDto[];
	public educationLevels: EducationLevelDto[];

	public identificationNumberDisabled = false;
	public identificationTypeIdDisabled = false;
	public genderIdDisabled = false;
	public firstNameDisabled = false;
	public middleNamesDisabled = false;
	public lastNameDisabled = false;
	public otherLastNamesDisabled = false;
	public birthDateDisabled = false;
	@ViewChild('startView') startView: ElementRef;

	constructor(
		private formBuilder: FormBuilder,
		private router: Router,
		private el: ElementRef,
		private patientService: PatientService,
		private personMasterDataService: PersonMasterDataService,
		private addressMasterDataService: AddressMasterDataService,
		private route: ActivatedRoute,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private dialog: MatDialog,
		private mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private permissionsService: PermissionsService,

	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {

		this.route.queryParams
			.subscribe(params => {
				this.identityVerificationStatus = Number(params.IdentityVerificationStatus);
				this.comments = params.comments;

				this.form = this.formBuilder.group({
					firstName: [params.firstName],
					middleNames: [params.middleNames],
					lastName: [params.lastName],
					otherLastNames: [params.otherLastNames],
					identificationTypeId: [Number(params.identificationTypeId)],
					identificationNumber: [params.identificationNumber, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)],
					genderId: [Number(params.genderId)],
					birthDate: [params.birthDate ? momentParseDate(params.birthDate) : undefined],

					// Person extended
					cuil: [params.cuil, Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)],
					mothersLastName: [],
					phonePrefix: [],
					phoneNumber: [],
					email: [null, [Validators.email]],
					ethnicityId: [],
					occupationId: [],
					educationLevelId: [],
					religion: [],
					nameSelfDetermination: [],
					genderSelfDeterminationId: [],
					otherGenderSelfDetermination: [{ value: null, disabled: true }, [Validators.required, Validators.maxLength(this.GENDER_MAX_LENGTH)]],

					// Address
					addressStreet: [],
					addressNumber: [],
					addressFloor: [],
					addressApartment: [],
					addressQuarter: [],
					addressCityId: { value: null, disabled: true },
					addressPostcode: [],

					addressProvinceId: [],
					addressCountryId: [],
					addressDepartmentId: { value: null, disabled: true },

					// doctors
					generalPractitioner: [],
					generalPractitionerPhoneNumber: [],
					pamiDoctor: [],
					pamiDoctorPhoneNumber: []
				});

				this.form.get("addressCountryId").valueChanges.subscribe(
					countryId => {
						this.form.controls.addressProvinceId.reset();
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
						this.form.controls.addressDepartmentId.reset();
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
						this.form.controls.addressCityId.reset();
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
						this.form.controls.addressNumber.reset();
						this.form.controls.addressFloor.reset();
						this.form.controls.addressApartment.reset();
						this.form.controls.addressQuarter.reset();
						this.form.controls.addressStreet.reset();
						this.form.controls.addressPostcode.reset();
					}
				);

				this.lockFormField(params);
			});

		this.personMasterDataService.getGenders()
			.subscribe(
				genders => {
					this.genders = genders;
				}
			);

		this.personMasterDataService.getSelfPerceivedGenders()
			.subscribe(
				genders => this.selfPerceivedGenders = genders
			);

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => {
				this.identificationTypeList = identificationTypes;
			}
		);

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
				this.form.controls.addressCountryId.setValue(DEFAULT_COUNTRY_ID);
				this.setProvinces();
			});
		setTimeout(() => {
			this.startView.nativeElement.scrollIntoView();}, TIME_TO_PREVENT_SCROLL);

		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE]).subscribe(hasInstitutionalAdministratorRole => this.hasInstitutionalAdministratorRole = hasInstitutionalAdministratorRole);

	}

	save(): void {
		this.formSubmitted = true;
		if (this.form.valid) {
			const personRequest: APatientDto = this.mapToPersonRequest();
			this.isSubmitButtonDisabled = true;
			this.patientService.addPatient(personRequest)
				.subscribe(patientId => {
					if (this.patientMedicalCoveragesToAdd) {
						const patientMedicalCoveragesDto: PatientMedicalCoverageDto[] =
							this.patientMedicalCoveragesToAdd.map(s => this.mapperService.toPatientMedicalCoverageDto(s));
						this.patientMedicalCoverageService.addPatientMedicalCoverages
							(patientId, patientMedicalCoveragesDto).subscribe();
					}
					this.router.navigate([this.routePrefix + ROUTE_PROFILE + patientId]);
					this.snackBarService.showSuccess(this.getMessagesSuccess());
				}, _ => {
					this.isSubmitButtonDisabled = false;
					this.snackBarService.showError(this.getMessagesError());
				});
		} else {
			scrollIntoError(this.form, this.el);
		}

	}
	private getMessagesSuccess(): string {
		return this.hasInstitutionalAdministratorRole ? 'pacientes.new.messages.SUCCESS_PERSON' : 'pacientes.new.messages.SUCCESS_PATIENT' ;
	}

	private getMessagesError(): string {
		return this.hasInstitutionalAdministratorRole ? 'pacientes.new.messages.ERROR_PERSON' : 'pacientes.new.messages.ERROR_PATIENT' ;
	}


	private mapToPersonRequest(): APatientDto {
		const patient: APatientDto = {
			birthDate: this.form.controls.birthDate.value,
			firstName: this.form.controls.firstName.value,
			genderId: this.form.controls.genderId.value,
			identificationTypeId: this.form.controls.identificationTypeId.value,
			identificationNumber: this.form.controls.identificationNumber.value,
			lastName: this.form.controls.lastName.value,
			middleNames: this.form.controls.middleNames.value,
			otherLastNames: this.form.controls.otherLastNames.value,
			// Person extended
			cuil: this.form.controls.cuil.value,
			email: this.form.controls.email.value,
			ethnicityId: this.form.controls.ethnicityId.value,
			educationLevelId: this.form.controls.educationLevelId.value,
			occupationId: this.form.controls.occupationId.value,
			genderSelfDeterminationId: this.form.controls.genderSelfDeterminationId.value,
			mothersLastName: this.form.controls.mothersLastName.value,
			nameSelfDetermination: this.form.controls.nameSelfDetermination.value,
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
			typeId: TEMPORARY_PATIENT,
			comments: this.comments,
			identityVerificationStatusId: this.identityVerificationStatus,
			// doctors
			generalPractitioner: {
				fullName: this.form.controls.generalPractitioner.value,
				phoneNumber: this.form.controls.generalPractitionerPhoneNumber.value,
				generalPractitioner: true
			},
			pamiDoctor: {
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
		const idCountry: number = this.form.controls.addressCountryId.value;
		this.addressMasterDataService.getByCountry(idCountry)
			.subscribe(provinces => {
				this.provinces = provinces;
			});
	}

	setDepartments() {
		const idProvince: number = this.form.controls.addressProvinceId.value;
		this.addressMasterDataService.getDepartmentsByProvince(idProvince)
			.subscribe(departments => {
				this.departments = departments;
			});
		this.form.controls.addressDepartmentId.enable();
	}

	setCities() {
		const idDepartment: number = this.form.controls.addressDepartmentId.value;
		this.addressMasterDataService.getCitiesByDepartment(idDepartment)
			.subscribe(cities => {
				this.cities = cities;
			});
		this.form.controls.addressCityId.enable();
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.form.getRawValue().genderId,
				identificationNumber: this.form.getRawValue().identificationNumber,
				identificationTypeId: this.form.getRawValue().identificationTypeId,
				initValues: this.patientMedicalCoveragesToAdd
			}
		});
		dialogRef.afterClosed().subscribe(medicalCoverages => {
			if (medicalCoverages) {
				this.patientMedicalCoveragesToAdd = medicalCoverages.patientMedicalCoverages;
			}
		});

	}

	goBack(): void {
		this.formSubmitted = false;
		this.router.navigate([this.routePrefix + ROUTE_HOME]);
	}


	private lockFormField(params) {

		if (params.identificationNumber) {
			this.form.controls.identificationNumber.disable();
			this.identificationNumberDisabled = true;
		}
		if (params.identificationTypeId) {
			this.form.controls.identificationTypeId.disable();
			this.identificationTypeIdDisabled = true;
		}
		if (params.genderId) {
			this.form.controls.genderId.disable();
			this.genderIdDisabled = true;
		}
		if (params.firstName) {
			this.form.controls.firstName.disable();
			this.firstNameDisabled = true;
		}
		if (params.middleNames) {
			this.form.controls.middleNames.disable();
			this.middleNamesDisabled = true;
		}
		if (params.lastName) {
			this.form.controls.lastName.disable();
			this.lastNameDisabled = true;
		}
		if (params.otherLastNames) {
			this.form.controls.otherLastNames.disable();
			this.otherLastNamesDisabled = true;
		}
		if (params.birthDate) {
			this.form.controls.birthDate.disable();
			this.birthDateDisabled = true;
		}

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

	updatePhoneValidators(){
		if (this.form.controls.phoneNumber.value||this.form.controls.phonePrefix.value) {
			updateControlValidator(this.form, 'phoneNumber', [Validators.required]);
			updateControlValidator(this.form, 'phonePrefix', [Validators.required]);
		} else {
			updateControlValidator(this.form, 'phoneNumber', []);
			updateControlValidator(this.form, 'phonePrefix', []);
		}

	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}
