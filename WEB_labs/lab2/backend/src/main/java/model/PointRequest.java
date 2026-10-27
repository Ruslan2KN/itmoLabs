package model;

import java.util.List;

public class PointRequest {
    private List<Double> x;
    private Double y;
    private List<Double> r;

    public List<Double> getX() {
        return x;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public List<Double> getR() {
        return r;
    }

    public void setR(List<Double> r) {
        this.r = r;
    }
}
