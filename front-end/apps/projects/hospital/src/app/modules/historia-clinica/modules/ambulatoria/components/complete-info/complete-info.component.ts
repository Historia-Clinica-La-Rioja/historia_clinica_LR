import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { CompleteRequestDto, DiagnosticReportInfoDto } from '@api-rest/api-model';
import { MatDialogRef } from '@angular/material/dialog';
import { ButtonService } from '../../services/button.service';
import { StudyInformation } from '../../modules/estudio/components/study/study.component';
import { ToFormGroup } from '@core/utils/form.utils';

@Component({
	selector: 'app-complete-info',
	templateUrl: './complete-info.component.html',
	styleUrls: ['./complete-info.component.scss']
})
export class CompleteInfoComponent implements OnInit {
	selectedFiles: File[] = [];
	formStudyClosure;
	@Input() patientId: number;
	@Input() diagnosticReport: DiagnosticReportInfoDto[] | StudyInformation[];
	constructor(
		public dialogRef: MatDialogRef<CompleteInfoComponent>,
		private prescripcionesService: PrescripcionesService,
		readonly buttonService: ButtonService,
	) { }

	ngOnInit() {

		this.formStudyClosure = new FormGroup<ToFormGroup<FormStudyClosure>>({
			description: new FormControl('', [Validators.required])
		})

		this.formStudyClosure.valueChanges.subscribe(_ =>
			this.buttonService.updateFormStatus(!this.formStudyClosure.valid)
		);

		this.buttonService.submit$.subscribe(submit => {
			if (submit)
				this.completeStudy();
		});
	}

	setSelectedFilesEmiit($event: File[]) {
		this.selectedFiles = $event;
	}

	private completeStudy() {
		const completeRequest: CompleteRequestDto = {
			observations: this.formStudyClosure.controls.description.value,
		};

		forkJoin(this.diagnosticReport.map(report => {
			const reportInfo: DiagnosticReportInfoDto = report?.diagnosticInformation ? report.diagnosticInformation : report;
			return this.prescripcionesService.completeStudy(this.patientId, reportInfo.id, completeRequest, this.selectedFiles)
		}
		)).subscribe(
			() => {
				this.closeModal(false, true);
			}, _ => {
				this.closeModal(false, false);
			});
	}

	private closeModal(simpleClose: boolean, completed?: boolean): void {
		this.dialogRef.close(simpleClose ? null : { completed });
	}

}

interface FormStudyClosure {
	description: string;
}
