import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { EAnthropometricGraphicOption } from '@api-rest/api-model';
import { AnthropometricGraphicService } from '@api-rest/services/anthropometric-graphic.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-evolution-chart-select',
	templateUrl: './evolution-chart-select.component.html',
	styleUrls: ['./evolution-chart-select.component.scss']
})
export class EvolutionChartSelectComponent implements OnInit {

	form: FormGroup<EvolutionChartSelectForm>;
	evolutionCharts$: Observable<EAnthropometricGraphicOption[]>;
	@Input() set patientId(patientId: number) {
		if (patientId)
			this.setChartOptions(patientId);
	};
	@Output() selectedChart = new EventEmitter<EAnthropometricGraphicOption>();

	constructor(
		private readonly percentilesService: AnthropometricGraphicService,
	) { }

	ngOnInit() {
		this.createForm();
		this.subscribeToFormChangesAndEmit();
	}

	private createForm() {
		this.form = new FormGroup<EvolutionChartSelectForm>({
			chartOption: new FormControl(null),
		});
	}

	private setChartOptions(patientId: number) {
		this.evolutionCharts$ = this.percentilesService.getChartOptions(patientId);
	}

	private subscribeToFormChangesAndEmit() {
		this.form.controls.chartOption.valueChanges.subscribe(chartValue => this.selectedChart.emit(chartValue));
	}

}

interface EvolutionChartSelectForm {
	chartOption: FormControl<EAnthropometricGraphicOption>;
}
