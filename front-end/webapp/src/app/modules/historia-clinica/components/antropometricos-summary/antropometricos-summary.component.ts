import { Component, Input, OnInit } from '@angular/core';
import { ANTROPOMETRICOS } from '../../constants/summaries';
import { DetailBox } from '@presentation/components/detail-box/detail-box.component';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { AnthropometricDataDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { AddAnthropometricComponent } from '../../dialogs/add-anthropometric/add-anthropometric.component';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-antropometricos-summary',
	templateUrl: './antropometricos-summary.component.html',
	styleUrls: ['./antropometricos-summary.component.scss']
})
export class AntropometricosSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() anthropometricData$: Observable<AnthropometricDataDto>;
	@Input() editable = false;

	antropometricosSummary = ANTROPOMETRICOS;

	details: DetailBox[] = [];

	private readonly LABELS = {
		bloodType: 'Grupo sanguíneo',
		height: 'Talla (cm)',
		weight: 'Peso (kg)',
		bmi: 'IMC',
	};

	constructor(
		private readonly internmentStateService: InternmentStateService,
		public dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.updateAnthropometricData();
	}

	private updateAnthropometricData() {
		this.anthropometricData$.subscribe(
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
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.updateAnthropometricData();
			}
		}
		);
	}
}
