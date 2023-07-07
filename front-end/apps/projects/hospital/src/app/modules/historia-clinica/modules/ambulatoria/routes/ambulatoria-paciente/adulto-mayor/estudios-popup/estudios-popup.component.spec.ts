import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstudiosPopupComponent } from './estudios-popup.component';

describe('EstudiosPopupComponent', () => {
  let component: EstudiosPopupComponent;
  let fixture: ComponentFixture<EstudiosPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstudiosPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EstudiosPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
