package UD07.P2_HashSet;

import java.util.HashSet;
import java.util.Scanner;

public class EjemploHashSet {

    public static void main(String[] args) {
        HashSet<Integer> conjunto = new HashSet<Integer>();
        Scanner teclado = new Scanner(System.in);
        int numero;
        do {
            try {
                System.out.print("Introduce un número " + (conjunto.size() + 1) + ": ");
                numero = teclado.nextInt();
                if (!conjunto.add(numero)) {
                    System.out.println("Número ya en la lista. Debes introducir otro.");
                }
            } catch (Exception e) {
                teclado.nextLine();
                System.out.println("Número erróneo.");
            }
        } while (conjunto.size() < 5);
        // Calcular la suma
        Integer suma = 0;
        for (Integer i : conjunto) {
            suma += i;
        }
        System.out.println("La suma es: " + suma);
    }
}
