import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { hasError } from "@core/utils/form.utils";
import { ActivatedRoute, Router } from '@angular/router';
import { PatientSearchDto, GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { DatePipe } from '@angular/common';

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
	public identifyTypeArray: IdentificationTypeDto[];
	public genderOptions: GenderDto[];
	public viewSearch: boolean = true;
	public hasError = hasError;
	public identificationTypeId;
	public identificationNumber;
	public genderId;
	public matchingPatient: PatientSearchDto[];

	constructor(private formBuilder: FormBuilder,
				private patientService: PatientService,
				private router: Router,
				private route: ActivatedRoute,
				private personMasterDataService : PersonMasterDataService
	) {
	}

	ngOnInit(): void {
		this.route.queryParams.subscribe(params => {
			this.identificationTypeId = params['identificationTypeId'];
			this.identificationNumber = params['identificationNumber'];
			this.genderId = params['genderId'];

			this.formSearch = this.formBuilder.group({
				identificationNumber: [this.identificationNumber, Validators.required],
				identificationType: [Number(this.identificationTypeId), Validators.required],
				firstName: [null, Validators.required],
				middleNames: [null],
				lastName: [null, Validators.required],
				otherLastNames: [null],
				gender: [Number(this.genderId), Validators.required],
				birthDate: [null, Validators.required]
			});
			
			this.personMasterDataService.getIdentificationTypes().subscribe(
				identificationTypes => { this.identifyTypeArray = identificationTypes; });

			this.personMasterDataService.getGenders().subscribe(
				genders => { this.genderOptions = genders; });
		});
	}

	back() {
		this.router.navigate([ROUTE_HOME])
	}

	search() {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
			let datePipe = new DatePipe('en-US');
			let searchRequest = {
					firstName: this.formSearch.controls.firstName.value,
					lastName: this.formSearch.controls.lastName.value,
					genderId: this.formSearch.controls.gender.value,
					identificationTypeId: this.formSearch.controls.identificationType.value,
					identificationNumber: this.formSearch.controls.identificationNumber.value,
					birthDate: datePipe.transform(this.formSearch.controls.birthDate.value,'yyyy-MM-dd') 
			}
			this.patientService.getPatientByCMD(JSON.stringify(searchRequest)).subscribe(
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
									middleNames: this.formSearch.controls.middleNames.value,
									birthDate: this.formSearch.controls.birthDate.value,
									otherLastNames: this.formSearch.controls.otherLastNames.value,
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
