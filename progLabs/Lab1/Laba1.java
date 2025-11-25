import java.util.Random;
public class Laba1 {
    static long[] s=new long[7];
    static float[] x=new float[18];
    public static void main(String[] args) {
        for (int i=0; i<7; i++){
            s[i]=4+i*2;

        }
        Random random=new Random();
        for (int i=0; i<18; i++){
            x[i]=-7.0f+random.nextFloat()*(12.0f-(-7.0f));
        }
        double[][] s1=new double[7][18];
        for (int i=0; i<7; i++){
            for (int j=0; j<18;j++){
                s1[i][j]= s1Elements(i,j);
            }
        }
        for (int i=0; i<7; i++){
            for (int j=0; j<18; j++){
                System.out.printf("%10.4f", s1[i][j]);
            }
            System.out.println();
        }


    }
    static double s1Elements(int i, int j){
        switch ((int)s[i]){
            case 16:
                return Math.cos(Math.cbrt(Math.tan(x[j])));
            case 6:
            case 10:
            case 14:
                return Math.cos(Math.pow(Math.cos(x[j]),Math.log(Math.abs(x[j]))));
            default:
                return Math.pow(Math.cbrt(Math.pow(Math.E, Math.sin(x[j]))),Math.tan(Math.pow(Math.pow(x[j],2*x[j]),(2/3f -Math.pow(3/(1/2f -x[j]),3))/Math.pow(x[j]/3,x[j]))));
        }
    }

}
