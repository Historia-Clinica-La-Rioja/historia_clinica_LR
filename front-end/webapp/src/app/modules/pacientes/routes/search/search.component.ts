import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatosPersonales } from '../../pacientes.module';
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

	public datosPersonales: DatosPersonales = new DatosPersonales();
	public formSearchSubmitted: boolean = false;
	public formSearch: FormGroup;
	public genderOptions = ['masculino', 'femenino'];
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
			this.searchPatientService.getPatientByCMD(this.datosPersonales).subscribe(
				data => {
					if (data.length == 0) {
						// ToDo ir a nuevo paciente
						this.router.navigate([ROUTE_NEW])
					}else{
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
