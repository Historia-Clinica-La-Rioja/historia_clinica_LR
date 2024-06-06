import {
	Slot,
	WCInfo,
} from '../wc-extensions.service';
import { SlotsStorageService } from './slots-storage.service';

const webCompontentInfo = (valuesToOverride = {}): WCInfo => {
	return {
		slot: 'HOME_MENU',
		url: 'index.js',
		componentName: 'wc-externo',
		...valuesToOverride
	} as WCInfo;
}

describe('SlotsStorageService', () => {
	const baseUrl = 'http://local.env';
	let slotsStorageService: SlotsStorageService;

	beforeEach(() => {
		slotsStorageService = new SlotsStorageService();
	});

	it('should be created', () => {
		expect(slotsStorageService).toBeTruthy();
		expect(slotsStorageService.length).toBe(0);
		expect(slotsStorageService.wcForSlot(Slot.CLINIC_HISTORY_TAB).length).toBe(0);
	});

	it('should ignore WebComponent info when required data is missing', () => {
		slotsStorageService.addAll([webCompontentInfo({slot: undefined})], baseUrl);
		expect(slotsStorageService.length).toBe(0);
		slotsStorageService.addAll([webCompontentInfo({url: undefined})], baseUrl);
		expect(slotsStorageService.length).toBe(0);
		slotsStorageService.addAll([webCompontentInfo({componentName: undefined})], baseUrl);
		expect(slotsStorageService.length).toBe(0);
	});

	it('should store WebComponent info', () => {
		slotsStorageService.addAll([webCompontentInfo()], baseUrl);
		expect(slotsStorageService.length).toBe(1);
		expect(slotsStorageService.wcForSlot(Slot.HOME_MENU).length).toBe(1);
		expect(slotsStorageService.wcForSlot(Slot.CLINIC_HISTORY_TAB).length).toBe(0);
	});

	it('should store duplicated WebComponent', () => {
		slotsStorageService.addAll([webCompontentInfo(), webCompontentInfo()], baseUrl);
		slotsStorageService.addAll([webCompontentInfo()], baseUrl);
		expect(slotsStorageService.length).toBe(3);
		expect(slotsStorageService.wcForSlot(Slot.HOME_MENU).length).toBe(3);
		expect(slotsStorageService.wcForSlot(Slot.CLINIC_HISTORY_TAB).length).toBe(0);
	});
});
