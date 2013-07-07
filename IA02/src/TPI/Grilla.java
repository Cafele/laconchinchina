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

//clase contenedora de las celdas
public class Grilla extends JPanel {
    //atributos:
    //matriz que contiene los estados de cada celda
    int grilla[][];
    //matriz que contiene todas las celdas (jpanels)
    Celda matrizCeldas [][];
    //matriz con las acciones posibles, contempla las 8 acciones posibles
    //true si es posible realizarla, false caso contrario
    Boolean matrizA [][][];
    //tamaño en filasxcolumnas es decir si es 10x10, tmño es 10.
    int tmno;
    //Colores segun el estado, para tener una referencia de que representan
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
        //inicializamos las matrices con tamaño referenciado
        grilla = new int [x][x];
        matrizCeldas = new Celda [x][x];
        matrizA = new Boolean [x][x][8];
        tmno=x;
        
        //creo la grilla con layout del tipo gridbaglayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        //inicializamos cada matriz
            for (int j = 0; j < tmno; j++) {
                for (int i = 0; i < tmno; i++) {
                    
                    // se inicializa la matriz de estados en 0 = normal;
                    grilla[i][j]=0;

                    // se inicializa la matriz de acciones posibles
                    for (int a=0; a<8; a++){
                        matrizA [i][j][a]=true;
                    }
                    //datos de control para gridbaglayout
                    gbc.gridx = i;
                    gbc.gridy = j;
                    
                    //se crea los bordes de la celda
                    Border border;
                    border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    
                    //se inicializa una nueva celda a añadir en la grilla
                    Posicion pos = new Posicion(i,j);
                    Celda celda = new Celda(pos);
                    celda.setBorder(border);
                    celda.setBackground(Color.LIGHT_GRAY);
                    
                    // añadimos la celda en la grilla con el layout y en la matriz de las celdas
                    add(celda, gbc);
                    matrizCeldas [i][j]=celda;
                }
            }
    }
    
    //getter de la grilla
    public int[][] getGrilla() {
        return grilla;
    }
    
    //cargar las acciones posibles en funcion de la posicion de la celda
    //y si el estado siguiente es distinto de pozo
    
    public void actualizarAcciones(){
        //para tener una referencia de las acciones:
        //O=0;SO=1;S=2;SE=3;E=4;NE=5;N=6;NO=7
        // y si dentro de la grilla[i][j] el valor es 5, corresponde a un pozo
        for(int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
                for (int a=0;a<8;a++){
                    matrizA[i][j][a]=true;
                }
                if(i==0){
                    matrizA[i][j][0]=false;
                    matrizA[i][j][1]=false;
                    matrizA[i][j][7]=false;
                } else{
                    if(grilla[i-1][j]==5){
                        matrizA[i][j][0]=false;
                    }
                    if(j>0){
                        if(grilla[i-1][j-1]==5){
                            matrizA[i][j][7]=false;
                        }
                    }
                    if(j<(tmno-1)){
                        if(grilla[i-1][j+1]==5){
                            matrizA[i][j][1]=false;
                        }
                    }
                }
                if(j==0){
                    matrizA[i][j][5]=false;
                    matrizA[i][j][6]=false;
                    matrizA[i][j][7]=false;
                }else{
                    if(grilla[i][j-1]==5){
                        matrizA[i][j][6]=false;
                    }
                    if(i<(tmno-1)){
                        if(grilla[i+1][j-1]==5){
                            matrizA[i][j][5]=false;
                       }
                    }
                }
                if(i==(tmno-1)){
                    matrizA[i][j][3]=false;
                    matrizA[i][j][4]=false;
                    matrizA[i][j][5]=false;
                }else{
                    if(grilla[i+1][j]==5){
                        matrizA[i][j][4]=false;
                    }
                    if(j<(tmno-1)){
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
                }
            }   
        }
    }
    
    // funcion para la generacion aleatoria de estados
    public void estadosAleatorios(){
        //asigna estados aleatorios, segun una probabilidad, siendo normal la mas probable
        for(int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
                float random = (float) java.lang.Math.random();
                if (random<0.80){
                    //asigno normal
                    grilla[i][j]=0;
                } else {
                    if (random < 0.85) {
                        //asigno malo
                        grilla[i][j]=1;
                    } else {
                        if (random < 0.925) {
                            //asigno bueno
                            grilla[i][j]=2;
                        } else{
                            if (random < 0.975 ) {
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
        
        // por ultimo asigno un final, tambien, aleatorio
        int ii = (int)(java.lang.Math.random()*(tmno));
        int jj = (int)(java.lang.Math.random()*(tmno));
        grilla[ii][jj]=4;
    }
    
    // funcion que pinte las celdas segun la grilla contenedora de tipos de estado
    // toma los valores de la grilla (generados aleatoriamente, o modificados)
    // y pinta las celdas segun su tipo
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
                        matrizCeldas[i][j].esFinal=true;
                        break;
                    case 5:
                        // es pozo
                        matrizCeldas[i][j].setBackground(colorPozo);
                        break;
                }
            }
         }
    }
    
    //funcion que limpia lo pintado, ya sea bordes o caminos
    public void limpiar(){
        Border border;
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                    Celda celda = matrizCeldas[i][j];
                    border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    celda.setBorder(border);
                    //devuelve los valores para graficar el camino a los valores por defecto
                    celda.caminoAnt=10;
                    celda.caminoSig=10;
                    celda.repaint();
            }
        } 
    }
    
    //funcion que indica que el tipo de seleccion de celdas es normal
    public void setearNormal(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=true;
                celda.selM=false;
                celda.selB=false;
                celda.selE=false;
                celda.selF=false;
                celda.selP=false;
                celda.seleccionInicio=false;
            }
        }
    }
    //funcion que indica que el tipo de seleccion de celdas es mala
    public void setearMala(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=false;
                celda.selM=true;
                celda.selB=false;
                celda.selE=false;
                celda.selF=false;
                celda.selP=false;
                celda.seleccionInicio=false;
            }
        }
    }
    //funcion que indica que el tipo de seleccion de celdas es buena
    public void setearBuena(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=false;
                celda.selM=false;
                celda.selB=true;
                celda.selE=false;
                celda.selF=false;
                celda.selP=false;
                celda.seleccionInicio=false;
            }
        }
    }
    //funcion que indica que el tipo de seleccion de celdas es pozo
    public void setearPozo(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=false;
                celda.selM=false;
                celda.selB=false;
                celda.selE=false;
                celda.selF=false;
                celda.selP=true;
                celda.seleccionInicio=false;
            }
        }
    }
    //funcion que indica que el tipo de seleccion de celdas es excelente
    public void setearExcelente(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=false;
                celda.selM=false;
                celda.selB=false;
                celda.selE=true;
                celda.selF=false;
                celda.selP=false;
                celda.seleccionInicio=false;
            }
        }
    }
    //funcion que indica que el tipo de seleccion de celdas es final
    public void setearFinal(){
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=false;
                celda.selM=false;
                celda.selB=false;
                celda.selE=false;
                celda.selF=true;
                celda.selP=false;
                celda.seleccionInicio=false;
            }
        }
    }
    
    //funcion que indica que el tipo de seleccion de celdas es de inicio
    public void setearInicio(){
        for (int i=0;i<tmno;i++){
            for (int j=0; j<tmno;j++){
                Celda celda = matrizCeldas[i][j];
                celda.esInicial=false;
                celda.selN=false;
                celda.selM=false;
                celda.selB=false;
                celda.selE=false;
                celda.selF=false;
                celda.selP=false;
                celda.seleccionInicio =true;
            }
        }
    }

    //setter de matriz de celdas
    public void setMatrizCeldas(Celda[][] matrizCeldas) {
        this.matrizCeldas = matrizCeldas;
    }
    
    // funcion que devuelve la posicion de la celda de inicio
    public Posicion getInicial(){
        // por defecto devuelve la posicion (0,0)
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
    
    //funcion que actualiza la grilla segun la matriz de celdas
    public void actualizarGrilla(){
        int x;
        for(int i=0;i<tmno;i++){
            for(int j=0;j<tmno;j++){
                
                x=matrizCeldas[i][j].getTipo();
                grilla[i][j]=x;
                //se aplica la variable booleana esFinal solo a las finales
                matrizCeldas[i][j].esFinal=false;
                if (x==4){
                    matrizCeldas[i][j].esFinal=true;
                    
                }
                
            }
        }
    }
    //funcion que devuelve la variable booleana noEsCamino a valor por defecto
    public void limpiarCaminos(){
        Celda celda;
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                    celda = matrizCeldas[i][j];
                    celda.noEsCamino=true;
            }
        } 
    }
    //funcion que devuelve si existe una variable final dentro de la grilla
    public Boolean hayFinal(){
        Celda celda;
        Boolean finals = false;
        for (int i=0; i<tmno; i++){
            for (int j=0; j<tmno; j++){
                    celda = matrizCeldas[i][j];
                    if (celda.esFinal){
                        finals = true;
                    }
            }
        } 
        return finals;
    }
    //funcion que devuelve todas las acciones como posibles
    public void borrarAcciones(){
        for(int j=0;j<tmno;j++){
            for(int i=0;i<tmno;i++){
                for(int a=0;a>8;a++){
                    matrizA[i][j][a]=true;
                }
            }
        }
    }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }  
}
