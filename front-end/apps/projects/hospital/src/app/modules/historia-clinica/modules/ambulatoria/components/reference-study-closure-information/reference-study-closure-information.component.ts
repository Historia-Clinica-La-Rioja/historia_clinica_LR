import { Component, Input, OnInit } from '@angular/core';
import { DiagnosticReportInfoWithFilesDto, FileDto } from '@api-rest/api-model';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { Observable, map } from 'rxjs';

@Component({
	selector: 'app-reference-study-closure-information',
	templateUrl: './reference-study-closure-information.component.html',
	styleUrls: ['./reference-study-closure-information.component.scss']
})
export class ReferenceStudyClosureInformationComponent implements OnInit {
	@Input() patientId: number = 0;
	@Input() diagnosticReportId: number = 0;
	@Input() reportReference: ReportReference;
	Color = Color;
	diagnosticReportFiles$: Observable<FileDto[]>;

	constructor(
		private prescripcionesService: PrescripcionesService,

	) { }

	ngOnInit() {

		this.diagnosticReportFiles$ = this.prescripcionesService.showStudyResults(this.patientId, this.diagnosticReportId).pipe(
			map((diagnosticReport: DiagnosticReportInfoWithFilesDto) => diagnosticReport.files ));

	}

	download(file: FileDto) {
		this.prescripcionesService.downloadStudyFile(this.patientId, file.fileId, file.fileName);
	}
}

export interface ReportReference {
	professionalFullName: string;
	observations: string;
	closureTypeDescription: string;
}
