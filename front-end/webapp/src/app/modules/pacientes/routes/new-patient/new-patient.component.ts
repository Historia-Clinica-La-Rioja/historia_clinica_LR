import { Component, OnInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormArray, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { APatientDto, BMPatientDto, GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { scrollIntoError, hasError, VALIDATIONS } from "@core/utils/form.utils";
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';

const VALID_PATIENT = 2;
@Component({
	selector: 'app-new-patient',
	templateUrl: './new-patient.component.html',
	styleUrls: ['./new-patient.component.scss']
})
export class NewPatientComponent implements OnInit {

	public form: FormGroup;
	public personResponse: BMPatientDto;
	public formSubmitted: boolean = false; 
	public todaysDate: Date = new Date();
	public hasError = hasError;
	// CAMBIAR LOS TIPOS POR LOS DTOS CORRESPONDIENTES
	public genders:GenderDto[];
	public countries:any[];
	public provinces:any[];
	public departments:any[];
	public cities:any[];
	public identificationTypeList:IdentificationTypeDto[];
	constructor(
		private formBuilder: FormBuilder,
		private router: Router,
		private el: ElementRef,
		private patientService: PatientService,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		private addressMasterDataService: AddressMasterDataService
	) { }

	ngOnInit(): void {
		
		this.route.queryParams
			.subscribe(params => {
				this.form = this.formBuilder.group({
					firstName: [params.firstName, [Validators.required]],
					middleNames: [params.middleNames],
					lastName: [params.lastName, [Validators.required]],
					otherLastNames: [params.otherLastNames],
					genderId: [Number(params.genderId), [Validators.required]],
					identificationNumber: [params.identificationNumber, [Validators.required,Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
					identificationTypeId: [Number(params.identificationTypeId), [Validators.required]],
					birthDate: [new Date(params.birthDate), [Validators.required]],
					
					//Person extended
					cuil: [null, [Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)]],
					mothersLastName: [],
					phoneNumber: [], 
					email: [null,Validators.email],
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
					addressCityId:  [],
					addressPostcode: [],

					addressProvinceId: [],
					addressCountryId: [],
					addressDepartmentId:[]
				});

				this.personMasterDataService.getGenders()
					.subscribe( genders => { this.genders = genders; });
						
				this.personMasterDataService.getIdentificationTypes()
					.subscribe( identificationTypes => { this.identificationTypeList = identificationTypes; });
			});

		this.addressMasterDataService.getAllCountries()
			.subscribe(countries => {this.countries = countries});

	}

	save():void{
		this.formSubmitted = true;
		if (this.form.valid) {
			let personRequest: APatientDto = this.mapToPersonRequest();
			this.patientService.addPatient(personRequest)
				.subscribe(person => {
					this.personResponse = person;
					// Ir a la proxima pantalla
				});
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
			middleNames: this.form.controls.middleNames.value && this.form.controls.middleNames.value.length ? this.form.controls.middleNames.value : null,
			otherLastNames: this.form.controls.otherLastNames.value && this.form.controls.otherLastNames.value.length ? this.form.controls.otherLastNames.value : null,
			//Person extended
			cuil: this.form.controls.cuil.value,
			email:this.form.controls.email.value,
			ethnic: this.form.controls.ethnic.value,
			genderSelfDeterminationId:this.form.controls.genderSelfDeterminationId.value,
			mothersLastName:this.form.controls.mothersLastName.value,
			nameSelfDetermination: this.form.controls.nameSelfDetermination.value,
			phoneNumber: this.form.controls.phoneNumber.value,
			religion: this.form.controls.religion.value,
			//Address
			apartment:this.form.controls.addressApartment.value,
			cityId:this.form.controls.addressCityId.value,
			floor:this.form.controls.addressFloor.value,
			number:this.form.controls.addressNumber.value,
			postcode:this.form.controls.addressPostcode.value,
			quarter:this.form.controls.addressQuarter.value,
			street:this.form.controls.addressStreet.value,
			//Patient
			typeId: VALID_PATIENT,
			comments: null,
			identityVerificationStatusId: null
		};
		 
	}

	changeStatus(nextControl:string): void {
		this.form.controls[nextControl].enable();
	}

	setProvinces(control: string){
		let idCountry:number = this.form.get(control).value;
		this.addressMasterDataService.getByCountry(idCountry)
			.subscribe(provinces => {this.provinces = provinces});		
	}

	setDepartments(control: string){
		let idProvince:number = this.form.get(control).value;
		this.addressMasterDataService.getDepartmentsByProvince(idProvince)
			.subscribe(departments => {this.departments = departments});	
	}

	setCities(control: string){
		let idDepartment:number = this.form.get(control).value;
		this.addressMasterDataService.getCitiesByDepartment(idDepartment)
			.subscribe( cities => { this.cities = cities;});
	}

	goBack(): void {
		this.formSubmitted = false;
		this.router.navigate(['/pacientes']);
	}
}