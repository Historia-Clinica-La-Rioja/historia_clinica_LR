import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntecentesPersonalesComponent } from './antecentes-personales.component';

describe('AntecentesPersonalesComponent', () => {
  let component: AntecentesPersonalesComponent;
  let fixture: ComponentFixture<AntecentesPersonalesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntecentesPersonalesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntecentesPersonalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
