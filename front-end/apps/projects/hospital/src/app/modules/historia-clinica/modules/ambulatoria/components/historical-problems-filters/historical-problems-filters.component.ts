import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';
import { momentFormat, DateFormat, momentParseDate } from '@core/utils/moment.utils';
import { Subscription } from 'rxjs';
import { ClinicalSpecialtyDto } from '@api-rest/api-model';
import { HistoricalProblemsFacadeService, Professional, Problem } from '../../services/historical-problems-facade.service';
import { REFERENCE_STATES } from '../../constants/reference-masterdata';


@Component({
	selector: 'app-historical-problems-filters',
	templateUrl: './historical-problems-filters.component.html',
	styleUrls: ['./historical-problems-filters.component.scss']
})
export class HistoricalProblemsFiltersComponent implements OnInit, OnDestroy {

	public form: FormGroup;
	public specialties: ClinicalSpecialtyDto[] = [];
	public professionals: Professional[] = [];
	public problems: Problem[] = [];
	public referenceStates = [];

	private historicalProblemsFilter$: Subscription;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly historicalProblemsFacadeService: HistoricalProblemsFacadeService
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			specialty: [null],
			professional: [null],
			problem: [null],
			consultationDate: [null],
			referenceState: [null],
		});

		this.setFilterOptions();

		this.historicalProblemsFilter$ = this.historicalProblemsFacadeService.getHistoricalProblemsFilter().subscribe(
			data => {
				this.form.controls.specialty.setValue(data.specialty);
				this.form.controls.professional.setValue(data.professional);
				this.form.controls.problem.setValue(data.problem);
				this.form.controls.consultationDate.setValue(data.consultationDate ? momentParseDate(data.consultationDate) : null);
				this.form.controls.referenceState.setValue(data.referenceStateId);
			});
	}

	public sendAllFiltersOnFilterChange() {
		this.historicalProblemsFacadeService.sendHistoricalProblemsFilter(this.getHistoricalProblemsFilter());
	}

	private getHistoricalProblemsFilter(): HistoricalProblemsFilter {
		return {
			specialty: this.form.value.specialty,
			professional: this.form.value.professional,
			problem: this.form.value.problem,
			consultationDate: this.form.value.consultationDate ? momentFormat(this.form.value.consultationDate, DateFormat.API_DATE) : null,
			referenceStateId: this.form.value.referenceState,
		};
	}

	clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

	ngOnDestroy(): void {
		this.historicalProblemsFilter$.unsubscribe();
	}

	private setFilterOptions(): void{
		const filterOptions = this.historicalProblemsFacadeService.getFilterOptions();
		this.specialties = filterOptions.specialties;
		this.professionals = filterOptions.professionals;
		this.problems = filterOptions.problems;
		this.referenceStates = filterOptions.referenceStates;
	}

}

export class HistoricalProblemsFilter {
	specialty: number;
	professional: number;
	problem: string;
	consultationDate: string;
	referenceStateId: REFERENCE_STATES;
}
