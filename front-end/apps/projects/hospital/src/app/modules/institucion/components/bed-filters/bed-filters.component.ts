import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { BedManagementFacadeService, Sector, Speciality, Category } from '../../services/bed-management-facade.service';
import { momentFormat, DateFormat, momentParse } from '@core/utils/moment.utils';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-bed-filters',
  templateUrl: './bed-filters.component.html',
  styleUrls: ['./bed-filters.component.scss']
})
export class BedFiltersComponent implements OnInit, OnDestroy {

	public form: FormGroup;
	public sectors: Sector[] = [];
	public specialities: Speciality[] = [];
	public categories: Category[] = [];

	private bedManagementFilter$: Subscription;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly bedManagementFacadeService: BedManagementFacadeService
  	) {	}

  	ngOnInit(): void {
		this.form = this.formBuilder.group({
			sector: [null],
			speciality: [null],
			category: [null],
			probableDischargeDate: [null],
			filled: [true]
		});

		this.bedManagementFacadeService.getBedSummary().subscribe( data => {
			const filterOptions = this.bedManagementFacadeService.getFilterOptions();
			this.sectors = filterOptions.sectors;
			this.specialities = filterOptions.specialities;
			this.categories = filterOptions.categories;
		})

		this.bedManagementFilter$ = this.bedManagementFacadeService.getBedManagementFilter().subscribe(
			data => {
				this.form.controls.sector.setValue(data.sector);
				this.form.controls.speciality.setValue(data.speciality);
				this.form.controls.category.setValue(data.category);
				this.form.controls.probableDischargeDate
					.setValue(data.probableDischargeDate ?
						momentParse(data.probableDischargeDate, DateFormat.API_DATE) : null);
				this.form.controls.filled.setValue(data.filled);
			});

  	}

	public sendAllFiltersOnFilterChange() {
		this.bedManagementFacadeService.sendBedManagementFilter(this.getBedManagementFilter());
	}

	private getBedManagementFilter(): BedManagementFilter {
		return {
			sector: this.form.value.sector,
			speciality: this.form.value.speciality,
			category: this.form.value.category,
			probableDischargeDate: this.form.value.probableDischargeDate ?
				momentFormat(this.form.value.probableDischargeDate, DateFormat.API_DATE) : null,
			filled: this.form.value.filled
		};
	}

	clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

	ngOnDestroy(): void {
		this.bedManagementFilter$.unsubscribe();
  	}

}

export class BedManagementFilter {
	sector: number;
	speciality: number;
	category: number;
	probableDischargeDate: string;
	filled: boolean;
}
