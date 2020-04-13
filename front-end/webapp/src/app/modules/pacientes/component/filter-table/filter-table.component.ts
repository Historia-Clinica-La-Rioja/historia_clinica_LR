import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { VALIDATIONS } from "@core/utils";

@Component({
	selector: 'app-filter-table',
	templateUrl: './filter-table.component.html',
	styleUrls: ['./filter-table.component.scss']
})
export class FilterTableComponent implements OnInit {

	public formFilterTable: FormGroup;

	constructor(private formBuilder: FormBuilder) {
	}

	ngOnInit(): void {
		this.formFilterTable = this.formBuilder.group({
			id_patient: [],
			identifType: [],
			identifNumber: [null, Validators.maxLength(VALIDATIONS.MAX_LENGTH.dni)],
			name: [],
			last_name: [],
			birth_date: [],
			gender: [],
		});
	}

	filterTable(): void {

	}

}
