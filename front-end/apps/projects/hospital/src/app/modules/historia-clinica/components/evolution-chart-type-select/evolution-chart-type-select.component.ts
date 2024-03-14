import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { EAnthropometricGraphicOption, EAnthropometricGraphicType } from '@api-rest/api-model';
import { AnthropometricGraphicService } from '@api-rest/services/anthropometric-graphic.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-evolution-chart-type-select',
	templateUrl: './evolution-chart-type-select.component.html',
	styleUrls: ['./evolution-chart-type-select.component.scss']
})
export class EvolutionChartTypeSelectComponent implements OnInit {

	evolutionChartTypes$: Observable<EAnthropometricGraphicType[]>;
	form: FormGroup<EvolutionChartTypeSelectForm>;

	@Input() patientId: number;
	@Input()
	set selectedChart(chart: EAnthropometricGraphicOption) {
		if (chart)
			this.setEvolutionChartTypesOptions(chart);
	}
	@Output() selectedTypeChart = new EventEmitter<EAnthropometricGraphicType>();

	constructor(
		private readonly anthropometricGraphicService: AnthropometricGraphicService,
	) { }

	ngOnInit() {
		this.createForm();
		this.subscribeToFormChangesAndEmit();
	}

	private setEvolutionChartTypesOptions(selectedChart: EAnthropometricGraphicOption) {
		this.evolutionChartTypes$ = this.anthropometricGraphicService.getAvailableGraphicTypes(selectedChart.id, this.patientId);
	}

	private createForm() {
		this.form = new FormGroup<EvolutionChartTypeSelectForm>({
			type: new FormControl(null),
		});
	}

	private subscribeToFormChangesAndEmit() {
		this.form.controls.type.valueChanges.subscribe(chartTypeValue => this.selectedTypeChart.emit(chartTypeValue));
	}

}

interface EvolutionChartTypeSelectForm {
	type: FormControl<EAnthropometricGraphicType>;
}
