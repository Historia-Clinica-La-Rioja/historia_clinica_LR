import { Component, Input, OnInit } from '@angular/core';
import { PersonPhotoDto } from '@api-rest/api-model';
import { Observable, Observer } from 'rxjs';

@Component({
	selector: 'app-patient-card',
	templateUrl: './patient-card.component.html',
	styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent implements OnInit {

	@Input() patient: PatientBasicData;
	@Input() personPhoto: PersonPhotoDto;
	decodedPhoto;
	constructor() { }

	ngOnInit(): void {
			if(this.personPhoto.imageData){
				this.getBase64ImageFromURL(this.personPhoto.imageData).subscribe(base64data => {
					// this is the image as dataUrl
					this.decodedPhoto = 'data:image/jpg;base64,' + base64data;
				}); 
			}
	}

	public viewGenderAge() {
		const gender = (this.patient?.gender) ? (this.patient.gender + ' · ') : '';
		const age = (this.patient?.age) ? (this.patient.age + ' años') : '';
		return gender + age;
	}



	getBase64ImageFromURL(url: string) {
		return Observable.create((observer: Observer<string>) => {
			// create an image object
			let img = new Image();
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

	getBase64Image(img: HTMLImageElement) {
		// We create a HTML canvas object that will create a 2d image
		var canvas = document.createElement("canvas");
		canvas.width = img.width;
		canvas.height = img.height;
		var ctx = canvas.getContext("2d");
		// This will draw image
		ctx.drawImage(img, 0, 0);
		// Convert the drawn image to Data URL
		var dataURL = canvas.toDataURL("image/png");
		return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
	}
}

export class PatientBasicData {
	id: number;
	firstName: string;
	middleNames?: string;
	lastName: string;
	otherLastNames?: string;
	gender?: string;
	age?: number;
}
