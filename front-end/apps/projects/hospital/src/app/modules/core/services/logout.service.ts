import { Injectable } from '@angular/core';
import { LogoutComponent } from '../../auth/dialogs/logout/logout.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';

@Injectable({
	providedIn: 'root'
})
export class LogoutService {

	constructor(
		private dialog: DialogService<LogoutComponent>
	) { }

	run() {
		this.dialog.open(LogoutComponent, { dialogWidth: DialogWidth.SMALL, })
	}
}

