import { Component, Input } from '@angular/core';
import { CardModel } from '@presentation/components/card/card.component';
import { InternacionService } from '@api-rest/services/internacion.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { ContextService } from '@core/services/context.service';
import { MapperService } from "@presentation/services/mapper.service";
import { DocumentsSummaryDto, RoomDto } from '@api-rest/api-model';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { Sector } from '@institucion/services/bed-management-facade.service';
import { SectorService } from '@api-rest/services/sector.service';
import { RoomService } from '@api-rest/services/room.service';
import { pushIfNotExists } from '@core/utils/array.utils';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';

const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_SIZE_MIN = [5];
const PAGE_MIN_SIZE = 5;

@Component({
	selector: 'app-internment-patient-card',
	templateUrl: './internment-patient-card.component.html',
	styleUrls: ['./internment-patient-card.component.scss']
})

export class InternmentPatientCardComponent {
	formFilter: FormGroup;
	panelOpenState = false;
	pageSliceObs$: Observable<CardModel[]>;
	sectors = [];
	rooms = [];
	private applySearchFilter = '';
	private sizePageSelect = PAGE_MIN_SIZE;
	pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	numberOfPatients = 0;
	private readonly routePrefix;
	internmentPatientCard: CardModel[] = [];
	pageSlice: CardModel[] = [];
	@Input()
	set redirect(redirect: Redirect) {

		this.sectorService.getAll().subscribe(data =>
			this.sectors = data.map((sector) => { return { sectorId: sector?.id, sectorDescription: sector?.description } })
		);

		this.roomService.getAll().subscribe((data: RoomDto[]) => {
			data.forEach((room: RoomDto) => {
				this.addControl(room);
			});
		});

		this.formFilter = this.formBuilder.group({
			room: [null],
			sector: [null],
			physical: [false]
		});

		if (redirect === Redirect.patientCard) {
			this.internmentPatientService.getAllInternmentPatientsBasicData().subscribe(data => {
				this.internmentPatientCard = this.buildCard(data.map(patient => this.mapperService.toInternmentPatientTableData(patient)), redirect);
				this.pageSlice = this.internmentPatientCard.slice(0, PAGE_MIN_SIZE);
				this.numberOfPatients = this.internmentPatientCard?.length;
			});
		}
		else {
			this.internacionService.getAllPacientesInternados().subscribe(data => {
				this.internmentPatientCard = this.buildCard(data.map(patient => this.mapperService.toInternmentPatientTableData(patient)), redirect);
				this.pageSlice = this.internmentPatientCard.slice(0, PAGE_MIN_SIZE);
				this.numberOfPatients = this.internmentPatientCard?.length;
			})
		}
	}

