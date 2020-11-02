import { Component, OnInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Moment } from 'moment';
import * as moment from 'moment';
import { APatientDto, BMPatientDto, GenderDto, IdentificationTypeDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { scrollIntoError, hasError, VALIDATIONS, DEFAULT_COUNTRY_ID } from "@core/utils/form.utils";
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from "@core/services/context.service";

const ROUTE_SEARCH = 'pacientes/search';
const ROUTE_PROFILE = 'pacientes/profile/';
const ROUTE_HOME_PATIENT = 'pacientes';

@Component({
	selector: 'app-new-patient',
	templateUrl: './new-patient.component.html',
	styleUrls: ['./new-patient.component.scss']
})
export class NewPatientComponent implements OnInit {

	public form: FormGroup;
	public personResponse: BMPatientDto;
	public formSubmitted: boolean = false;
	public today: Moment = moment();
	public hasError = hasError;
	public genders: GenderDto[];
	public countries: any[];
	public provinces: any[];
	public departments: any[];
	public cities: any[];
	public identificationTypeList: IdentificationTypeDto[];
	private readonly routePrefix;
	public patientType;
	public personPhoto : PersonPhotoDto;

	constructor(private formBuilder: FormBuilder,
		private router: Router,
		private el: ElementRef,
		private patientService: PatientService,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		private addressMasterDataService: AddressMasterDataService,
		private snackBarService: SnackBarService,
		private contextService: ContextService) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.route.queryParams
			.subscribe(params => {
				this.form = this.formBuilder.group({
					firstName: [params.firstName? params.firstName: null, [Validators.required]],
					middleNames: [params.middleNames? params.middleNames: null],
					lastName: [params.lastName? params.lastName: null, [Validators.required]],
					otherLastNames: [params.otherLastNames? params.otherLastNames: null],
					genderId: [Number(params.genderId), [Validators.required]],
					identificationNumber: [params.identificationNumber, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
					identificationTypeId: [Number(params.identificationTypeId), [Validators.required]],
					birthDate: [params.birthDate? moment(params.birthDate): null, [Validators.required]],

					//Person extended
					cuil: [null, [Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)]],
					mothersLastName: [],
					phoneNumber: [],
					email: [null, Validators.email],
					ethnic: [],
					religion: [],
					nameSelfDetermination: [],
					genderSelfDeterminationId: [],

					//Address
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
					//Patient
					medicalCoverageName: [null, Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageName)],
					medicalCoverageAffiliateNumber: [null, Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageAffiliateNumber)],

					//doctors
					generalPractitioner:[],
					generalPractitionerPhoneNumber:[],
					pamiDoctor:[],
					pamiDoctorPhoneNumber:[]
				});
				this.lockFormField(params);
				this.patientType = params.typeId;
				this.personPhoto = { imageData: params.photo? params.photo : null};
			});

		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});

		this.addressMasterDataService.getAllCountries()
			.subscribe(countries => {
				this.countries = countries;
				this.form.controls.addressCountryId.setValue(DEFAULT_COUNTRY_ID);
				this.setProvinces();
			});

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
			this.form.controls.middleNames.disable();
		}
		if (params.lastName) {
			this.form.controls.lastName.disable();
			this.form.controls.otherLastNames.disable();
		}
		if (params.birthDate) {
			this.form.controls.birthDate.disable();
		}
	}

	save(): void {
		this.formSubmitted = true;
		if (this.form.valid) {
			let personRequest: APatientDto = this.mapToPersonRequest();
			this.patientService.addPatient(personRequest)
				.subscribe(patientId => {
					if (this.personPhoto != null)
						this.patientService.addPatientPhoto(patientId, this.personPhoto).subscribe();
					this.router.navigate([this.routePrefix + ROUTE_PROFILE + patientId]);
					this.snackBarService.showSuccess('pacientes.new.messages.SUCCESS');
				}, _ => this.snackBarService.showError('pacientes.new.messages.ERROR'));
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	private mapToPersonRequest(): APatientDto {
		return {
			birthDate: this.form.controls.birthDate.value.toDate(),
			firstName: this.form.controls.firstName.value,
			genderId: this.form.controls.genderId.value,
			identificationTypeId: this.form.controls.identificationTypeId.value,
			identificationNumber: this.form.controls.identificationNumber.value,
			lastName: this.form.controls.lastName.value,
			middleNames: this.form.controls.middleNames.value && this.form.controls.middleNames.value.length ? this.form.controls.middleNames.value : null,
			otherLastNames: this.form.controls.otherLastNames.value && this.form.controls.otherLastNames.value.length ? this.form.controls.otherLastNames.value : null,
			//Person extended
			cuil: this.form.controls.cuil.value,
			email: this.form.controls.email.value,
			ethnic: this.form.controls.ethnic.value,
			genderSelfDeterminationId: this.form.controls.genderSelfDeterminationId.value,
			mothersLastName: this.form.controls.mothersLastName.value,
			nameSelfDetermination: this.form.controls.nameSelfDetermination.value,
			phoneNumber: this.form.controls.phoneNumber.value,
			religion: this.form.controls.religion.value,
			//Address
			apartment: this.form.controls.addressApartment.value,
			cityId: this.form.controls.addressCityId.value,
			floor: this.form.controls.addressFloor.value,
			number: this.form.controls.addressNumber.value,
			postcode: this.form.controls.addressPostcode.value,
			quarter: this.form.controls.addressQuarter.value,
			street: this.form.controls.addressStreet.value,
			//Patient
			typeId: this.patientType,
			comments: null,
			identityVerificationStatusId: null,
			medicalCoverageName: this.form.controls.medicalCoverageName.value,
			medicalCoverageAffiliateNumber: this.form.controls.medicalCoverageAffiliateNumber.value,
			//doctors
			generalPractitioner: {
				fullName: this.form.controls.generalPractitioner.value,
				phoneNumber: this.form.controls.generalPractitionerPhoneNumber.value,
				generalPractitioner: true
			},
			pamiDoctor: {
				fullName:this.form.controls.pamiDoctor.value,
				phoneNumber: this.form.controls.pamiDoctorPhoneNumber.value,
				generalPractitioner: false

			}
		};

	}

	setProvinces() {
		let countryId: number = this.form.controls.addressCountryId.value;
		this.addressMasterDataService.getByCountry(countryId)
			.subscribe(provinces => {
				this.provinces = provinces
			});
	}

	setDepartments() {
		let provinceId: number = this.form.controls.addressProvinceId.value;
		this.addressMasterDataService.getDepartmentsByProvince(provinceId)
			.subscribe(departments => {
				this.departments = departments
			});
		this.form.controls.addressDepartmentId.enable();
	}

	setCities() {
		let departmentId: number = this.form.controls.addressDepartmentId.value;
		this.addressMasterDataService.getCitiesByDepartment(departmentId)
			.subscribe(cities => {
				this.cities = cities;
			});
		this.form.controls.addressCityId.enable();
	}

	goBack(): void {
		this.formSubmitted = false;
		this.router.navigate([this.routePrefix + ROUTE_HOME_PATIENT]);
	}
}
