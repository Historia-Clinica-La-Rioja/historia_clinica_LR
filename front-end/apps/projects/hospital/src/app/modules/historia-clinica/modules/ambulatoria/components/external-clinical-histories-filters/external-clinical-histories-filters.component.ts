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
	private filters: ExternalClinicalHistoryFilter = {};

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

		this.externalClinicalHistoryFacadeService.getFiltersOptions$().subscribe(
			(filtersOtions: ExternalClinicalHistoryFiltersOptions) => {
				this.specialties = filtersOtions.specialties;
				this.professionals = filtersOtions.professionals;
				this.institutions = filtersOtions.institutions;
			}
		);
	}

	public setSpecialtyFilter(): void {
		if (this.form.value.specialty)
			this.filters.specialty = this.form.value.specialty;
		else
			delete this.filters.specialty;
		this.externalClinicalHistoryFacadeService.setFilters(this.filters);
	}

	public setProfessionalFilter(): void {
		if (this.form.value.professional)
			this.filters.professional = this.form.value.professional;
		else
			delete this.filters.professional;
		this.externalClinicalHistoryFacadeService.setFilters(this.filters);
	}

	public setConsultationDateFilter(): void {
		if (this.form.value.consultationDate)
			this.filters.consultationDate = this.form.value.consultationDate;
		else
			delete this.filters.consultationDate;
		this.externalClinicalHistoryFacadeService.setFilters(this.filters);
	}

	public setKeyWordFilter(): void {
		if (this.form.value.keyWord)
			this.filters.keyWord = this.form.value.keyWord;
		else
			delete this.filters.keyWord;
		this.externalClinicalHistoryFacadeService.setFilters(this.filters);
	}

	public setInstitutionFilter(): void {
		if (this.form.value.institution)
			this.filters.institution = this.form.value.institution;
		else
			delete this.filters.institution;
		this.externalClinicalHistoryFacadeService.setFilters(this.filters);
	}

	public clear(control: AbstractControl, propertyName: string): void {
		control.reset();
		delete this.filters[propertyName];
		this.externalClinicalHistoryFacadeService.setFilters(this.filters);
	}

}
