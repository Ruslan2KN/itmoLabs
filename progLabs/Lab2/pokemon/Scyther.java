package pokemon;
import ru.ifmo.se.pokemon.*;
import move.*;
public class Scyther extends Pokemon {
    public Scyther(String name, int level){
        super(name, level);
        setType(Type.BUG, Type.FLYING);
        setStats(70.0, 110.0, 80.0, 55.0, 80.0, 105.0);
        this.addMove(new NightSlash());
        this.addMove(new move.DoubleTeam());
        this.addMove(new move.Slash());

    }
}
