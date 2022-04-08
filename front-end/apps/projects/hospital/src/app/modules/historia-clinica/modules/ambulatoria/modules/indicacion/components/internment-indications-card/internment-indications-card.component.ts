import { Component, Input, OnInit } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";
import { getDay, getMonth, isTomorrow, isYesterday, isToday, differenceInCalendarDays, isSameDay } from "date-fns";
import { MONTHS_OF_YEAR, DAYS_OF_WEEK } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { DietDto } from "@api-rest/api-model";
import { DateTimeDto } from "@api-rest/api-model";
import { DietComponent } from '../../dialogs/diet/diet.component';
import { MatDialog } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationsFacadeService } from "@historia-clinica/modules/ambulatoria/modules/indicacion/services/indications-facade.service";
import { dateDtoToDate } from "@api-rest/mapper/date-dto.mapper";

const DIALOG_SIZE = '35%';

@Component({
	selector: 'app-internment-indications-card',
	templateUrl: './internment-indications-card.component.html',
	styleUrls: ['./internment-indications-card.component.scss']
})
export class InternmentIndicationsCardComponent implements OnInit {

	internmentIndication = INTERNMENT_INDICATIONS;
	viewDay: ViewDate;
	isToday = true;
	isYesterday = false;
	isTomorrow = false;
	actualDate: Date = new Date();
	entryDate: Date;
	currentViewIsEntryDate = false;
	dateTime: DateTimeDto;
	professionalId: number;
	diets: DietDto[] = [];

	@Input() internmentEpisodeId: number;
	@Input() epicrisisConfirmed: boolean;
	@Input() patientId: number;

	constructor(
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly indicationsFacadeService: IndicationsFacadeService,
		private readonly internmentEpisode: InternmentEpisodeService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService
	) { }

	ngOnInit(): void {
		this.viewDay = {
			nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
			numberDay: this.actualDate.getDate(),
			month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
		};
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => {
				this.entryDate = new Date(internmentEpisode.entryDate);
				const differenceInDays = differenceInCalendarDays(this.actualDate, this.entryDate);
				if (differenceInDays <= 0)
					this.currentViewIsEntryDate = true;
			}
		);
		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe((professionalId: number) => this.professionalId = professionalId);
		this.indicationsFacadeService.setInternmentEpisodeId(this.internmentEpisodeId);
		this.filterIndications();
	}

	viewAnotherDay(daysToMove: number) {
		this.actualDate.setDate(this.actualDate.getDate() + daysToMove);
		this.isToday = isToday(this.actualDate);
		this.isTomorrow = isTomorrow(this.actualDate);
		this.isYesterday = isYesterday(this.actualDate);
		this.loadDay();
	}

	private loadDay() {
		const differenceInDays = differenceInCalendarDays(this.actualDate, this.entryDate);
		if (differenceInDays >= 0) {
			this.currentViewIsEntryDate = false;
			this.viewDay = {
				nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
				numberDay: this.actualDate.getDate(),
				month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
			};
			this.filterIndications();
		}
		if (differenceInDays <= 0)
			this.currentViewIsEntryDate = true;
	}

	openDietDialog() {
		const dialogRef = this.dialog.open(DietComponent, {
			data: {
				entryDate: this.entryDate,
				actualDate: this.actualDate,
				patientId: this.patientId,
				professionalId: this.professionalId
			},
			disableClose: false,
			width: DIALOG_SIZE
		});

		dialogRef.afterClosed().subscribe((diet: DietDto) => {
			if (diet) {
				this.indicationsFacadeService.addDiet(diet).subscribe(_ => {
					this.snackBarService.showSuccess('indicacion.internment-card.dialogs.diet.messages.SUCCESS');
					this.indicationsFacadeService.updateIndication({ diets: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('indicacion.internment-card.dialogs.diet.messages.ERROR');
					}
				);
			}
		});
	}

	filterIndications() {
		this.indicationsFacadeService.diets$.subscribe(d => this.diets = d.filter((diet: DietDto) => isSameDay(dateDtoToDate(diet.indicationDate), this.actualDate) === true));
	}

}

interface ViewDate {
	nameDay: string,
	numberDay: number,
	month: string,
}
