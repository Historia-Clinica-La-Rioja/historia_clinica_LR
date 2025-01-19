
import { findSlotedInfoById } from './wc-extensions.mappers';
import { SlotedInfo } from './wc-extensions.service';

const mockSlotedInfo = (componentName: string): SlotedInfo => ({
	componentName,
	fullUrl: `http://someurl.componentName.com/main.js`,
} as SlotedInfo);

describe('wc-extensions.mappers.findSlotedInfoById = (wcId) => (slotedInfoList): SlotedInfo', () => {
	it('Should return undefined if wcId or slotedInfoList is not defined', () => {
		expect(findSlotedInfoById(undefined)(undefined)).toBeUndefined();
		expect(findSlotedInfoById(undefined)([])).toBeUndefined();
		expect(findSlotedInfoById('an-id')(undefined)).toBeUndefined();
		expect(findSlotedInfoById('an-id')([])).toBeUndefined();
	});

	it('Should return undefined if wcId or slotedInfoList is not defined', () => {
		const slotedInfoList = [mockSlotedInfo('an-id'), mockSlotedInfo('an-id')];
		expect(findSlotedInfoById('an-id')(slotedInfoList)).toBeDefined();
		expect(findSlotedInfoById('an-id')(slotedInfoList).componentName).toBe('an-id');
	});
});

describe('wc-extensions.mappers.slotedInfoMapper = (definition: ExtensionComponentDto) => (element: WCInfo): SlotedInfo', () => {

});
