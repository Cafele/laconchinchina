/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author fede
 */
public class Grilla extends JPanel {
    //matriz que contiene los estados de cada celda
    int grilla[][];
    //matriz que contiene todas las celdas
    Celda matrizCeldas [][];
    //tamaño en filasxcolumnas es decir si es 10x10, tmño es 10.
    int tmño;
    // mismos colores segun el estado, para tener una referencia de que representan
    Color colorBueno = Color.YELLOW;
    Color colorExcelente = Color.GREEN;
    Color colorMalo = Color.RED;
    Color colorNormal = Color.LIGHT_GRAY;
    Color colorPozo = Color.BLACK;
    Color colorFinal = Color.BLUE;
    Color colorCamino = Color.CYAN;
    Color colorAgente = Color.WHITE;
    Color colorInicio = Color.ORANGE;
    
    //constructor
    public Grilla (int x){
        //armo las matrices con tamaño referenciado y cargo el tamaño a utilizar
        this.grilla = new int [x][x];
        this.matrizCeldas = new Celda [x][x];
        this.tmño=x;    
        //creo la grilla con ayuda del gridbaglayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
            for (int row = 0; row < tmño; row++) {
                for (int col = 0; col < tmño; col++) {
                    // se inicializa la matriz de estados en 0 = normal;
                    grilla[col][row]=0;
                    gbc.gridx = col;
                    gbc.gridy = row;
                    // creamos la celda nueva y le damos un borde segun posicion
                    Posicion pos = new Posicion(col,row);
                    Celda celda = new Celda(pos);
                    Border border;
                    if (row < (tmño-1)) {
                        if (col < (tmño-1)) {
                            border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                        }
                    } else {
                        if (col < (tmño-1)) {
                            border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                        }
                    }
                    celda.setBorder(border);
                    celda.setBackground(Color.LIGHT_GRAY);
                    // añadimos la celda en la grilla con el layout y en la lista de las celdas
                    add(celda, gbc);
                    matrizCeldas [col][row]=celda;
                }
            }
    }
    //metodo para crear la grilla de estados
    public void armarGrilla(){
        for (int i=0;i<tmño;i++){
            for(int j=0;i<tmño;j++){
                int tipo = matrizCeldas[i][j].getTipo();
                grilla[i][j]=tipo;
            }
        }
    }
    //getter de la grilla
    public int[][] getGrilla() {
        return grilla;
    }
    
    
    
}
