import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { EImageMoveStatus, EquipmentDto, ImageQueueFilteringCriteriaDto, MasterDataDto, ModalityDto } from '@api-rest/api-model';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { ModalityService } from '@api-rest/services/modality.service';
import { newDate } from '@core/utils/moment.utils';
import { BehaviorSubject, Observable, Subject, combineLatest, filter, of, pairwise, startWith, takeUntil, tap } from 'rxjs';
import { ButtonType } from '@presentation/components/button/button.component';
import { MatSelect } from '@angular/material/select';
import { MatOption } from '@angular/material/core';
import { ImageQueueManagementStateService } from '../../services/image-queue-management-state.service';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { subDays } from 'date-fns';

@Component({
	selector: 'app-image-queue-filters',
	templateUrl: './image-queue-filters.component.html',
	styleUrls: ['./image-queue-filters.component.scss'],
})


export class ImageQueueFiltersComponent implements OnInit, OnDestroy {

	@ViewChild('select') select: MatSelect;
	POSITION_EVALUATE: 0  = 0
	POSITION_DATE_EVALUATE: 1  = 1
	getValueFromState = (elements: MainFilterModel[], position : 0): MainFilterModel => elements.at(position)
	getValueDateFromState = (elements: DateRange[], position : 1 ): DateRange => elements.at(position)

	currentFilterState: ImageQueueFilteringCriteriaDto
	filterForm: FormGroup<MainFilterFormQueue>;
	secundaryFilterForm: FormGroup<SecundaryFilterFormQueue>;
	readonly ButtonTypeRaised = ButtonType.RAISED;
	minDay = subDays(new Date(), 60)
	today = newDate();
	dateRange = { start: newDate(), end: newDate() }
	enableMoreFilters = false
	allSelected = false

	INIT_FILTER_STATE = {
		equipment: null,
		modality: null,
		status: ['ERROR']
	}
	MAX_STATES = 3
	MIN_STATES = 0

	modalities$: Observable<ModalityDto[]>
	equipments$: Observable<EquipmentDto[]>
	status$: Observable<MasterDataDto[]>;
	allEquipments$: BehaviorSubject<EquipmentDto[]> = new BehaviorSubject<EquipmentDto[]>(null);
	dateSubject$: BehaviorSubject<DateRange> = new BehaviorSubject<DateRange>({ start: newDate(), end: newDate() });
	skipSubject$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
	destroyed$ = new Subject()


	constructor(
		private readonly modalityService: ModalityService,
		private readonly equipmentService: EquipmentService,
		private readonly imageQueueManagementStateService: ImageQueueManagementStateService,
	) { }

	ngOnInit(): void {
		this.filterForm = new FormGroup<MainFilterFormQueue>({
			modality: new FormControl(null),
			equipment: new FormControl(null),
			status: new FormControl([EImageMoveStatus.ERROR]),
		});
		this.secundaryFilterForm = new FormGroup<SecundaryFilterFormQueue>({
			patientDocument: new FormControl(null),
			patientName: new FormControl(null),
			study: new FormControl(null)
		}
		)
		this.modalities$ = this.modalityService.getAll();
		this.equipments$ = this.equipmentService.getAll().pipe(tap(equipments => this.allEquipments$.next(equipments)));
		this.status$ = of(STATUS_QUEUE_IMAGE)

		combineLatest([
			this.filterForm.valueChanges.pipe(startWith(this.INIT_FILTER_STATE)),
			this.dateSubject$
		]).pipe(
			takeUntil(this.destroyed$),
			pairwise(),
			filter(([previousValues, currentValues]) =>
				!this.skipEmissionsModalityEquipment(
				this.getValueFromState(previousValues as MainFilterModel[], this.POSITION_EVALUATE),
				this.getValueFromState(currentValues as MainFilterModel[], this.POSITION_EVALUATE)
			)),
			filter(([previousValues, currentValues]) => !this.skipEmissionStatus(this.getValueFromState(currentValues as MainFilterModel[], this.POSITION_EVALUATE)))
		).subscribe(
				([previousValues, currentValues]) => {
					const mainFormValue = this.getValueFromState(currentValues as MainFilterModel[], this.POSITION_EVALUATE)
					const dateValues = this.getValueDateFromState(currentValues as DateRange[], this.POSITION_DATE_EVALUATE)
					this.currentFilterState = {
						equipmentId: mainFormValue?.equipment?.id,
						from: dateToDateDto(dateValues.start),
						modalityId: mainFormValue?.modality,
						statusList: mainFormValue.status as EImageMoveStatus[],
						to: dateToDateDto(dateValues.end),
						name: this.currentFilterState?.name,
						identificationNumber: this.currentFilterState?.identificationNumber,
						study: this.currentFilterState?.study
					}
					this.imageQueueManagementStateService.setListImageByFilters(this.currentFilterState)
				});

	}

