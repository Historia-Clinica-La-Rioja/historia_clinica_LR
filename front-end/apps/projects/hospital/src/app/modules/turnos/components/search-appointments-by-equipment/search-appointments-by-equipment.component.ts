import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EquipmentDiaryDto, EquipmentDto, ModalityDto } from '@api-rest/api-model';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { ModalityService } from '@api-rest/services/modality.service';
import { ContextService } from '@core/services/context.service';
import { fromStringToDateByDelimeter } from '@core/utils/date.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { isAfter, startOfToday, parseISO } from 'date-fns';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { EquipmentDiaryOptionsData, SearchEquipmentDiaryService } from '../../services/search-equipment-diary.service';
import { MatOptionSelectionChange } from '@angular/material/core';

@Component({
	selector: 'app-search-appointments-by-equipment',
	templateUrl: './search-appointments-by-equipment.component.html',
	styleUrls: ['./search-appointments-by-equipment.component.scss'],
	providers: [SearchEquipmentDiaryService]
})
export class SearchAppointmentsByEquipmentComponent implements OnInit {

	selectedEquipment: EquipmentDto;
	selectedDiary: EquipmentDiaryDto;

	modalities$: Observable<TypeaheadOption<ModalityDto>[]>;

	equipmentsTypeaheadList: TypeaheadOption<EquipmentDto>[];
	equipments: EquipmentDto[] = [];

	diarySelected: DiaryList;
	equipmentSelected: EquipmentDto;

	diaries: EquipmentDiaryDto[];
	activeDiaries: DiaryList[] = [];
	expiredDiaries: DiaryList[] = [];

	externalSelectedEquipment: TypeaheadOption<EquipmentDto>;

	constructor(
		private readonly modalityService: ModalityService,
		private readonly equipmentService: EquipmentService,
		private readonly router: Router,
		private contextService: ContextService,
		private readonly searchEquipmentService: SearchEquipmentDiaryService,
		private changeDetectorRef: ChangeDetectorRef,
	) { }

	ngOnInit(): void {
		this.selectedEquipment = window.history.state.selectedEquipment;
		this.selectedDiary = window.history.state.selectedDiary;
		this.modalities$ = this.setModalityTypeaheadOptions();
		this.setEquipments();
		this.searchEquipmentService.getAgendas$().subscribe((data: EquipmentDiaryOptionsData) => {
			if (data) {
				let idAgendaSelected = this.selectedDiary ? this.selectedDiary.id : data.idAgendaSelected;
				this.loadAgendas(data.diaries, idAgendaSelected);
			}
		});
	}

	changeDiarySelected(event: MatOptionSelectionChange, diary: DiaryList) {
		if (event.isUserInput) {
			this.diarySelected = diary;
		}
	}

	goToNewAgenda() {
		this.router.navigate([`institucion/${this.contextService.institutionId}/turnos/imagenes/nueva-agenda`]);
	}

	clear() {
		this.diarySelected = null;
		this.selectedDiary = null;
		this.changeDetectorRef.detectChanges();
	}

	filterEquipmentsByModality(modality: ModalityDto) {
		if (modality) {
			const equipmentsFilteredByModality: EquipmentDto[] = this.equipments.filter(equipment => equipment.modalityId === modality.id);
			this.equipmentsTypeaheadList = equipmentsFilteredByModality.map(e => this.toEquipmentTypeaheadOptions(e));
		}
		else {
			this.equipmentsTypeaheadList = this.equipments.map(e => this.toEquipmentTypeaheadOptions(e));
		}
		this.equipmentSelected = null;
		this.clear();
	}

	loadDiaryByEquipment(e: EquipmentDto) {
		if (!e) {
			this.selectedEquipment = null;
			this.selectedDiary = null;
		}
		this.equipmentSelected = e;
		this.searchEquipmentService.search(e?.id);
	}

	compareDiaries(diaryOne: DiaryList, diaryTwo: DiaryList): boolean {
		if (diaryOne && diaryTwo) {
			return diaryOne.diaryList.id === diaryTwo.diaryList.id;
		}
		return false;
	}

	private setEquipments() {
		this.equipmentService.getAll().subscribe((equipments: EquipmentDto[]) => {
			this.equipments = equipments;
			this.equipmentsTypeaheadList = equipments.map(e => this.toEquipmentTypeaheadOptions(e));
			this.setSelectedEquipmentIfDiaryWasEdited()
		});
	}

	private setSelectedEquipmentIfDiaryWasEdited() {
		if (this.selectedEquipment) {
			if (!this.selectedEquipment.name) {
				this.selectedEquipment = this.equipments.find(e => e.id == this.selectedEquipment.id)
			}
			this.externalSelectedEquipment = this.toEquipmentTypeaheadOptions(this.selectedEquipment);
			this.loadDiaryByEquipment(this.selectedEquipment);
		}
	}

	private setModalityTypeaheadOptions(): Observable<TypeaheadOption<ModalityDto>[]> {
		return this.modalityService.getAll().pipe(map(toTypeaheadOptionList));

		function toTypeaheadOptionList(modalities: ModalityDto[]): TypeaheadOption<ModalityDto>[] {
			return modalities.map(toTypeaheadOption);

			function toTypeaheadOption(m: ModalityDto): TypeaheadOption<ModalityDto> {
				return {
					compareValue: m.description,
					value: m
				};
			}
		}
	}

	private toEquipmentTypeaheadOptions(equipment: EquipmentDto,): TypeaheadOption<EquipmentDto> {
		return {
			compareValue: equipment.name,
			value: equipment
		};
	}

	private loadAgendas(diaries: EquipmentDiaryDto[], diarySelectedId: number) {
		delete this.diaries;
		delete this.diarySelected;
		this.diaries = diaries;
		this.categorizeAgendas(diaries);
		if (diarySelectedId) {
			const diariesList: DiaryList[] = this.activeDiaries.concat(this.expiredDiaries);
			this.diarySelected = diariesList.find(agenda => agenda.diaryList.id === diarySelectedId);
		}
	}

	private categorizeAgendas(diaries: EquipmentDiaryDto[]) {
		this.expiredDiaries = [];
		this.activeDiaries = [];
		if (diaries?.length)
			diaries.forEach(diary =>{
				const newDiary: DiaryList = {
					diaryList: diary,
					endDate: fromStringToDateByDelimeter(diary.endDate, '-'),
					startDate: fromStringToDateByDelimeter(diary.startDate, '-')
				}
				isAfter(startOfToday(), parseISO(diary.endDate)) ?
				this.expiredDiaries.push(newDiary) : this.activeDiaries.push(newDiary)
			});
	}

	goToEditAgenda(){
		const currenteDiarySelected: EquipmentDiaryDto = this.diarySelected.diaryList;
		this.router.navigate([`institucion/${this.contextService.institutionId}/turnos/imagenes/agenda/${currenteDiarySelected.id}/editar`],
		{ state : { selectedEquipment: this.equipmentSelected, selectedDiary: currenteDiarySelected}});
	}
}

export interface DiaryList {
	diaryList: EquipmentDiaryDto;
    endDate: Date;
    startDate: Date;
}
