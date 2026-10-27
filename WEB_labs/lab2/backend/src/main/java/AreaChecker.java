public class AreaChecker {
    public static boolean checkHit(double x, double y, double r){
        boolean inRectangle = (x >=0 && x<= r/2.0) && (y >= 0 && y<=r);

        boolean inCircle = (x >= 0 && y<=0) && (x*x + y*y <= (r/ 2.0)* (r/2.0));

        boolean inTriangle = (x <= 0 && y<=0) && (y >= -x - (r/2.0));

        return inRectangle || inCircle || inTriangle;
    }
}
