import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { BedManagementFacadeService } from '@institucion/services/bed-management-facade.service';
import { SECTOR_GUARDIA } from '../../constants/masterdata';
import { Subscription, tap } from 'rxjs';
import { BedInfoDto, EmergencyCareBedDto } from '@api-rest/api-model';

@Component({
  selector: 'app-emergency-care-change-attention-place-beds',
  templateUrl: './emergency-care-change-attention-place-beds.component.html',
  styleUrls: ['./emergency-care-change-attention-place-beds.component.scss'],
  providers: [ BedManagementFacadeService ]
})
export class EmergencyCareChangeAttentionPlaceBedsComponent implements OnInit, OnDestroy {

	@Input() sectorId: number;
	@Output() selectedBedInfo: EventEmitter<EmergencyCareBedDto> = new EventEmitter<EmergencyCareBedDto>();
	public selectedBed: number;
	public existBedManagementList = false;
	public bedsAmount: number;
	emergencyCareSector: number = SECTOR_GUARDIA;

	private managementBed$: Subscription;

	constructor(
		private bedManagementFacadeService: BedManagementFacadeService,
  	) {}

	ngOnInit(){
		this.bedManagementFacadeService.setInitialFilters({
			sector: this.sectorId,
			service: null,
			probableDischargeDate: null,
			filled: true,
			hierarchicalUnits: null
		});
		this.managementBed$ = this.bedManagementFacadeService.getBedManagement([this.emergencyCareSector]).pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0)
		).subscribe(data => {
			this.existBedManagementList = data ? true : false;
		});
	}

	onSelectBed(bedId: number) {
		this.selectedBed = bedId;
	}

	onAssignedBed(bedInfo: BedInfoDto) {
		const selectedBedDto: EmergencyCareBedDto = {
			id: bedInfo.bed.id,
			sectorDescription: bedInfo.bed.room.sector.description,
			available: bedInfo.bed.free,
			description: `${bedInfo.bed.room.description} - ${bedInfo.bed.bedNumber}`
		};
		this.selectedBedInfo.emit(selectedBedDto);
	}

	ngOnDestroy(){
		this.managementBed$.unsubscribe();
  	}

}
