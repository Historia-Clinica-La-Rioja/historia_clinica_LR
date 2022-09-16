import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatOptionSelectionChange } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { isAfter, parseISO, startOfToday } from 'date-fns';

import { ContextService } from '@core/services/context.service';
import { processErrors } from '@core/utils/form.utils';
import { DatePipeFormat } from '@core/utils/date.utils';

import { DailyAppointmentService } from '@api-rest/services/daily-appointment.service';
import { DiaryService } from '@api-rest/services/diary.service';
import { DiaryListDto } from '@api-rest/api-model';

import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { DatePickerComponent } from '@presentation/dialogs/date-picker/date-picker.component';

import { AgendaSearchService, AgendaFilters, AgendaOptionsData } from '../../services/agenda-search.service';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { BlockAgendaRangeComponent } from '@turnos/dialogs/block-agenda-range/block-agenda-range.component';

@Component({
	selector: 'app-select-agenda',
	templateUrl: './select-agenda.component.html',
	styleUrls: ['./select-agenda.component.scss']
})
export class SelectAgendaComponent implements OnInit, OnDestroy {

	viewDate: Date = new Date();

	agendaSelected: DiaryListDto;
	agendas: DiaryListDto[];
	activeAgendas: DiaryListDto[] = [];
	expiredAgendas: DiaryListDto[] = [];
	agendaFiltersSubscription: Subscription;
	agendaIdSubscription: Subscription;
	readonly dateFormats = DatePipeFormat;
	filters: AgendaFilters;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private patientId: number;

	constructor(
		private readonly router: Router,
		public readonly route: ActivatedRoute,
		private readonly diaryService: DiaryService,
		private readonly dialog: MatDialog,
		private readonly datePipe: DatePipe,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private readonly dailyAppointmentService: DailyAppointmentService,
		private readonly agendaSearchService: AgendaSearchService,
		private readonly appointmentsFacadeService: AppointmentsFacadeService,
	) {
	}

	ngOnInit(): void {
		this.agendaFiltersSubscription = this.agendaSearchService.getAgendas$().subscribe((data: AgendaOptionsData) => {
			if (data) {
				this.loadAgendas(data.agendas, data.idAgendaSelected);
				this.filters = data.filteredBy;
			}
		});

		this.route.queryParams.subscribe(qp => this.patientId = Number(qp.idPaciente));

	}

	ngOnDestroy() {
		this.agendaFiltersSubscription.unsubscribe();
	}

	changeAgendaSelected(event: MatOptionSelectionChange, agenda: DiaryListDto): void {
		if (event.isUserInput) {
			this.agendaSelected = agenda;
			if (this.patientId) {
				this.router.navigate([`agenda/${agenda.id}`], { relativeTo: this.route, queryParams: { idPaciente: this.patientId } });
			} else {
				this.router.navigate([`agenda/${agenda.id}`], { relativeTo: this.route });
			}
		}
	}

	private loadAgendas(diaries, idAgendaSelected?): void {
		delete this.agendas;
		delete this.agendaSelected;
		this.agendas = diaries;
		this.categorizeAgendas(diaries);
		if (idAgendaSelected) {
			this.agendaSelected = this.agendas.find(agenda => agenda.id === idAgendaSelected);
			if (!this.agendaSelected) {
				this.router.navigate([`institucion/${this.contextService.institutionId}/turnos`]);
			}
		}
	}

	private categorizeAgendas(diaries: DiaryListDto[]): void {
		this.expiredAgendas = [];
		this.activeAgendas = [];
		if (diaries?.length)
			diaries.forEach(diary =>
				isAfter(startOfToday(), parseISO(diary.endDate)) ? this.expiredAgendas.push(diary) : this.activeAgendas.push(diary)
			);
	}

	goToEditAgenda(): void {
		this.router.navigate([`institucion/${this.contextService.institutionId}/turnos/agenda/${this.agendaSelected.id}/editar`]);
	}

	blockAgenda() {
		this.diaryService.get(this.agendaSelected.id).subscribe(result => {
			const dialogRef = this.dialog.open(BlockAgendaRangeComponent, {
				data: {
					selectedAgenda: result
				}
			})

			dialogRef.afterClosed().subscribe(response => {
				if (response) {
					this.appointmentsFacadeService.loadAppointments();
				}
			});
		})
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
							this.categorizeAgendas(this.agendas);
							this.router.navigateByUrl(`${this.routePrefix}/turnos`);
						}
					}, error => processErrors(error, (msg) => {
						this.snackBarService.showError(msg);
					}));
			}
		});
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/turnos/nueva-agenda/`, this.filters.idProfesional]);
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

	clear(control: any): void {
		this.agendaSelected = null;
		if (this.patientId) {
			this.router.navigate([`${this.routePrefix}/turnos`], { queryParams: { idPaciente: this.patientId } });
		} else {
			this.router.navigate([`${this.routePrefix}/turnos`]);
		}

	}

	getAliasAndSpecialtyText(alias: string, clinicalSpecialtyName: string): string {
		return `${alias} (${clinicalSpecialtyName})`;
	}

}
