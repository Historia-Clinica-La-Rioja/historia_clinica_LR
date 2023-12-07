import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationAddReasonFormComponent } from '@historia-clinica/dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { MotivoConsulta, MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
@Component({
	selector: 'app-button-and-motive-list',
	templateUrl: './button-and-motive-list.component.html',
	styleUrls: ['./button-and-motive-list.component.scss']
})
export class ButtonAndMotiveListComponent implements OnInit {
	@Output() selectionChange = new EventEmitter();
	@Input() limit: number;
	searchConceptsLocallyFFIsOn: boolean;
	motives: MotivoConsulta[];
	motivoNuevaConsultaService = new MotivoNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);

	constructor(private readonly snomedService: SnomedService, private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService, private readonly featureFlagService: FeatureFlagService,
		private readonly formBuilder: UntypedFormBuilder) {
	}

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
		this.motivoNuevaConsultaService.motivosConsulta$.subscribe(
			motives => {
				this.motives = motives
				this.selectionChange.emit(this.motives);
			}
		)
	}

	addReason(): void {
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
