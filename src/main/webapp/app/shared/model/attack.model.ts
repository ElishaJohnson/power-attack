import { IWeapon } from 'app/shared/model/weapon.model';
import { IAttackCycle } from 'app/shared/model/attack-cycle.model';

export interface IAttack {
  id?: number;
  name?: string;
  attackModifier?: number;
  critChance?: number;
  critDamage?: number;
  dieValue?: number;
  diceCount?: number;
  damageBonus?: number;
  weapon?: IWeapon;
  attackCycles?: IAttackCycle[];
}

export class Attack implements IAttack {
  constructor(
    public id?: number,
    public name?: string,
    public attackModifier?: number,
    public critChance?: number,
    public critDamage?: number,
    public dieValue?: number,
    public diceCount?: number,
    public damageBonus?: number,
    public weapon?: IWeapon,
    public attackCycles?: IAttackCycle[]
  ) {}
}
