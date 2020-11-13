import { ComponentRef, Injectable, Injector } from '@angular/core';
import { ComponentPortal, PortalInjector } from '@angular/cdk/portal';
import { Overlay, OverlayConfig, OverlayRef } from '@angular/cdk/overlay';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { DockPopupComponent } from '../components/dock-popup/dock-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';

@Injectable({
	providedIn: 'root'
})
export class DockPopupService {

	overlayRef: OverlayRef;

	constructor(
		private readonly overlay: Overlay,
		private readonly injector: Injector
	) {
	}

	open(): DockPopupRef {

		const overlayRef = this.overlay.create(this.getOverlayConfig());
		const dockPopupRef = new DockPopupRef(overlayRef);
		const overlayComponent = this.attachDialogContainer(overlayRef, null, dockPopupRef);
		return dockPopupRef;
	}

	private attachDialogContainer(overlayRef: OverlayRef, data: any, appOverlayRef: DockPopupRef) {
		const injector = this.createInjector(data, appOverlayRef);

		const containerPortal = new ComponentPortal(DockPopupComponent, null, injector);
		const containerRef: ComponentRef<DockPopupComponent> = overlayRef.attach(containerPortal);

		return containerRef.instance;
	}

	private createInjector(data: any, overlayRef: DockPopupRef): PortalInjector {
		// Instantiate new WeakMap for our custom injection tokens
		const injectionTokens = new WeakMap();

		// Set custom injection tokens
		injectionTokens.set(DockPopupRef, overlayRef);
		injectionTokens.set(OVERLAY_DATA, data);

		// Instantiate new PortalInjector
		return new PortalInjector(this.injector, injectionTokens);
	}

	private getOverlayConfig(): OverlayConfig {
		const positionStrategy = this.overlay.position()
			.global()
			.bottom()
			.right();

		const overlayConfig = new OverlayConfig({
			scrollStrategy: this.overlay.scrollStrategies.noop(),
			positionStrategy,
			height: '70vh',
			width: '35vw',
			panelClass: 'overlay-class',
		});

		return overlayConfig;
	}
}

