import { Component, OnInit, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HCEDocumentDataDto } from '@api-rest/api-model';
import { DocumentService } from '@api-rest/services/document.service';
import { StudyAppointmentReportService } from '@api-rest/services/study-appointment-report.service';
import { processErrors } from "@core/utils/form.utils";
import { ButtonType } from "@presentation/components/button/button.component";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { Observable, of } from 'rxjs';

@Component({
    selector: 'app-view-report',
    templateUrl: './view-report.component.html',
    styleUrls: ['./view-report.component.scss']
})
export class ViewReportComponent implements OnInit {

    @Input() appointmentId: number;
    @Input() buttonText: string;
    @Input() buttonIcon?: string;
    @Input() fileInfo: HCEDocumentDataDto = null;
    isLoading = false;
    readonly ButtonType = ButtonType;

    constructor(
        public dialog: MatDialog,
		private readonly documentService: DocumentService,
		private readonly studyAppointmentReportService: StudyAppointmentReportService,
        private readonly snackBarService: SnackBarService,
    ) { }

    ngOnInit(): void {
    }

    viewReport() {
        this.isLoading = true;
        const sourceDocFile$: Observable<HCEDocumentDataDto> = !this.fileInfo ? this.studyAppointmentReportService.getFileInfo(this.appointmentId) : of(this.fileInfo)
        sourceDocFile$.subscribe({
            next: (studyDocInfo) => {
                this.documentService.downloadFile(studyDocInfo);
            },
            error: (error) => {
                processErrors(error, (msg: string) => this.snackBarService.showError(msg));
            },
            complete: () => {
                this.isLoading = false;
            }
        });
    }
}
