import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { Subscription } from 'rxjs';
import { BedManagementFacadeService } from '@institucion/services/bed-management-facade.service';
import { BedInfoDto } from '@api-rest/api-model';
import { tap } from 'rxjs/operators';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-bed-assignment',
  templateUrl: './bed-assignment.component.html',
  styleUrls: ['./bed-assignment.component.scss'],
  providers: [ BedManagementFacadeService ]
})
export class BedAssignmentComponent implements OnInit, OnDestroy {

  	public selectedBed: number;
	public existBedManagementList = false;
	public bedsAmount: number;

	private managementBed$: Subscription;

	constructor(
		public dialogRef: MatDialogRef<BedAssignmentComponent>,
		private bedManagementFacadeService: BedManagementFacadeService,
		@Inject(MAT_DIALOG_DATA) public data:{
			sectorsType,
			preselectedBed: number
		},
  	) {}

	ngOnInit(): void {
		this.bedManagementFacadeService.setInitialFilters({
			sector: null,
			service: null,
			probableDischargeDate: null,
			filled: false,
			hierarchicalUnits: null
		});
		this.managementBed$ = this.bedManagementFacadeService.getBedManagement(this.data.sectorsType).pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0)
		).subscribe(data => {
			this.existBedManagementList = data ? true : false;
			this.selectedBed = this.data.preselectedBed;
		});
	}

	onSelectBed(bedId: number): void {
		this.selectedBed = bedId;
	}

	onAssignedBed(bedInfo: BedInfoDto): void {
		this.dialogRef.close(bedInfo);
	}

	ngOnDestroy(): void {
		this.managementBed$.unsubscribe();
  	}

}
