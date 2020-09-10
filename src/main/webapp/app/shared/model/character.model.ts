import { IWeapon } from 'app/shared/model/weapon.model';
import { IAttackCycle } from 'app/shared/model/attack-cycle.model';
import { IUser } from 'app/core/user/user.model';

export interface ICharacter {
  id?: number;
  name?: string;
  baseAttack?: number;
  weapons?: IWeapon[];
  attackCycles?: IAttackCycle[];
  user?: IUser;
}

export class Character implements ICharacter {
  constructor(
    public id?: number,
    public name?: string,
    public baseAttack?: number,
    public weapons?: IWeapon[],
    public attackCycles?: IAttackCycle[],
    public user?: IUser
  ) {}
}
