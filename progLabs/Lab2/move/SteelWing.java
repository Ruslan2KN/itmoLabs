package move;
import ru.ifmo.se.pokemon.*;

import java.util.Random;

public final class SteelWing extends PhysAdditionalDamage {
    public SteelWing(){
        super(Type.STEEL, 70.0, 90.0);
    }
    @Override
    protected void applySelfEffects(Pokemon p){
        Random random=new Random();
        if ((random.nextDouble()<= (double) 0.1F)) {
            //p.setMod(Stat.DEFENSE,1);
            p.addEffect(new Effect().stat(Stat.DEFENSE,1).chance(0.1));

        }
    }
    @Override
    public String describe(){
        return "использует Steel Wing";
    }
}
