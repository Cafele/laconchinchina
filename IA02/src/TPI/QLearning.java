package TPI;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author fede
 */
public class QLearning implements Runnable {
    //atributos:
    //las 8 acciones posibles
    static final int N=0;
    static final int NE=1;
    static final int E=2;
    static final int SE=3;
    static final int S=4;
    static final int SO=5;
    static final int O=6;
    static final int NO=7;
    //
    long maxIteracion=100;
    double cantPasos=500;
    //recompensas
    double recBueno = 5;
    double recExcelente = 10;
    double recFinal = 100.0;
    double recMalo = -10.0;
    double recNormal = 0;
    
    //parametros para los metodos de seleccion
    double tau=400; //temperatura para softmax
    double epsilon = 0.1; //exploration rate para egreedy
    double gamma = 0.1; //tambien para esa formula, es la de amortizacion
    boolean edec = false; //booleano si el egreedy es decreciente
    boolean softdec = false; //booleano para softmax decreciente
    // tabla de Qvalues en [i][j][accion]
    double Qvalues [][][] = new double [6][6][8];
    //grilla que contiene tipos de las celdas
    int map[][];
    //tamaño de la columna/fila
    int tamano=0;
    //matriz que contiene todas las celdas
    Celda matrizCelda[][];
    //matriz que contiene las acciones posibles
    Boolean matrizAccion[][][];
    //una grilla para manejar el gridworld
    Grilla grilla;
    // contador
    double recompensa =0;
    double pasoe;
    double pasot;
    
    Boolean soft=false;
    Boolean egreed=true;
    
    Boolean decreE=true;
    Boolean decreG=false;
    XYSeries serieAp = null;
    XYSeriesCollection conjdatosap = null;
    XYDataset datosAp;
    XYDataset listadat;
    double listaSerie[] ;
    //iteracion donde converge
    int iterConv;
    // valor acumulado de convergencia
    double conv;
    //constructor
    public QLearning (double temp,int tmno,long itmax, double exp, double amort, double recB, double recE, double recN, double recF,double recM,Grilla grid,double pasos,Boolean softmax,Boolean egr, Boolean ed, Boolean sd){
        this.maxIteracion=itmax;
        this.cantPasos=pasos;
        this.tamano=tmno;
        this.tau=temp;
        this.epsilon=exp;
        this.gamma=amort;
        this.recBueno=recB;
        this.recMalo=recM;
        this.recExcelente=recE;
        this.recFinal=recF;
        this.recNormal=recN;
        this.map=grid.getGrilla();
        this.matrizCelda=grid.matrizCeldas;
        this.matrizAccion=grid.matrizA;
        this.soft=softmax;
        this.egreed=egr;
        this.pasoe=epsilon/maxIteracion;
        this.pasot=tau/maxIteracion;
        this.edec=ed;
        this.softdec=sd;
        this.serieAp = new XYSeries("titulo");
        this.conjdatosap = null;
        listaSerie = new double [(int) maxIteracion];
        //iniciar la tabla de qvalues, el 8 va por las 8 acciones posibles 
        Qvalues=new double[tamano][tamano][8];
        for(int j=0;j<tamano;j++){
            for(int i=0;i<tamano;i++){
                for(int a=0;a<8;a++){
                Qvalues[i][j][a]=0.0;
                }
            }
        }
                }
                
    //metodos de seleccion: 
    //-egreedy
    public int eGreedy(Posicion pos){
        int accion;
        int i=pos.getI(); int j=pos.getJ();
        //Celda celda = matrizCelda[i][j];
        Boolean x;
        double random = java.lang.Math.random();
        do {
            if (random<this.epsilon){
                //cae dentro de la exploracion, accion es aleatoria
                accion = (int)((java.lang.Math.random())*8);
            } else {
                //cae dentro de la parte de explotacion, accion es la mejor
                accion = mejorAccion(pos);
            }
            x = !(matrizAccion[i][j][accion]);
        } while (x);
        // repito hasta que la accion es una accion valida. es decir en la matrizA, es true.
        return accion;
    }
    //-aleatorio
    public int aleatorio(Posicion pos){
        
        int accion;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        Boolean x;
        double random;
        do {
            random = java.lang.Math.random();
            accion=(int)(random*8);
            x = !(matrizAccion[i][j][accion]);
        } while (x);
        // repito hasta que la accion es una accion valida. es decir en la matrizA, es true.
        return accion;
    }
    //-softmax
    public int softmax(Posicion pos){
        int i=pos.getI(); int j =pos.getJ();
        int accion=0;
        double total=0;
        double probN=0;
        double probNE=0;
        double probE=0;
        double probSE=0;
        double probS=0;
        double probSO=0;
        double probO=0;
        double probNO=0;
        double random;
        Boolean x;
        
        for(int a=0; a<8;a++){
            total = total + (Math.exp((Qvalues[i][j][a])/tau));
        }
        
        for(int a=0; a<8;a++){
            switch (a){
            case N:
                probN=(Math.exp((Qvalues[i][j][a])/tau))/total; break;
            case NE:
                probNE=((Math.exp((Qvalues[i][j][a])/tau))/total)+probN;break;
            case E:
                probE=((Math.exp((Qvalues[i][j][a])/tau))/total)+probNE;   break; 
            case SE:
                probSE=((Math.exp((Qvalues[i][j][a])/tau))/total)+probE;break;
            case S:
                probS=((Math.exp((Qvalues[i][j][a])/tau))/total)+probSE;break;
            case SO:
                probSO=((Math.exp((Qvalues[i][j][a])/tau))/total)+probS;break;
            case O:
                probO=((Math.exp((Qvalues[i][j][a])/tau))/total)+probSO; break;   
            case NO:
                probNO=((Math.exp((Qvalues[i][j][a])/tau))/total)+probO; break;  
            }
        }
        do {
            random = java.lang.Math.random();
            
            if(random < probN){
                accion = 0;//va norte
            }else{
                if(random < (probNE)){
                    accion = 1;//va nordeste
                } else {
                    if(random < probE){
                        accion = 2;//va este
                    } else {
                        if(random < probSE){
                            accion = 3;//va sudeste
                        } else {
                            if(random < probS){
                                accion = 4;//va sur
                            } else {
                                if(random < probSO){
                                    accion = 5;//va sudoeste
                                } else {
                                    if(random < probO){
                                        accion = 6;//va oeste
                                    } else {
                                        accion = 7;//va noroeste
                                    }
                                }
                            }
                        }
                    }
                }
            }
            x = !(matrizAccion[i][j][accion]);
        }while(x);

        return accion;
    }
    
