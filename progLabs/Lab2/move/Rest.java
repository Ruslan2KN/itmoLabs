package move;
import ru.ifmo.se.pokemon.*;
public final class Rest extends StatusMove {
    public Rest(){
        super(Type.PSYCHIC, 0.0, 0.0 );
    }

    public static void sleep(Pokemon p){
        Effect self= (new Effect()).condition(Status.SLEEP).attack((double) 0.0).turns(2);
        p.setCondition(self);

    }
    public void applySelfEffects(Pokemon p){
        //p.setMod(Stat.HP, (int) (p.getStat(Stat.HP)-p.getHP()));
        p.addEffect(new Effect().stat(Stat.HP, (int) (p.getStat(Stat.HP)-p.getHP())));
        Rest.sleep(p);
    }
    @Override
    public String describe(){
        return "использует Rest";
    }
}
