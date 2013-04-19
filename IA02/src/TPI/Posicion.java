/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

/**
 *
 * @author fede
 */

// del tipo (i,j) para simplificar uso de posiciones en la grilla
public class Posicion {
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
