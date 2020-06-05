import { PublicService } from '@api-rest/services/public.service';
import { Component, OnInit } from '@angular/core';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

	flavoredImages: FlavoredImages = {
		tandil: [
			{
				location: 'assets/flavors/tandil/images/logos/ministerio-salud.png',
				alt: 'ministerio-salud'
			},
			{
				location: 'assets/flavors/tandil/images/logos/sisp.png',
				alt: 'sisp'
			},
			{
				location: 'assets/flavors/tandil/images/logos/minicipio-tandil.png',
				alt: 'minicipio-tandil'
			},
			{
				location: 'assets/flavors/tandil/images/logos/pladema.png',
				alt: 'pladema'
			},
			{
				location: 'assets/flavors/tandil/images/logos/unicen.png',
				alt: 'unicen'
			},
		],
		minsal: [
			{
				location: 'assets/flavors/minsal/images/logos/ministerio-salud.png',
				alt: 'ministerio-salud'
			},
			{
				location: 'assets/flavors/minsal/images/logos/pladema.png',
				alt: 'pladema'
			},
			{
				location: 'assets/flavors/minsal/images/logos/unicen.png',
				alt: 'unicen'
			},
		],
		chaco: [
			{
				location: 'assets/flavors/chaco/images/logos/ministerio-salud.png',
				alt: 'ministerio-salud'
			},
			{
				location: 'assets/flavors/chaco/images/logos/prov-chaco.png',
				alt: 'prov-chaco'
			},
			{
				location: 'assets/flavors/chaco/images/logos/ministerio-salud-chaco.png',
				alt: 'ministerio-salud-chaco'
			},
			{
				location: 'assets/flavors/chaco/images/logos/pladema.png',
				alt: 'pladema'
			},
			{
				location: 'assets/flavors/chaco/images/logos/unicen.png',
				alt: 'unicen'
			},
		],
	}

	images: ImageSrc[] = [];

	constructor(
		private readonly publicInfoService: PublicService,
	) { }

	ngOnInit(): void {
		this.publicInfoService.getInfo().subscribe(
			publicInfo => this.images = this.flavoredImages[publicInfo.flavor]
		);
	}

}
interface ImageSrc {
	location: string
	alt: string
}
interface FlavoredImages {
	tandil: ImageSrc[]
	minsal: ImageSrc[]
	chaco: ImageSrc[]
}
