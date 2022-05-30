import { Component, OnInit, OnDestroy } from '@angular/core';
import { tap } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { BedManagementFacadeService } from '@institucion/services/bed-management-facade.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss'],
	providers: [ BedManagementFacadeService ]
})
export class HomeComponent implements OnInit, OnDestroy {

	public selectedBed: number;
	public bedsAmount: number;
	public existBedManagementList = false;
	updateMappingBed = false;

	private managementBed$: Subscription;

	constructor(
		private bedManagementFacadeService: BedManagementFacadeService
  	) { }

	ngOnInit(): void {
		this.managementBed$ = this.bedManagementFacadeService.getBedManagement().pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0)
		).subscribe(data => this.existBedManagementList = data ? true : false);
	}

	onSelectBed(bedId): void {
		this.selectedBed = bedId;
	}

	updateMapping(event) {
		this.updateMappingBed = event;
		delete this.selectedBed;
	}

	ngOnDestroy(): void {
		this.managementBed$.unsubscribe();
  	}

}

export class BedManagement {
	sectorId: number;
	sectorDescription: string;
	careType: string;
	organizationType: string;
	ageGroup: string;
  	specialties:
	{
		specialtyId: number;
		specialtyName: string;
  	}[];
	beds:
	{
		bedId: number;
		bedNumber: string;
		free: boolean;
	}[];
}
