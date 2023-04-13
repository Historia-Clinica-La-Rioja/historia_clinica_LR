import { Injectable } from '@angular/core';
import { CompleteEquipmentDiaryDto, EquipmentDiaryDto } from '@api-rest/api-model';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class SearchEquipmentDiaryService {

	private diarySource = new BehaviorSubject<EquipmentDiaryOptionsData>(undefined);
	private diarySelected: CompleteEquipmentDiaryDto;

	constructor(
		private readonly equipmentDiaryService: EquipmentDiaryService,
	) {
	}

	getAgendas$(): Observable<EquipmentDiaryOptionsData> {
		return this.diarySource.asObservable();
	}

	search(equipmentId: number): void {
		if (equipmentId) {
			this.updateDiaries(equipmentId);
		} else {
			this.clearSearch();
		}
	}

	setAgendaSelected(agendaSelected: CompleteEquipmentDiaryDto): void {
		if (!agendaSelected) {
			this.updateSelectAgenda();
		}
		this.diarySelected = agendaSelected;
	}

	private updateDiaries(equipmentId: number): void {
		this.equipmentDiaryService.getDiariesBy(equipmentId).subscribe((diaries: EquipmentDiaryDto[]) => {
			this.diarySource.next({
				diaries,
				idAgendaSelected: this.diarySelected?.id,
			});
		});
	}

	private clearSearch(): void {
		delete this.diarySelected;
		this.diarySource.next({
			diaries: undefined,
			idAgendaSelected: undefined
		});
	}

	private updateSelectAgenda(): void {
		const equipmentId = this.diarySelected?.equipmentId;
		if (equipmentId) {
			this.updateDiaries(equipmentId);
		}
		else {
			this.diarySource.next({
				diaries: this.diarySource.getValue()?.diaries,
				idAgendaSelected: undefined,
			});
		}
	}
}

export interface EquipmentDiaryOptionsData {
	diaries: EquipmentDiaryDto[];
	idAgendaSelected?: number;
}
