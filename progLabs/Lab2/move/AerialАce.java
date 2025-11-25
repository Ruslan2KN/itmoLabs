package move;

import ru.ifmo.se.pokemon.*;

public final class AerialАce extends PhysAdditionalDamage {
    public AerialАce() {
        super(Type.FLYING, 60.0, 100.0);
    }
    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def){
        return true;
    }
    @Override
    public String describe(){
        return "использует Aerial Аce";
    }
    @Override
    public double calcTypeEffect(Pokemon var1, Pokemon var2){
        System.out.println( this.type.getEffect(var2.getTypes()));
        System.out.println( "Сработал calcTypeEffect ");
        return this.type.getEffect(var2.getTypes());
    }

}
