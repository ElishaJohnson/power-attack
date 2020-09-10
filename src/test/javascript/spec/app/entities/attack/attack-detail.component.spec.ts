import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PowerattackTestModule } from '../../../test.module';
import { AttackDetailComponent } from 'app/entities/attack/attack-detail.component';
import { Attack } from 'app/shared/model/attack.model';

describe('Component Tests', () => {
  describe('Attack Management Detail Component', () => {
    let comp: AttackDetailComponent;
    let fixture: ComponentFixture<AttackDetailComponent>;
    const route = ({ data: of({ attack: new Attack(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [AttackDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AttackDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttackDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attack on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attack).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
