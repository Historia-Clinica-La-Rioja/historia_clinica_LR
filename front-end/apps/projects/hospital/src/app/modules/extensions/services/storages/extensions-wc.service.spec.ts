import { ExtensionComponentDto } from '@extensions/extensions-model';
import {
	Slot,
	WCInfo,
} from '../wc-extensions.service';
import { ExtensionsWCService } from './extensions-wc.service';

const NO_EXTENSIONS = [];
const DEFAULT_TESTING_SLOT = Slot.HOME_MENU;

const extensionComponent = (valuesToOverride = {}): ExtensionComponentDto => {
	return {
		name: 'My Extension',
		path: 'http://url.extension.com',
		...valuesToOverride
	} as ExtensionComponentDto;
}

const webCompontentInfo = (valuesToOverride = {}): WCInfo => {
	return {
		slot: DEFAULT_TESTING_SLOT,
		url: 'index.js',
		componentName: 'external-wc',
		...valuesToOverride
	} as WCInfo;
}

describe('ExtensionsWCService', () => {

	it('should be created', () => {
		const extensionsWCService = new ExtensionsWCService(NO_EXTENSIONS);
		expect(extensionsWCService).toBeTruthy();
	});

	it('should be fully loaded when no extensions is set', () => {
		const extensionsWCService = new ExtensionsWCService(NO_EXTENSIONS);
		expect(extensionsWCService.stillLoading.length).toBe(0);
		expect(extensionsWCService.wcSlots.length).toBe(0);
	});

	it('should shown still loading all extensions', () => {
		const extensions = [
			extensionComponent(),
		];
		const extensionsWCService = new ExtensionsWCService(extensions);
		expect(extensionsWCService.stillLoading.length).toBe(1);
		expect(extensionsWCService.wcSlots.length).toBe(0);
	});

	it('should handle duplicated extensions', () => {
		const extensions = [
			extensionComponent(),
			extensionComponent(),
		];
		const extensionsWCService = new ExtensionsWCService(extensions);
		expect(extensionsWCService).toBeTruthy();
		expect(extensionsWCService.stillLoading.length).toBe(1);
		expect(extensionsWCService.wcSlots.length).toBe(0);
	});

	it('should record loaded extensions', () => {
		// setup
		const extensions = [ extensionComponent() ];
		const extensionsWCService = new ExtensionsWCService(extensions);
		expect(extensionsWCService.stillLoading.length).toBe(1);
		// test
		extensionsWCService.loaded(extensions[0], [ ]);
		expect(extensionsWCService.stillLoading.length).toBe(0);
		expect(extensionsWCService.wcSlots.length).toBe(0);
	});

	it('should handle duplicated WC in one Extension', () => {
		// setup
		const extensions = [ extensionComponent() ];
		const extensionsWCService = new ExtensionsWCService(extensions);
		expect(extensionsWCService.stillLoading.length).toBe(1);
		// test
		extensionsWCService.loaded(
			extensions[0],
			[
				webCompontentInfo(),
				webCompontentInfo(),
				webCompontentInfo({componentName: 'otro'}),
				webCompontentInfo({componentName: 'otro'}),
			],
		);
		expect(extensionsWCService.stillLoading.length).toBe(0);
		expect(extensionsWCService.wcSlots.length).toBe(4);
	});

	it('should handle duplicated WC in two Extension', () => {
		const extensions = [
			extensionComponent({name: 'e1'}),
			extensionComponent({name: 'e2'}),
		];
		const extensionsWCService = new ExtensionsWCService(extensions);
		extensionsWCService.loaded(
			extensions[0],
			[
				webCompontentInfo({componentName: 'wc1'}),
			],
		);
		expect(extensionsWCService.stillLoading.length).toBe(1);
		expect(extensionsWCService.wcSlots.length).toBe(1);
		extensionsWCService.loaded(
			extensions[1],
			[
				webCompontentInfo({componentName: 'wc1'}),
			],
		);
		expect(extensionsWCService.stillLoading.length).toBe(0);
		expect(extensionsWCService.wcSlots.length).toBe(2);
	});

});
