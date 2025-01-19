import { Component, Inject, OnInit } from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CompleteRequestDto, DiagnosticReportInfoDto, DoctorInfoDto } from '@api-rest/api-model';
import { PrescriptionItemData } from '../../../modules/indicacion/components/item-prescripciones/item-prescripciones.component';
import { PrescripcionesService, PrescriptionTypes } from './../../../services/prescripciones.service';
import { hasError } from '@core/utils/form.utils';
import {TEXT_AREA_MAX_LENGTH} from '@core/constants/validation-constants';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-completar-estudio',
  templateUrl: './completar-estudio.component.html',
  styleUrls: ['./completar-estudio.component.scss']
})
export class CompletarEstudioComponent implements OnInit {

	diagnosticReport; //Puede ser de tipo DiagnosticReportInfoDto[] o StudyInformation[];
	completeStudyForm: UntypedFormGroup;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public hasError = hasError;
	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private prescripcionesService: PrescripcionesService,
		public dialogRef: MatDialogRef<CompletarEstudioComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			diagnosticReport; //Puede ser de tipo DiagnosticReportInfoDto[] o StudyInformation[],
			patientId: number,
		}
	) { }

	ngOnInit(): void {
		this.diagnosticReport = this.data.diagnosticReport;

		this.completeStudyForm = this.formBuilder.group({
			observations: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
		});
	}

	closeModal(simpleClose: boolean, completed?: boolean): void {
		this.dialogRef.close(simpleClose ? null : {completed});
	}

	//diagnosticReport puede ser de tipo DiagnosticReportInfoDto[] o de StudyInformation[]
	prescriptionItemDataBuilder(diagnosticReport): PrescriptionItemData {
		let prescriptionPt: string;
		let problemPt: string;
		let doctor: DoctorInfoDto;
		let statusId: string;
		if (diagnosticReport[0].diagnosticInformation) {
			prescriptionPt = diagnosticReport[0].diagnosticInformation.snomed.pt;
			problemPt = diagnosticReport[0].diagnosticInformation.healthCondition.snomed.pt;
			doctor = diagnosticReport[0].diagnosticInformation.doctor;
			statusId = diagnosticReport[0].diagnosticInformation.statusId
		} else {
			prescriptionPt = diagnosticReport[0].snomed.pt;
			problemPt = diagnosticReport[0].healthCondition.snomed.pt;
			doctor = diagnosticReport[0].doctor;
			statusId = diagnosticReport[0].statusId;
		}
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, statusId),
			prescriptionPt: prescriptionPt,
			problemPt: problemPt,
			doctor: doctor,
		};
	}

	completeStudy(): void {
		const completeRequest: CompleteRequestDto = {
			observations: this.completeStudyForm.controls.observations.value,
		};

		forkJoin(this.diagnosticReport.map(report =>{
			let reportInfo: DiagnosticReportInfoDto;
			reportInfo = report.diagnosticInformation ? report.diagnosticInformation : report;
			return this.prescripcionesService.completeStudy(this.data.patientId, reportInfo.id, completeRequest, this.selectedFiles)
		})).subscribe(
			() => {
				this.closeModal(false, true);
			}, _ => {
				this.closeModal(false, false);
		});
	}

	onSelectFileFormData($event): void {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	removeSelectedFile(index): void {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

	disableButton(): boolean {
		return !(this.selectedFiles.length || this.completeStudyForm.controls.observations.value) || this.completeStudyForm.invalid;
	}
}
