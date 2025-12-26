package lab3.world.playgrounds;

import java.util.Objects;

public class Basketball extends Playground {
    private  boolean hasHoops;  // Флаг наличия колец
    private boolean has3PointShotLine;
    public Basketball(boolean hasHoops, boolean has3PointShotLine){
        super(Type.BASKETBALL_COURT);
        this.hasHoops=hasHoops;
        this.has3PointShotLine=has3PointShotLine;
    }
    @Override
    public String describe(){
        return isReady() ? "Баскетбольная площадка с кольцами и разметкой для трехочковых бросков" : "Баскетбольная площадка, временно закрыта";
    }
    public boolean isReady(){
        return hasHoops && has3PointShotLine;
    }
    public void changeHoops(){
        hasHoops= !hasHoops;
    }
    public void change3PointShotLine(){
        has3PointShotLine= !has3PointShotLine;
    }

    public boolean hasHoops() {
        return hasHoops;
    }

    public boolean Has3PointShotLine() {
        return has3PointShotLine;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Basketball basketball = (Basketball) obj;
        return hasHoops == basketball.hasHoops && has3PointShotLine == basketball.has3PointShotLine;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hasHoops, has3PointShotLine);
    }
    @Override
    public String toString() {
        return "Баскетбольная площадка " + (hasHoops ? "с кольцами ": " без колец ")+( has3PointShotLine ? " и с трехочковой линией для бросков": " и без трехочковой линии для бросков");
    }
}
