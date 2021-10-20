import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';
import { MessageSnackbarComponent } from '@presentation/components/message-snackbar/message-snackbar.component';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

const DEFAULT_DURATION = 5000;
const DEFAULT_H_POSITION = 'right';
const DEFAULT_V_POSITION = 'top';

export interface ToastConfig {
	style?: string;
	duration?: number;
	horizontalPosition?: MatSnackBarHorizontalPosition;
	verticalPosition?: MatSnackBarVerticalPosition;
	icon?: string;
}

@Injectable({
	providedIn: 'root'
})
export class SnackBarService {

	constructor(
		private snackBar: MatSnackBar
	) { }

	showSuccess(message: string, config?: ToastConfig) {
		this.snackBar.openFromComponent(MessageSnackbarComponent, {
			duration: config?.duration || DEFAULT_DURATION,
			horizontalPosition: config?.horizontalPosition || DEFAULT_H_POSITION,
			verticalPosition: config?.verticalPosition || DEFAULT_V_POSITION,
			data: { message, icon: 'check' },
			panelClass: 'success',
		});
	}

	showError(message: string, config?: ToastConfig) {
		this.snackBar.openFromComponent(MessageSnackbarComponent, {
			duration: config?.duration || DEFAULT_DURATION,
			horizontalPosition: config?.horizontalPosition || DEFAULT_H_POSITION,
			verticalPosition: config?.verticalPosition || DEFAULT_V_POSITION,
			data: { message, icon: 'cancel' },
			panelClass: 'error',
		});
	}

	showAction<T>(message: string, action: {text: string, payload: T}, config?: ToastConfig): Observable<T> {
		return this.snackBar.open(message, action.text, {
			duration: config?.duration || DEFAULT_DURATION,
			horizontalPosition: config?.horizontalPosition || DEFAULT_H_POSITION,
			verticalPosition: config?.verticalPosition || DEFAULT_V_POSITION,
		}).onAction().pipe(map(_ => action.payload));
	}
}
