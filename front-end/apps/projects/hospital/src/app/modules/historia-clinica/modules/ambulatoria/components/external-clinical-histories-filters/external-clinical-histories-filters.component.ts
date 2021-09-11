import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-external-clinical-histories-filters',
	templateUrl: './external-clinical-histories-filters.component.html',
	styleUrls: ['./external-clinical-histories-filters.component.scss']
})
export class ExternalClinicalHistoriesFiltersComponent implements OnInit {

	public form: FormGroup;
	public specialties: string[] = [];
	public professionals: string[] = [];
	public institutions: string[] = [];


	constructor(private readonly formBuilder: FormBuilder) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			keyWord: [null],
			specialty: [null],
			professional: [null],
			institution: [null],
			consultationDate: [null]
		});
	}

	public sendAllFiltersOnFilterChange(): void {

	}

	clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

}