	skipEmissionsModalityEquipment(prev: MainFilterModel, current: MainFilterModel): boolean {
		return (prev.equipment === null && current.equipment === null) && (prev.modality !== null && current.modality === null)
			||
			(prev.equipment !== null && current.equipment !== null) && (prev.modality !== null && current.modality === null)
			||
			(prev.equipment !== null && current.equipment !== null) && (prev.modality === null && current.modality !== null)
			||
			(prev.equipment === null && current.equipment === null) && (prev.modality === null && current.modality !== null)
			||
			(prev.equipment !== null && current.equipment !== null) && (prev.modality !== current.modality)
	}

	skipEmissionStatus(current: MainFilterModel): boolean {
		if (this.skipSubject$.getValue() === true && (current.status.length !== this.MIN_STATES && current.status.length !== this.MAX_STATES)) {
			return !(current.status.length === this.MIN_STATES || current.status.length === this.MAX_STATES)
		} else {
			this.skipSubject$.next(false)
			return false
		}
	}

	onModalityChange(): void {
		let modalityId = this.filterForm.controls.modality.value
		this.filterForm.controls.equipment.setValue(null)
		this.equipments$ = modalityId ? this.equipmentService.getEquipmentByModality(modalityId) : this.allEquipments$.asObservable()
	}

	cleanModalities(): void {
		this.filterForm.controls.modality.setValue(null);
		this.onModalityChange();
	}

	selectDate(event: DateRange): void {
		this.dateSubject$.next(event)
	}

	applyFilter(): void {
		this.currentFilterState = {
			equipmentId: this.currentFilterState?.equipmentId,
			from: this.currentFilterState.from,
			modalityId: this.currentFilterState?.modalityId,
			statusList: this.currentFilterState.statusList as EImageMoveStatus[],
			to: this.currentFilterState.to,
			name: this.secundaryFilterForm.controls.patientName.value,
			identificationNumber: this.secundaryFilterForm.controls.patientDocument.value,
			study:this.secundaryFilterForm.controls.study.value
		}
		this.imageQueueManagementStateService.setListImageByFilters(this.currentFilterState)
	}

	toggleAllSelection(): void {
		this.skipSubject$.next(true)
		if (this.allSelected) {
			this.select.options.forEach((item: MatOption) => item.select());
		} else {
			this.select.options.forEach((item: MatOption) => item.deselect());
		}
	}

	checkSelection(): void {
		if (this.select) {
			let newStatus = true;
			this.select.options.forEach((item: MatOption) => newStatus = !item.selected ? false : newStatus);
			this.allSelected = newStatus;
		}
	}

	ngOnDestroy(): void {
		this.destroyed$.next;
		this.destroyed$.complete();
	}
}

const STATUS_QUEUE_IMAGE = [
	{ id: 0, description: EImageMoveStatus.ERROR, label: 'Error'},
	{ id: 1, description: EImageMoveStatus.MOVING, label: 'Moviendo' },
	{ id: 2, description: EImageMoveStatus.PENDING,label: 'Pendiente de moverse' }
]

interface MainFilterFormQueue {
	modality: FormControl<number>;
	equipment: FormControl<EquipmentDto>;
	status: FormControl<EImageMoveStatus[]>;
}

interface MainFilterModel {
	modality: number;
	equipment: EquipmentDto;
	status: EImageMoveStatus[];
}

interface SecundaryFilterFormQueue {
	patientDocument: FormControl<string>,
	patientName: FormControl<string>,
	study: FormControl<string>
}


interface DateRange {
	start: Date,
	end: Date
}