    //funcion que devuelve la accion que da el mejor Qvalue de una posicion dada
    public int mejorAccion(Posicion pos){
        int laMejor = 0;
        double mejorQ=-100000000.0;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        for (int a=0;a<8;a++){
            //si la accion es una accion valida en la matrizA es true
            if (matrizAccion[i][j][a]){
                //reviso si el Q es mejor que el anterior
                if(Qvalues[i][j][a]>mejorQ){
                    // si lo es, actualizo el mejorQ y la mejor A
                    mejorQ=Qvalues[i][j][a];
                    laMejor=a;
                }   
            }       
        }
        return laMejor;
    }
    
    //funcion que devuelve el mejor Qvalue para una posicion dada
    public double mejorQ(Posicion pos){
        double mejorQ=-100000000.0;
        int i=pos.getI(); int j=pos.getJ();
        
        for (int a=0;a<8;a++){    
            //si la accion es una accion valida en la matrizA es true
            if (matrizAccion[i][j][a]){
                //reviso si el Q es mejor que el anterior
                if(Qvalues[i][j][a]>mejorQ){
                    // si lo es, actualizo el mejorQ y la mejor A
                    mejorQ=Qvalues[i][j][a];
                }   
            }           
        }
        return mejorQ;
    }
    
    // funcion que devuelve el siguiente estado segun una accion realizada
    public Posicion elsiguiente(Posicion pos, int accion){
        Posicion sig = new Posicion();
        int i = pos.getI(); int j = pos.getJ();
        
        if(!(matrizAccion[i][j][accion])){
            accion = aleatorio(pos);
        }
        
        switch (accion){
            case N:
                sig.setI(i-1);
                sig.setJ(j);
                break;
            case NE:
                sig.setI(i-1);
                sig.setJ(j+1);
                break;
            case E:
                sig.setI(i);
                sig.setJ(j+1);
                break;
            case SE:
                sig.setI(i+1);
                sig.setJ(j+1);
                break;    
            case S:
                sig.setI(i+1);
                sig.setJ(j);
                break;
            case SO:
                sig.setI(i+1);
                sig.setJ(j-1);
                break;    
            case O:
                sig.setI(i);
                sig.setJ(j-1);
                break;    
            case NO:
                sig.setI(i-1);
                sig.setJ(j-1);
                break; 
        }
        return sig;
    }
   
    // funcion que devuelve la recompensa directa de realizar una accion
    public double recompensar(Posicion pos,int accion){
        //valor por defecto
        double resultado=0.0;
        int i = pos.getI(); int j = pos.getJ();
        //calidad, bueno malo, etc..        
        int calidad = map[i][j];
          switch (calidad){
            case 0:resultado=this.recNormal;break; //es normal
            case 1:resultado=this.recMalo;break; //es malo 
            case 2:resultado=this.recBueno;break; //es bueno
            case 3:resultado=this.recExcelente;break; //es excelente
            case 4:resultado=this.recFinal;break; // es el obj final
            case 5:break; //es pozo no deberia tomarla igual
          }  
        return resultado;
    }
   
    //funcion que devuelve un estado de partida aleatoria para el algoritmo Q
    private Posicion estadoInicialAleatorio() {
        Posicion resultado = new Posicion();
        resultado.setI((int)(java.lang.Math.random()*(tamano)));
        resultado.setJ((int)(java.lang.Math.random()*(tamano)));
        return resultado;
    }
    
