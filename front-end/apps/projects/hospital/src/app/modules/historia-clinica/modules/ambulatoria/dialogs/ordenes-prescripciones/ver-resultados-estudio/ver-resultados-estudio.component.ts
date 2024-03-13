import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, DiagnosticReportInfoWithFilesDto, DoctorInfoDto, FileDto } from '@api-rest/api-model';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';
import { CompletarEstudioComponent } from '../completar-estudio/completar-estudio.component';
import { PrescriptionStatus } from '@historia-clinica/modules/ambulatoria/components/reference-request-data/reference-request-data.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { PatientNameService } from '@core/services/patient-name.service';
import { StudyData } from '@historia-clinica/modules/ambulatoria/components/complete-study-information/complete-study-information.component';

const OBSERVATIONS = 'ambulatoria.paciente.ordenes_prescripciones.show_study_results_dialog.OBSERVATIONS';

@Component({
	selector: 'app-ver-resultados-estudio',
	templateUrl: './ver-resultados-estudio.component.html',
	styleUrls: ['./ver-resultados-estudio.component.scss']
})
export class VerResultadosEstudioComponent implements OnInit {
	observationsConst = OBSERVATIONS;
	diagnosticReport: DiagnosticReportInfoDto;
	diagnosticReportFiles: FileDto[] = [];
	observations: string;
	study: StudyData;
	order: number;
	status: PrescriptionStatus;
	patientId: number;

	constructor(
		public dialogRef: MatDialogRef<CompletarEstudioComponent>,
		private prescripcionesService: PrescripcionesService,
		private patientNameService: PatientNameService,

		@Inject(MAT_DIALOG_DATA) public data: {
			diagnosticReport: DiagnosticReportInfoDto,
			patientId: number,
			creationDate: Date,
			order: number,
			status: PrescriptionStatus,
		},
	) { }

	ngOnInit() {

		this.diagnosticReport = this.data.diagnosticReport;

		this.prescripcionesService.showStudyResults(this.data.patientId, this.diagnosticReport.id).subscribe((diagnosticReport: DiagnosticReportInfoWithFilesDto) => {
			this.diagnosticReportFiles = diagnosticReport.files;
			this.observations = diagnosticReport.observations;
		});

		this.setValues();
	}

	closeModal(): void {
		this.dialogRef.close();
	}

	download(file: FileDto) {
		this.prescripcionesService.downloadStudyFile(this.data.patientId, file.fileId, file.fileName);
	}

	private mapRegisterEditor(doctor: DoctorInfoDto, creationDate: Date): RegisterEditor {
		return {
			createdBy: this.getFullName(doctor.firstName, doctor.nameSelfDetermination),
			date: creationDate
		}
	}

	private getFullName(firstName: string, nameSelfDetermination: string): string {
		return `${this.patientNameService.getPatientName(firstName, nameSelfDetermination)}`;
	}

	private setValues() {
		this.study = this.prescriptionItemDataBuilder(this.data.diagnosticReport);
		this.order = this.data.order;
		this.status = this.data.status;
		this.patientId = this.data.patientId;
	}

	private prescriptionItemDataBuilder(diagnosticReport: DiagnosticReportInfoDto): StudyData {
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId),
			prescriptionPt: diagnosticReport.snomed.pt,
			problemPt: diagnosticReport.healthCondition.snomed.pt,
			registerEditor: this.mapRegisterEditor(diagnosticReport.doctor, this.data.creationDate)
		};
	}
}
