import {Component, Inject, OnInit} from '@angular/core';
import {HCEPersonalHistoryDto, HealthConditionNewConsultationDto, MasterDataInterface} from '@api-rest/api-model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SnackBarService} from '@presentation/services/snack-bar.service';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConfirmDialogComponent} from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import {HealthConditionService} from '@api-rest/services/healthcondition.service';
import {ProblemasService} from '../../services/problemas-nueva-consulta.service';
import {SnomedService} from '../../services/snomed.service';
import {HEALTH_CLINICAL_STATUS} from '../../modules/internacion/constants/ids';
import {OutpatientConsultationService} from '@api-rest/services/outpatient-consultation.service';
import {hasError} from '@core/utils/form.utils';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';
import {format} from "date-fns";
import {DateFormatter, MIN_DATE} from "@core/utils/date.utils";

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
	private readonly startDate: Date;
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
		this.today = this.toFormatDate(format( new Date(), DateFormatter.VIEW_DATE));
		this.startDate = this.dataDto.startDate ? this.toFormatDate(this.dataDto.startDate) : this.today;
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
		this.form.controls.cronico.setValue(p.isChronic);
		p.startDate ? this.form.controls.fechaInicio.setValue(p.startDate) : this.form.controls.fechaInicio.setValue(this.toFormatDate(format( new Date(), DateFormatter.VIEW_DATE)));
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

		if (fechaFin) {
			const inactivationDate = fechaFin.toDate();
			if (this.startDate > inactivationDate) {
				this.form.controls.fechaFin.setErrors({min: true});
			}
			const actualDate = new Date();
			if (inactivationDate > actualDate) {
				this.form.controls.fechaFin.setErrors({max: true});
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
