import { Component, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationAddReasonFormComponent } from '@historia-clinica/dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { MotivoConsulta, MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-motivo-form',
	templateUrl: './motivo-form.component.html',
	styleUrls: ['./motivo-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => MotivoFormComponent),
			multi: true,
		}
	]
})
export class MotivoFormComponent implements ControlValueAccessor {


	formMotivo = this.formBuilder.group({
		motivo: new FormControl<MotivoConsulta[] | null>([]),
	});
	onChangeSub: Subscription;

	motivoNuevaConsultaService = new MotivoNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);
	searchConceptsLocallyFFIsOn = false;
	constructor(
		private formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});

		this.motivoNuevaConsultaService.motivosConsulta$.subscribe(
			motivos => this.formMotivo.controls.motivo.setValue(motivos)
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

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj) {
			this.formMotivo.setValue(obj);
			this.motivoNuevaConsultaService.setMotives(obj.motivo);
		}
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.formMotivo.valueChanges
			.subscribe(value => {
				const toEmit = this.formMotivo.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.formMotivo.disable() : this.formMotivo.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
