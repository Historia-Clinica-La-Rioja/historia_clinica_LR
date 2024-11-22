import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ReferenceEditionPopUpComponent, ReferenceEditionPopUpData } from '@access-management/dialogs/reference-edition-pop-up/reference-edition-pop-up.component';
import { ReportCompleteDataPopupComponent } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { DashboardService } from '@access-management/services/dashboard.service';
import { Component, Input } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';

@Component({
	selector: 'app-reference-state-actions',
	templateUrl: './reference-state-actions.component.html',
	styleUrls: ['./reference-state-actions.component.scss']
})
export class ReferenceStateActionsComponent {

	ButtonType = ButtonType;
	@Input() referenceCompleteDataDto: ReferenceCompleteDataDto;

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
					const referenceId = this.referenceCompleteDataDto.reference.id;
					this.institutionalReferenceReportService.cancelReference(referenceId).subscribe({
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
		const referenceEditionData: ReferenceEditionPopUpData = {
			referenceDataDto: this.referenceCompleteDataDto.reference,
			referencePatientDto: this.referenceCompleteDataDto.patient
		}
		const referenceEditionDialogRef = this.dialog.open(ReferenceEditionPopUpComponent, {
			data: referenceEditionData,
			autoFocus: false,
			disableClose: true,
		});

		referenceEditionDialogRef.afterClosed().subscribe(edited => {
			if (edited) {
				this.dashboardService.updateReports();
				this.reportCompleteDataDialogRef.close();
			}
		});
	}

}
