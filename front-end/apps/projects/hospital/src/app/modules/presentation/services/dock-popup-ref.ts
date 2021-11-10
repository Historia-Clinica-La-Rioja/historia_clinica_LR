import { OverlayRef } from '@angular/cdk/overlay';
import { Observable, Subject } from 'rxjs';

export class DockPopupRef {

	private minimized = false;
	private openedHeight: number | string;
	private afterClosedSubject: Subject<any> = new Subject<any>();

	constructor(private overlayRef: OverlayRef) {
		this.openedHeight = overlayRef.getConfig().height;
	}

	close(dockPopupResult?: any): void {
		this.overlayRef.dispose();
		this.afterClosedSubject.next(dockPopupResult);
	}

	afterClosed(): Observable<any> {
		return this.afterClosedSubject.asObservable();
	}

	toggle(): void {
		if (this.minimized) {
			this.maximize();
		} else {
			this.minimize();
		}
	}

	maximize() {
		this.overlayRef.updateSize({
			height: this.openedHeight
		});
		this.overlayRef.addPanelClass('maximized');
		this.overlayRef.removePanelClass('minimized');
		this.minimized = false;
	}

	minimize() {
		this.overlayRef.updateSize({
			height: '40px'
		});
		this.overlayRef.addPanelClass('minimized');
		this.overlayRef.removePanelClass('maximized');
		this.minimized = true;
	}

	isMinimized(): boolean {
		return this.minimized;
	}

}
