package UD07.P2_4_Maps;

/**
 *
 * @author David Martínez (wwww.martinezpenya.es|ieseduardoprimo.es)
 */
import java.util.HashMap;

public class Ejemplo07 {

    public static void main(String[] args) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        //Insertamos un solo elemento A con valor 1
        hashMap.put("A", 1);
        
        //Busqueda por clave
        if (hashMap.containsKey("A")) {
            System.out.printf("Contiene la clave A. Su valor es: %d\n", hashMap.get("A"));
        }

        //Busqueda por valor
        if (hashMap.containsValue(0)) {
            System.out.println("Contiene el valor 0");
        }

        //Eliminar el elemento con clave A
        hashMap.remove("A");

        //Ahora añadimos varios elementos para imprimirlos
        hashMap.put("A", 1);
        hashMap.put("E", 12);
        hashMap.put("I", 15);
        hashMap.put("O", 0);
        hashMap.put("U", 0);
        //Recorremos el mapa y lo imprimimos
        for (HashMap.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.printf("Clave: %s. Valor: %d\n", entry.getKey(), entry.getValue());
        }
    }
}