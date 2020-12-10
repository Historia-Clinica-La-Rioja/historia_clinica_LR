import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgregarPreinscripcionItemComponent } from './agregar-preinscripcion-item.component';

describe('AgregarPreinscripcionItemComponent', () => {
  let component: AgregarPreinscripcionItemComponent;
  let fixture: ComponentFixture<AgregarPreinscripcionItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgregarPreinscripcionItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgregarPreinscripcionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
