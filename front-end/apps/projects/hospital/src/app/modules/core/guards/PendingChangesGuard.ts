import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CanDeactivate } from '@angular/router';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { Observable } from 'rxjs';
import { first, map } from 'rxjs/operators';

export interface ComponentCanDeactivate {
	canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable({ providedIn: 'root' })
export class PendingChangesGuard implements CanDeactivate<ComponentCanDeactivate> {

	constructor(
		private readonly dialog: MatDialog
	) { }

	canDeactivate(component: ComponentCanDeactivate): boolean | Observable<boolean> {
		if (component.canDeactivate()) {
			const dialog = this.dialog.open(DiscardWarningComponent,
				{
					disableClose: true,
					data: {
						content: 'ambulatoria.screen_change_warning_dialog.CONTENT',
						contentBold: `ambulatoria.screen_change_warning_dialog.ANSWER_CONTENT`,
						okButtonLabel: 'ambulatoria.screen_change_warning_dialog.CONFIRM_BUTTON',
						cancelButtonLabel: 'ambulatoria.screen_change_warning_dialog.CANCEL_BUTTON',
					}
				});

			return dialog.afterClosed().pipe(
				map((result: boolean) => {
					return result;
				}),
				first()
			);
		}
		else
			return true;
	}
}
