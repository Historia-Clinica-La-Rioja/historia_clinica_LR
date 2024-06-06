import {
	Slot,
	SlotedInfo,
	WCInfo,
} from '../wc-extensions.service';

const slotedInfoMapper = (element: WCInfo, slot: Slot, baseUrl: string): SlotedInfo  => ({
	slot,
	componentName: element.componentName,
	fullUrl: (new URL(element.url, baseUrl)).toString(),
	title: element.title,
});

// const wcNameIs = (name: string) => (slotedInfo: SlotedInfo) => slotedInfo.componentName === name;
const wcSlotIs = (slot: Slot) => (slotedInfo: SlotedInfo) => slotedInfo.slot === slot;

export class SlotsStorageService {
	private valuesToEmit: SlotedInfo[] = [];

	constructor(
	) {
	}

	get length(): number {
		return this.valuesToEmit.length;
	}

	wcForSlot(slot: Slot): SlotedInfo[] {
		return this.valuesToEmit.filter(wcSlotIs(slot));
	}

	addAll(list: WCInfo[], baseUrl: string) {
		list.forEach(
			webCompontentInfo => this.put(webCompontentInfo, baseUrl)
		)
	}

	private put(webCompontentInfo: WCInfo, baseUrl: string) {
		const slot = webCompontentInfo.slot as Slot;

		if (!slot) {
			console.warn(`Extension ${webCompontentInfo.slot} inexistente`);
			return;
		}
		if (!webCompontentInfo.url) {
			console.warn(`Extension ${webCompontentInfo.slot} no tiene url`);
			return;
		}
		if (!webCompontentInfo.componentName) {
			console.warn(`Extension ${webCompontentInfo.slot} no tiene componentName`);
			return;
		}
		this.valuesToEmit.push(slotedInfoMapper(webCompontentInfo, slot, baseUrl))
	}
}
