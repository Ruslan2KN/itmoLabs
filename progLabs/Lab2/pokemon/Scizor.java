package pokemon;
import ru.ifmo.se.pokemon.*;
public final class Scizor extends Scyther {
    public Scizor(String name, int level){
        super(name, level);
        setType(Type.BUG, Type.STEEL );
        setStats(70.0, 130.0, 100.0, 55.0, 80.0, 65.0);
        addMove(new move.MetalClaw());

    }
}
