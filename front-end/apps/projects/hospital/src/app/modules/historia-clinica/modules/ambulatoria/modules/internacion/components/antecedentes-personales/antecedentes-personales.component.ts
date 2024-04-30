import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { HealthHistoryConditionDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { NewConsultationPersonalHistoriesService } from '@historia-clinica/modules/ambulatoria/services/new-consultation-personal-histories.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ConceptsList } from 'projects/hospital/src/app/modules/hsi-components/concepts-list/concepts-list.component';

@Component({
	selector: 'app-antecedentes-personales',
	templateUrl: './antecedentes-personales.component.html',
	styleUrls: ['./antecedentes-personales.component.scss']
})
export class AntecedentesPersonalesComponent {


	@Output() personalHistoriesChange = new EventEmitter();
	@Input() personalHistories: HealthHistoryConditionDto[] = [];
	@Output() isPersonalHistoriesNoRefer = new EventEmitter<boolean>();
	searchConceptsLocallyFFIsOn: boolean = false;
	personalHistoriesNewConsultationService: NewConsultationPersonalHistoriesService;
	personalHistoriesContent: ConceptsList = {
		header: {
			text: 'ambulatoria.paciente.nueva-consulta.antecedentes-personales.TITLE',
			icon: 'report'
		},
		titleList: 'ambulatoria.paciente.nueva-consulta.antecedentes-personales.REGISTERED_PERSONAL_HISTORIES',
		actions: {
			button: 'ambulatoria.paciente.nueva-consulta.antecedentes-personales.buttons.ADD',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}

	constructor(
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
	) {
		this.personalHistoriesNewConsultationService = new NewConsultationPersonalHistoriesService(this.snomedService, this.snackBarService);
	}

	addSnomedConcept(snomedConcept: SnomedDto) {
		if (snomedConcept) {
			const antecedentePersonal: HealthHistoryConditionDto = {
				startDate: null,
				note: null,
				snomed: snomedConcept
			};
			this.add(antecedentePersonal);
		}
	}

	add(ap: HealthHistoryConditionDto): void {
		this.personalHistories = pushTo<HealthHistoryConditionDto>(this.personalHistories, ap);
		this.personalHistoriesChange.next(this.personalHistories);
	}

	remove(index: number): void {
		this.personalHistories = removeFrom<HealthHistoryConditionDto>(this.personalHistories, index);
		this.personalHistoriesChange.next(this.personalHistories);
	}

	addPersonalHistory(): void {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.antecedentes-personales.PERSONAL_HISTORY',
			title: 'internaciones.anamnesis.antecedentes-personales.ADD',
			eclFilter: SnomedECL.FAMILY_RECORD
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			this.addSnomedConcept(snomedConcept);
			this.personalHistoriesNewConsultationService.add({snomed: snomedConcept, type: snomedConcept.type, startDate: null});
		})
	}

	checkPersonalHistoriesEvent = ($event) => {
		if ($event.addPressed) {
			this.addPersonalHistory();
		}
		this.isPersonalHistoriesNoRefer.emit(!$event.checkboxSelected);
	}

}
