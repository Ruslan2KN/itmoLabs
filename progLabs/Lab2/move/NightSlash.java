package move;
import ru.ifmo.se.pokemon.*;
public final class NightSlash extends PhysAdditionalDamage {
    public NightSlash(){
        super(Type.DARK, 70.0, 100.0);
    }
    @Override
    public double calcCriticalHit(Pokemon att, Pokemon def){
        if ((att.getStat(Stat.SPEED)*(double) 3.0F ) / (double)512.0F >= Math.random()){
            System.out.println("Crit Damage");
            return (double) 2.0F;

        }
        else {
            return (double) 1.0F;
        }
    }
    @Override
    public String describe(){
        return "использует Night Slash";
    }
}
