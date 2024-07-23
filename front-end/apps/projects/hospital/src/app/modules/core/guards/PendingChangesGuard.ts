import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { ConfirmDialogV2Component } from '@presentation/dialogs/confirm-dialog-v2/confirm-dialog-v2.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { Observable } from 'rxjs';
import { first, map } from 'rxjs/operators';

export interface ComponentCanDeactivate {
	canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable({ providedIn: 'root' })
export class PendingChangesGuard implements CanDeactivate<ComponentCanDeactivate> {

	constructor(
		private readonly dialog: DialogService<ConfirmDialogV2Component>
	) { }

	canDeactivate(component: ComponentCanDeactivate): boolean | Observable<boolean> {
		if (component.canDeactivate()) {
			const dialog = this.dialog.open(ConfirmDialogV2Component,
				{
					dialogWidth: DialogWidth.MEDIUM
				}, {
					hasIcon: true,
					content: 'ambulatoria.screen_change_warning_dialog.CONTENT',
					contentBold: `ambulatoria.screen_change_warning_dialog.ANSWER_CONTENT`,
					okButtonLabel: 'ambulatoria.screen_change_warning_dialog.CONFIRM_BUTTON',
					cancelButtonLabel: 'ambulatoria.screen_change_warning_dialog.CANCEL_BUTTON',
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
