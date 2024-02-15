import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, AbstractControl } from '@angular/forms';
import { BedManagementFacadeService, Sector, Service } from '../../services/bed-management-facade.service';
import { momentFormat, DateFormat, momentParse } from '@core/utils/moment.utils';
import { Observable, Subscription } from 'rxjs';
import { SECTOR_GUARDIA } from '@historia-clinica/modules/guardia/constants/masterdata';
import { HierarchicalUnitsService } from '@api-rest/services/hierarchical-units.service';
import { HierarchicalUnitDto } from '@api-rest/api-model';

@Component({
  selector: 'app-bed-filters',
  templateUrl: './bed-filters.component.html',
  styleUrls: ['./bed-filters.component.scss']
})
export class BedFiltersComponent implements OnInit, OnDestroy {

	public form: UntypedFormGroup;
	public sectors: Sector[] = [];
	public services: Service[] = [];

	private bedManagementFilter$: Subscription;

	@Input() sectorTypeId?: number;
	isEmergencyEpisode: boolean = false;
	hierarchicalUnits$: Observable<HierarchicalUnitDto[]>;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly bedManagementFacadeService: BedManagementFacadeService,
		private readonly hierarchicalUnitsService: HierarchicalUnitsService
  	) { }
	
	ngOnInit(): void {
		this.isEmergencyEpisode = this.sectorTypeId === SECTOR_GUARDIA;
		this.form = this.formBuilder.group({
			sector: [null],
			service: [null],
			probableDischargeDate: [null],
			filled: [true],
			hierarchicalUnits: [null]
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
		this.setHierarchicalUnits();
  	}

	public sendAllFiltersOnFilterChange() {
		this.bedManagementFacadeService.sendBedManagementFilter(this.getBedManagementFilter());
	}

	get hierarchicalUnits() {
		return this.form.get('hierarchicalUnits');
	}

	private getBedManagementFilter(): BedManagementFilter {
		return {
			sector: this.form.value.sector,
			service: this.form.value.service,
			probableDischargeDate: this.form.value.probableDischargeDate ?
				momentFormat(this.form.value.probableDischargeDate, DateFormat.API_DATE) : null,
			filled: this.form.value.filled,
			hierarchicalUnits: this.form.value.hierarchicalUnits?.map(hu => hu.id)
		};
	}

	clear(control: AbstractControl): void {
		control.reset();
		this.sendAllFiltersOnFilterChange();
	}

	ngOnDestroy(): void {
		this.bedManagementFilter$.unsubscribe();
  	}

	private setHierarchicalUnits() {
		this.hierarchicalUnits$ = this.hierarchicalUnitsService.getByInstitution();
	}

}

export class BedManagementFilter {
	sector: number;
	service: number;
	probableDischargeDate: string;
	filled: boolean;
	hierarchicalUnits: number[]
}
