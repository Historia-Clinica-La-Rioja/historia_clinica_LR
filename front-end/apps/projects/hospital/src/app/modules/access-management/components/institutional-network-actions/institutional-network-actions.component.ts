import { ReferenceApprovalState } from '@access-management/constants/approval';
import { ReasonPopUpComponent, ReasonPopupData } from '@access-management/dialogs/reason-pop-up/reason-pop-up.component';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';

@Component({
	selector: 'app-institutional-network-actions',
	templateUrl: './institutional-network-actions.component.html',
	styleUrls: ['./institutional-network-actions.component.scss']
})
export class InstitutionalNetworkActionsComponent {

	private readonly translatePrefixRoute = 'access-management.search_references.reference.discard_popup';

	@Input() reportCompleteData: ReferenceCompleteDataDto;
	@Output() newAction = new EventEmitter<boolean>();

	constructor(
		private readonly dialog: MatDialog,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
	) { }

	reject() {
		const reasonData = this.toReasonData('rejected.TITLE', 'rejected.SUBTITLE', 'rejected.PLACEHOLDER');
		this.openReasonAndUpdateState(reasonData, ReferenceApprovalState.REJECTED);
	}

	review() {
		const reasonData = this.toReasonData('suggested-review.TITLE', 'suggested-review.SUBTITLE', 'suggested-review.PLACEHOLDER');
		this.openReasonAndUpdateState(reasonData, ReferenceApprovalState.SUGGESTED_REVISION);
	}

	approve() {
		this.updateState(ReferenceApprovalState.APPROVED);
	}

	private openReasonAndUpdateState(data: ReasonPopupData, stateId: number) {
		const dialogRef = this.dialog.open(ReasonPopUpComponent, {
			data,
			autoFocus: false,
			disableClose: true,
			width: '491px',
		});

		dialogRef.afterClosed().subscribe(response => {
			if (response)
				this.updateState(stateId, response.motive);
		});
	}

	private updateState(stateId: number, motive?: string) {
		const updateState$ = this.institutionalNetworkReferenceReportService.changeReferenceRegulationState(this.reportCompleteData.reference.id, stateId, motive || null);
		updateState$.subscribe(_ => this.emit());
	}

	private emit() {
		this.newAction.emit(true);
	}

	private toReasonData(titleKey: string, subtitleKey: string, placeholderKey: string): ReasonPopupData {
		return {
			title: `${this.translatePrefixRoute}.${titleKey}`,
			subtitle: `${this.translatePrefixRoute}.${subtitleKey}`,
			placeholder: `${this.translatePrefixRoute}.${placeholderKey}`,
		}
	}
}
