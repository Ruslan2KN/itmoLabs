package pokemon;

import ru.ifmo.se.pokemon.*;

public  class Piloswine extends Swinub {
    public Piloswine(String name, int level){
        super(name, level);
        setStats(100.0, 100.0, 80.0, 60.0, 60.0,50.0 );
        addMove(new move.IcyWind());

    }
}
