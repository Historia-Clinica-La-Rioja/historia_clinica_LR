import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SearchPatientService } from '@api-rest/services/search-patient.service';
import { VALIDATIONS, hasError } from "@core/utils/form.utils";
import { Router } from '@angular/router';

const ROUTE_NEW = 'pacientes/new';
const ROUTE_HOME = 'pacientes';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

	public formSearchSubmitted: boolean = false;
	public formSearch: FormGroup;
	public genderOptions = [
		{id: '1', description: 'femenino'},
		{id: '2', description: 'masculino'}
	];
	public viewSearch: boolean = true;
	public hasError = hasError;

  constructor(private formBuilder: FormBuilder,
			  private searchPatientService: SearchPatientService,
			  private router: Router,
			  ) { }

	ngOnInit(): void {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [null, Validators.required],
			identificationTypeId: [null, Validators.required],
			firstName: [null, Validators.required],
			middleNames: [null],
			lastName: [null, Validators.required],
			mothersLastName: [null],
			gender: [null, Validators.required],
			birthDate: [null, Validators.required]
		 });
	}

	back(){
		this.router.navigate([ROUTE_HOME])
	}

	serch() {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
//			let searchRequest: String;
/* 			searchRequest["firstName"] =this.formSearch.controls.firstName.value;
			searchRequest["lastName"] =this.formSearch.controls.lastName.value;
			searchRequest["genderId"] =this.formSearch.controls.gender.value;
			searchRequest["identificationTypeId"] =this.formSearch.controls.identificationNumber.value;
			searchRequest["identificationNumber"] =this.formSearch.controls.identificationTypeId.value; */
			
			let searchRequest = {
				searchFilterStr: {
					lastName: this.formSearch.controls.firstName.value,
					firsName: this.formSearch.controls.lastName.value,
					genderId: this.formSearch.controls.gender.value,
					identificationTypeId: this.formSearch.controls.identificationNumber.value,
					identificationNumber: this.formSearch.controls.identificationTypeId.value,
				}
			}
			this.searchPatientService.getPatientByCMD(JSON.stringify(searchRequest)).subscribe(
				data => {
					if(!data.length){
						this.router.navigate([ROUTE_NEW]);
					}
					else {
						// ocultar Search y ver Tabla de coincidencias parciales
						this.viewSearch = false;
					}
				}
			);
		}
	}

	viewSearchComponent(){
		return this.viewSearch;
	}

}
