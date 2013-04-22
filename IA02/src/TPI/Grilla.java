/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
import java.awt.Dimension;
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
    
    //constructores
    public Grilla(){};
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
                //añado todo y despues elimino las que no deberian estar
                matrizCeldas[i][j].listaAcciones.add("0");
                matrizCeldas[i][j].listaAcciones.add("1");
                matrizCeldas[i][j].listaAcciones.add("2");
                matrizCeldas[i][j].listaAcciones.add("3");
                matrizCeldas[i][j].listaAcciones.add("4");
                matrizCeldas[i][j].listaAcciones.add("5");
                matrizCeldas[i][j].listaAcciones.add("6");
                matrizCeldas[i][j].listaAcciones.add("7");
                // aca tengo que tener cuidado, yo estoy suponiendo que elimino por objeto y no por index
                if(i==0){
                    matrizCeldas[i][j].listaAcciones.remove("0");
                    matrizCeldas[i][j].listaAcciones.remove("1");
                    matrizCeldas[i][j].listaAcciones.remove("7");
                } else {
                    if(grilla[i-1][j]==5){
                        matrizCeldas[i][j].listaAcciones.remove("0");
                    }
                    if(grilla[i-1][j-1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("7");
                    }
                    if(grilla[i-1][j+1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("1");
                    }
                }
                if(j==0){
                    matrizCeldas[i][j].listaAcciones.remove("5");
                    matrizCeldas[i][j].listaAcciones.remove("6");
                    matrizCeldas[i][j].listaAcciones.remove("7");
                } else {
                    if(grilla[i][j-1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("6");
                    }
                    if(grilla[i-1][j-1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("7");
                    }
                    if(grilla[i+1][j-1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("5");
                    }
                }
                if(i==(tmño-1)){
                    matrizCeldas[i][j].listaAcciones.remove("3");
                    matrizCeldas[i][j].listaAcciones.remove("4");
                    matrizCeldas[i][j].listaAcciones.remove("5");
                    if(grilla[i+1][j]==5){
                        matrizCeldas[i][j].listaAcciones.remove("4");
                    }
                    if(grilla[i+1][j-1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("5");
                    }
                    if(grilla[i+1][j+1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("3");
                    }
                }
                if(j==(tmño-1)){
                    matrizCeldas[i][j].listaAcciones.remove("3");
                    matrizCeldas[i][j].listaAcciones.remove("2");
                    matrizCeldas[i][j].listaAcciones.remove("1");
                    if(grilla[i][j+1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("2");
                    }
                    if(grilla[i-1][j+1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("1");
                    }
                    if(grilla[i+1][j+1]==5){
                        matrizCeldas[i][j].listaAcciones.remove("3");
                    }
                }
                

            }   
        }
    }
    // funcion para la generacion aleatoria de estados
    public void estadosAleatorios(){
        
        for(int i=0;i<tmño;i++){
            for(int j=0;j<tmño;j++){
                float random = (float) java.lang.Math.random();
                if (random<0.52){
                    //asigno normal
                    grilla[i][j]=0;
                } else {
                    if (random < 0.64) {
                        //asigno malo
                        grilla[i][j]=1;
                    } else {
                        if (random < 0.76) {
                            //asigno bueno
                            grilla[i][j]=2;
                        } else{
                            if (random < 0.90 ) {
                                //asigno excelente
                                grilla[i][j]=3;
                            } else {
                                // asigno pozo
                                grilla[i][j]=5;
                            }
                        }
                    }
                }
            }   
        }
        // por ultimo asigno un final
        int ii = (int)(java.lang.Math.random()*tmño);
        int jj = (int)(java.lang.Math.random()*tmño);
        grilla[ii][jj]=4;
    }
    // funcion que pinte las celdas segun la grilla
    public void pintarCeldas(){
         for(int i=0;i<tmño;i++){
            for(int j=0;j<tmño;j++){
                int estado = grilla[i][j];
                switch(estado) {
                    case 0:
                        //es normal
                        matrizCeldas[i][j].setBackground(colorNormal);
                        break;
                    case 1:
                        //es malo
                        matrizCeldas[i][j].setBackground(colorMalo);
                        break;
                    case 2:
                        //es bueno
                        matrizCeldas[i][j].setBackground(colorBueno);
                        break;
                    case 3:
                        //es excelente
                        matrizCeldas[i][j].setBackground(colorExcelente);
                        break;
                    case 4:
                        //es final
                        matrizCeldas[i][j].setBackground(colorFinal);
                        break;
                    case 5:
                        // es pozo
                        matrizCeldas[i][j].setBackground(colorPozo);
                        break;
                }
            }
         }
    }
    // funcion que revisa que por lo menos haya una celda final
    public Boolean hayEstadoFinal(){
        Boolean hayFinal = false;
        for (int i=0;i<tmño;i++){
            for (int j=0;i<tmño;i++){
                if(grilla[i][j]==4){
                    hayFinal = true;
                }
            } 
        }
        return hayFinal;
    }
    //funcion que limpia grilla
    public void limpiarGrilla(){
        for (int i=0;i<tmño;i++){
            for (int j=0;i<tmño;i++){
                grilla[i][j]=0;
            } 
        }
    }
    
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }  
}
