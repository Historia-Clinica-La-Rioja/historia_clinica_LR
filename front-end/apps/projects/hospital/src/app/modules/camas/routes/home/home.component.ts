import { Component, OnInit, OnDestroy } from '@angular/core';
import { tap } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { BedManagementFacadeService } from '@institucion/services/bed-management-facade.service';
import { INTERNMENT_SECTOR, SECTOR_GUARDIA } from '@historia-clinica/modules/guardia/constants/masterdata';

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
	internmentSector: number = INTERNMENT_SECTOR;
	emergencySector: number = SECTOR_GUARDIA;

	constructor(
		private bedManagementFacadeService: BedManagementFacadeService
  	) { }

	ngOnInit(): void {
		this.existsBedManagementList();
	}

	onSelectBed(bedId): void {
		this.selectedBed = bedId;
	}

	updateMapping(event) {
		this.updateMappingBed = event;
		delete this.selectedBed;
		this.existsBedManagementList();
	}

	ngOnDestroy(): void {
		this.managementBed$.unsubscribe();
  	}

	private existsBedManagementList() {
		this.managementBed$ = this.bedManagementFacadeService.getBedManagement([this.internmentSector, this.emergencySector]).pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0)
		).subscribe(data => this.existBedManagementList = data ? true : false);
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
		isBlocked: boolean;
	}[];
	sectorTypeDescription: string
}
