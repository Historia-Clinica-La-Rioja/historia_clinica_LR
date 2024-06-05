import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationAddReasonFormComponent } from '@historia-clinica/dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { MotivoConsulta, MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-reasons-form',
	templateUrl: './reasons-form.component.html',
	styleUrls: ['./reasons-form.component.scss']
})
export class ReasonsFormComponent {

	motivoNuevaConsultaService = new MotivoNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);
	searchConceptsLocallyFFIsOn = false;
	form = new FormGroup<ReasonsForm>({
		reasons: new FormControl<MotivoConsulta[]>([]),
	});

	@Input() set reasons(reasons: MotivoConsulta[]) {
		if (reasons.length) {
			this.form.controls.reasons.setValue(reasons);
			this.motivoNuevaConsultaService.setMotives(reasons);
		}
	}
	@Output() selectedReasons = new EventEmitter<MotivoConsulta[]>;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService,
	) {

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});

		this.motivoNuevaConsultaService.motivosConsulta$.subscribe(
			motivos => this.form.controls.reasons.setValue(motivos)
		);

		this.form.controls.reasons.valueChanges.subscribe(reasons => this.selectedReasons.emit(reasons));
	}

	addReason() {
		this.dialog.open(NewConsultationAddReasonFormComponent, {
			data: {
				reasonService: this.motivoNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

}

interface ReasonsForm {
	reasons: FormControl<MotivoConsulta[]>,
}