import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SnomedECL } from '@api-rest/api-model';
import { SnomedDto } from '@api-rest/api-model';
import { BasicTable } from '@material/model/table.model';
import { Observable } from 'rxjs';
import { ExternalCauseService } from '../../services/external-cause.service';

@Component({
  selector: 'app-event-serch',
  templateUrl: './event-serch.component.html',
  styleUrls: ['./event-serch.component.scss']
})
export class EventSerchComponent implements OnInit {
	ECL = SnomedECL.EVENT;

	table$: Observable<BasicTable<SnomedDto>>;
	@Input() searchConceptsLocallyFF = false;
	@Output() eventSelected = new EventEmitter<SnomedDto>();
	constructor(
		readonly externalCauseServise: ExternalCauseService,
	) { }

	ngOnInit() {
		this.table$ = this.externalCauseServise.table$;
	}

}
