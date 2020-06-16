import { Component, Input, OnInit } from '@angular/core';
import { SummaryHeader } from 'src/app/modules/presentation/components/summary-card/summary-card.component';
import { SIGNOS_VITALES } from '../../constants/summaries';
import { VitalSingCurrentPrevious } from 'src/app/modules/presentation/components/signo-vital-current-previous/signo-vital-current-previous.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { EvolutionNoteDto, Last2VitalSignsDto, VitalSignDto } from '@api-rest/api-model';
import { momentParseDate } from '@core/utils/moment.utils';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MatDialog } from '@angular/material/dialog';
import { AddVitalSignsComponent } from '../../dialogs/add-vital-signs/add-vital-signs.component';

@Component({
	selector: 'app-signos-vitales-summary',
	templateUrl: './signos-vitales-summary.component.html',
	styleUrls: ['./signos-vitales-summary.component.scss']
})
export class SignosVitalesSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable: boolean = false;

	signosVitalesSummary: SummaryHeader = SIGNOS_VITALES;
	signosVitales: VitalSingCurrentPrevious[] = [];

	constructor(
		private internmentStateService: InternmentStateService,
		private evolutionNoteService: EvolutionNoteService,
		private snackBarService: SnackBarService,
		public dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.internmentStateService.getVitalSigns(this.internmentEpisodeId).subscribe(
			this.initSignosVitales(), this.initSignosVitales()
		);
	}

	initSignosVitales(): (vitalSigns: Last2VitalSignsDto) => void {
		const LABELS = {
			systolicBloodPressure: 'Tensión arterial sistólica',
			diastolicBloodPressure: 'Tensión arterial diastólica',
			heartRate: 'Frecuencia cardíaca',
			respiratoryRate: 'Frecuencia respiratoria',
			temperature: 'Temperatura',
			bloodOxygenSaturation: 'Saturación de oxígeno',
		};
		return (vitalSigns: Last2VitalSignsDto) => {
			this.signosVitales = [];
			let current: VitalSignDto = vitalSigns.current || {};
			let previous: VitalSignDto = vitalSigns.previous || {};
			Object.keys(LABELS).forEach(key => this.signosVitales.push(
				{
					description: LABELS[key],
					currentValue: {
						value: Number(current[key]?.value),
						effectiveTime: current[key]?.effectiveTime ? momentParseDate(current[key].effectiveTime) : undefined
					},
					previousValue: {
						value: Number(previous[key]?.value),
						effectiveTime: previous[key]?.effectiveTime ? momentParseDate(previous[key].effectiveTime) : undefined
					}
				}
			));
		}
	}


	openDialog() {
		const dialogRef = this.dialog.open(AddVitalSignsComponent, {
			disableClose: true,
			width: '35%',
		});

		dialogRef.afterClosed().subscribe(vitalSigns => {
			if (vitalSigns) {
				const evolutionNote = this.buildEvolutionNote(vitalSigns);
				this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId).subscribe(_ => {
						this.snackBarService.showSuccess('internaciones.internacion-paciente.vital-signs-summary.save.SUCCESS');
						this.refreshVitalSigns();
					}, _ => this.snackBarService.showError('internaciones.internacion-paciente.vital-signs-summary.save.ERROR')
				);
			}
		});
	}

	private buildEvolutionNote(vitalSigns): EvolutionNoteDto {
		const vitalSignsDto = isNull(vitalSigns) ? undefined : {
			bloodOxygenSaturation: getEffectiveValue(vitalSigns.bloodOxygenSaturation),
			diastolicBloodPressure: getEffectiveValue(vitalSigns.diastolicBloodPressure),
			heartRate: getEffectiveValue(vitalSigns.heartRate),
			respiratoryRate: getEffectiveValue(vitalSigns.respiratoryRate),
			systolicBloodPressure: getEffectiveValue(vitalSigns.systolicBloodPressure),
			temperature: getEffectiveValue(vitalSigns.temperature)
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every(el => el === null);
		}

		function getEffectiveValue(controlValue: any) {
			return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
		}

		return {
			confirmed: true,
			vitalSigns: vitalSignsDto
		};
	}

	private refreshVitalSigns(): void {
		this.internmentStateService.getVitalSigns(this.internmentEpisodeId).subscribe(
			this.initSignosVitales(), this.initSignosVitales()
		);
	}
}
