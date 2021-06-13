import { Asset, Favicon, SponsorLogo, Icon72, Icon96, Icon128, Icon144, Icon152, Icon192, Icon384, Icon512 } from './asset.model';

export abstract class AssetCreator {
	public abstract factoryMethod(): Asset;
}

export class FaviconCreator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Favicon();
	}
}

export class SponsorLogoCreator extends AssetCreator {
	public factoryMethod(): Asset {
		return new SponsorLogo();
	}
}

export class Icon72Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon72();
	}
}

export class Icon96Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon96();
	}
}

export class Icon128Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon128();
	}
}

export class Icon144Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon144();
	}
}

export class Icon152Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon152();
	}
}

export class Icon192Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon192();
	}
}

export class Icon384Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon384();
	}
}

export class Icon512Creator extends AssetCreator {
	public factoryMethod(): Asset {
		return new Icon512();
	}
}
