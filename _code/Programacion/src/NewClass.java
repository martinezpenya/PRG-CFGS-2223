
import java.util.Arrays;

/**
 *
 * @author davpen
 */
public class NewClass {

    public static void main(String[] args) {
        double meters = 0;
        int iterations = 100000000;
        for (int i = 0; i < iterations; i++) {
            meters += 0.01;
        }
        System.out.printf("Expected: %f km\n", 0.01 * iterations / 1000);
        System.out.printf("Got: %f km \n", meters / 1000);

        String email = "@david";
        
//        String[] partes = email.split("@");
//        System.out.println(partes.length);
//        System.out.println(partes[0]=="");

        String texto="Z,B,A,X,M,O,P,U";
        String[] partes=texto.split(",");
        //partes={"Z", "B", "A", "X", "M", "O", "P", "U"}
        Arrays.sort(partes);//lo ordenamos
        //partes={"Z", "B", "A", "X", "M", "O", "P", "U"}
        System.out.println(Arrays.toString(partes));
    }
    
    
   

}
