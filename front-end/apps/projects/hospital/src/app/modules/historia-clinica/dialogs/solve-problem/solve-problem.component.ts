import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';

import { hasError } from '@core/utils/form.utils';
import { MIN_DATE } from "@core/utils/date.utils";
import { HCEHealthConditionDto, HealthConditionNewConsultationDto, MasterDataInterface } from '@api-rest/api-model';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ProblemasService } from '../../services/problemas.service';
import { SnomedService } from '../../services/snomed.service';
import { HEALTH_CLINICAL_STATUS } from "@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids";
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { stringToDate } from '@api-rest/mapper/date-dto.mapper';

@Component({
	selector: 'app-solve-problem',
	templateUrl: './solve-problem.component.html',
	styleUrls: ['./solve-problem.component.scss']
})
export class SolveProblemComponent implements OnInit {

	removeForm: UntypedFormGroup;
	clinicalStatus: MasterDataInterface<string>[];
	problemasService: ProblemasService;
	private readonly form: UntypedFormGroup;
	private problema: HealthConditionNewConsultationDto;
	private dataDto: HCEHealthConditionDto;
	private readonly patientId: number;
	private readonly problemId: number;
	severityTypeMasterData: any[];
	today: Date;
	minDate = MIN_DATE;
	maxDate = new Date();
	dateToSet: Date;
	markAsTouched = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) data,
		private dialogRef: MatDialogRef<SolveProblemComponent>,
		private snackBarService: SnackBarService,
		private formBuilder: UntypedFormBuilder,
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
		this.today = new Date();
		console.log('fecha de hoy: ', this.today);
		this.form = this.formBuilder.group({
			snomed: [{value: null, disabled: true}, Validators.required],
			severidad: [null],
			cronico: [{value: null, disabled: true}, Validators.required],
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

		if (p.startDate) {
			this.dateToSet = stringToDate(this.dataDto.startDate)
			this.minDate = p.startDate
			this.maxDate = p.startDate
		} else {
			this.dateToSet = new Date();
		}

		this.form.controls.cronico.setValue(p.isChronic);
		this.form.controls.fechaInicio.setValue(this.dateToSet);
	}

	solveProblem() {
		this.markAsTouched = true;
		if (this.form.valid) {
			const dialogRefConfirmation = this.dialog.open(DiscardWarningComponent,
				{
					data: {
						title: 'ambulatoria.paciente.problemas.TITLE_POPUP_RESOLVER_PROBLEMA',
						content: `ambulatoria.paciente.problemas.CONFIRMACION_POPUP`,
						okButtonLabel: 'ambulatoria.paciente.problemas.BOTON_CONFIRMAR_POPUP',
						cancelButtonLabel: 'ambulatoria.paciente.problemas.BOTON_CANCEL_POPUP'
					}
				});

			dialogRefConfirmation.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.problema.inactivationDate = this.form.value.fechaFin;
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

	getForm(): UntypedFormGroup {
		return this.form;
	}

	checkInactivationDate() {
		const fechaFin = this.form.controls.fechaFin.value;
		const fechaInicio = this.form.controls.fechaInicio.value;

		if (fechaFin) {
			if (fechaInicio) {
				const inactivationDate = fechaFin;
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

	fechaInicioChanged(date: Date) {
		this.form.controls.fechaInicio.setValue(date);
	}

	fechaFinChanged(date: Date) {
		this.form.controls.fechaFin.setValue(date);
	}

	hasError(type: string, controlName: string): boolean {
		return hasError(this.form, type, controlName);
	}

}
