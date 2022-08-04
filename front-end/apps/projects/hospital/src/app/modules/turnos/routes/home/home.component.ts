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
import {PatientNameService} from "@core/services/patient-name.service";

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

	profesionalesTypeahead: TypeaheadOption<ProfessionalDto>[];
	profesionalInitValue: TypeaheadOption<ProfessionalDto>;
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
		private readonly patientNameService: PatientNameService,
		private readonly appointmentFacadeService: AppointmentsFacadeService

	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	ngOnInit(): void {

		this.route.queryParams.subscribe(qp => this.patientId = Number(qp.idPaciente));

		this.healthCareProfessionalService.getAllAssociated().subscribe(doctors => {
			this.especialidadesTypeaheadOptions$ = this.getEspecialidadesTypeaheadOptions$(doctors);

			this.profesionales = doctors;
			this.profesionalesTypeahead = doctors.map(d => this.toProfessionalTypeahead(d));

			if ((this.profesionales.length === 1) && (!this.route.firstChild)) {
				this.profesionalInitValue = this.toProfessionalTypeahead(this.profesionales[0]);
				this.setProfesional(this.profesionales[0]);
			}

			this.agendaFiltersSubscription = this.agendaSearchService.getFilters$().subscribe(filters => {
				if (filters?.idProfesional && filters?.idProfesional !== this.idProfesional) {
					this.loadProfessionalSelected(filters.idProfesional);
				}
			});

		});
	}

	ngOnDestroy() {
		this.agendaSearchService.clearAll();
		this.agendaFiltersSubscription?.unsubscribe();
		this.appointmentFacadeService.clearInterval();
	}

	private loadProfessionalSelected(idProfesional: number) {
		const profesional: ProfessionalDto = this.profesionales.find(p => p.id === idProfesional);
		this.idProfesional = idProfesional;
		if (profesional) {
			this.appointmentFacadeService.setProfessionalId(idProfesional);
			this.profesionalInitValue = this.toProfessionalTypeahead(profesional);
			this.agendaSearchService.search(idProfesional);
		} else {
			this.snackBarService.showError('turnos.home.AGENDA_NOT_FOUND');
			this.profesionalInitValue = undefined;
			this.router.navigate([`institucion/${this.contextService.institutionId}/turnos`]);
		}
	}

	setEspecialidad(professionalsByClinicalSpecialtyDto: ProfessionalsByClinicalSpecialtyDto) {
		this.profesionalInitValue = null;
		this.idEspecialidad = professionalsByClinicalSpecialtyDto?.clinicalSpecialty?.id;

		const profesionalesFilteredBy = this.getProfesionalesFilteredBy(professionalsByClinicalSpecialtyDto);
		this.profesionalesTypeahead = profesionalesFilteredBy.map(d => this.toProfessionalTypeahead(d));

		this.idProfesional = null;
		this.router.navigate([`${this.routePrefix}`]);
		this.appointmentFacadeService.setProfessionalId(this.idProfesional);
		this.agendaSearchService.search(this.idProfesional);
	}

	setProfesional(result: ProfessionalDto) {
		this.idProfesional = result?.id;
		this.appointmentFacadeService.setProfessionalId(result?.id);
		this.agendaSearchService.search(this.idProfesional);
		if (!result) {
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

	getFullName(profesional: ProfessionalDto): string {
		return `${profesional.lastName}, ${this.patientNameService.getPatientName(profesional.firstName, profesional.nameSelfDetermination)}`;
	}

	getFullNameLicence(profesional: ProfessionalDto): string {
		return `${this.getFullName(profesional)}`;
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

	private toProfessionalTypeahead(professionalDto: ProfessionalDto): TypeaheadOption<ProfessionalDto> {
		return {
			compareValue: this.getFullNameLicence(professionalDto),
			value: professionalDto
		};
	}


}

