import { Slot, SlotedInfo, WCInfo } from "../wc-extensions.service";

const slotedInfoMapper = (element: WCInfo, baseUrl: string): SlotedInfo  => ({
	componentName: element.componentName,
	url: (new URL(element.url, baseUrl)).toString(),
	title: element.title,
});

export class SlotsStorageService {
	private valuesToEmit: Map<Slot, SlotedInfo[]>;
	constructor(
		private readonly baseUrl: string,
	) {
		this.valuesToEmit = new Map();
		// Un menú mas a nivel sistema
		this.valuesToEmit.set(Slot.HOME_MENU, []);
		// Un menú mas a nivel institución
		this.valuesToEmit.set(Slot.INSTITUTION_MENU, []);
		// Un componente mas de la pantalla inicial del sistema
		this.valuesToEmit.set(Slot.SYSTEM_HOME_PAGE, []);
		// Un componente mas de la pantalla inicial de la institucion
		this.valuesToEmit.set(Slot.INSTITUTION_HOME_PAGE, []);
		// Una solapa mas en la historia clinica
		this.valuesToEmit.set(Slot.CLINIC_HISTORY_TAB, []);
	}


	put(webCompontentInfo: WCInfo) {
		const slot = webCompontentInfo.slot as Slot;
		const list = this.valuesToEmit.get(slot);

		if (!list) {
			console.warn(`Extension ${webCompontentInfo.slot} inexistente`);
		} else {
			list.push(slotedInfoMapper(webCompontentInfo, this.baseUrl));
		}
	}

	forEachSlot(fnc : (slot: Slot, list: SlotedInfo[]) => any) {
		this.valuesToEmit.forEach((list, slot) => fnc(slot, list));
	}
}
