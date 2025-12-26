package lab3.world.playgrounds;

import lab3.world.*;

import java.util.Objects;

public abstract class Playground implements Describable {
    private final Type type;
    public Playground(Type type){
        if (type==null){ throw new IllegalArgumentException("Type не может быть null"); }
        this.type=type;
    }
    public abstract String describe ();
    public Type getType(){
        return type;
    }
    public abstract boolean isReady();

    @Override
    public boolean equals(Object obj){
        if (this==obj) return true;
        if(obj==null || obj.getClass()!= getClass()) return false;
        Playground playground = (Playground) obj;
        return type== playground.type;
    }
    @Override
    public int hashCode(){
        return Objects.hash(type);
    }
    @Override
    public String toString(){
        return "Площадка типа "+this.getType();
    }
}
