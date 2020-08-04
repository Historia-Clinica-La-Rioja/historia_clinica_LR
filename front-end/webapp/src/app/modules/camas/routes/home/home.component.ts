import { Component, OnInit } from '@angular/core';
import { BedService } from '@api-rest/services/bed.service';
import {BedSummaryDto} from '@api-rest/api-model';
import { MapperService } from '@presentation/services/mapper.service';
import {map} from 'rxjs/operators';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	public selectedCama;
	public allBeds;

	constructor(
		private readonly bedService: BedService,
		private readonly mapperService: MapperService
  	) { }

	ngOnInit(): void {
		this.bedService.getBedsSummary().pipe(
			map((bedsSummary: BedSummaryDto[]) => this.mapperService.toBedManagment(bedsSummary))
		).subscribe(data => this.allBeds = data);
	}

	
	selectBed(bedId){
		this.selectedCama = bedId;
	}
}

export class BedManagment {
	sectorId: number;
	sectorDescription: string;
  	specialty:
	{
		specialtyId: number;
		specialtyName: string;
		beds:
			{
				bedId: number;
				bedNumber: string;
				free: boolean;
			}[];
  	}[];
}
