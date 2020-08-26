import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { Subscription } from 'rxjs';
import { MapperService } from '@presentation/services/mapper.service';
import { BedManagementService } from 'src/app/modules/institucion/services/bed-management.service';
import { BedSummaryDto, BedInfoDto } from '@api-rest/api-model';
import { BedManagement } from 'src/app/modules/camas/routes/home/home.component';
import { tap, map } from 'rxjs/operators';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-bed-assignment',
  templateUrl: './bed-assignment.component.html',
  styleUrls: ['./bed-assignment.component.scss'],
  providers: [ BedManagementService ]
})
export class BedAssignmentComponent implements OnInit, OnDestroy {

  	public selectedBed: number;
	public bedManagementList: BedManagement[];
	public bedsAmount: number;

	private ManagementBed$: Subscription;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {  },
		public dialogRef: MatDialogRef<BedAssignmentComponent>,
		private mapperService: MapperService,
		private bedManagementService: BedManagementService
  	) { }

	ngOnInit(): void {
		this.ManagementBed$ = this.bedManagementService.getBedManagement().pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0),
			map((bedsSummary: BedSummaryDto[]) => bedsSummary ? this.mapperService.toBedManagement(bedsSummary) : null)
		).subscribe(data => this.bedManagementList = data);
	}

	onSelectBed(bedId: number): void {
		this.selectedBed = bedId;
	}

	onAssignedBed(bedInfo: BedInfoDto): void {
		this.dialogRef.close(bedInfo);
	}

	ngOnDestroy(): void {
		this.ManagementBed$.unsubscribe();
  	}

}
