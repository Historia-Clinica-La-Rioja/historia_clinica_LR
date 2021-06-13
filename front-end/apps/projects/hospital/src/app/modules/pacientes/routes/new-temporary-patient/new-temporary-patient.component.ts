import { Component, OnInit, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { APatientDto, BMPatientDto, EthnicityDto, PersonOccupationDto, EducationLevelDto, GenderDto, IdentificationTypeDto, PatientMedicalCoverageDto } from '@api-rest/api-model';
import { scrollIntoError, hasError, VALIDATIONS, DEFAULT_COUNTRY_ID } from '@core/utils/form.utils';
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
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PERSON } from '@core/constants/validation-constants';

const TEMPORARY_PATIENT = 3;
const ROUTE_HOME = 'pacientes';
const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
	selector: 'app-new-temporary-patient',
	templateUrl: './new-temporary-patient.component.html',
	styleUrls: ['./new-temporary-patient.component.scss']
})
export class NewTemporaryPatientComponent implements OnInit {

	readonly PERSON_MAX_LENGTH = PERSON.MAX_LENGTH;

	public form: FormGroup;
	public personResponse: BMPatientDto;
	public formSubmitted = false;
	public today: Moment = moment();
	public hasError = hasError;
	public genders: GenderDto[];
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
					phoneNumber: [],
					email: [null, [Validators.email]],
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

				this.lockFormField(params);
			});

		this.personMasterDataService.getGenders()
			.subscribe(
				genders => {
					this.genders = genders;
				}
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
	}

	save(): void {
		this.formSubmitted = true;
		if (this.form.valid) {
			const personRequest: APatientDto = this.mapToPersonRequest();
			this.patientService.addPatient(personRequest)
				.subscribe(patientId => {
					if (this.patientMedicalCoveragesToAdd) {
						const patientMedicalCoveragesDto: PatientMedicalCoverageDto[] =
							this.patientMedicalCoveragesToAdd.map(s => this.mapperService.toPatientMedicalCoverageDto(s));
						this.patientMedicalCoverageService.addPatientMedicalCoverages
							(patientId, patientMedicalCoveragesDto).subscribe();
					}
					this.router.navigate([this.routePrefix + ROUTE_PROFILE + patientId]);
					this.snackBarService.showSuccess('pacientes.new.messages.SUCCESS');
				}, _ => this.snackBarService.showError('pacientes.new.messages.ERROR'));
		} else {
			scrollIntoError(this.form, this.el);
		}
	}


	private mapToPersonRequest(): APatientDto {
		return {
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
		}
		if (params.identificationTypeId) {
			this.form.controls.identificationTypeId.disable();
		}
		if (params.genderId) {
			this.form.controls.genderId.disable();
		}
		if (params.firstName) {
			this.form.controls.firstName.disable();
		}
		if (params.middleNames) {
			this.form.controls.middleNames.disable();
		}
		if (params.lastName) {
			this.form.controls.lastName.disable();
		}
		if (params.otherLastNames) {
			this.form.controls.otherLastNames.disable();
		}
		if (params.birthDate) {
			this.form.controls.birthDate.disable();
		}

	}

}
