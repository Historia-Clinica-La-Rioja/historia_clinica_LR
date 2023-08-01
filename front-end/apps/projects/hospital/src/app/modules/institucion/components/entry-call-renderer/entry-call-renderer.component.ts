import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ShowEntryCallService } from '@presentation/services/show-entry-call.service';
import { EntryCallComponent } from '../entry-call/entry-call.component';

@Component({
	selector: 'app-entry-call-renderer',
	templateUrl: './entry-call-renderer.component.html',
	styleUrls: ['./entry-call-renderer.component.scss']
})

/*  Este es el componente que siempre esta activo esperando
	que (desde showEntryCallService) le digan que tiene que mostrar
	o cortar una solicitud de llamada ( dialogo )
*/
export class EntryCallRendererComponent implements OnInit {

	private dialogRef: MatDialogRef<EntryCallComponent>;

	constructor(
		private readonly showEntryCallService: ShowEntryCallService,
		private readonly dialog: MatDialog,
	) { }


	ngOnInit(): void {
		this.showEntryCallService.$newCall.subscribe(
			roomId => {
				if (roomId) {
					this.dialogRef = this.dialog.open(EntryCallComponent, {
						data: roomId
					});
				} else {
					this.dialogRef.close()
				}
			}
		)
	}

}
