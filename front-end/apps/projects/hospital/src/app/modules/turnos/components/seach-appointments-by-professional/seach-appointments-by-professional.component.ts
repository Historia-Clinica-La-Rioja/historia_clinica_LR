import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { ContextService } from '@core/services/context.service';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { AgendaSearchService } from '../../services/agenda-search.service';

@Component({
	selector: 'app-seach-appointments-by-professional',
	templateUrl: './seach-appointments-by-professional.component.html',
	styleUrls: ['./seach-appointments-by-professional.component.scss'],
})
export class SeachAppointmentsByProfessionalComponent implements OnInit, OnDestroy {

	professionalsFilteredBySpecialty: ProfessionalDto[] = [];
	professionalSelected: ProfessionalDto;
	profesionales: ProfessionalDto[] = [];

	especialidadesTypeahead: TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[];
	especialidadesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

	routePrefix: string;

	idProfesional: number;
	idEspecialidad: number;
	patientId: number;
	agendaFiltersSubscription: Subscription;

	constructor(
		private readonly router: Router,
		private readonly healthCareProfessionalService: HealthcareProfessionalByInstitutionService,
		public readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly agendaSearchService: AgendaSearchService,
		private readonly snackBarService: SnackBarService,
		private readonly appointmentFacadeService: AppointmentsFacadeService

	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	ngOnInit(): void {

		this.route.queryParams.subscribe(qp => this.patientId = Number(qp.idPaciente));

		this.healthCareProfessionalService.getAllAssociated().subscribe(doctors => {
			this.especialidadesTypeaheadOptions$ = this.getEspecialidadesTypeaheadOptions$(doctors);
			this.profesionales = doctors;
			this.agendaFiltersSubscription = this.agendaSearchService.getFilters$().subscribe(filters => {
				if (filters?.idProfesional && filters?.idProfesional !== this.idProfesional) {
					const profesional: ProfessionalDto = this.profesionales.find(p => p.id === filters.idProfesional);
					if (!profesional)
						this.diaryNotFound();
					else
						this.professionalSelected = profesional;

				}
			});
		});
	}

	ngOnDestroy() {
		this.agendaSearchService.clearAll();
		this.agendaFiltersSubscription?.unsubscribe();
	}

	private diaryNotFound() {
		this.snackBarService.showError('turnos.home.AGENDA_NOT_FOUND');
		this.professionalSelected = undefined;
		this.router.navigate([`institucion/${this.contextService.institutionId}/turnos`]);
	}

	setEspecialidad(professionalsByClinicalSpecialtyDto: ProfessionalsByClinicalSpecialtyDto) {
		this.professionalSelected = null;
		this.idEspecialidad = professionalsByClinicalSpecialtyDto?.clinicalSpecialty?.id;

		this.professionalsFilteredBySpecialty = this.getProfesionalesFilteredBy(professionalsByClinicalSpecialtyDto);
		this.idProfesional = null;
		if (this.patientId) {
			this.router.navigate([`${this.routePrefix}`], { queryParams: { idPaciente: this.patientId } });
		} else {
			this.router.navigate([`${this.routePrefix}`]);
		}
		this.appointmentFacadeService.setProfessional(this.professionalSelected);
		this.agendaSearchService.search(this.idProfesional);
	}

	goBack(professional: ProfessionalDto) {
		this.idProfesional = professional?.id;
		if (!professional) {
			if (this.patientId) {
				this.router.navigate([`${this.routePrefix}`], { queryParams: { idPaciente: this.patientId } });
			} else {
				this.router.navigate([`${this.routePrefix}`]);
			}
		}
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda/`]);
	}

	private getProfesionalesFilteredBy(especialidad: ProfessionalsByClinicalSpecialtyDto): ProfessionalDto[] {
		if (especialidad?.professionalsIds) {
			return this.profesionales.filter(p => especialidad.professionalsIds.find(e => e === p.id));
		}
		return this.profesionales;
	}

	private getEspecialidadesTypeaheadOptions$(doctors: ProfessionalDto[]) {
		return this.clinicalSpecialtyService.getActiveDiariesByProfessionalsClinicalSpecialties(doctors.map(d => d.id))
			.pipe(map(toTypeaheadOptionList));

		function toTypeaheadOptionList(prosBySpecialtyList: ProfessionalsByClinicalSpecialtyDto[]):
			TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[] {
			return prosBySpecialtyList.map(toTypeaheadOption);

			function toTypeaheadOption(s: ProfessionalsByClinicalSpecialtyDto): TypeaheadOption<ProfessionalsByClinicalSpecialtyDto> {
				return {
					compareValue: s.clinicalSpecialty.name,
					value: s
				};
			}
		}
	}

}

