package pokemon;
import ru.ifmo.se.pokemon.*;
public final class Mamoswine extends Piloswine{
    public Mamoswine (String name, int level){
        super (name, level);
        setStats(110.0, 130.0, 80.0, 70.0, 60.0, 80.0);
        addMove(new move.Rest());
    }

}
