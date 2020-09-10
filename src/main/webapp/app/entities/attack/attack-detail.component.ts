import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttack } from 'app/shared/model/attack.model';

@Component({
  selector: 'jhi-attack-detail',
  templateUrl: './attack-detail.component.html'
})
export class AttackDetailComponent implements OnInit {
  attack: IAttack | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attack }) => (this.attack = attack));
  }

  previousState(): void {
    window.history.back();
  }
}
