package pokemon;
import ru.ifmo.se.pokemon.*;
public  class Swinub extends Pokemon{
    public Swinub(String name, int level){
        super(name, level);
        setType(Type.ICE,Type.GROUND);
        setStats(50.0, 50.0, 40.0, 30.0, 30.0, 50.0);
        this.addMove(new move.PowderSnow());
        this.addMove(new move.DoubleTeam());
    }
}
