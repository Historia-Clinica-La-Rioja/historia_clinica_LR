import { Component, OnInit, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { APatientDto, BMPatientDto, GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { scrollIntoError, hasError, VALIDATIONS, DEFAULT_COUNTRY_ID } from "@core/utils/form.utils";
import { Router, ActivatedRoute } from '@angular/router';
import { PatientService } from '@api-rest/services/patient.service';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { Moment } from 'moment';
import * as moment from 'moment';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const TEMPORARY_PATIENT = 3;
@Component({
  selector: 'app-new-temporary-patient',
  templateUrl: './new-temporary-patient.component.html',
  styleUrls: ['./new-temporary-patient.component.scss']
})
export class NewTemporaryPatientComponent implements OnInit {

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
  private identityVerificationStatus;
  private comments;
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private el: ElementRef,
	private patientService: PatientService,
	private personMasterDataService: PersonMasterDataService,
	private addressMasterDataService: AddressMasterDataService,
	private route: ActivatedRoute,
	private snackBarService: SnackBarService
  ) { }

  ngOnInit(): void {

	this.route.queryParams
       	.subscribe(params => {
			this.identityVerificationStatus = Number(params.IdentityVerificationStatus);
			this.comments = params.comments;

			this.form = this.formBuilder.group({
				firstName:[],
				middleNames: [],
				lastName: [],
				otherLastNames: [],
				identificationTypeId:[Number(params.identificationTypeId)],
				identificationNumber: [params.identificationNumber,Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)],
				genderId: [Number(params.genderId)],
				birthDate: [],

				//Person extended
				cuil: [null,Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)],
				mothersLastName: [],
				phoneNumber: [],
				email: [null, [Validators.email]],
				ethnic: [],
				religion: [],
				nameSelfDetermination: [],
				genderSelfDeterminationId: [],
				//Address
				addressStreet: [],
				addressNumber: [],
				addressFloor: [],
				addressApartment: [],
				addressQuarter:[],
				addressCityId:  { value: null, disabled: true },
				addressPostcode: [],

				addressProvinceId: [],
				addressCountryId: [],
				addressDepartmentId: { value: null, disabled: true },
				//Patient
				medicalCoverageName:[null,Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageName)],
				medicalCoverageAffiliateNumber:[null,Validators.maxLength(VALIDATIONS.MAX_LENGTH.medicalCoverageAffiliateNumber)]
			  });
		});

	this.personMasterDataService.getGenders()
		.subscribe(
        	genders => { this.genders = genders;}
    	);

    this.personMasterDataService.getIdentificationTypes().subscribe(
      identificationTypes => { this.identificationTypeList = identificationTypes;}
	);

	this.addressMasterDataService.getAllCountries()
			.subscribe(countries => {
				this.countries = countries;
				this.form.controls.addressCountryId.setValue(DEFAULT_COUNTRY_ID);
				this.setProvinces();
			});
  }

  save():void{
	this.formSubmitted = true;
	if (this.form.valid) {
		let personRequest: APatientDto = this.mapToPersonRequest();
      	this.patientService.addPatient(personRequest)
        	.subscribe(patient => {
				  this.router.navigate(['pacientes/profile/' + patient.id]);
				  this.snackBarService.showSuccess('pacientes.new.messages.SUCCESS');
        		}, _ => this.snackBarService.showError('pacientes.new.messages.ERROR'));
	} else {
		scrollIntoError(this.form,this.el);
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
      typeId: TEMPORARY_PATIENT,
      comments: this.comments,
	  identityVerificationStatusId: this.identityVerificationStatus,
	  medicalCoverageName: this.form.controls.medicalCoverageName.value,
	  medicalCoverageAffiliateNumber: this.form.controls.medicalCoverageAffiliateNumber.value,
    };
  }

  setProvinces() {
	let idCountry: number = this.form.controls.addressCountryId.value;
	this.addressMasterDataService.getByCountry(idCountry)
		.subscribe(provinces => {
			this.provinces = provinces
		});
}

setDepartments() {
	let idProvince: number = this.form.controls.addressProvinceId.value;
	this.addressMasterDataService.getDepartmentsByProvince(idProvince)
		.subscribe(departments => {
			this.departments = departments
		});
	this.form.controls.addressDepartmentId.enable();
}

setCities() {
	let idDepartment: number = this.form.controls.addressDepartmentId.value;
	this.addressMasterDataService.getCitiesByDepartment(idDepartment)
		.subscribe(cities => {
			this.cities = cities;
		});
	this.form.controls.addressCityId.enable();
}

  goBack(): void {
    this.formSubmitted = false;
		this.router.navigate(['/pacientes']);
  }


}
