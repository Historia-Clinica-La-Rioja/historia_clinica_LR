import { Component, EventEmitter, Input, Output } from '@angular/core';
import { fixDate } from '@core/utils/date/format';
import { AntecedenteFamiliar } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares-nueva-consulta.service';

@Component({
	selector: 'app-background-list',
	templateUrl: './background-list.component.html',
	styleUrls: ['./background-list.component.scss']
})
export class BackgroundListComponent {
	antecedentes;
	@Output() remove = new EventEmitter;
	@Input() set familyHistory(ant: AntecedenteFamiliar[]) {
		this.antecedentes = ant.map(
			a => {
				return {
					...a,
					date: fixDate(a.fecha)
				}
			}
		)
	};

	onClick(index) {
		this.remove.emit(index);
	}
}
