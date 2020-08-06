import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CompleteDiaryDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import { map, tap } from 'rxjs/operators';
import { CalendarEvent } from 'angular-calendar';
import { momentParseTime } from '@core/utils/moment.utils';
import { NewAgendaService } from '../../../../../../services/new-agenda.service';
import { MatDialog } from '@angular/material/dialog';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DiaryService } from '@api-rest/services/diary.service';

@Component({
	selector: 'app-agenda',
	templateUrl: './agenda.component.html',
	styleUrls: ['./agenda.component.scss']
})
export class AgendaComponent implements OnInit {

	newAgendaService: NewAgendaService;
	agenda: CompleteDiaryDto;

	constructor(
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly route: ActivatedRoute,
		private readonly diaryService: DiaryService
	) {
		this.newAgendaService = new NewAgendaService(this.dialog, this.cdr);
	}

	ngOnInit(): void {
		this.route.paramMap
			.subscribe((params: ParamMap) => {
				const idAgenda = Number(params.get('idAgenda'));
				this.diaryService.get(idAgenda).subscribe(agenda => this.setAgenda(agenda));
			});
	}

	setAgenda(agenda: CompleteDiaryDto): void {
		this.agenda = agenda;
		this.newAgendaService.setAppointmentDuration(this.agenda.appointmentDuration);

		const openingHours$ = this.diaryOpeningHoursService.getMany([this.agenda.id]);
		openingHours$
			.pipe(
				tap(openingHours => console.log('openingHours', openingHours)),
				map((openingHours: DiaryOpeningHoursDto[]): CalendarEvent[] =>
					openingHours.map(toCalendarEvent)))
			.subscribe(a => {
				console.log('events', a);
				this.newAgendaService.setEvents(a);
			});


		function toCalendarEvent(diaryOpeningHours: DiaryOpeningHoursDto) {
			return {
				start: buildFullDate(diaryOpeningHours.openingHours.from),
				end: buildFullDate(diaryOpeningHours.openingHours.to),
				title: `Agenda ${agenda.id}`,
				color: {
					primary: getRandomColor(),
					secondary: getRandomColor()
				}
			};

			function buildFullDate(time: string): Date {
				return momentParseTime(time)
					.day(diaryOpeningHours.openingHours.dayWeekId)
					.toDate();
			}

			function getRandomColor() {
				return `#${Math.floor(Math.random() * 16777215).toString(16)}`;

			}
		}

	}

}
