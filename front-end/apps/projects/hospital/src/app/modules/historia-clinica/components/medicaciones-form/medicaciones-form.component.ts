import { Component, OnInit, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationMedicationFormComponent } from '@historia-clinica/dialogs/new-consultation-medication-form/new-consultation-medication-form.component';
import { MedicacionesNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-medicaciones-form',
	templateUrl: './medicaciones-form.component.html',
	styleUrls: ['./medicaciones-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => MedicacionesFormComponent),
			multi: true,
		}
	]
})
export class MedicacionesFormComponent implements OnInit, ControlValueAccessor {

	searchConceptsLocallyFFIsOn = false;
	onChangeSub: Subscription;
	medicaciones = this.formBuilder.group({
		data: []
	})

	medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);
	constructor(
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});

		this.medicacionesNuevaConsultaService.medicaciones$.subscribe(medicaciones => this.medicaciones.controls.data.setValue(medicaciones));
	}

	addMedication(): void {
		this.dialog.open(NewConsultationMedicationFormComponent, {
			data: {
				medicationService: this.medicacionesNuevaConsultaService,
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
			this.medicaciones.setValue(obj);
			this.medicacionesNuevaConsultaService.setMedications(obj.data);
		}
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.medicaciones.valueChanges
			.subscribe(value => {
				const toEmit = this.medicaciones.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.medicaciones.disable() : this.medicaciones.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
