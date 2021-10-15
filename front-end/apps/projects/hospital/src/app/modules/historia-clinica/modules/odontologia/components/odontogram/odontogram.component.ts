import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { OdontologyConsultationIndicesDto, ToothDrawingsDto, ToothDto } from '@api-rest/api-model';
import { deepClone } from '@core/utils/core.utils';
import { OdontogramService as OdontogramRestService } from '../../api-rest/odontogram.service';
import { ToothAction, Action, ActionType } from '../../services/actions.service';
import { OdontogramService } from '../../services/odontogram.service';
import { ToothTreatment } from '../../services/surface-drawer.service';
import { ToothSurfaceId } from '../../utils/Surface';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { OdontologyConsultationDockPopupComponent } from '../odontology-consultation-dock-popup/odontology-consultation-dock-popup.component';
import { ToothDialogComponent } from '../tooth-dialog/tooth-dialog.component';
import { OdontologyConsultationService } from '../../api-rest/odontology-consultation.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-odontogram',
	templateUrl: './odontogram.component.html',
	styleUrls: ['./odontogram.component.scss']
})
export class OdontogramComponent implements OnInit {

	constructor(
		private readonly odontogramRestService: OdontogramRestService,
		private readonly dialog: MatDialog,
		public readonly odontogramService: OdontogramService,
		private readonly activatedRoute: ActivatedRoute,
		private readonly dockPopupService: DockPopupService,
		private readonly odontologyConsultationService: OdontologyConsultationService
	) { }

	readonly toothTreatment = ToothTreatment.AS_WHOLE_TOOTH;

	quadrants;
	currentDraws = {};
	actionsFrom = {};
	private patientId: number;
	consultationsIndices$: Observable<OdontologyConsultationIndicesDto[]>;
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

		this.odontogramService.actionedTeeth.forEach(actionedTooth => {
			this.actionsFrom[actionedTooth.tooth.snomed.sctid] = actionedTooth.actions;
		});
		this.activatedRoute.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.odontogramRestService.getOdontogramDrawings(this.patientId).subscribe(
					(records: ToothDrawingsDto[]) => {
						records.forEach(record => {
							this.currentDraws[record.toothSctid] = mapRecords(record.drawings);
						})
					}
				)
			});

		this.consultationsIndices$ = this.odontologyConsultationService.getConsultationIndices(this.patientId);
	}

	openToothDialog(tooth: ToothDto, quadrantCode: number) {
		const dialogRef = this.dialog.open(ToothDialogComponent, {
			width: '464px',
			data: {
				tooth,
				quadrantCode,
				currentActions: this.odontogramService.getActionsFrom(tooth.snomed.sctid),
				records: this.currentDraws[tooth.snomed.sctid] ? deepClone(this.currentDraws[tooth.snomed.sctid]) : [],
				patientId: this.patientId
			},
			autoFocus: false
		});

		dialogRef.afterClosed().subscribe((findingsAndProcedures: ToothAction[]) => {
			if (findingsAndProcedures) {
				this.odontogramService.setActionsTo(findingsAndProcedures, `${quadrantCode}${tooth.code}`, tooth);
				this.actionsFrom[tooth.snomed.sctid] = findingsAndProcedures;
			}
		});
	}

	clearOdontogram() {
		this.odontogramService.resetOdontogram();
		this.actionsFrom = [];
	}

	openConsultationPopup() {
		const dialogRef = this.dockPopupService.open(OdontologyConsultationDockPopupComponent, { patientId: this.patientId });
		dialogRef.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.setActionsAsRecords();
				this.consultationsIndices$ = this.odontologyConsultationService.getConsultationIndices(this.patientId);
			}
		})
	}

	private setActionsAsRecords() {
		this.odontogramService.actionedTeeth.forEach(actionedTooth => {
			const actions = actionedTooth.actions
				.map(currentActionsToRecords);
			this.currentDraws[actionedTooth.tooth.snomed.sctid] = actions;
		});
		this.odontogramService.resetOdontogram();
	}

}

const currentActionsToRecords = (action: ToothAction) => {
	return {
		action:
		{
			sctid: action.action.sctid,
			type: action.action.type === ActionType.RECORD
		},
		surfaceId: action.surfaceId,
		wholeProcedureOrder: action.wholeProcedureOrder
	}
}

const mapRecords = (drawings: Drawings): ToothAction[] => {
	const recordsToAdd: ToothAction[] = []
	Object.keys(drawings).forEach(key => {
		if ([key]) {
			const action: Action = { sctid: drawings[key], type: ActionType.RECORD };
			const surfaceId = key !== ToothSurfaceId.WHOLE ? key : null;
			const toAdd: ToothAction = { surfaceId, action };
			recordsToAdd.push(toAdd);
		}
	});
	return recordsToAdd;
}

interface Drawings {
	[ToothSurfaceId.INTERNAL]?: string;
	[ToothSurfaceId.EXTERNAL]?: string;
	[ToothSurfaceId.LEFT]?: string;
	[ToothSurfaceId.RIGHT]?: string;
	[ToothSurfaceId.CENTRAL]?: string;
	[ToothSurfaceId.WHOLE]?: string
}
