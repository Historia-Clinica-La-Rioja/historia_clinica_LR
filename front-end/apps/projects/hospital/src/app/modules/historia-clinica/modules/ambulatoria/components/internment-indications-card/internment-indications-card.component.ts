import { Component, Input, OnInit } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";
import { getDay, getMonth, isTomorrow, isYesterday, isToday, differenceInCalendarDays } from "date-fns";
import { MONTHS_OF_YEAR, DAYS_OF_WEEK } from "@historia-clinica/modules/ambulatoria/constants/internment-indications";
import { BehaviorSubject } from "rxjs";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";

@Component({
	selector: 'app-internment-indications-card',
	templateUrl: './internment-indications-card.component.html',
	styleUrls: ['./internment-indications-card.component.scss']
})
export class InternmentIndicationsCardComponent implements OnInit {

	internmentIndication = INTERNMENT_INDICATIONS;
	viewDay$: BehaviorSubject<ViewDate>;
	isToday = true;
	isYesterday = false;
	isTomorrow = false;
	actualDate: Date = new Date();
	entryDate: Date;
	currentViewIsEntryDate = false;
	@Input() internmentEpisodeId: number;
	@Input() epicrisisConfirmed: boolean;

	constructor(
		private readonly internmentEpisode: InternmentEpisodeService,
	) { }

	ngOnInit(): void {
		this.viewDay$ = new BehaviorSubject<ViewDate>({
			nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
			numberDay: this.actualDate.getDate(),
			month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
		});
		this.internmentEpisode.getInternmentEpisode(this.internmentEpisodeId).subscribe(
			internmentEpisode => this.entryDate = new Date(internmentEpisode.entryDate)
		);
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
			this.viewDay$.next({
				nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
				numberDay: this.actualDate.getDate(),
				month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
			});
		}
		if (differenceInDays <= 0)
			this.currentViewIsEntryDate = true;
	}
}

interface ViewDate {
	nameDay: string,
	numberDay: number,
	month: string,
}
