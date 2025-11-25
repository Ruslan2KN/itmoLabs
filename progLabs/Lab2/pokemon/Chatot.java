package pokemon;
import ru.ifmo.se.pokemon.*;
//import move.*;
public final class Chatot extends Pokemon{
    public Chatot(String name, int level){
        super(name, level);
        setType(Type.NORMAL,Type.FLYING);
        setStats(76.0, 65.0, 45.0, 92.0, 42.0, 91.0);
        this.addMove(new move.AerialАce());
        this.addMove(new move.AirCutter());
        this.addMove(new move.SteelWing());
        this.addMove(new move.Sing());
    }

}
