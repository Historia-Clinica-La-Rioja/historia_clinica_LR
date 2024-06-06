import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-img-carousel',
    templateUrl: './img-carousel.component.html',
    styleUrls: ['./img-carousel.component.scss']
})
export class ImgCarouselComponent implements OnInit {

    @Input() imgList: string[] = [];
    currentIndex: number = 0;
    prevBtnDisabled: boolean;
    nextBtnDisabled: boolean;

    constructor() { }

    ngOnInit() {
        this.handleBtnDisabled();
    }

    getPrev() {
        this.currentIndex == 0 ? (this.currentIndex = this.imgList.length - 1) : this.currentIndex--;
        this.handleBtnDisabled();
    }

    getNext() {
        this.currentIndex < this.imgList.length - 1 ? this.currentIndex++ : (this.currentIndex = 0);
        this.handleBtnDisabled();
    }

    private handleBtnDisabled() {
        this.prevBtnDisabled = this.currentIndex == 0;
        this.nextBtnDisabled = this.currentIndex == (this.imgList.length - 1);
    }
}