	constructor(
		private internacionService: InternacionService,
		private readonly patientNameService: PatientNameService,
		private readonly contextService: ContextService,
		private readonly mapperService: MapperService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly formBuilder: FormBuilder,
		private readonly sectorService: SectorService,
		private roomService: RoomService,

	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}`
	}

	private buildCard(data: InternmentPatientTableData[], redirect: Redirect): CardModel[] {
		return data?.map((person: InternmentPatientTableData) => {
			return {
				name: person.nameSelfDetermination ? `${this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)} ${person.lastName}` : this.getName(person),
				header: [{ title: "", value: person.nameSelfDetermination ? `${this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)} ${person.lastName}` : this.getName(person) }],
				headerSimple: [{ title: "DNI", value: person.identificationNumber || "-" }],
				dni: person.identificationNumber || "-",
				hasPhysicalDischarge: person.hasPhysicalDischarge,
				roomNumber: person.bedInfo.roomNumber,
				bedNumber: person.bedInfo.bedNumber,
				sectorDescription: person.bedInfo.sector,
				lastMissingDocument: person.hasPhysicalDischarge ? this.missingDocument(person.documentsSummary, person.hasMedicalDischarge) : null,
				action: {
					display: 'ambulatoria.card-patient.BUTTON',
					do: (redirect === Redirect.patientCard) ? `${this.routePrefix}/pacientes/profile/${person.patientId}` : `${this.routePrefix}/ambulatoria/paciente/${person.patientId}`
				},

			}

		})

	}

	private getName(person: InternmentPatientTableData): string {
		return person.fullName ? `${person.fullName}` : `internaciones.internment-patient-card.NO_INFO`
	}

	private missingDocument(document: DocumentsSummaryDto, hasMedicalDischarge: boolean): Documnet {
		if (!document.lastEvaluationNote.confirmed)
			return { matIcon: "assignment", title: 'internaciones.internment-patient-card.PENDING_EVOLUTION' }
		if (!document.epicrisis.confirmed)
			return { matIcon: "assignment_turned_in", title: 'internaciones.internment-patient-card.PENDING_EPICRISIS' }
		if (!hasMedicalDischarge)
			return { matIcon: "assignment_return", title: 'internaciones.internment-patient-card.PENDING_MEDICAL_DISCHARGE' }
		return { matIcon: "keyboard_backspace", title: 'internaciones.internment-patient-card.PENDING_ADMINISTRATIVE_DISCHARGE' }
	}

	onPageChange($event: any): void {
		const page = $event;
		this.sizePageSelect = page.pageSize;
		const startPage = page.pageIndex * page.pageSize;
		this.applyFiltes();
		this.pageSlice = this.pageSlice.slice(startPage, $event.pageSize + startPage);
	}

	applyFilter($event: any): void {
		this.applySearchFilter = ($event.target as HTMLInputElement).value?.replace(REMOVE_SUBSTRING_DNI,'');
		this.upDateFilters();
	}

	upDateFilters(): void {
		this.applyFiltes();
		this.pageSlice = this.pageSlice.slice(0, this.sizePageSelect);
	}

	setPanelState(): void {
		this.panelOpenState = !this.panelOpenState;
	}

	clearFilterField(control: AbstractControl): void {
		control.reset();
		this.applyFiltes();
		this.pageSlice = this.pageSlice.slice(0, this.sizePageSelect);
	}

	private compareRoom(a1: RoomDto, a2: RoomDto): boolean {
		return a1.roomNumber === a2.roomNumber;
	}

	private addControl(room: RoomDto): boolean {
		const currentItems = this.rooms.length;
		this.rooms = pushIfNotExists<RoomDto>(this.rooms, room, this.compareRoom);
		return currentItems === this.rooms.length;
	}

	private filter(): CardModel[] {
		let listFilter = this.internmentPatientCard;
		if (this.formFilter?.value.room)
			listFilter = listFilter.filter(p => p.roomNumber === this.formFilter.value.room)
		if (this.formFilter?.value.sector)
			listFilter = listFilter.filter(p => p.sectorDescription === this.formFilter.value.sector)
		if (this.formFilter?.value.physical)
			listFilter = listFilter.filter(p => p.hasPhysicalDischarge === this.formFilter.value.physical);
		if (this.applySearchFilter) {
			listFilter = listFilter.filter((e: CardModel) => e?.name.toLowerCase().includes(this.applySearchFilter.toLowerCase()) || e?.dni.toString().includes(this.applySearchFilter));
		}
		return listFilter;
	}

	private applyFiltes(): void {
		this.pageSlice = this.filter();
		this.setPageSizeOptions();
	}

	private setPageSizeOptions(): void {
		this.pageSizeOptions = (this.pageSlice.length < PAGE_MIN_SIZE) ? PAGE_SIZE_MIN : PAGE_SIZE_OPTIONS;
		this.numberOfPatients = this.pageSlice.length;
		this.pageSlice.length = this.numberOfPatients;
	}

}

export interface InternmentPatientTableData {
	patientId: number;
	firstName: string;
	identificationNumber: string;
	identificationTypeId: number;
	internmentId: number;
	lastName: string;
	fullName: string;
	nameSelfDetermination: string;
	bedInfo: {
		sector: string;
		roomNumber: string;
		bedNumber: string;
	}
	hasPhysicalDischarge: boolean;
	hasMedicalDischarge?: boolean;
	sectorDescription?: Sector;
	documentsSummary: DocumentsSummaryDto;
}

export enum Redirect {
	HC, patientCard
}

interface Documnet {
	matIcon: string;
	title: string;
}

