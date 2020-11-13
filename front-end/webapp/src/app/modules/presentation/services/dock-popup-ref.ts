import { OverlayRef } from '@angular/cdk/overlay';

export class DockPopupRef {

	private minimized = false;
	private openedHeight: number | string;

	constructor(private overlayRef: OverlayRef) {
		this.openedHeight = overlayRef.getConfig().height;
	}

	close(): void {
		this.overlayRef.dispose();
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
		this.minimized = false;
	}

	minimize() {
		this.overlayRef.updateSize({
			height: '40px'
		});
		this.minimized = true;
	}

	isMinimized(): boolean {
		return this.minimized;
	}
}
