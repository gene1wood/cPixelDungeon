/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.cpixeldungeon.actors.blobs;

import com.watabou.cpixeldungeon.Assets;
import com.watabou.cpixeldungeon.Dungeon;
import com.watabou.cpixeldungeon.Journal;
import com.watabou.cpixeldungeon.Journal.Feature;
import com.watabou.cpixeldungeon.actors.buffs.Hunger;
import com.watabou.cpixeldungeon.actors.hero.Hero;
import com.watabou.cpixeldungeon.effects.BlobEmitter;
import com.watabou.cpixeldungeon.effects.CellEmitter;
import com.watabou.cpixeldungeon.effects.Speck;
import com.watabou.cpixeldungeon.effects.particles.ShaftParticle;
import com.watabou.cpixeldungeon.items.DewVial;
import com.watabou.cpixeldungeon.items.Item;
import com.watabou.cpixeldungeon.items.potions.PotionOfHealing;
import com.watabou.cpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfHealth extends WellWater {

	private static final String TXT_PROCCED =
		"As you take a sip, you feel your wounds heal completely.";
	
	@Override
	protected boolean affectHero( Hero hero ) {
		
		Sample.INSTANCE.play( Assets.SND_DRINK );
		
		PotionOfHealing.heal( hero );
		hero.belongings.uncurseEquipped();
		DewVial vial = hero.belongings.getItem( DewVial.class );
		
		if (vial != null)
		{
			vial.fill();
		}
		
		((Hunger)hero.buff( Hunger.class )).satisfy( Hunger.STARVING );
		
		CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

		Dungeon.hero.interrupt();
	
		GLog.p( TXT_PROCCED );
		
		Journal.remove( Feature.WELL_OF_HEALTH );
		
		return true;
	}
	
	@Override
	protected Item affectItem( Item item ) {
		if (item instanceof DewVial && !((DewVial)item).isFull()) {
			((DewVial)item).fill();
			return item;
		}
		
		return null;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );	
		emitter.start( Speck.factory( Speck.HEALING ), 0.5f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return 
			"Power of health radiates from the water of this well. " +
			"Take a sip from it to heal your wounds and satisfy hunger.";
	}
}
