import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { VALIDATIONS } from "@core/utils/form.utils";

const ROUTE_SEARCH = 'pacientes/search';
const ROUTE_ADD = 'pacientes/add';

@Component({
	selector: 'app-search-create',
	templateUrl: './search-create.component.html',
	styleUrls: ['./search-create.component.scss']
})
export class SearchCreateComponent implements OnInit {

	public formSearch: FormGroup;
	public formAdd: FormGroup;
	public genderOptions = ['masculino', 'femenino'];
	public noIdentity = false;
	public causeOptions = ['Alta de emergencia', 'Falta documento', 'Recien nacido', 'Otros'];

	constructor(private formBuilder: FormBuilder,
				private router: Router) {
	}

	ngOnInit(): void {
		this.formSearch = this.formBuilder.group({
			identifType: [],
			identifNumber: [null, Validators.maxLength(VALIDATIONS.MAX_LENGTH.dni)],
			gender: [],
		});

		this.formAdd = this.formBuilder.group({
			comments: [],
		});
	}

	search(): void {
		this.router.navigate([ROUTE_SEARCH]);
	}

	add(): void {
		this.router.navigate([ROUTE_ADD]);
	}

	noIdentityChange() {
		console.log(this.noIdentity);
		this.noIdentity = !this.noIdentity;
	}

}
