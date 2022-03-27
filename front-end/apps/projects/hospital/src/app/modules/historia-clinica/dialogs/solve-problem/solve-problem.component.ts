import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { format } from "date-fns";

import { hasError } from '@core/utils/form.utils';
import { DateFormat, MIN_DATE } from "@core/utils/date.utils";
import { HCEPersonalHistoryDto, HealthConditionNewConsultationDto, MasterDataInterface } from '@api-rest/api-model';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';

import { ProblemasService } from '../../services/problemas.service';
import { SnomedService } from '../../services/snomed.service';
import { HEALTH_CLINICAL_STATUS } from "@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids";

@Component({
	selector: 'app-solve-problem',
	templateUrl: './solve-problem.component.html',
	styleUrls: ['./solve-problem.component.scss']
})
export class SolveProblemComponent implements OnInit {

	removeForm: FormGroup;
	clinicalStatus: MasterDataInterface<string>[];
	problemasService: ProblemasService;
	private readonly form: FormGroup;
	private problema: HealthConditionNewConsultationDto;
	private dataDto: HCEPersonalHistoryDto;
	private readonly patientId: number;
	private readonly problemId: number;
	dateIsReadOnly: boolean;
	severityTypeMasterData: any[];
	today: Date;
	minDate = MIN_DATE;

	constructor(
		@Inject(MAT_DIALOG_DATA) data,
		private dialogRef: MatDialogRef<SolveProblemComponent>,
		private snackBarService: SnackBarService,
		private formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly healthConditionService: HealthConditionService,
		private readonly snomedService: SnomedService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
	) {
		this.problemasService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.dataDto = data.problema;
		this.patientId = data.patientId;
		this.problemId = this.dataDto.id;
		this.today = this.toFormatDate(format(new Date(), DateFormat.VIEW_DATE));
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			severidad: [null],
			cronico: [null, Validators.required],
			fechaInicio: [null, Validators.required],
			fechaFin: [null, Validators.required]
		});
	}

	ngOnInit(): void {
		this.healthConditionService.getHealthCondition(this.dataDto.id).subscribe(p => {
			this.problema = p;
			this.initializeFields(p);
		});

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypeMasterData = healthConditionSeverities;
		});
	}

	initializeFields(p: HealthConditionNewConsultationDto) {
		this.form.controls.snomed.setValue(p.snomed.pt);

		if (p.severity) {
			this.form.controls.severidad.setValue(p.severity);
			this.form.controls.severidad.disable();
		}

		let dateToSet;
		if (p.startDate) {
			dateToSet = this.toFormatDate(this.dataDto.startDate)
			this.dateIsReadOnly = true
		} else {
			dateToSet = this.toFormatDate(format(new Date(), DateFormat.VIEW_DATE))
		}

		this.form.controls.cronico.setValue(p.isChronic);
		this.form.controls.fechaInicio.setValue(dateToSet);
	}

	solveProblem() {
		if (this.form.valid) {
			const dialogRefConfirmation = this.dialog.open(ConfirmDialogComponent,
				{
					data: {
						title: 'ambulatoria.paciente.problemas.TITLE_POPUP_RESOLVER_PROBLEMA',
						content: `ambulatoria.paciente.problemas.CONFIRMACION_POPUP`,
						okButtonLabel: 'ambulatoria.paciente.problemas.BOTON_CONFIRMAR_POPUP'
					}
				});

			dialogRefConfirmation.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.problema.inactivationDate = this.form.value.fechaFin.toDate();
					this.problema.startDate = this.form.value.fechaInicio;
					this.problema.statusId = HEALTH_CLINICAL_STATUS.RESUELTO;
					this.problema.id = this.problemId;
					if (this.form.value.severidad) {
						this.problema.severity = this.form.value.severidad;
					}

					this.outpatientConsultationService.solveProblem(this.problema, this.patientId).subscribe(
						_ => {
							this.snackBarService.showSuccess('ambulatoria.paciente.problemas.SUCCESS_CONFIRMAR_POPUP');
							this.dialogRef.close(true);
						},
						_ => {
							this.snackBarService.showError('ambulatoria.paciente.problemas.ERROR_CONFIRMAR_POPUP');
						}
					);
				}
			});
		}
	}

	getForm(): FormGroup {
		return this.form;
	}

	checkInactivationDate() {
		const fechaFin = this.form.controls.fechaFin.value;
		const fechaInicio = this.form.controls.fechaInicio.value;

		if (fechaFin) {
			if (fechaInicio) {
				const inactivationDate = fechaFin.toDate();
				const initDate = fechaInicio;
				if (initDate > inactivationDate) {
					this.form.controls.fechaFin.setErrors({ min: true });
				}
				const actualDate = new Date();
				if (inactivationDate > actualDate) {
					this.form.controls.fechaFin.setErrors({ max: true });
				}
			}
		}
	}

	hasError(type: string, controlName: string): boolean {
		return hasError(this.form, type, controlName);
	}


	toFormatDate = (dateStr) => {
		const [day, month, year] = dateStr.split('/');
		return new Date(year, month - 1, day);
	}
}
