import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { AntecedentesFamiliaresNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ConceptsList } from 'projects/hospital/src/app/modules/hsi-components/concepts-list/concepts-list.component';

@Component({
	selector: 'app-antecedentes-familiares',
	templateUrl: './antecedentes-familiares.component.html',
	styleUrls: ['./antecedentes-familiares.component.scss']
})

export class AntecedentesFamiliaresComponent{

	@Output() familyHistoriesChange = new EventEmitter();

	@Input() familyHistories: HealthHistoryConditionDto[] = [];

	@Output() isFamilyHistoriesNoRefer = new EventEmitter<boolean>();

	antecedentesFamiliaresNuevaConsultaService: AntecedentesFamiliaresNuevaConsultaService;

	familyHistoriesContent: ConceptsList = {
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

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
	) {
		this.antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);

	}

	addSnomedConcept(snomedConcept: SnomedDto) {
		if (snomedConcept) {
			const antecedenteFamiliar: HealthHistoryConditionDto = {
				startDate: null,
				note: null,
				snomed: snomedConcept
			};
			this.add(antecedenteFamiliar);
		}
	}


	add(af: HealthHistoryConditionDto): void {
		this.familyHistories = pushTo<HealthHistoryConditionDto>(this.familyHistories, af);
		this.familyHistoriesChange.next(this.familyHistories);
	}

	remove(index: number): void {
		this.familyHistories = removeFrom<HealthHistoryConditionDto>(this.familyHistories, index);
		this.familyHistoriesChange.next(this.familyHistories);
	}

	addFamilyHistories(): void {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.antecedentes-familiares.FAMILY_HISTORY',
			title: 'internaciones.anamnesis.antecedentes-familiares.ADD',
			eclFilter: SnomedECL.PERSONAL_RECORD
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept) {
				this.addSnomedConcept(snomedConcept)
				this.antecedentesFamiliaresNuevaConsultaService.add({snomed: snomedConcept, fecha: null});
			}
		});
	}

	checkFamilyHistoriesEvent = ($event) => {
		if ($event.addPressed) {
			this.addFamilyHistories();
		}
		this.isFamilyHistoriesNoRefer.emit(!$event.checkboxSelected);
	}

}
