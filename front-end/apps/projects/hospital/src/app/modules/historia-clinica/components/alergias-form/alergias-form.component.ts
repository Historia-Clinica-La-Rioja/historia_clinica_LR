import { Component, forwardRef, EventEmitter, Output } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { NewConsultationAllergyFormComponent } from '@historia-clinica/dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subscription } from 'rxjs';
import { ConceptsList } from '../../../hsi-components/concepts-list/concepts-list.component';

@Component({
	selector: 'app-alergias-form',
	templateUrl: './alergias-form.component.html',
	styleUrls: ['./alergias-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => AlergiasFormComponent),
			multi: true,
		}
	]
})
export class AlergiasFormComponent implements ControlValueAccessor {


	alergias = this.formBuilder.group({ data: [] });
	onChangeSub: Subscription;
	searchConceptsLocallyFFIsOn = false;

	allergyContent: ConceptsList = {
		id: 'allergy-checkbox-concepts-list',
		header: {
			text: 'ambulatoria.paciente.nueva-consulta.alergias.TITLE',
			icon: 'cancel'
		},
		titleList: 'ambulatoria.paciente.nueva-consulta.alergias.table.TITLE',
		actions: {
			button: 'ambulatoria.paciente.nueva-consulta.alergias.ADD',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}
	@Output() isAllergyNoRefer = new EventEmitter<boolean>();


	alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService, this.internacionMasterDataService);

	constructor(private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly internacionMasterDataService: InternacionMasterDataService,
	) {

		this.alergiasNuevaConsultaService.alergias$.subscribe(alergias => this.alergias.controls.data.setValue(alergias));
	}

	addAllergy(): void {
		this.dialog.open(NewConsultationAllergyFormComponent, {
			data: {
				allergyService: this.alergiasNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	checkAllergyEvent($event) {
		if ($event.addPressed) {
			this.addAllergy();
		}
		this.isAllergyNoRefer.emit(!$event.checkboxSelected);
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj) {
			this.alergias.setValue(obj);
			this.alergiasNuevaConsultaService.setAllergies(obj.data);
		}
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.alergias.valueChanges
			.subscribe(value => {
				const toEmit = this.alergias.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.alergias.disable() : this.alergias.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}


}
