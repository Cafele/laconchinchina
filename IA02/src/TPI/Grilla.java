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
    //cargar las acciones posibles
    public void actualizarAcciones(){
        //para tener una referencia de las acciones:
        //N=0,NE=1;E=2;SE=3;S=4;SO=5;O=6;NO=7
        for(int i=0;i<tmño;i++){
            for(int j=0;j<tmño;j++){
                //si no estoy en la primera fila y la celda de arriba no es pozo, añado accion norte
                if(grilla[i][j-1]!=5 && j>0 ){matrizCeldas[i][j].listaAcciones.add(0);}
                //si no estoy en la primera fila o ultima columna y la celda de arriba/der(diagonal) no es pozo, añado accion NE
                if(grilla[i+1][j-1]!=5 && (j>0 || i<(tmño-1)) ){matrizCeldas[i][j].listaAcciones.add(1);}
                //si no estoy en la ultima columna y la celda de la derecha no es pozo, añado la accion E;
                if(grilla[i+1][j]!=5 && i<(tmño-1) ){matrizCeldas[i][j].listaAcciones.add(2);}
                //si no estoy ni en la ultima columna ni la ultima fila y la diag abajo,derecha no es pozo, añado la accion SE
                if(grilla[i+1][j+1]!=5 && (i<(tmño-1) || j<(tmño-1)) ) {matrizCeldas[i][j].listaAcciones.add(3);}
                //si no estoy en la ultima fila y la celda de abajo no es pozo, añado la accion S
                if(grilla[i][j+1]!=5 && j<(tmño-1) ){matrizCeldas[i][j].listaAcciones.add(4);}
                //si no estoy en la ultima fila, ni la primera columna, y la diag (abajo,izq) no es pozo, añado la accion SO
                if(grilla[i-1][j+1]!=5 && (i>0 || j<(tmño-1)) ){matrizCeldas[i][j].listaAcciones.add(5);}
                //si no estoy en la primera columna y la celda de la izq no es pozo, añado la accion O
                if(grilla[i-1][j]!=5 && i>0 ){matrizCeldas[i][j].listaAcciones.add(6);}
                //si no estoy en la primera columna o fila, y la celda de la diag (izq,arriba) no es pozo, añado la accion NO
                if(grilla[i-1][j-1]!=5 && (i>0 || j>0) ){matrizCeldas[i][j].listaAcciones.add(7);}
            }   
            }
        }
  
}
