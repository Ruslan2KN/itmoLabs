package model;

public record PointResult(double x, double y, double r, boolean hit,
                          String currentTime, long executionTimeNs) {

}
