import { Component, OnInit, SimpleChange, SimpleChanges } from '@angular/core';
import { SnomedDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { TableModel } from '@core/components/table/table.component';

@Component({
	selector: 'app-antecentes-personales',
	templateUrl: './antecentes-personales.component.html',
	styleUrls: ['./antecentes-personales.component.scss']
})
export class AntecentesPersonalesComponent implements OnInit {

	current: HealthHistoryConditionDto = this.initDTO();
	antecedentesPersonales: HealthHistoryConditionDto[] = [];
	antecedentesPersonalesTable: TableModel<HealthHistoryConditionDto> = {
		columns: [
			{
				columnDef: 'problemType',
				header: 'internaciones.anamnesis.antecedentes-personales.table.columns.PROBLEM_TYPE',
				text: ap => ap.snomed.fsn
			},
			{
				columnDef: 'date',
				header: 'internaciones.anamnesis.antecedentes-personales.table.columns.REGISTRY_DATE',
				text: ap => ap.date
			},
			{
				columnDef: 'delete',
				action: {
					isDelete: true,
					do: ap => this.remove(ap)
				}
			}
		],
		data: this.antecedentesPersonales
	}

	constructor() { }

	ngOnInit(): void { }

	setConcept(selectedConcept: SnomedDto): void {
		this.current.snomed = selectedConcept;
	}

	add(): void {
		this.antecedentesPersonales.push(this.current);
		this.current = this.initDTO();
	}

	private remove(ap: HealthHistoryConditionDto): void {
		// TODO ver referencias de memoria
		this.antecedentesPersonales = this.antecedentesPersonales.filter(
			_ap => _ap !== ap
		);
	}

	// TODO
	// Revisar si se puede evitar esto
	private initDTO(): HealthHistoryConditionDto {
		return {
			note: '',
			date: '',
			verificationId: '',
			statusId: '',
			snomed: {
				id: '',
				fsn: '',
				parentFsn: '',
				parentId: '',
			}
		};
	}

}
