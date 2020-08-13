import { Component, OnInit } from '@angular/core';
import { DiaryListDto } from '@api-rest/api-model';
import { MatOptionSelectionChange } from '@angular/material/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { Observable } from 'rxjs';
import { DiariesService } from '@api-rest/services/diaries.service';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { DiaryService } from '@api-rest/services/diary.service';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
@Component({
	selector: 'app-select-agenda',
	templateUrl: './select-agenda.component.html',
	styleUrls: ['./select-agenda.component.scss']
})
export class SelectAgendaComponent implements OnInit {

	agendaSelected: DiaryListDto;
	agendas: DiaryListDto[];
	idProfesional: number;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly diariesService: DiariesService,
		private readonly diaryService: DiaryService,
		private readonly dialog: MatDialog,
		private readonly datePipe: DatePipe,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe((params: ParamMap) => {
			this.idProfesional = Number(params.get('idProfesional'));
			if (this.route.firstChild) {
				this.route.firstChild.params.subscribe((paramsFirstChild: Params) => {
					const idAgenda = Number(paramsFirstChild.idAgenda);
					this.loadAgendas(idAgenda);
				});
			} else {
				this.loadAgendas();
			}
		});
	}

	changeAgendaSelected(event: MatOptionSelectionChange, agenda: DiaryListDto): void {
		if (event.isUserInput) {
			this.agendaSelected = agenda;
			this.router.navigate([`agenda/${agenda.id}`], { relativeTo: this.route });
		}
	}

	loadAgendas(idAgendaSelected?: number): void {
		delete this.agendas;
		const diaries$: Observable<DiaryListDto[]> = this.diariesService.getDiaries(this.idProfesional);
		diaries$.subscribe(diaries => {
			this.agendas = diaries;
			if (idAgendaSelected) {
				this.agendaSelected = this.agendas.find(agenda => agenda.id === idAgendaSelected);
			}
		});
	}

	goToEditAgenda(): void {
		this.router.navigate([`${this.router.url}/editar`]);
	}

	deleteAgenda(): void {
		const dialogRef = this.dialog.open(ConfirmDialogComponent,
			{
				data: {
					title: 'turnos.delete-agenda.DELETE',
					content: `¿Seguro desea eliminar su agenda? <br> ${this.agendaSelected.doctorsOfficeDescription} <br>
						Desde ${this.datePipe.transform(this.agendaSelected.startDate, 'dd/MM/yyyy')}, hasta
						${this.datePipe.transform(this.agendaSelected.endDate, 'dd/MM/yyyy')} `,
					okButtonLabel: 'buttons.DELETE',
					okBottonColor: 'warn'
				}
			});

		dialogRef.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.diaryService.delete(this.agendaSelected.id)
					.subscribe((deleted: boolean) => {
						if (deleted) {
							this.snackBarService.showSuccess('turnos.delete-agenda.messages.SUCCESS');
							this.router.navigate([this.routePrefix + 'turnos']);
						}
					} , (msg)  => {
						msg.errors.forEach(error => {
							this.snackBarService.showError(error);
						});
					});
			}
		});
	}
}
