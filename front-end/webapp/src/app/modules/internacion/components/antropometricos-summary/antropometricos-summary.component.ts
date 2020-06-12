import { Component, Input, OnInit } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from 'src/app/modules/presentation/components/detail-box/detail-box.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { AnthropometricDataDto, EvolutionNoteDto } from '@api-rest/api-model';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MatDialog } from '@angular/material/dialog';
import { AddAnthropometricComponent } from '../../dialogs/add-anthropometric/add-anthropometric.component';

@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit {

	@Input()
	internmentEpisodeId: number;
	antropometricosSummary = ANTROPOMETRICOS;

	details: DetailBox[] = [];

	private readonly LABELS = {
		bloodType: 'Grupo sanguíneo',
		height: 'Talla (cm)',
		weight: 'Peso (kg)',
		bmi: 'IMC',
	};

	constructor(
		private internmentStateService: InternmentStateService,
		private evolutionNoteService: EvolutionNoteService,
		private snackBarService: SnackBarService,
		public dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.updateAnthropometricData();
	}

	private updateAnthropometricData() {
		this.internmentStateService.getAnthropometricData(this.internmentEpisodeId).subscribe(
			(anthropometricData: AnthropometricDataDto) => {
				if (anthropometricData) {
					this.details = [];
					Object.keys(anthropometricData).forEach(key => this.details.push(
						{
							description: this.LABELS[key],
							value: anthropometricData[key]?.value
						}
					));
				}
			}
		);
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddAnthropometricComponent, {
			disableClose: true,
			width: '25%',
		});

		dialogRef.afterClosed().subscribe((antropometricData: any) => {
				if (antropometricData) {
					const evolutionNote: EvolutionNoteDto = buildEvolutionNote(antropometricData);
					this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId).subscribe(_ => {
							this.snackBarService.showSuccess('internaciones.internacion-paciente.anthropometric-summary.save.SUCCESS');
							this.updateAnthropometricData();
						}, _ => this.snackBarService.showError('internaciones.internacion-paciente.anthropometric-summary.save.ERROR')
					);
				}
			}
		);

		function buildEvolutionNote(anthropometricData: any): EvolutionNoteDto {
			let anthropometricDataDto: AnthropometricDataDto;
			anthropometricDataDto = isNull(anthropometricData) ? undefined : {
				bloodType: anthropometricData.bloodType ? {
					id: anthropometricData.bloodType.id,
					value: anthropometricData.bloodType.description
				} : undefined,
				height: getValue(anthropometricData.height),
				weight: getValue(anthropometricData.weight),
			};

			function isNull(formGroupValues: any): boolean {
				return Object.values(formGroupValues).every(el => el === null);
			}

			function getValue(controlValue: any) {
				return controlValue ? {value: controlValue} : undefined;
			}

			return {
				confirmed: true,
				anthropometricData: anthropometricDataDto
			};
		}
	}
}
