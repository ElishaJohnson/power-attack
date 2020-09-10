import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWeapon } from 'app/shared/model/weapon.model';
import { WeaponService } from './weapon.service';

@Component({
  templateUrl: './weapon-delete-dialog.component.html'
})
export class WeaponDeleteDialogComponent {
  weapon?: IWeapon;

  constructor(protected weaponService: WeaponService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weaponService.delete(id).subscribe(() => {
      this.eventManager.broadcast('weaponListModification');
      this.activeModal.close();
    });
  }
}
