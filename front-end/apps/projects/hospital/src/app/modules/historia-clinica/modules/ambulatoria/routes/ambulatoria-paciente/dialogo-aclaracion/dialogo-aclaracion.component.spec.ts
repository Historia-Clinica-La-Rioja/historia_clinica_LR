import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogoAclaracionComponent } from './dialogo-aclaracion.component';

describe('DialogoAclaracionComponent', () => {
  let component: DialogoAclaracionComponent;
  let fixture: ComponentFixture<DialogoAclaracionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogoAclaracionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogoAclaracionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
