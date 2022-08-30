import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { BedManagementFacadeService, Sector, Service } from '../../services/bed-management-facade.service';
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
	public services: Service[] = [];

	private bedManagementFilter$: Subscription;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly bedManagementFacadeService: BedManagementFacadeService
  	) {	}

  	ngOnInit(): void {
		this.form = this.formBuilder.group({
			sector: [null],
			service: [null],
			probableDischargeDate: [null],
			filled: [true]
		});

		this.bedManagementFacadeService.getBedSummary().subscribe( data => {
			const filterOptions = this.bedManagementFacadeService.getFilterOptions();
			this.sectors = filterOptions.sectors;
			this.services = filterOptions.services;
		})

		this.bedManagementFilter$ = this.bedManagementFacadeService.getBedManagementFilter().subscribe(
			data => {
				this.form.controls.sector.setValue(data.sector);
				this.form.controls.service.setValue(data.service);
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
			service: this.form.value.service,
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
	service: number;
	probableDischargeDate: string;
	filled: boolean;
}
