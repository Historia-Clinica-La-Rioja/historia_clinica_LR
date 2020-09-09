import {Component, Inject, OnInit} from '@angular/core';
import {HCEPersonalHistoryDto, HealthConditionNewConsultationDto, MasterDataInterface} from '@api-rest/api-model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SnackBarService} from '@presentation/services/snack-bar.service';
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "@core/dialogs/confirm-dialog/confirm-dialog.component";
import {HealthConditionService} from "@api-rest/services/healthcondition.service";
import {ProblemasNuevaConsultaService} from "../../modules/ambulatoria/services/problemas-nueva-consulta.service";
import {SnomedService} from "../../services/snomed.service";
import {HEALTH_CLINICAL_STATUS} from "../../modules/internacion/constants/ids";
import {OutpatientConsultationService} from "@api-rest/services/outpatient-consultation.service";

@Component({
	selector: 'app-solve-problem',
	templateUrl: './solve-problem.component.html',
	styleUrls: ['./solve-problem.component.scss']
})
export class SolveProblemComponent implements OnInit {

	removeForm: FormGroup;
	clinicalStatus: MasterDataInterface<string>[];
    problemasNuevaConsultaService: ProblemasNuevaConsultaService;
	private readonly form: FormGroup;
	private problema: HealthConditionNewConsultationDto;
	private dataDto:HCEPersonalHistoryDto;
	private readonly patientId:number;

	constructor(
		@Inject(MAT_DIALOG_DATA) data,
		private snackBarService: SnackBarService,
		private formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly healthConditionService: HealthConditionService,
		private readonly snomedService: SnomedService,
		private readonly outpatientConsultationService: OutpatientConsultationService
	) {
		this.problemasNuevaConsultaService = new ProblemasNuevaConsultaService(formBuilder, snomedService);
		this.dataDto = data.problema;
		this.patientId = data.patientId;
		this.form = this.formBuilder.group({
			cronico: [null, Validators.required],
			fechaInicio: [null, Validators.required],
			fechaFin: [null]
		});
	}

	ngOnInit(): void {
		this.healthConditionService.getHealthCondition(this.dataDto.id).subscribe(p => {
			this.problema = p;
			this.form.controls.cronico.setValue(p.isChronic);
			this.form.controls.fechaInicio.setValue(new Date(p.startDate).toLocaleDateString());
			const inactivationDate = p.inactivationDate;
			if(inactivationDate) {
				this.form.controls.fechaFin.setValue(new Date(p.inactivationDate).toLocaleDateString());
			}
		});

	}


	solveProblem() {
		if(this.form.valid) {
			const dialogRef = this.dialog.open(ConfirmDialogComponent,
				{
					data: {
						title: 'ambulatoria.paciente.problemas.TITLE_POPUP_RESOLVER_PROBLEMA',
						content: `ambulatoria.paciente.problemas.CONFIRMACION_POPUP`,
						okButtonLabel: 'ambulatoria.paciente.problemas.BOTON_CONFIRMAR_POPUP'
					}
				});

			dialogRef.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.problema.inactivationDate = this.form.value.inactivationDate;
					this.problema.statusId = HEALTH_CLINICAL_STATUS.RESUELTO;
					this.outpatientConsultationService.solveProblem(this.problema, this.patientId).subscribe(
						_ => {
							this.snackBarService.showSuccess('ambulatoria.paciente.problemas.SUCCESS_CONFIRMAR_POPUP')
						},
						_ => {
							this.snackBarService.showError('ambulatoria.paciente.problemas.ERROR_CONFIRMAR_POPUP')
						}
					);
				}
			});
		}
	}

	getForm(): FormGroup{
		return this.form;
	}
}