    //actualizar tabla Qvalues
    private void actualizarQtable (int i, int j, int accion){
        Posicion actual = new Posicion(i,j);
        double recomp = this.recompensar(actual, accion);
        Posicion siguiente = this.elsiguiente(actual, accion);
        double maxQ = this.mejorQ(siguiente);
        // se aplica la formula
        Qvalues [i][j][accion] = recomp + gamma*maxQ;
    }
    
    @Override
    public void run(){
        int i ;int j ; int x; int accion;
        Posicion pos;
        Posicion sig;
        Border border;
        long reward;
        double totalR=0.0;
        double qactual;
        serieAp = new XYSeries ("titulo");
        // valor donde convergio
        conv = 0.0;
        //numero de iteracion donde convergio
        iterConv =0;
        // contador de repeticion del valor acumulado
        int contRep=0;
        
        //corrida
        for (int iter=0; iter<this.maxIteracion;iter++){
            reward = 0;
            qactual = 0.0;
            //si es decreciente Egreedy y epsilon es positivo
            if(edec && epsilon>0){
                this.setEpsilon(epsilon-pasoe);
            }
            //si softmax es decreciente y como tau no puede ser cero
            if(softdec && tau>0.5){
                this.setTau((tau-pasot));
            }
            
            //arranca de una posicion aleatoria cada episodio
            pos=estadoInicialAleatorio();
            x=0;
            //en cada episodio se mueve una cant de pasos maxima y sale antes si llega al final
            do{
                i=pos.getI(); j=pos.getJ();
                //se pinta el borde donde esta el agente
                border = new MatteBorder(3,3,3,3,Color.RED);
                matrizCelda[i][j].setBorder(border);
                //selecciono la accion siguiente segun metodo de seleccion
                if(egreed){
                    accion=this.eGreedy(pos);
                } else {
                    if(soft){
                        accion=this.softmax(pos);
                    } else {
                        accion=this.aleatorio(pos);
                    }
                }
                //calculo recompensa
                reward = (long) (reward + this.recompensar(pos, accion));
                //actualizo la tabla Q
                actualizarQtable(i,j, accion);
                //vuelvo a pintar los bordes cuando el agente se va
                border = new MatteBorder(1,1,1,1,Color.GRAY);
                matrizCelda[i][j].setBorder(border);
                //busco posicion siguiente
                sig = elsiguiente(pos, accion);
                //actualizo posicion
                pos=sig;
                //incremento numero de paso
                x++;
                
            }while (x<cantPasos && (map[i][j]!=4)) ;
            //recompensa acumulada promedio obtenida
            //totalR=totalR+(reward);
            totalR=0.0;
            for(int ix=0;ix<tamano;ix++){
                for(int jx=0;jx<tamano;jx++){
                    for(int ax=0;ax<8;ax++){
                         totalR = totalR+Qvalues[ix][jx][ax];
                    }
                }
            }
            //System.out.println(iter);
            //voy a chequear mientras no converge el valor mas alto.
            switch (contRep) {
                case 0:
                    //es la primera vez
                     if(conv == totalR){
                        //si se repitio entonces contabilizo 
                        contRep=contRep+1;
                        //y como es la primera vez cargo la iteracion
                        iterConv=iter-1;
                    } else {
                        //si no se repitio actualizo el ultimo valor acumulado
                        conv = totalR;
                    } 
                case 1: case 2: case 3: case 4: case 5:
                case 6: case 7: case 8: case 9: case 10:
                case 11: case 12: case 13: case 14: case 15:
                    // en las siguientes repeticiones
                    if(conv == totalR){
                        //si se repitio entonces contabilizo 
                        contRep=contRep+1;
                    } else {
                        //si no se repitio actualizo el ultimo valor acumulado
                        conv = totalR;
                        // comienzo de nuevo el contador
                        contRep=0;
                    } 
                        
            }
            //if (contRep < 15){
              //  if(conv == totalR){
                    
                    //si se repitio entonces contabilizo 
                //    contRep=contRep+1;
                //} else {
                    //si no se repitio actualizo el ultimo valor acumulado
                  //  conv = totalR;
                //}
            //}
            listaSerie[iter] = totalR;

        }

        JOptionPane.showMessageDialog(grilla, "Terminado el ciclo de aprendizaje", "Mensaje de finalizacion", JOptionPane.INFORMATION_MESSAGE);
    }
    //@Override

    //setters y getters :
    public void setMaxIteracion(long maxIteracion) {
        this.maxIteracion = maxIteracion;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public void setRecBueno(double recBueno) {
        this.recBueno = recBueno;
    }

    public void setRecExcelente(double recExcelente) {
        this.recExcelente = recExcelente;
    }

    public void setRecFinal(double recFinal) {
        this.recFinal = recFinal;
    }

    public void setRecMalo(double recMalo) {
        this.recMalo = recMalo;
    }

    public void setRecNormal(double recNormal) {
        this.recNormal = recNormal;
    }


    public Grilla getGrilla() {
        return grilla;
    }
    
    public double acierto(){
        double res = 0.0;
        
        return res;
    }

    public double[] getListaSerie() {
        return listaSerie;
    }

    public int getIterConv() {
        return iterConv;
    }

    public double getConv() {
        return conv;
    }
    
}
