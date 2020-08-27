import { Component, OnInit, Input, SimpleChanges, OnChanges } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { BedManagementService, Sector, Speciality, Category } from '../../services/bed-management.service';
import { momentFormat, DateFormat } from '@core/utils/moment.utils';

@Component({
  selector: 'app-bed-filters',
  templateUrl: './bed-filters.component.html',
  styleUrls: ['./bed-filters.component.scss']
})
export class BedFiltersComponent implements OnInit, OnChanges {

  	@Input() filled?: boolean;

	public form: FormGroup;
	public sectors: Sector[] = [];
	public specialities: Speciality[] = [];
	public categories: Category[] = [];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly bedManagementService: BedManagementService
  	) {	}

  	ngOnInit(): void {
		this.form = this.formBuilder.group({
			sector: [null],
			speciality: [null],
			category: [null],
			probableDischargeDate: [null],
			filled: [true]
		});

		const filterOptions = this.bedManagementService.getFilterOptions();
		this.sectors = filterOptions.sectors;
		this.specialities = filterOptions.specialities;
		this.categories = filterOptions.categories;
  	}

	ngOnChanges(changes: SimpleChanges) {
		if (!changes.filled.firstChange && !changes.filled.currentValue) {
			this.form.controls.filled.setValue(changes.filled.currentValue);
			this.sendAllFiltersOnFilterChange();
		}
	}

	public sendAllFiltersOnFilterChange() {
		this.bedManagementService.sendBedManagementFilter(this.getBedManagementFilter());
	}

	private getBedManagementFilter(): BedManagementFilter {
		return {
			sector: this.form.value.sector,
			speciality: this.form.value.speciality,
			category: this.form.value.category,
			probableDischargeDate: this.form.value.probableDischargeDate ? momentFormat(this.form.value.probableDischargeDate, DateFormat.API_DATE) : null,
			filled: this.form.value.filled
		};
	}

	clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

}

export class BedManagementFilter {
	sector: number;
	speciality: number;
	category: number;
	probableDischargeDate: string;
	filled: boolean;
}
