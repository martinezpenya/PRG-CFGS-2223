package UD07.P3_Comparator1;

import java.util.TreeSet;

public class Principal {

    public static void main(String[] args) {
        TreeSet<Objeto> ts = new TreeSet<Objeto>(new ComparadorDeObjetos());
        
        Objeto o1= new Objeto(0, 1);
        ts.add(o1);
        
        ts.add(new Objeto(1, 2));
        ts.add(new Objeto(4, 5));
        ts.add(new Objeto(2, 3));

        for (Objeto o : ts) {
            System.out.println(o);
        }
    }
}
