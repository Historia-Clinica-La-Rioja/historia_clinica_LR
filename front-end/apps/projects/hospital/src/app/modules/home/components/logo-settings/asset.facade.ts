import { FaviconCreator, SponsorLogoCreator, Icon72Creator, Icon96Creator, Icon128Creator, Icon144Creator, Icon152Creator, Icon192Creator, Icon384Creator, Icon512Creator, AssetCreator } from './asset.creator';
import { Asset } from './asset.model';

export function addAvailableAssetsToList(): Asset[] {
	return [
		clientCode(new FaviconCreator()),
		clientCode(new SponsorLogoCreator()),
		clientCode(new Icon72Creator()),
		clientCode(new Icon96Creator()),
		clientCode(new Icon128Creator()),
		clientCode(new Icon144Creator()),
		clientCode(new Icon152Creator()),
		clientCode(new Icon192Creator()),
		clientCode(new Icon384Creator()),
		clientCode(new Icon512Creator()),
	];
}

function clientCode(creator: AssetCreator): Asset {
	return creator.factoryMethod();
}
