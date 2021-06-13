import { Injectable } from '@angular/core';
import { Observable, Observer, of } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class ImageDecoderService {

	constructor() {
	}

	decode(imageData: string): Observable<string> {
		if (imageData) {
			return this.getBase64ImageFromURL(imageData).pipe(map(base64data => {
				return 'data:image/jpg;base64,' + base64data;
			}));
		}
		return of(undefined);
	}

	private getBase64ImageFromURL(url: string): Observable<string> {
		return new Observable((observer: Observer<string>) => {
			// create an image object
			const img = new Image();
			img.crossOrigin = 'Anonymous';
			img.src = url;
			if (!img.complete) {
				// This will call another method that will create image from url
				img.onload = () => {
					observer.next(this.getBase64Image(img));
					observer.complete();
				};
				img.onerror = (err) => {
					observer.error(err);
				};
			} else {
				observer.next(this.getBase64Image(img));
				observer.complete();
			}
		});
	}

	private getBase64Image(img: HTMLImageElement) {
		// We create a HTML canvas object that will create a 2d image
		const canvas = document.createElement('canvas');
		canvas.width = img.width;
		canvas.height = img.height;
		const ctx = canvas.getContext('2d');
		// This will draw image
		ctx.drawImage(img, 0, 0);
		// Convert the drawn image to Data URL
		const dataURL = canvas.toDataURL('image/png');
		return dataURL.replace(/^data:image\/(png|jpg);base64,/, '');
	}
}
