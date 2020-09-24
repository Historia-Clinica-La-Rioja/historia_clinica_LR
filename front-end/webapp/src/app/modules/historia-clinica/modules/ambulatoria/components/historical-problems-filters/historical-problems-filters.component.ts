import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';
import { momentFormat, DateFormat, momentParseDate } from '@core/utils/moment.utils';
import { Subscription } from 'rxjs';
import { HistoricalProblemsFacadeService, Speciality, Professional, Problem } from '../../services/historical-problems-facade.service';

@Component({
  selector: 'app-historical-problems-filters',
  templateUrl: './historical-problems-filters.component.html',
  styleUrls: ['./historical-problems-filters.component.scss']
})
export class HistoricalProblemsFiltersComponent implements OnInit, OnDestroy {

  	public form: FormGroup;
	public specialities: Speciality[] = [];
	public professionals: Professional[] = [];
	public problems: Problem[] = [];

	private historicalProblemsFilter$: Subscription;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly historicalProblemsFacadeService: HistoricalProblemsFacadeService
  	) {	}

  	ngOnInit(): void {
		this.form = this.formBuilder.group({
			speciality: [null],
			professional: [null],
			problem: [null],
			consultationDate: [null]
		});

		const filterOptions = this.historicalProblemsFacadeService.getFilterOptions();
		this.specialities = filterOptions.specialities;
		this.professionals = filterOptions.professionals;
		this.problems = filterOptions.problems;

		this.historicalProblemsFilter$ = this.historicalProblemsFacadeService.getHistoricalProblemsFilter().subscribe(
			data => {
				this.form.controls.speciality.setValue(data.speciality);
				this.form.controls.professional.setValue(data.professional);
				this.form.controls.problem.setValue(data.problem);
				this.form.controls.consultationDate.setValue(data.consultationDate ? momentParseDate(data.consultationDate) : null);
			});
  	}

	public sendAllFiltersOnFilterChange() {
		this.historicalProblemsFacadeService.sendHistoricalProblemsFilter(this.getHistoricalProblemsFilter());
	}

	private getHistoricalProblemsFilter(): HistoricalProblemsFilter {
		return {
			speciality: this.form.value.speciality,
			professional: this.form.value.professional,
			problem: this.form.value.problem,
			consultationDate: this.form.value.consultationDate ? momentFormat(this.form.value.consultationDate, DateFormat.API_DATE) : null,
		};
	}

	clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

	ngOnDestroy(): void {
		this.historicalProblemsFilter$.unsubscribe();
  	}

}

export class HistoricalProblemsFilter {
	speciality: number;
	professional: number;
	problem: string;
	consultationDate: string;
}
