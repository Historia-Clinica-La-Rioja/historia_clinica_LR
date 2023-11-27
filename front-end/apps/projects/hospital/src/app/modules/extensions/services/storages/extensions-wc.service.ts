import { ExtensionComponentDto } from '@extensions/extensions-model';
import {
	WCInfo,
} from '../wc-extensions.service';
import { SlotsStorageService } from './slots-storage.service';

const extensionNameIs = (name: string) => (e: ExtensionComponentDto) => e.name === name;
const extensionNameNotIs = (name: string) => (e: ExtensionComponentDto) => e.name !== name;

export class ExtensionsWCService {
	public wcSlots: SlotsStorageService;
	private extensionsToLoad: ExtensionComponentDto[];

	constructor(
		extensions: ExtensionComponentDto[],
	) {
		this.extensionsToLoad = extensions.filter(
			(e, i) => i === extensions.findIndex(extensionNameIs(e.name))
		);
		this.wcSlots = new SlotsStorageService();
	}

	get stillLoading(): ExtensionComponentDto[] {
		return this.extensionsToLoad;
	}

	loaded(extension: ExtensionComponentDto, defPluginArr: WCInfo[]) {
		this.extensionsToLoad = this.extensionsToLoad.filter(extensionNameNotIs(extension.name));
		this.wcSlots.addAll(defPluginArr, extension.path);
	}

}
