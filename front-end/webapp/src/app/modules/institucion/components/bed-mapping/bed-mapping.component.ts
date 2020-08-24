import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import {BedManagement} from 'src/app/modules/camas/routes/home/home.component';

@Component({
  selector: 'app-bed-mapping',
  templateUrl: './bed-mapping.component.html',
  styleUrls: ['./bed-mapping.component.scss']
})
export class BedMappingComponent implements OnInit {

  	@Input() bedManagementList: BedManagement[];

	@Output() selectedBed = new EventEmitter<number>();

  	constructor() { }

	ngOnInit(): void {
	}

  	selectBed(bedId: number) {
		this.selectedBed.emit(bedId);
	}

}
