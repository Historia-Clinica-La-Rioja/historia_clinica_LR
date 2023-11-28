import { Component, OnInit, Output, EventEmitter, OnDestroy, Input, SimpleChanges, OnChanges } from '@angular/core';
import { BedManagement } from '@camas/routes/home/home.component';
import { Subscription } from 'rxjs';
import { MapperService } from '@presentation/services/mapper.service';
import { BedManagementFacadeService } from '../../services/bed-management-facade.service';
import { BedSummaryDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-bed-mapping',
	templateUrl: './bed-mapping.component.html',
	styleUrls: ['./bed-mapping.component.scss']
})
export class BedMappingComponent implements OnInit, OnChanges, OnDestroy {
	@Input() updateData: boolean;
	@Input() sectorsType?: number[];
	@Output() selectedBed = new EventEmitter<number>();
	selectedBedIndex: number;
	selectedBedSectorId: number;

	public bedManagementList: BedManagement[];
	private managementBed$: Subscription;

	constructor(
		private mapperService: MapperService,
		private bedManagementFacadeService: BedManagementFacadeService
	) { }

	ngOnInit(): void {
		this.setManagementList();
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.updateData?.currentValue) 
			this.setManagementList();
	}
	
	selectBed(bedId: number, index: number, sectorId: number) {
		this.selectedBedSectorId = sectorId;
		this.selectedBedIndex = index;
		this.selectedBed.emit(bedId);
	}
	
	ngOnDestroy(): void {
		this.managementBed$.unsubscribe();
	}
	
	private setManagementList() {
		this.managementBed$ = this.bedManagementFacadeService.getBedManagement(this.sectorsType).pipe(
			map((bedsSummary: BedSummaryDto[]) => bedsSummary ? this.mapperService.toBedManagement(bedsSummary) : null)
		).subscribe(data => this.bedManagementList = data);
	}
}
