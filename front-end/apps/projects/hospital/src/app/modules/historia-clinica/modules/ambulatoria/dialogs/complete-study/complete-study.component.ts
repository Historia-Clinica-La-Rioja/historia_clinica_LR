import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, DoctorInfoDto } from '@api-rest/api-model';
import { PrescripcionesService, PrescriptionTypes } from '../../services/prescripciones.service';
import { CompletarEstudioComponent } from '../ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { PatientNameService } from '@core/services/patient-name.service';
import { StudyData } from '../../components/complete-study-information/complete-study-information.component';
import { PrescriptionStatus } from '../../components/reference-request-data/reference-request-data.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { ButtonService } from '../../services/button.service';
import { StudyInformation } from '../../modules/estudio/components/study/study.component';
import { StudyInfo } from '../../services/study-results.service';

@Component({
	selector: 'app-complete-study',
	templateUrl: './complete-study.component.html',
	styleUrls: ['./complete-study.component.scss'],
	providers: [ButtonService]
})

export class CompleteStudyComponent implements OnInit {
	diagnosticReport: StudyInformation[] | DiagnosticReportInfoDto[];
	study: StudyData;
	buttonTypeFlat = ButtonType.FLAT;
	constructor(
		readonly buttonService: ButtonService,
		public dialogRef: MatDialogRef<CompletarEstudioComponent>,
		private patientNameService: PatientNameService,
		private prescripcionesService: PrescripcionesService,
		@Inject(MAT_DIALOG_DATA) public data: CompleteStudy,

	) { }

	ngOnInit() {
		this.diagnosticReport = this.data.diagnosticReport;
		this.setValues();
	}

	completeStudy() {
		this.buttonService.submit();
	}

	completePartialStudy() {
		this.buttonService.submitPartialSave();
	}


	private prescriptionItemDataBuilder(diagnosticReport): StudyData {
		const prescriptionPt: string = diagnosticReport[0].diagnosticInformation ? diagnosticReport[0].diagnosticInformation.snomed.pt : diagnosticReport[0].snomed.pt;
		const problemPt: string = diagnosticReport[0].diagnosticInformation ? diagnosticReport[0].diagnosticInformation.healthCondition.snomed.pt : diagnosticReport[0].healthCondition.snomed.pt;
		const registerEditor: RegisterEditor = diagnosticReport[0].diagnosticInformation ? this.mapRegisterEditor(diagnosticReport[0].diagnosticInformation.doctor, this.data.creationDate) : this.mapRegisterEditor(diagnosticReport[0].doctor, this.data.creationDate);
		const statusId: string = diagnosticReport[0].diagnosticInformation ? diagnosticReport[0].diagnosticInformation.statusId : diagnosticReport[0].statusId;
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, statusId),
			prescriptionPt,
			problemPt,
			registerEditor
		};
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
	}

}


export interface CompleteStudy {
	diagnosticReport: StudyInformation[] | DiagnosticReportInfoDto[],
	patientId: number,
	creationDate: Date,
	order: number,
	status: PrescriptionStatus,
	studies: StudyInfo[],
}
