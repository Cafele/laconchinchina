/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author fede
 */
public class QLearning implements Runnable {
    //las 8 acciones posibles
    static final int N=0;
    static final int NE=1;
    static final int E=2;
    static final int SE=3;
    static final int S=4;
    static final int SO=5;
    static final int O=6;
    static final int NO=7;
    //manejar iteraciones
    long iteracion=0;
    long maxIteracion=10000;
    //recompensas
    double recBueno = 25.0;
    double recExcelente = 50.0;
    double recFinal = 100.0;
    double recMalo = -10.0;
    double recNormal = 10.0;
    // parametros
    double Tau; //temperatura para softmax
    double epsilon = 0.6; //exploration rate para egreedy
    //alpha parece q no va, se saca de ultima
    double alpha = 0.1; //para la formula de Q(s,a), es la de aprendizaje
    double gamma = 0.8; //tambien para esa formula, es la de amortizacion
    // tabla de Qvalues en [i][j][accion]
    double Qvalues [][][] = new double [6][6][8];
    //grilla 
    int map[][];
    //tamaño de la columna/fila
    int tamano=0;
    // a modo de prueba por ahora
    Celda matrizCelda[][];
    Boolean matrizAccion[][][];
    //constructor
    public QLearning (int tmno,long itmax, double exp, double amort, double recB, double recE, double recN, double recF,double recM,int [][] mapa,Celda [][] mapaCeldas,Boolean [][][] matrizA){
        this.maxIteracion=itmax;
        this.tamano=tmno;
        //this.Tau=temp;
        //this.alpha=apren;
        this.epsilon=exp;
        this.gamma=amort;
        this.recBueno=recB;
        this.recMalo=recM;
        this.recExcelente=recE;
        this.recFinal=recF;
        this.recNormal=recN;
        this.map=mapa;
        this.matrizCelda=mapaCeldas;
        this.matrizAccion=matrizA;
        //iniciar la tabla de q, el 8 va por las 8 acciones posibles 
        Qvalues=new double[tamano][tamano][8];
        for(int j=0;j<tamano;j++){
            for(int i=0;i<tamano;i++){
                for(int a=0;a<8;a++){
                Qvalues[i][j][a]=0.0;
                }
            }
        }
        
    }
    //egreedy
    public int eGreedy(Posicion pos){
        int accion;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        
        double random = java.lang.Math.random();
        do {
            if (random<this.epsilon){
                //cae dentro de la exploracion, accion es aleatoria
                accion = (int)((java.lang.Math.random())*7);
            } else {
                //cae dentro de la parte de explotacion, accion es la mejor
                accion = mejorAccion(pos);
            }
        } while (!(matrizAccion[i][j][accion]));
        // repito hasta que la accion es una accion valida. es decir en la matrizA, es true.
        return accion;
    }
    
    //para egreedy mejor accion, es greedy, devuelve la accion que da el mejor Q
    public int mejorAccion(Posicion pos){
        int laMejor = 0;
        double mejorQ=-10000.0;
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
    //calcular max Q para la formula de Q(s,a)
    public double mejorQ(Posicion pos){
        double mejorQ=-10000.0;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
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
    
    // devuelve el siguiente estado segun una accion realizada
    public Posicion elsiguiente(Posicion pos, int accion){
        Posicion sig = new Posicion();
        int i = pos.getI(); int j = pos.getJ();
        //si es egreedy
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
   
    // funcion que devuelve la recompensa directa
    public double recompensar(Posicion pos,int accion){
                //valor por defecto
        int i = pos.getI(); int j = pos.getJ();
        double resultado=0.0;
        Posicion sig = this.elsiguiente(pos, accion);
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
   
    //estado de partida aleatoria
    private Posicion estadoInicialAleatorio() {
        Posicion resultado = new Posicion();
        resultado.setI((int)(java.lang.Math.random()*(tamano-1)));
        resultado.setJ((int)(float)(java.lang.Math.random()*(tamano-1)));
        return resultado;
    }
    //actualizar tabla Qvalues
    private void actualizarQtable (int i, int j, int accion){
        Posicion actual = new Posicion(i,j);
        double recompensa = this.recompensar(actual, accion);
        Posicion siguiente = this.elsiguiente(actual, accion);
        double maxQ = this.mejorQ(siguiente);
        // se aplica la formula
        Qvalues [i][j][accion] = recompensa + gamma*maxQ;
        //Qvalues [i][j][accion] = (1-this.alpha)*qViejo + this.alpha*(recompensa+(this.gamma*maxQ));
    }

    public long getIteracion() {
        return iteracion;
    }
    

    @Override
    //la corrida
    public void run() {
        
        Posicion estadoActual = estadoInicialAleatorio();
        //prueba seteo 
        int i = estadoActual.getI();int j = estadoActual.getJ();
        Border border = new MatteBorder(1,1,1,1,Color.RED) {};
        matrizCelda[i][j].setBorder(border);
        //fin prueba
        for (long iter=0; iter<this.maxIteracion;iter++){
            //prueba
            System.out.println(iter);
            border = new MatteBorder(1,1,1,1,Color.GRAY);
            matrizCelda[i][j].setBorder(border);
            //fin prueba
            //this.iteracion=iter;
            //int accion=this.elegirSiguiente(estadoActual);
            int accion=this.eGreedy(estadoActual);
            this.actualizarQtable(i,j, accion);
            Posicion estadoSiguiente = this.elsiguiente(estadoActual, accion);
            // prueba
            i = estadoSiguiente.getI();j = estadoSiguiente.getJ();
            border = new MatteBorder(1,1,1,1,Color.RED) {};
            matrizCelda[i][j].setBorder(border);
            
            //fin prueba
            switch (map[i] [j]){
                case 0: estadoActual=estadoSiguiente;break; //normal
                case 1: estadoActual=estadoSiguiente;break; //malo
                case 2: estadoActual=estadoSiguiente;break;  //bueno
                case 3: estadoActual=estadoSiguiente;break;  //excelente
                case 4: estadoActual = estadoInicialAleatorio();break; // es el final, llegamos al objetivo, arranca de nuevo
                case 5: estadoActual=estadoSiguiente;break;  //pozo  
            }
        }
        //prueba
        border = new MatteBorder(1,1,1,1,Color.GRAY) {};
        matrizCelda[i][j].setBorder(border);
        System.out.println("terminado aprendizaje");
        //fin prueba
    }
    public void pintarCamino(){
                    Boolean esFinal=true;
                    int i=0;int j =0;
                    Posicion pos = new Posicion(i,j);
                    Posicion sigx;
                    Border border = new MatteBorder(1,1,1,1,Color.WHITE) {};
                    this.matrizCelda[i][j].setBorder(border);
                    int accion = this.mejorAccion(pos);
                    Posicion sig = this.elsiguiente(pos, accion);
                    int x=sig.i;
                    int y =sig.j;
                    do{
                        if(Color.BLUE.equals(matrizCelda[x][y].getBackground())){
                            esFinal=false;
                        }
                        this.matrizCelda[x][y].setBorder(border);
                        sigx = sig;
                        accion = this.mejorAccion(sig);
                        sig = this.elsiguiente(sigx, accion);
                        x= sig.getI();
                        y =sig.getJ();
                    } while(esFinal);
    }
    // esto era para probar la carga de Qtable
    public void imprimirQtable(){
        double x;
        for(int j=0;j<tamano;j++){
           for(int i=0;i<tamano;i++){
               for (int a=0;a<8;a++){
                    x=Qvalues[i][j][a];
                    System.out.println(x); 
               }

            }
        }
    }
    
}
