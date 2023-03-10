import { Component, EventEmitter, OnInit, Output } from '@angular/core';
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


	table$: Observable<BasicTable<SnomedDto>>;
	@Output() eventSelected = new EventEmitter<SnomedDto>();
	constructor(
		readonly externalCauseServise: ExternalCauseService,
	) { }

	ngOnInit() {
		this.table$ = this.externalCauseServise.table$;
	}

}
