import { Component, OnDestroy, OnInit } from '@angular/core';
import { BedManagementFacadeService } from '@institucion/services/bed-management-facade.service';
import { SECTOR_GUARDIA } from '../../constants/masterdata';
import { Subscription, tap } from 'rxjs';
import { BedInfoDto } from '@api-rest/api-model';

@Component({
  selector: 'app-emergency-care-change-attention-place-beds',
  templateUrl: './emergency-care-change-attention-place-beds.component.html',
  styleUrls: ['./emergency-care-change-attention-place-beds.component.scss'],
  providers: [ BedManagementFacadeService ]
})
export class EmergencyCareChangeAttentionPlaceBedsComponent implements OnInit, OnDestroy {

	public selectedBed: number;
	public existBedManagementList = false;
	public bedsAmount: number;
	emergencyCareSector: number = SECTOR_GUARDIA;

	private managementBed$: Subscription;

	constructor(
		private bedManagementFacadeService: BedManagementFacadeService,
  	) {}

	ngOnInit(): void {
		this.bedManagementFacadeService.setInitialFilters({
			sector: null,
			service: null,
			probableDischargeDate: null,
			filled: false,
			hierarchicalUnits: null
		});
		this.managementBed$ = this.bedManagementFacadeService.getBedManagement([this.emergencyCareSector]).pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0)
		).subscribe(data => {
			this.existBedManagementList = data ? true : false;
		});
	}

	onSelectBed(bedId: number): void {
		this.selectedBed = bedId;
	}

	onAssignedBed(bedInfo: BedInfoDto): void {
		// this.dialogRef.close(bedInfo);
	}

	ngOnDestroy(): void {
		this.managementBed$.unsubscribe();
  	}

}
