package move;
import ru.ifmo.se.pokemon.*;
public final class Sing extends StatusMove{
    public Sing(){
        super(Type.NORMAL,0.0, 55.0);
    }

    /**public static void sleep(Pokemon p) {
    Effect opp= (new Effect()).condition(Status.SLEEP).attack((double) 0.0F).turns((int) (Math.random() * (double) 3.0F + (double) 1.0F  ));
    p.setCondition(opp);
    } */
    @Override
    public void applyOppEffects(Pokemon p){
        Effect.sleep(p);
    }
    @Override
    public String describe(){
        return "использует Sing";
    }
}
