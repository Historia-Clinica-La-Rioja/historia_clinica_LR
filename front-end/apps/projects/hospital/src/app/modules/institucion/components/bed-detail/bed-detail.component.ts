import { Component, OnInit, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { BedService } from '@api-rest/services/bed.service';
import { BedInfoDto } from '@api-rest/api-model';

@Component({
  selector: 'app-bed-detail',
  templateUrl: './bed-detail.component.html',
  styleUrls: ['./bed-detail.component.scss']
})
export class BedDetailComponent implements OnInit, OnChanges {

  	@Input() bedId: number;
	@Input() bedAssign = false;
	@Output() assignedBed?: EventEmitter<BedInfoDto> = new EventEmitter<BedInfoDto>();

	bedInfo: BedInfoDto;

	constructor(private readonly bedService: BedService) { }

	ngOnInit(): void {
		if (this.bedId) {
			this.bedService.getBedInfo(this.bedId).subscribe(bedInfo => this.bedInfo = bedInfo);
		}
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.bedId.currentValue) {
			this.bedService.getBedInfo(this.bedId).subscribe(bedInfo => this.bedInfo = bedInfo);
		}
	}

	assignBed() {
		this.assignedBed.emit(this.bedInfo);
	}

}
