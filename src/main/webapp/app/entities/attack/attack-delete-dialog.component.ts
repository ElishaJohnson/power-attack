import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttack } from 'app/shared/model/attack.model';
import { AttackService } from './attack.service';

@Component({
  templateUrl: './attack-delete-dialog.component.html'
})
export class AttackDeleteDialogComponent {
  attack?: IAttack;

  constructor(protected attackService: AttackService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attackService.delete(id).subscribe(() => {
      this.eventManager.broadcast('attackListModification');
      this.activeModal.close();
    });
  }
}
