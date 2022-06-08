import { ElementRef } from '@angular/core';
import { ComponentRef, Injectable, Injector } from '@angular/core';
import { ComponentPortal, ComponentType, PortalInjector } from '@angular/cdk/portal';
import { GlobalPositionStrategy, Overlay, OverlayConfig, OverlayRef } from '@angular/cdk/overlay';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { take } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class DockPopupService {

	eventsSubscription: Subscription;
	overlayRef: OverlayRef;

	constructor(
		private readonly overlay: Overlay,
		private readonly injector: Injector,
		private readonly router: Router
	) {
	}

	open(type: ComponentType<any>, data?: any): DockPopupRef {
		const overlayRef = this.overlay.create(this.getOverlayConfig());
		return this.createDockPopUp(overlayRef, type, data);
	}

	openOnTop(type: ComponentType<any>, data?: any): DockPopupRef {
		const overlayRef = this.overlay.create(this.getOverlayOnTopConfig());
		return this.createDockPopUp(overlayRef, type, data);
	}

	private createDockPopUp(overlayRef: OverlayRef, type: ComponentType<any>, data: any): DockPopupRef {
		const dockPopupRef = new DockPopupRef(overlayRef);
		this.attachDialogContainer(overlayRef, data, dockPopupRef, type);
		this.eventsSubscription = this.router.events.pipe(take(1)).subscribe(_ => dockPopupRef.close());
		return dockPopupRef;
	}

	private attachDialogContainer(overlayRef: OverlayRef, data: any, appOverlayRef: DockPopupRef, type: ComponentType<any>) {
		const injector = this.createInjector(data, appOverlayRef);

		const containerPortal = new ComponentPortal(type, null, injector);
		const containerRef: ComponentRef<any> = overlayRef.attach(containerPortal);

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
		const positionStrategy: GlobalPositionStrategy = this.overlay.position().global().right().bottom();
		const overlayConfig = new OverlayConfig({
			scrollStrategy: this.overlay.scrollStrategies.noop(),
			positionStrategy,
			maxHeight: '95vh',
			width: '40vw',
			panelClass: ['dock-popup-overlay', 'maximized'],
		});
		return overlayConfig;
	}

	private getOverlayOnTopConfig(): OverlayConfig {
		let positionStrategy: GlobalPositionStrategy = this.overlay.position().global().right().top();
		const overlayConfig = new OverlayConfig({
			scrollStrategy: this.overlay.scrollStrategies.noop(),
			positionStrategy,
			maxHeight: '95vh',
			width: '40vw',
			panelClass: ['dock-popup-overlay', 'maximized'],
		});

		return overlayConfig;
	}
}

