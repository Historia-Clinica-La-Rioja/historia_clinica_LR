import { Component, OnInit, OnDestroy } from '@angular/core';
import { BedSummaryDto } from '@api-rest/api-model';
import { MapperService } from '@presentation/services/mapper.service';
import { map, tap } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { BedManagementService } from 'src/app/modules/institucion/services/bed-management.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss'],
	providers: [ BedManagementService ]
})
export class HomeComponent implements OnInit, OnDestroy {

	public selectedBed: number;
	public bedManagementList: BedManagement[];
	public bedsAmount: number;

	private managementBed$: Subscription;

	constructor(
		private mapperService: MapperService,
		private bedManagementService: BedManagementService
  	) { }

	ngOnInit(): void {
		this.managementBed$ = this.bedManagementService.getBedManagement().pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0),
			map((bedsSummary: BedSummaryDto[]) => bedsSummary ? this.mapperService.toBedManagement(bedsSummary) : null)
		).subscribe(data => this.bedManagementList = data);
	}

	onSelectBed(bedId): void {
		this.selectedBed = bedId;
	}

	ngOnDestroy(): void {
		this.managementBed$.unsubscribe();
  	}

}

export class BedManagement {
	sectorId: number;
	sectorDescription: string;
  	specialty:
	{
		specialtyId: number;
		specialtyName: string;
		beds:
			{
				bedId: number;
				bedNumber: string;
				free: boolean;
			}[];
  	}[];
}
