import { Component, Input, OnInit } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";
import { getDay, getMonth, isTomorrow, isYesterday, isToday, differenceInCalendarDays, getYear } from "date-fns";
import { MONTHS_OF_YEAR, DAYS_OF_WEEK } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { EIndicationStatus, EIndicationType } from "@api-rest/api-model";
import { DietDto } from "@api-rest/api-model";
import { DateTimeDto } from "@api-rest/api-model";
import { DietComponent } from '../../dialogs/diet/diet.component';
import { MatDialog } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { IndicationsFacadeService } from "@historia-clinica/modules/ambulatoria/modules/indicacion/services/indications-facade.service";

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
		}
		if (differenceInDays <= 0)
			this.currentViewIsEntryDate = true;
	}

	private dietIndications(submitted: string): DietDto {
		const today = new Date();
		return {
			id: 0,
			patientId: this.patientId,
			type: EIndicationType.DIET,
			status: EIndicationStatus.INDICATED,
			professionalId: this.professionalId,
			createdBy: null,
			indicationDate: {
				year: getYear(this.actualDate),
				month: getMonth(this.actualDate) + 1,
				day: this.actualDate.getDate()
			},
			createdOn: null,
			description: submitted
		}
	}

	openDietDialog() {
		const dialogRef = this.dialog.open(DietComponent, {
			disableClose: false,
			width: DIALOG_SIZE
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.indicationsFacadeService.addDiet(this.dietIndications(submitted)).subscribe(_ => {
					this.snackBarService.showSuccess('ambulatoria.dialogs.diet.messages.SUCCESS');
					this.indicationsFacadeService.updateIndication({ diets: true });
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('ambulatoria.dialogs.diet.messages.ERROR');
					}
				);
			}
		});
	}

}

interface ViewDate {
	nameDay: string,
	numberDay: number,
	month: string,
}
