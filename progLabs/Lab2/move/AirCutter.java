package move;
import ru.ifmo.se.pokemon.*;

public final class AirCutter extends SpecAdditionDamage {
    public AirCutter(){
        super(Type.FLYING,55.0,95.0);
    }
    @Override
    public double calcCriticalHit(Pokemon att, Pokemon def){
        if ((att.getStat(Stat.SPEED)*(double) 3.0F)/ (double) 512.0F >= Math.random()){
            System.out.println("Crit Damage");
            //System.out.println(att.getStat(Stat.SPEED));
            return (double ) 2.0F;
        }
        else {
            return (double) 1.0F;
        }
    }
    @Override
    public String describe(){
        return "использует Air Cutter";
    }
}
