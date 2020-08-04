import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BedDetailComponent } from './Bed-detail.component';

describe('BedDetailComponent', () => {
  let component: BedDetailComponent;
  let fixture: ComponentFixture<BedDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BedDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BedDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
