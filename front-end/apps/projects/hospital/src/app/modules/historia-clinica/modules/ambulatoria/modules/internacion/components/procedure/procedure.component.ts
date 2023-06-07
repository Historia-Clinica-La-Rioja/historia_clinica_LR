import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { HospitalizationProcedureDto, SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { Concept, ConceptDateFormComponent } from '../../dialogs/concept-date-form/concept-date-form.component';

@Component({
	selector: 'app-procedure',
	templateUrl: './procedure.component.html',
	styleUrls: ['./procedure.component.scss']
})
export class ProcedureComponent {
	@Output() proceduresChange = new EventEmitter();
	@Input() procedures: HospitalizationProcedureDto[] = [];
	@Input() hideSuspended: boolean;

	displayedColumns: string[] = [];

	constructor(
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly dialog: MatDialog,
	) { }

	addSnomedConcept(procedure: Concept) {
		if (procedure) {
			const hospitalizationProcedureDto: HospitalizationProcedureDto = {
				performedDate: procedure?.data,
				snomed: procedure.snomedConcept
			};
			this.add(hospitalizationProcedureDto);
		}
	}

	add(hospitalizationProcedure: HospitalizationProcedureDto) {
		this.procedures = pushTo<HospitalizationProcedureDto>(this.procedures, hospitalizationProcedure);
		this.componentEvaluationManagerService.hospitalizationProcedures = this.procedures;
		this.proceduresChange.emit(this.procedures);
	}

	remove(index: number) {
		this.procedures = removeFrom<HospitalizationProcedureDto>(this.procedures, index);
		this.componentEvaluationManagerService.hospitalizationProcedures = this.procedures;
		this.proceduresChange.emit(this.procedures);
	}

	addProcedure() {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label:  'internaciones.anamnesis.procedimientos.PROCEDIMIENTO',
			title: 'internaciones.anamnesis.procedure.ADD_PROCEDURE',
			eclFilter: SnomedECL.PROCEDURE
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept) {
				const dialog = new MatDialogConfig();
				dialog.width = '35%';
				dialogConfig.disableClose = false;
				dialog.data = {
					label:  'internaciones.anamnesis.procedimientos.PROCEDIMIENTO',
					add: 'internaciones.anamnesis.procedure.ADD_PROCEDURE',
					title: 'internaciones.anamnesis.procedimientos.PROCEDIMIENTO',
					snomedConcept: snomedConcept
				};
				const dialogRef = this.dialog.open(ConceptDateFormComponent, dialog);
				dialogRef.afterClosed().subscribe((procedure: Concept) => {
					if (procedure)
						this.addSnomedConcept(procedure);
				});
			}
		});
	}
}
