import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntryCallRendererComponent } from './entry-call-renderer.component';

describe('EntryCallRendererComponent', () => {
  let component: EntryCallRendererComponent;
  let fixture: ComponentFixture<EntryCallRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EntryCallRendererComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntryCallRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
