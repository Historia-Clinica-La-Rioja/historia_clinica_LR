import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatOptionSelectionChange } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, ChildrenOutletContexts, Router } from '@angular/router';
import { isAfter, parseISO, startOfToday } from 'date-fns';
import { Subscription } from 'rxjs';

import { ContextService } from '@core/services/context.service';
import { fromStringToDateByDelimeter } from '@core/utils/date.utils';
import { processErrors } from '@core/utils/form.utils';

import { DiaryListDto } from '@api-rest/api-model';
import { DailyAppointmentService } from '@api-rest/services/daily-appointment.service';
import { DiaryService } from '@api-rest/services/diary.service';

import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { DatePickerComponent } from '@presentation/dialogs/date-picker/date-picker.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import { BlockAgendaRangeComponent } from '@turnos/dialogs/block-agenda-range/block-agenda-range.component';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { AgendaFilters, AgendaOptionsData, AgendaSearchService } from '../../services/agenda-search.service';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-select-agenda',
	templateUrl: './select-agenda.component.html',
	styleUrls: ['./select-agenda.component.scss'],
	providers: [
		DateFormatPipe
	]
})
export class SelectAgendaComponent implements OnInit, OnDestroy {

	viewDate: Date = new Date();

	currentAgenda: DiaryListDto;
	agendaSelected: DiaryList;
	agendas: DiaryList[];
	activeAgendas: DiaryList[] = [];
	expiredAgendas: DiaryList[] = [];
	agendaFiltersSubscription: Subscription;
	agendaSelectedSubscription: Subscription;
	filters: AgendaFilters;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private patientId: number;

	constructor(
		private readonly router: Router,
		public readonly route: ActivatedRoute,
		private readonly diaryService: DiaryService,
		private readonly dialog: MatDialog,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private readonly dailyAppointmentService: DailyAppointmentService,
		private readonly agendaSearchService: AgendaSearchService,
		private readonly appointmentsFacadeService: AppointmentsFacadeService,
		private childrenOutlets: ChildrenOutletContexts,
		private readonly dateFormatPipe: DateFormatPipe
	) {
	}

	ngOnInit(): void {
		this.agendaSelectedSubscription = this.agendaSearchService.getAgendaSelected$().subscribe(agenda => {
			this.agendaSelected = this.agendas?.find(a => a.diaryList.id === agenda?.id);
		});
		this.agendaFiltersSubscription = this.agendaSearchService.getAgendas$().subscribe((data: AgendaOptionsData) => {
			if (data?.agendas) {
				this.loadAgendas(data.agendas, data.idAgendaSelected);
				this.filters = data.filteredBy;
			} else {
				this.agendas = null;
			}
		});
		this.route.queryParams.subscribe(qp => {
			this.patientId = Number(qp.idPaciente);
		});
	}

	ngOnDestroy() {
		this.agendaFiltersSubscription?.unsubscribe();
		this.agendaSelectedSubscription.unsubscribe();
		this.childrenOutlets.onOutletDeactivated();
	}

	changeAgendaSelected(event: MatOptionSelectionChange, agenda: DiaryListDto): void {
		if (event.isUserInput) {
			this.agendaSelected = {
				diaryList: agenda,
				startDate: null,
				endDate: null
			};
			this.currentAgenda = agenda;
			if (this.patientId) {
				this.router.navigate([`agenda/${agenda.id}`], { relativeTo: this.route, queryParams: { idPaciente: this.patientId } });
			} else {
				this.router.navigate([`agenda/${agenda.id}`], { relativeTo: this.route });
			}
		}
	}

	private loadAgendas(diaries: DiaryListDto[], idAgendaSelected?: number): void {
		this.agendas = [];
		delete this.agendaSelected;
		diaries.forEach(diary => {
			this.agendas.push({
				diaryList: diary,
				endDate: null,
				startDate: null
			});
		});
		this.categorizeAgendas(diaries);
		if (idAgendaSelected) {
			this.agendaSelected = this.agendas.find(agenda => {
				return agenda.diaryList.id === idAgendaSelected;
			});
			if (!this.agendaSelected) {
				this.router.navigate([`institucion/${this.contextService.institutionId}/turnos`]);
			} else {
				this.currentAgenda = this.agendaSelected.diaryList;
			}
		}
	}

