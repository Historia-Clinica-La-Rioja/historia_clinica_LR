import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { map } from 'rxjs/operators';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { Location } from '@angular/common';
import { DiariesService } from '@api-rest/services/diaries.service';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ContextService } from '@core/services/context.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { TypeaheadOption } from '@core/components/typeahead/typeahead.component';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	profesionalesTypeahead: TypeaheadOption<ProfessionalDto>[];
	profesionalInitValue: TypeaheadOption<ProfessionalDto>;
	profesionales: ProfessionalDto[] = [];

	especialidadesTypeahead: TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[];
	especialidadesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

	routePrefix: string;

	constructor(
		private readonly router: Router,
		private readonly formBuilder: FormBuilder,
		private readonly healthCareProfessionalService: HealthcareProfessionalService,
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly location: Location,
		public readonly route: ActivatedRoute,
		private readonly diariesService: DiariesService,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly contextService: ContextService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	ngOnInit(): void {

		this.healthCareProfessionalService.getAll()
			.subscribe(doctors => {
				this.especialidadesTypeaheadOptions$ = this.getEspecialidadesTypeaheadOptions$(doctors);

				this.profesionales = doctors;
				this.profesionalesTypeahead = doctors.map(d => this.toProfessionalTypeahead(d));

				if (this.route.firstChild) {
					this.loadProfessionalSelected();
				} else {
					if (this.profesionales.length === 1) {
						this.profesionalInitValue = this.toProfessionalTypeahead(this.profesionales[0]);
						this.navigate(this.profesionales[0]);
					}
				}

			});
	}

	private loadProfessionalSelected() {
		this.route.firstChild.params.subscribe(params => {
			const idProfesional = Number(params.idProfesional);
			if (idProfesional) {
				this.healthCareProfessionalService.getOne(idProfesional)
					.subscribe((profesional: ProfessionalDto) => {
						if (profesional) {
							this.profesionalInitValue = this.toProfessionalTypeahead(profesional);
						}
					});
			}
		});
	}

	loadProfessionals(professionalsByClinicalSpecialtyDto: ProfessionalsByClinicalSpecialtyDto) {
		this.profesionalInitValue = null;
		const profesionalesFilteredBy = this.getProfesionalesFilteredBy(professionalsByClinicalSpecialtyDto);
		this.profesionalesTypeahead = profesionalesFilteredBy.map(d => this.toProfessionalTypeahead(d));
	}

	navigate(result: ProfessionalDto) {
		if (result) {
			this.router.navigate([`profesional/${result.id}`], {relativeTo: this.route});
		} else {
			this.router.navigate([`${this.routePrefix}`]);
		}
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda/`]);
	}

	getFullName(profesional: ProfessionalDto): string {
		return `${profesional.lastName}, ${profesional.firstName}`;
	}

	getFullNameLicence(profesional: ProfessionalDto): string {
		return `${this.getFullName(profesional)} - ${profesional.licenceNumber}`;
	}

	private getProfesionalesFilteredBy(especialidad: ProfessionalsByClinicalSpecialtyDto): ProfessionalDto[] {
		if (especialidad?.professionalsIds) {
			return this.profesionales.filter(p => especialidad.professionalsIds.find(e => e === p.id));
		}
		return this.profesionales;
	}

	private getEspecialidadesTypeaheadOptions$(doctors: ProfessionalDto[]) {
		return this.clinicalSpecialtyService.getClinicalSpecialties(doctors.map(d => d.id))
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

