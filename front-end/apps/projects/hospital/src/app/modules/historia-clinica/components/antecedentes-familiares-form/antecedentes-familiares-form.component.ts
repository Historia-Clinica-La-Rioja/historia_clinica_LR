import { Component, forwardRef, EventEmitter, Output, Input } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationFamilyHistoryFormComponent } from '@historia-clinica/modules/ambulatoria/dialogs/new-consultation-family-history-form/new-consultation-family-history-form.component';
import { AntecedentesFamiliaresNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subscription } from 'rxjs';
import { ConceptsList } from '../../../hsi-components/concepts-list/concepts-list.component';

@Component({
	selector: 'app-antecedentes-familiares-form',
	templateUrl: './antecedentes-familiares-form.component.html',
	styleUrls: ['./antecedentes-familiares-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => AntecedentesFamiliaresFormComponent),
			multi: true,
		}
	]
})
export class AntecedentesFamiliaresFormComponent implements ControlValueAccessor {


	antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);
	antecedentesFamiliares = this.formBuilder.group({
		data: []
	});
	searchConceptsLocallyFFIsOn = false;
	onChangeSub: Subscription;
	familyHistoriesContent: ConceptsList = {
		id: 'family-histories-checkbox-concepts-list',
		header: {
			text: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.TITLE',
			icon: 'report'
		},
		titleList: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.LIST_CARD_TITLE',
		actions: {
			button: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.buttons.ADD',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}
	@Input() isFamilyHistoryNoRefer: boolean;
	@Output() isFamilyHistoriesNoRefer = new EventEmitter<boolean>();

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});

		this.antecedentesFamiliaresNuevaConsultaService.data$.subscribe(antecedentesFamiliares =>
			this.antecedentesFamiliares.controls.data.setValue(antecedentesFamiliares)
		);

	}

	addFamilyHistory(): void {
		this.dialog.open(NewConsultationFamilyHistoryFormComponent, {
			data: {
				familyHistoryService: this.antecedentesFamiliaresNuevaConsultaService,
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
			this.antecedentesFamiliares.setValue(obj);
			this.antecedentesFamiliaresNuevaConsultaService.setFamilyHistories(obj.data);
		}
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.antecedentesFamiliares.valueChanges
			.subscribe(value => {
				const toEmit = this.antecedentesFamiliares.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.antecedentesFamiliares.disable() : this.antecedentesFamiliares.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

	checkFamilyHistoriesEvent = ($event) => {
		if ($event.addPressed) {
			this.addFamilyHistory();
		}
		this.isFamilyHistoriesNoRefer.emit(!$event.checkboxSelected);
	}

}
