import { Component, OnInit, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HCEDocumentDataDto } from '@api-rest/api-model';
import { DocumentService } from '@api-rest/services/document.service';
import { StudyAppointmentReportService } from '@api-rest/services/study-appointment-report.service';

@Component({
    selector: 'app-view-report',
    templateUrl: './view-report.component.html',
    styleUrls: ['./view-report.component.scss']
})
export class ViewReportComponent implements OnInit {

    @Input() appointmentId: number;
    @Input() buttonText: string;
    @Input() buttonIcon?: string;
    private docFile: HCEDocumentDataDto;

    constructor(
        public dialog: MatDialog,
		private readonly documentService: DocumentService,
		private readonly studyAppointmentReportService: StudyAppointmentReportService
    ) { }

    ngOnInit(): void {
        this.studyAppointmentReportService.getFileInfo(this.appointmentId).subscribe(studyDocInfo => this.docFile = studyDocInfo);
    }

    viewReport() {
        this.documentService.downloadFile(this.docFile);
    }
}