import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttackCycle } from 'app/shared/model/attack-cycle.model';

@Component({
  selector: 'jhi-attack-cycle-detail',
  templateUrl: './attack-cycle-detail.component.html'
})
export class AttackCycleDetailComponent implements OnInit {
  attackCycle: IAttackCycle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attackCycle }) => (this.attackCycle = attackCycle));
  }

  previousState(): void {
    window.history.back();
  }
}
