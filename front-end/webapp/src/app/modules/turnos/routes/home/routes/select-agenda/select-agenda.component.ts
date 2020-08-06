import { Component, OnInit } from '@angular/core';
import { DiaryListDto } from '@api-rest/api-model';
import { MatOptionSelectionChange } from '@angular/material/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { Observable } from 'rxjs';
import { DiariesService } from '@api-rest/services/diaries.service';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-select-agenda',
	templateUrl: './select-agenda.component.html',
	styleUrls: ['./select-agenda.component.scss']
})
export class SelectAgendaComponent implements OnInit {

	agendaSelected: DiaryListDto;
	agendas: DiaryListDto[];
	routePrefix: string;
	idProfesional: number;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly diariesService: DiariesService,
		private readonly contextService: ContextService
	) {
	}

	ngOnInit(): void {
		this.route.paramMap
			.subscribe((params: ParamMap) => {
				this.idProfesional = Number(params.get('idProfesional'));
				this.loadAgendas();
			});
	}

	changeAgendaSelected(event: MatOptionSelectionChange, agenda: DiaryListDto): void {
		if (event.isUserInput) {
			this.agendaSelected = agenda;
			this.router.navigate([`agenda/${agenda.id}`], {relativeTo: this.route});
		}
	}

	loadAgendas(): void {
		const diaries$: Observable<DiaryListDto[]> = this.diariesService.getDiaries(this.idProfesional);
		diaries$.subscribe(diaries => {
			this.agendas = diaries;
		});
	}

}
