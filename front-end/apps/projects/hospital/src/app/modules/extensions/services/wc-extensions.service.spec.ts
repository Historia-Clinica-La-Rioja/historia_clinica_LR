// import { TestBed } from '@angular/core/testing';

import { WCExtensionsService } from './wc-extensions.service';
import { ExtensionsService } from './extensions.service';
import { of } from 'rxjs';

describe('WCExtensionsService', () => {
	let wcExtensionsService: WCExtensionsService;
	let extensionsServiceMock: jasmine.SpyObj<ExtensionsService>;

	beforeEach(() => {
		extensionsServiceMock = jasmine.createSpyObj(
			'ExtensionsService', [
				'getExtensions',
				'getDefinition'
			]);
		const fakeObservable = of([]);
		extensionsServiceMock.getExtensions.and.returnValue(fakeObservable);
		wcExtensionsService = new WCExtensionsService(
			extensionsServiceMock,
		);

	});

	it('should be created', () => {
		expect(wcExtensionsService).toBeTruthy();
		expect(wcExtensionsService.getSystemHomeMenu()).toBeDefined()

	});

});
