export interface Asset {
	name: string;
	path: string;
	width: number;
	height: number;
	extension: string;
	getName(): string;
	getPath(): string;
	getWidth(): number;
	getHeight(): number;
	getExtension(): string;
}

export class Favicon implements Asset {
	public name = 'favicon';
	public path = 'favicon.ico';
	public width = 16;
	public height = 16;
	public extension = '.ico';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class SponsorLogo implements Asset {
	public name = 'logotipo de auspiciante';
	public path = 'sponsor-logo-512x128.png';
	public width = 512;
	public height = 128;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon72 implements Asset {
	public name = 'icono de 72 x 72 pixeles';
	public path = 'icons/icon-72x72.png';
	public width = 72;
	public height = 72;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon96 implements Asset {
	public name = 'icono de 96 x 96 pixeles';
	public path = 'icons/icon-96x96.png';
	public width = 96;
	public height = 96;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon128 implements Asset {
	public name = 'icono de 128 x 128 pixeles';
	public path = 'icons/icon-128x128.png';
	public width = 128;
	public height = 128;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon144 implements Asset {
	public name = 'icono de 144 x 144 pixeles';
	public path = 'icons/icon-144x144.png';
	public width = 144;
	public height = 144;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon152 implements Asset {
	public name = 'icono de 152 x 152 pixeles';
	public path = 'icons/icon-152x152.png';
	public width = 152;
	public height = 152;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon192 implements Asset {
	public name = 'icono de 192 x 192 pixeles';
	public path = 'icons/icon-192x192.png';
	public width = 192;
	public height = 192;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon384 implements Asset {
	public name = 'icono de 384 x 384 pixeles';
	public path = 'icons/icon-384x384.png';
	public width = 384;
	public height = 384;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}

export class Icon512 implements Asset {
	public name = 'icono de 512 x 512 pixeles';
	public path = 'icons/icon-512x512.png';
	public width = 512;
	public height = 512;
	public extension = '.png';

	getName(): string {
		return this.name;
	}
	getPath(): string {
		return this.path;
	}
	getWidth(): number {
		return this.width;
	}
	getHeight(): number {
		return this.height;
	}
	getExtension(): string {
		return this.extension;
	}
}
