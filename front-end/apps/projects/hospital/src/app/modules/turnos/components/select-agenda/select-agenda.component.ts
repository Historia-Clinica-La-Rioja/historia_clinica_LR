import { Component, OnDestroy, OnInit } from '@angular/core';
import { DiaryListDto } from '@api-rest/api-model';
import { MatOptionSelectionChange } from '@angular/material/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { DiaryService } from '@api-rest/services/diary.service';
import { MatDialog } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { processErrors } from '@core/utils/form.utils';
import { DatePickerComponent } from '@core/dialogs/date-picker/date-picker.component';
import { DailyAppointmentService } from '@api-rest/services/daily-appointment.service';
import { AgendaSearchService, AgendaFilters, AgendaOptionsData } from '../../services/agenda-search.service';

@Component({
	selector: 'app-select-agenda',
	templateUrl: './select-agenda.component.html',
	styleUrls: ['./select-agenda.component.scss']
})
export class SelectAgendaComponent implements OnInit, OnDestroy {

	viewDate: Date = new Date();

	agendaSelected: DiaryListDto;
	agendas: DiaryListDto[];
	agendaFiltersSubscription: Subscription;
	agendaIdSubscription: Subscription;

	filters: AgendaFilters;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;

	constructor(
		private readonly router: Router,
		public readonly route: ActivatedRoute,
		private readonly diaryService: DiaryService,
		private readonly dialog: MatDialog,
		private readonly datePipe: DatePipe,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private readonly dailyAppointmentService: DailyAppointmentService,
		private readonly agendaSearchService: AgendaSearchService
	) {
	}

	ngOnInit(): void {

		this.agendaFiltersSubscription = this.agendaSearchService.getAgendas$().subscribe((data: AgendaOptionsData) => {
			if (data) {
				this.loadAgendas(data.agendas, data.idAgendaSelected);
				this.filters = data.filteredBy;
			}
		});
	}

	ngOnDestroy() {
		this.agendaFiltersSubscription.unsubscribe();
	}

	changeAgendaSelected(event: MatOptionSelectionChange, agenda: DiaryListDto): void {
		if (event.isUserInput) {
			this.agendaSelected = agenda;
			this.router.navigate([`agenda/${agenda.id}`], { relativeTo: this.route });
		}
	}

	loadAgendas(diaries, idAgendaSelected?): void {
		delete this.agendas;
		delete this.agendaSelected;
		this.agendas = diaries;
		if (idAgendaSelected) {
			this.agendaSelected = this.agendas.find(agenda => agenda.id === idAgendaSelected);
			if (!this.agendaSelected) {
				this.router.navigate([`institucion/${this.contextService.institutionId}/turnos`]);
			}
		}
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
							this.agendas = this.agendas.filter(agenda => agenda.id !== this.agendaSelected.id);
							this.router.navigateByUrl(`${this.routePrefix}/turnos`);
						}
					}, error => processErrors(error, (msg) => {
						this.snackBarService.showError(msg);
					}));
			}
		});
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/turnos/nueva-agenda/`]);
	}

	printAgenda(): void {
		const dialogRef = this.dialog.open(DatePickerComponent, {
			data: {
				title: 'Imprimir agenda del día',
				okButtonLabel: 'Imprimir',
				cancelButtonLabel: 'cancelar',
				minDate: this.agendaSelected.startDate,
				maxDate: this.agendaSelected.endDate
			}
		});

		dialogRef.afterClosed()
			.subscribe(value => {
				if (value) {
					this.dailyAppointmentService.getDailyAppointmentsByDiaryIdAndDate(this.agendaSelected.id, value)
						.subscribe();
				}
			});
	}
}
