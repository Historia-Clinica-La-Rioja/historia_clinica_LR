import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IconedTextComponent } from './iconed-text.component';

describe('IconedTextComponent', () => {
  let component: IconedTextComponent;
  let fixture: ComponentFixture<IconedTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IconedTextComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IconedTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
