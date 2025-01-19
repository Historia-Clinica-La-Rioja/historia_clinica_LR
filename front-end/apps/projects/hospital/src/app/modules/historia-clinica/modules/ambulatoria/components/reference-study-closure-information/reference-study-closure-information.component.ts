import { Component, Input, OnInit } from '@angular/core';
import { DateTimeDto, DiagnosticReportInfoWithFilesDto, FileDto, ProfessionalCompleteDto } from '@api-rest/api-model';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { Observable, map } from 'rxjs';
import { PatientNameService } from '@core/services/patient-name.service';
import { ResultPractice } from '../../dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';

@Component({
	selector: 'app-reference-study-closure-information',
	templateUrl: './reference-study-closure-information.component.html',
	styleUrls: ['./reference-study-closure-information.component.scss']
})
export class ReferenceStudyClosureInformationComponent implements OnInit {
	@Input() patientId: number = 0;
	@Input() diagnosticReportId: number = 0;
	@Input() reportReference: ReportReference;
	@Input() resultsPractices: ResultPractice[];

	Color = Color;
	diagnosticReportFiles$: Observable<FileDto[]>;
	doctorName: string;
	constructor(
		private prescripcionesService: PrescripcionesService,
		private readonly patientNameService: PatientNameService,
	) { }

	ngOnInit() {
		this.diagnosticReportFiles$ = this.prescripcionesService.showStudyResults(this.patientId, this.diagnosticReportId).pipe(
			map((diagnosticReport: DiagnosticReportInfoWithFilesDto) => diagnosticReport.files));

		if (this.reportReference?.doctor)
			this.doctorName = this.completeName(this.reportReference.doctor.firstName, this.reportReference.doctor.nameSelfDetermination,
				this.reportReference.doctor.lastName, this.reportReference.doctor?.middleNames, this.reportReference.doctor.otherLastNames);
	}

	download(file: FileDto) {
		this.prescripcionesService.downloadStudyFile(this.patientId, file.fileId, file.fileName);
	}

	private completeName(firstName: string, nameSelfDetermination: string, lastName: string, patientSecondsName: string, otherLastNames: string): string {
		return this.patientNameService.completeName(firstName, nameSelfDetermination, lastName, patientSecondsName, otherLastNames)
	}
}

export interface ReportReference {
	doctor: ProfessionalCompleteDto;
	observations: string;
	date: DateTimeDto;
}
