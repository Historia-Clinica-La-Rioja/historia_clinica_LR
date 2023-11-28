import { Component, Input, OnInit } from '@angular/core';
import { DateTimeDto, DiagnosticReportInfoWithFilesDto, DoctorInfoDto, FileDto } from '@api-rest/api-model';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { Observable, map } from 'rxjs';
import { PatientNameService } from '@core/services/patient-name.service';

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
	doctorName: string;
	constructor(
		private prescripcionesService: PrescripcionesService,
		private readonly patientNameService: PatientNameService,
	) { }

	ngOnInit() {

		this.diagnosticReportFiles$ = this.prescripcionesService.showStudyResults(this.patientId, this.diagnosticReportId).pipe(
			map((diagnosticReport: DiagnosticReportInfoWithFilesDto) => diagnosticReport.files ));

		this.doctorName = this.getFullName(this.reportReference.doctor.firstName, this.reportReference.doctor.nameSelfDetermination);

	}

	download(file: FileDto) {
		this.prescripcionesService.downloadStudyFile(this.patientId, file.fileId, file.fileName);
	}

	private getFullName(firstName: string, nameSelfDetermination: string): string {
		return `${this.patientNameService.getPatientName(firstName, nameSelfDetermination)}`;
	}
}

export interface ReportReference {
	doctor: DoctorInfoDto;
	observations: string;
	closureTypeDescription: string;
	date: DateTimeDto;
}
