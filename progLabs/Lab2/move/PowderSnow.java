package move;
import ru.ifmo.se.pokemon.*;
public final class PowderSnow extends SpecAdditionDamage{
    public PowderSnow(){
        super(Type.ICE, 40.0, 100.0 );
    }
    /**public static void freeze(Pokemon p){
        if (!p.hasType(Type.ICE)){
            Effect opp= (new Effect()).condition(Status.FREEZE).attack((double) 0.0F).turns(-1);
            p.setCondition(opp);
        } */
    @Override
    public void applyOppEffects(Pokemon p){
        Effect.freeze(p);
    }
    @Override
    public String describe(){
        return "использует Powder Snow";
    }
}
