import { Component } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { BasicTable } from '@material/model/table.model';
import { Observable } from 'rxjs';
import { ExternalCauseService } from '../../services/external-cause.service';

@Component({
	selector: 'app-table-event',
	templateUrl: './table-event.component.html',
	styleUrls: ['./table-event.component.scss']
})
export class TableEventComponent {
	table$: Observable<BasicTable<SnomedDto>>;

	constructor(
		readonly externalCauseServise: ExternalCauseService
	) { }

	ngOnInit() {
		this.table$ = this.externalCauseServise.table$;
	}
}
