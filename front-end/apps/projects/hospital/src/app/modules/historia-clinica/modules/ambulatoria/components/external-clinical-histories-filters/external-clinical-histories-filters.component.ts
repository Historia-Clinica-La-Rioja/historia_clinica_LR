import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { Moment } from 'moment';
import { ExternalClinicalHistoryFacadeService } from '../../services/external-clinical-history-facade.service';

export interface ExternalClinicalHistoryFiltersOptions {
	specialties: string[],
	professionals: string[],
	institutions: string[]
}
export interface ExternalClinicalHistoryFilter {
	keyWord?: string,
	specialty?: string,
	professional?: string,
	institution?: string,
	consultationDate?: Moment
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
		private readonly externalClinicalHistoryFacadeService: ExternalClinicalHistoryFacadeService
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			keyWord: [null],
			specialty: [null],
			professional: [null],
			institution: [null],
			consultationDate: [null]
		});

		this.externalClinicalHistoryFacadeService.getFiltersOptions().subscribe(
			(filtersOtions: ExternalClinicalHistoryFiltersOptions) => {
				this.specialties = filtersOtions.specialties;
				this.professionals = filtersOtions.professionals;
				this.institutions = filtersOtions.institutions;
			}
		);
	}

	public sendAllFilters(): void {
		const filters: ExternalClinicalHistoryFilter = {};
		if (this.form.controls.consultationDate.value)
			filters.consultationDate = this.form.controls.consultationDate.value;
		if (this.form.controls.institution.value)
			filters.institution = this.form.controls.institution.value;
		if (this.form.controls.keyWord.value)
			filters.keyWord = this.form.controls.keyWord.value;
		if (this.form.controls.professional.value)
			filters.professional = this.form.controls.professional.value;
		if (this.form.controls.specialty.value)
			filters.specialty = this.form.controls.specialty.value;

		this.externalClinicalHistoryFacadeService.setFilters(filters);
	}

	public clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFilters();
	}

}
