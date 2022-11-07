import { Component, Input, OnInit } from '@angular/core';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { FACTORES_DE_RIESGO } from '../../constants/summaries';
import { RiskFactorCurrentPrevious } from '@presentation/components/factor-de-riesgo-current-previous/factor-de-riesgo-current-previous.component';
import { Last2RiskFactorsDto, RiskFactorDto } from '@api-rest/api-model';
import { momentParseDateTime } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { AddRiskFactorsComponent } from '../../dialogs/add-risk-factors/add-risk-factors.component';
import { Observable } from 'rxjs';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-factores-de-riesgo-summary',
	templateUrl: './factores-de-riesgo-summary.component.html',
	styleUrls: ['./factores-de-riesgo-summary.component.scss']
})
export class FactoresDeRiesgoSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable = false;
	@Input() riskFactors$: Observable<Last2RiskFactorsDto>;

	factoresDeRiesgoSummary: SummaryHeader = FACTORES_DE_RIESGO;
	factoresDeRiesgo: RiskFactorCurrentPrevious[] = [];

	constructor(
		public dialog: MatDialog,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) { }

	ngOnInit(): void {
		this.refreshRiskFactors();
	}

	refreshRiskFactors(): void {
		this.riskFactors$.subscribe(
			this.initFactoresDeRiesgo(), this.initFactoresDeRiesgo()
		);
	}

	initFactoresDeRiesgo(): (riskFactors: Last2RiskFactorsDto) => void {
		const LABELS = {
			systolicBloodPressure: 'Tensión arterial sistólica',
			diastolicBloodPressure: 'Tensión arterial diastólica',
			heartRate: 'Frecuencia cardíaca',
			respiratoryRate: 'Frecuencia respiratoria',
			temperature: 'Temperatura',
			bloodOxygenSaturation: 'Saturación de oxígeno',
			bloodGlucose: 'Glucemia (mg/dl)',
			glycosylatedHemoglobin: 'Hemoglobina glicosilada (%)',
			cardiovascularRisk: 'Riesgo cardiovascular (%)'
		};
		return (riskFactors: Last2RiskFactorsDto) => {
			this.factoresDeRiesgo = [];
			const current: RiskFactorDto = riskFactors.current || {};
			const previous: RiskFactorDto = riskFactors.previous || {};
			Object.keys(LABELS).forEach(key => this.factoresDeRiesgo.push(
				{
					description: LABELS[key],
					currentValue: {
						value: Number(current[key]?.value),
						effectiveTime: current[key]?.effectiveTime ? momentParseDateTime(current[key].effectiveTime) : undefined
					},
					previousValue: {
						value: Number(previous[key]?.value),
						effectiveTime: previous[key]?.effectiveTime ? momentParseDateTime(previous[key].effectiveTime) : undefined
					}
				}
			));
		};
	}


	openDialog() {
		const dialogRef = this.dialog.open(AddRiskFactorsComponent, {
			disableClose: true,
			width: '35%',
			height: '90%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.refreshRiskFactors();
				this.internmentSummaryFacadeService.setFieldsToUpdate({ riskFactors: true, evolutionClinical: true });
			}
		});
	}
}
