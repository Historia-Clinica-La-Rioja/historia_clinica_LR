import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToothDto } from '@api-rest/api-model';
import { OdontogramService as OdontogramRestService } from '../../api-rest/odontogram.service';
import { ToothAction } from '../../services/actions-service.service';
import { OdontogramService } from '../../services/odontogram.service';
import { ToothTreatment } from '../../services/surface-drawer.service';
import { ToothDialogComponent } from '../tooth-dialog/tooth-dialog.component';

@Component({
	selector: 'app-odontogram',
	templateUrl: './odontogram.component.html',
	styleUrls: ['./odontogram.component.scss']
})
export class OdontogramComponent implements OnInit {

	constructor(
		private odontogramRestService: OdontogramRestService,
		private dialog: MatDialog,
		public odontogramService: OdontogramService
	) { }

	readonly toothTreatment = ToothTreatment.AS_WHOLE_TOOTH;

	quadrants;

	findingsAndProcedures = {};
	ngOnInit(): void {

		this.odontogramRestService.getOdontogram().subscribe(
			odontogram => {

				const quadrant1 = odontogram.find(q => q.permanent && !q.left && q.top);
				const quadrant2 = odontogram.find(q => q.permanent && q.left && q.top);
				const quadrant5 = odontogram.find(q => !q.permanent && !q.left && q.top);
				const quadrant6 = odontogram.find(q => !q.permanent && q.left && q.top);
				const quadrant8 = odontogram.find(q => !q.permanent && !q.left && !q.top);
				const quadrant7 = odontogram.find(q => !q.permanent && q.left && !q.top);
				const quadrant4 = odontogram.find(q => q.permanent && !q.left && !q.top);
				const quadrant3 = odontogram.find(q => q.permanent && q.left && !q.top);

				this.quadrants = { quadrant1, quadrant2, quadrant3, quadrant4, quadrant5, quadrant6, quadrant7, quadrant8 };
			}
		);
	}

	openToothDialog(tooth: ToothDto, quadrantCode: number) {
		const dialogRef = this.dialog.open(ToothDialogComponent, {
			width: '464px',
			data: {
				tooth,
				quadrantCode,
				currentActions: this.odontogramService.getActionsFrom(tooth.snomed.sctid)
			}
		});

		dialogRef.afterClosed().subscribe((findingsAndProcedures: ToothAction[]) => {
			if (findingsAndProcedures) {
				this.odontogramService.setActionsTo(findingsAndProcedures, tooth.snomed.sctid);
			}
		});
	}

	clearOdontogram() {
		this.odontogramService.resetOdontogram();
	}

}
