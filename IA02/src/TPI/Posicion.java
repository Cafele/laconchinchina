package TPI;

/**
 *
 * @author fede
 */

// Una posicion compuesta de una tupla (i,j), para simplificar uso de posiciones en las matrices 

public class Posicion {
    //atributos
    
    int i;
    int j;
    
    //constructores
    public Posicion(){};
    
    public Posicion(int i, int j){
        this.i=i;
        this.j=j;
    }

    //setters y getters simples
    
    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }
    
}
