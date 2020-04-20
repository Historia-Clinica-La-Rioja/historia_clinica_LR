import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SearchPatientService } from '@api-rest/services/search-patient.service';
import { hasError } from "@core/utils/form.utils";
import { ActivatedRoute, Router } from '@angular/router';
import { PatientSearchDto } from '@api-rest/api-model';

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
	public identifyTypeArray = [
		{id: '1', description: 'DNI'},
		{id: '0', description: 'CUIL'}
	]
	public genderOptions = [
		{id: '1', description: 'Femenino'},
		{id: '2', description: 'Masculino'}
	];
	public viewSearch: boolean = true;
	public hasError = hasError;
	public identificationTypeId;
	public identificationNumber;
	public genderId;
	public matchingPatient: PatientSearchDto[];

	constructor(private formBuilder: FormBuilder,
				private searchPatientService: SearchPatientService,
				private router: Router,
				private route: ActivatedRoute
	) {
	}

	ngOnInit(): void {
		this.route.queryParams.subscribe(params => {
			this.identificationTypeId = params['identificationTypeId'];
			this.identificationNumber = params['identificationNumber'];
			this.genderId = params['genderId'];

			this.formSearch = this.formBuilder.group({
				identificationType: [this.identificationTypeId, Validators.required],
				identificationNumber: [this.identificationNumber, Validators.required],
				firstName: [null, Validators.required],
				middleNames: [null],
				lastName: [null, Validators.required],
				mothersLastName: [null],
				gender: [this.genderId, Validators.required],
				birthDate: [null, Validators.required]
			});
		});
	}

	back() {
		this.router.navigate([ROUTE_HOME])
	}

	search() {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
			let searchRequest = {
				searchFilterStr: {
					firstName: this.formSearch.controls.firstName.value,
					lastName: this.formSearch.controls.lastName.value,
					genderId: this.formSearch.controls.gender.value,
					identificationTypeId: this.formSearch.controls.identificationType.value,
					identificationNumber: this.formSearch.controls.identificationNumber.value,
				}
			}
			this.searchPatientService.getPatientByCMD(JSON.stringify(searchRequest)).subscribe(
				data => {
					if (!data.length) {
						this.router.navigate([ROUTE_NEW],
							{
								queryParams: {
									identificationTypeId: this.formSearch.controls.identificationType.value,
									identificationNumber: this.formSearch.controls.identificationNumber.value,
									genderId: this.formSearch.controls.gender.value,
									firstName: this.formSearch.controls.firstName.value,
									lastName: this.formSearch.controls.lastName.value,
								}
							});
					} else {
						// ocultar Search y ver Tabla de coincidencias parciales
						this.matchingPatient = data;
						this.viewSearch = false;
					}
				}
			);
		}
	}

	viewSearchComponent() {
		return this.viewSearch;
	}

}
