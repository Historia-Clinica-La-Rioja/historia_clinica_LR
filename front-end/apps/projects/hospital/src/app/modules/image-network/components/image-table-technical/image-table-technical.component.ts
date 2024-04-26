import { Component, OnInit } from '@angular/core';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { Observable, map, of } from 'rxjs';
import { Color } from '@presentation/colored-label/colored-label.component';
import { PAGE_SIZE_OPTIONS } from '../../../documents-signature/modules/joint-signature/constants/joint-signature.constants';
import { ButtonType } from '@presentation/components/button/button.component';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';

@Component({
	selector: 'app-image-table-technical',
	templateUrl: './image-table-technical.component.html',
	styleUrls: ['./image-table-technical.component.scss']
})
export class ImageTableTechnicalComponent implements OnInit {

	imageList$: Observable<ItemImageQueue[]> = imagequeue.pipe(map(itemImages => this.mapToItemImageQueue(itemImages)))
	size: Size = Size.SMALL
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	readonly PAGE_SIZE = 20;
	readonly FIRST_PAGE = 0;
	readonly ButtonType = ButtonType;
	isLoading = false


	constructor(private personMasterDataService:PersonMasterDataService) { }

	ngOnInit(): void {

		this.personMasterDataService.getIdentificationTypes()
		.subscribe( _  => {
		})
	}


	mapToItemImageQueue(items: ItemImageQueueDto[]): ItemImageQueue[] {
		return items.map(item => {
			return {
				person: item.person,
				studies: item.studiesNames ? item.studiesNames.join(' | ') : null,
				status: item.status,
				date: item.date
			}
		})
	}

	tryAgain(): void {
	}

	doIndex(): void { }

	onPageChange(event: any): void {
	}
}

export const imagequeue: Observable<ItemImageQueueDto[]> = of([
	{
		person: {
			fullName: 'Michael Jordan',
			identification: {
				type: 'DNI',
				number: 11235790,
			},
			id: 1,
			gender: 'Masculino',
			age: 32,
		},
		studiesNames: ["administración de inmunoglobulina anti - varicela", "prevención de lesiones"],
		status: {
			description: "pendiente de moverse",
			color: Color.YELLOW,
		},
		serviceRequestId: null,
		date: new Date("2023-12-22T14:11:43.423Z")
	},
	{
		person: {
			fullName: 'Michael Jordan',
			identification: {
				type: 'DNI',
				number: 11235790,
			},
			id: 1,
			gender: 'Masculino',
			age: 32,
		},
		studiesNames: ["administración de inmunoglobulina anti - varicela", "prevención de lesiones"],
		status: {
			description: "moviendo ...",
			color: Color.YELLOW,
		},
		serviceRequestId: 12,
		date: new Date("2023-12-22T14:11:43.423Z")
	},
	{
		person: {
			fullName: 'Michael Jordan',
			identification: {
				type: 'DNI',
				number: 11235790,
			},
			id: 1,
			gender: 'Masculino',
			age: 32,
		},
		studiesNames: ["administración de inmunoglobulina anti - varicela", "prevención de lesiones"],
		status: {
			description: "Error",
			color: Color.RED,
		},
		serviceRequestId: 12,
		date: new Date("2023-12-22T14:11:43.423Z")
	},
	{
		person: {
			fullName: 'Michael Jordan',
			identification: {
				type: 'DNI',
				number: 11235790,
			},
			id: 1,
			gender: 'Masculino',
			age: 32,
		},
		studiesNames: null,
		status: {
			description: "Error",
			color: Color.RED,
		},
		serviceRequestId: null,
		date: new Date("2023-12-22T14:11:43.423Z")
	}
])


export interface ItemImageQueueDto {
	person: PatientSummary
	studiesNames: String[],
	status?: {
		description: String,
		color: Color,
		icon?: String
	},
	serviceRequestId: number,
	date?: Date,
}

export interface ItemImageQueue {
	person: PatientSummary
	studies: String
	date?: Date,
	hour?: string,
	status: any
}