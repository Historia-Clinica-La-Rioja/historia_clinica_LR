import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ReferenceEditionPopUpComponent } from '@access-management/dialogs/reference-edition-pop-up/reference-edition-pop-up.component';
import { ReportCompleteDataPopupComponent } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { DashboardService } from '@access-management/services/dashboard.service';
import { Component, Input } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { ReferenceDataDto } from '@api-rest/api-model';

@Component({
	selector: 'app-approval-actions',
	templateUrl: './approval-actions.component.html',
	styleUrls: ['./approval-actions.component.scss']
})
export class ApprovalActionsComponent {

	ButtonType = ButtonType;
	@Input() referenceDataDto: ReferenceDataDto;

	constructor(
		private reportCompleteDataDialogRef: MatDialogRef<ReportCompleteDataPopupComponent>,
		private readonly snackBarService: SnackBarService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly dashboardService: DashboardService,
		private readonly dialog: MatDialog,
	) { }

	cancel() {
		const confirmDialog = this.dialog.open(ConfirmDialogComponent, {
			data: {
				title: 'access-management.search_references.reference.approval_actions.CONFIRMATION_CANCEL_TITLE',
				okButtonLabel: 'access-management.search_references.reference.approval_actions.CONFIRMATION_CANCEL_BUTTON_CONFIRM',
				cancelButtonLabel: 'access-management.search_references.reference.approval_actions.CONFIRMATION_CANCEL_BUTTON_CANCEL',
				showMatIconError: true,
				okBottonColor: 'warn'
			}
		})
		confirmDialog.afterClosed().subscribe(
			cancelReference => {
				if (cancelReference) {
					this.institutionalReferenceReportService.cancelReference(this.referenceDataDto.id).subscribe({
						next: (response) => {
							this.snackBarService.showSuccess('access-management.search_references.reference.approval_actions.CANCEL_SUCCESS');
							this.dashboardService.updateReports();
							this.reportCompleteDataDialogRef.close();
						},
						error: (_) => this.snackBarService.showError('access-management.search_references.reference.approval_actions.CANCEL_ERROR')
					});
				}
			}
		)
	}

	edit() {

		const referenceEditionDialogRef = this.dialog.open(ReferenceEditionPopUpComponent, {
			data: {
				referenceDataDto: this.referenceDataDto
			},
			autoFocus: false,
			disableClose: true,
			width: '50%',
		});

		referenceEditionDialogRef.afterClosed().subscribe(edited => {
			if (edited) {
				this.dashboardService.updateReports();
				this.reportCompleteDataDialogRef.close();
			}
		});
	}

}
