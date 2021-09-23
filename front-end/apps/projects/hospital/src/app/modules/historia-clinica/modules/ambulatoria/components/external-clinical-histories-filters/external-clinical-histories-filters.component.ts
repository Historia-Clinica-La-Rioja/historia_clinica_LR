import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { ExternalClinicalHistoryFacadeService } from '../../services/external-clinical-history-facade.service';

export interface ExternalClinicalHistoryFiltersOptions {
	specialties: string[],
	professionals: string[],
	institutions: string[]
}

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

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly externalClinicalHistoryService: ExternalClinicalHistoryFacadeService
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			keyWord: [null],
			specialty: [null],
			professional: [null],
			institution: [null],
			consultationDate: [null]
		});

		this.externalClinicalHistoryService.getFiltersOptions().subscribe(
			(filtersOtions: ExternalClinicalHistoryFiltersOptions) => {
				this.specialties = filtersOtions.specialties;
				this.professionals = filtersOtions.professionals;
				this.institutions = filtersOtions.institutions;
			}
		);
	}

	public sendAllFiltersOnFilterChange(): void {

	}

	public clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

}
