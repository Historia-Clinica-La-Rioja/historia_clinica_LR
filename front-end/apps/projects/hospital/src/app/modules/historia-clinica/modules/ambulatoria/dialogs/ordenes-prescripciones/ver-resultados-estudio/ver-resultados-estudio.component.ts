import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, DiagnosticReportInfoWithFilesDto, FileDto } from '@api-rest/api-model';
import { PrescriptionItemData } from '../../../modules/indicacion/components/item-prescripciones/item-prescripciones.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';
import { CompletarEstudioComponent } from '../completar-estudio/completar-estudio.component';

@Component({
  selector: 'app-ver-resultados-estudio',
  templateUrl: './ver-resultados-estudio.component.html',
  styleUrls: ['./ver-resultados-estudio.component.scss']
})
export class VerResultadosEstudioComponent implements OnInit {

	diagnosticReport: DiagnosticReportInfoDto;
	diagnosticReportFiles: FileDto[] = [];
	observations: string;

  	constructor(
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<CompletarEstudioComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			diagnosticReport: DiagnosticReportInfoDto,
			patientId: number,
		},
  	) { }

	ngOnInit(): void {
		this.diagnosticReport = this.data.diagnosticReport;

		this.prescripcionesService.showStudyResults(this.data.patientId, this.diagnosticReport.id).subscribe((diagnosticReport: DiagnosticReportInfoWithFilesDto) => {
			this.diagnosticReportFiles = diagnosticReport.files;
			this.observations = diagnosticReport.observations;
		});
	}

	closeModal(): void {
		this.dialogRef.close();
	}

	prescriptionItemDataBuilder(diagnosticReport: DiagnosticReportInfoDto): PrescriptionItemData {
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId),
			prescriptionPt: diagnosticReport.snomed.pt,
			problemPt: diagnosticReport.healthCondition.snomed.pt,
			doctor: diagnosticReport.doctor,
		};
	}

	download(file: FileDto) {
		this.prescripcionesService.downloadStudyFile(this.data.patientId, file.fileId, file.fileName);
	}
}
