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
    //prueba matrizAccionesPosibles
    Boolean matrizA [][][];
    //tamaño en filasxcolumnas es decir si es 10x10, tmño es 10.
    int tmno;
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
        this.matrizA = new Boolean [x][x][8];
        this.tmno=x;    
        //creo la grilla con ayuda del gridbaglayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
            for (int j = 0; j < tmno; j++) {
                for (int i = 0; i < tmno; i++) {
                    // se inicializa la matriz de estados en 0 = normal;
                    Posicion pos = new Posicion(i,j);
                    Celda celda = new Celda(pos);
                    //matrizCeldas [i][j]=celda;
                    grilla[i][j]=0;
                    //prueba
                    for (int a=0; a<8; a++){
                        matrizA [i][j][a]=true;
                    }
                    
                    gbc.gridx = i;
                    gbc.gridy = j;
                    // creamos la celda nueva y le damos un borde segun posicion


                    Border border;
                    //if (j < (tmno-1)) {
                    //    if (i < (tmno-1)) {
                    //        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    //    } else {
                    //        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    //    }
                    //} else {
                    //    if (i < (tmno-1)) {
                    //        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    //    } else {
                    //        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    //    }
                    //}
                    border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    
                    celda.setBorder(border);
                    celda.setBackground(Color.LIGHT_GRAY);
                    // añadimos la celda en la grilla con el layout y en la lista de las celdas
                    add(celda, gbc);
                    matrizCeldas [i][j]=celda;
                }
            }
    }
    
    //metodo para crear la grilla de estados
    public void armarGrilla(){
        for (int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
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
        for(int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
                if(i==0){
                    matrizA[i][j][0]=false;
                    matrizA[i][j][1]=false;
                    matrizA[i][j][7]=false;
                } else {
                    if(grilla[i-1][j]==5){
                        matrizA[i][j][0]=false;
                    }
                    if(j==0){
                        matrizA[i][j][7]=false;
                    } else {
                        if(grilla[i-1][j-1]==5){
                            matrizA[i][j][7]=false;
                        }
                    }
                    if(j==(tmno-1)){
                        matrizA[i][j][1]=false;
                    } else{
                        if(grilla[i-1][j+1]==5){
                            matrizA[i][j][1]=false;
                        }
                    }
                }
                
                if(j==0){
                    matrizA[i][j][5]=false;
                    matrizA[i][j][6]=false;
                    matrizA[i][j][7]=false;
                } else {
                    if(grilla[i][j-1]==5){
                        matrizA[i][j][6]=false;
                    }
                    if(i==0){
                        matrizA[i][j][7]=false;
                    } else {
                        if(grilla[i-1][j-1]==5){
                            matrizA[i][j][7]=false;
                        }
                    }
                    if(i==(tmno-1)){
                        matrizA[i][j][5]=false;
                    } else {
                        if(grilla[i+1][j-1]==5){
                            matrizA[i][j][5]=false;
                        }
                    }
                }
                
                if(i==(tmno-1)){
                    matrizA[i][j][3]=false;
                    matrizA[i][j][4]=false;
                    matrizA[i][j][5]=false;
                } else {
                    if(grilla[i+1][j]==5){
                        matrizA[i][j][4]=false;
                    }
                    if(j==0){
                        matrizA[i][j][5]=false;
                    } else {
                        if(grilla[i+1][j-1]==5){
                        matrizA[i][j][5]=false;
                        }
                    }
                    if(j==tmno-1){
                        matrizA[i][j][3]=false;
                    } else {
                        if(grilla[i+1][j+1]==5){
                        matrizA[i][j][3]=false;
                        }
                    }
                }
                
                if(j==(tmno-1)){
                    matrizA[i][j][1]=false;
                    matrizA[i][j][2]=false;
                    matrizA[i][j][3]=false;  
                } else {
                    if(grilla[i][j+1]==5){
                        matrizA[i][j][2]=false;
                    }
                    if (i==0){
                        matrizA[i][j][1]=false;
                    } else {
                        if(grilla[i-1][j+1]==5){
                        matrizA[i][j][1]=false;
                        }
                    }
                    if(i==(tmno-1)){
                        matrizA[i][j][3]=false;
                    } else {
                        if(grilla[i+1][j+1]==5){
                        matrizA[i][j][3]=false;
                        }
                    }
                }
                

            }   
        }
    }
    // funcion para la generacion aleatoria de estados
    public void estadosAleatorios(){
        
        for(int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
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
        int ii = (int)(java.lang.Math.random()*(tmno-1));
        int jj = (int)(java.lang.Math.random()*(tmno-1));
        grilla[ii][jj]=4;
    }
    // funcion que pinte las celdas segun la grilla
    public void pintarCeldas(){
         for(int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
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
        for (int j=0;j<tmno;j++){
            for (int i=0;i<tmno;i++){
                if(grilla[i][j]==4){
                    hayFinal = true;
                }
            } 
        }
        return hayFinal;
    }
    //funcion que limpia grilla
    public void limpiarGrilla(){
        for (int j=0;j<tmno;j++){
            for (int i=0;i<tmno;i++){
                grilla[i][j]=0;
            } 
        }
    }
    public void pintarBordes(){
        Border border;
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                 Celda celda = matrizCeldas[i][j];
                    //if (j < (tmno-1)) {
                    //    if (i < (tmno-1)) {
                    //        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    //    } else {
                    //        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    //    }
                    //} else {
                    //    if (i < (tmno-1)) {
                    //        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    //    } else {
                    //        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    //    }
                    //}
                    border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    celda.setBorder(border);
            }
        } 
    }
    public void setearNormal(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.seleccionInicio=false;
            }
        }
    }
    
    public void setearInicio(){
        for (int i=0;i<tmno;i++){
            for (int j=0; j<tmno;j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.seleccionInicio = true;
            }
        }
    }

    public void setMatrizCeldas(Celda[][] matrizCeldas) {
        this.matrizCeldas = matrizCeldas;
    }
    
    
    
    public Posicion getInicial(){
        // por defecto devuelve la posicion 0,0
        Posicion pos = new Posicion(0,0);
        for (int i=0;i<tmno;i++){
            for (int j=0; j<tmno;j++){
                Celda celda = matrizCeldas[i][j];
                if(celda.esInicial()){
                    pos.setI(i); pos.setJ(j);
                }
            }
        }
        return pos;
    }
    public void actualizarGrilla(){
        int x;
        for(int i=0;i<tmno;i++){
            for(int j=0;j<tmno;j++){
                x=matrizCeldas[i][j].getTipo();
                grilla[i][j]=x;
            }
        }
    }
    
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }  
}
