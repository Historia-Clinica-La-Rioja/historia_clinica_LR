import { ReportCompleteDataPopupComponent } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { DashboardService } from '@access-management/services/dashboard.service';
import { Component, Input } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-approval-actions',
	templateUrl: './approval-actions.component.html',
	styleUrls: ['./approval-actions.component.scss']
})
export class ApprovalActionsComponent {

	@Input() referenceId: number;

	constructor(
		private dialogRef: MatDialogRef<ReportCompleteDataPopupComponent>,
		private readonly snackBarService: SnackBarService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly dashboardService: DashboardService,
	) { }

	cancel() {
		this.institutionalReferenceReportService.cancelReference(this.referenceId).subscribe({
			next: (response) => {
				this.snackBarService.showSuccess('access-management.search_references.reference.approval_actions.CANCEL_SUCCESS');
				this.dashboardService.updateReports();
				this.dialogRef.close();
			},
			error: (_) => this.snackBarService.showError('access-management.search_references.reference.approval_actions.CANCEL_ERROR')
		});
	}

}