	private categorizeAgendas(diaries: DiaryListDto[]): void {
		this.expiredAgendas = [];
		this.activeAgendas = [];
		if (diaries?.length) {
			diaries.forEach(diary => {
				const newDiary: DiaryList = {
					diaryList: diary,
					endDate: fromStringToDateByDelimeter(diary.endDate, '-'),
					startDate: fromStringToDateByDelimeter(diary.startDate, '-')
				};
				isAfter(startOfToday(), parseISO(diary.endDate)) ? this.expiredAgendas.push(newDiary) : this.activeAgendas.push(newDiary);
			});
		}
	}

	goToEditAgenda(): void {
		this.router.navigate([`institucion/${this.contextService.institutionId}/turnos/agenda/${this.agendaSelected.diaryList.id}/editar`]);
	}

	blockAgenda() {
		this.diaryService.get(this.agendaSelected.diaryList.id).subscribe(result => {
			const dialogRef = this.dialog.open(BlockAgendaRangeComponent, {
				data: {
					selectedAgenda: result
				}
			});

			dialogRef.afterClosed().subscribe(response => {
				if (response) {
					this.appointmentsFacadeService.loadAppointments();
				}
			});
		});
	}

	deleteAgenda(): void {
		const agendaName = this.agendaSelected.diaryList.alias ? `${this.agendaSelected.diaryList.alias} (${this.agendaSelected.diaryList.clinicalSpecialtyName})` : this.agendaSelected.diaryList.clinicalSpecialtyName;

		const startDate = dateISOParseDate(this.agendaSelected.diaryList.startDate);
		const endDate = dateISOParseDate(this.agendaSelected.diaryList.endDate);

		const content = `¿Seguro desea eliminar su agenda? <br> ${agendaName} <br>
						Desde ${this.dateFormatPipe.transform(startDate, 'date')} hasta
						${this.dateFormatPipe.transform(endDate, 'date')} `;
		const dialogRef = this.dialog.open(ConfirmDialogComponent,
			{
				data: {
					title: 'turnos.delete-agenda.DELETE',
					content: content,
					okButtonLabel: 'buttons.DELETE',
					okBottonColor: 'warn'
				}
			});

		dialogRef.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.diaryService.delete(this.agendaSelected.diaryList.id)
					.subscribe((deleted: boolean) => {
						if (deleted) {
							this.snackBarService.showSuccess('turnos.delete-agenda.messages.SUCCESS');
							this.agendas = this.agendas.filter(agenda => agenda.diaryList.id !== this.agendaSelected.diaryList.id);
							this.categorizeAgendas(this.agendas.map(agenda => agenda.diaryList));
							this.agendaSearchService.search(this.filters.idProfesional);
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
				minDate: this.agendaSelected.diaryList.startDate,
				maxDate: this.agendaSelected.diaryList.endDate
			}
		});

		dialogRef.afterClosed()
			.subscribe(value => {
				if (value) {
					this.dailyAppointmentService.getDailyAppointmentsByDiaryIdAndDate(this.agendaSelected.diaryList.id, value);
				}
			});
	}

	clear(control: any): void {
		this.agendaSelected = null;
		this.currentAgenda = null;
		if (this.patientId) {
			this.router.navigate([`${this.routePrefix}/turnos`], { queryParams: { idPaciente: this.patientId } });
		} else {
			this.router.navigate([`${this.routePrefix}/turnos`]);
		}

	}

	getAliasAndSpecialtyText(alias: string, clinicalSpecialtyName: string): string {
		return clinicalSpecialtyName ? `${alias} (${clinicalSpecialtyName})` : `${alias}`;
	}

}

export interface DiaryList {
	diaryList: DiaryListDto;
	endDate: Date;
	startDate: Date;
}
