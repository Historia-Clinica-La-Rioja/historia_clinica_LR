import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewGestationPopupComponent } from './new-gestation-popup.component';

describe('NewGestationPopupComponent', () => {
  let component: NewGestationPopupComponent;
  let fixture: ComponentFixture<NewGestationPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewGestationPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewGestationPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
