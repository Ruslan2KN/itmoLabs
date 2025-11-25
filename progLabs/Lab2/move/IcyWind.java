package move;
import ru.ifmo.se.pokemon.*;
public final class IcyWind extends SpecAdditionDamage {
    public IcyWind(){
        super(Type.ICE, 55.0, 95.0 );

    }
    @Override
    public void applyOppEffects(Pokemon p){
        System.out.println(p.getStat(Stat.SPEED));
        p.addEffect(new Effect().stat(Stat.SPEED,-1));
        System.out.println(p.getStat(Stat.SPEED));
        //p.setMod(Stat.SPEED,-1);
    }
    @Override
    public String describe(){
        return "использует Icy Wind";
    }


}
