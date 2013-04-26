package TPI;

import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

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
    long maxIteracion=10;
    //recompensas
    double recBueno = 25.0;
    double recExcelente = 50.0;
    double recFinal = 100.0;
    double recMalo = -10.0;
    double recNormal = 10.0;
    
    //estos parametros capaz se sacan: (alpha, tau hay q hacer softmax)
    //double Tau; //temperatura para softmax
    //double alpha = 0.1; //para la formula de Q(s,a), es la de aprendizaje
    
    double epsilon = 0.6; //exploration rate para egreedy
    double gamma = 0.8; //tambien para esa formula, es la de amortizacion
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
    
    //constructor
    public QLearning (int tmno,long itmax, double exp, double amort, double recB, double recE, double recN, double recF,double recM,Grilla grid){
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
        this.map=grid.getGrilla();
        this.matrizCelda=grid.matrizCeldas;
        this.matrizAccion=grid.matrizA;
        
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
        Celda celda = matrizCelda[i][j];
        Boolean x;
        double random = java.lang.Math.random();
        do {
            if (random<this.epsilon){
                //cae dentro de la exploracion, accion es aleatoria
                accion = (int)((java.lang.Math.random())*7);
            } else {
                //cae dentro de la parte de explotacion, accion es la mejor
                accion = mejorAccion(pos);
            }
            x = !(matrizAccion[i][j][accion]);
        } while (x);
        // repito hasta que la accion es una accion valida. es decir en la matrizA, es true.
        return accion;
    }
    
    //funcion que devuelve la accion que da el mejor Qvalue de una posicion dada
    public int mejorAccion(Posicion pos){
        int laMejor = 0;
        double mejorQ=-100000.0;
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
        double mejorQ=-100000.0;
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
    
    // funcion que devuelve el siguiente estado segun una accion realizada
    public Posicion elsiguiente(Posicion pos, int accion){
        Posicion sig = new Posicion();
        int i = pos.getI(); int j = pos.getJ();
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
   
    //funcion que devuelve un estado de partida aleatoria para el algoritmo Q
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


    @Override
    
    //la corrida del algoritmo
    public void run() {
        //se arranca de un estado inicial aleatorio
        Posicion estadoActual = estadoInicialAleatorio();
        int i = estadoActual.getI();int j = estadoActual.getJ();
        Border border = new MatteBorder(1,1,1,1,Color.RED) {};
        matrizCelda[i][j].setBorder(border);
        
        //se repite lo siguiente hasta llegar a la iteracion maxima
        for (long iter=0; iter<this.maxIteracion;iter++){
            System.out.println(iter);
            border = new MatteBorder(1,1,1,1,Color.GRAY);
            matrizCelda[i][j].setBorder(border);
            
            //se calcula la accion por el metodo de seleccion
            int accion=this.eGreedy(estadoActual);
            //se calcula y actualiza el valor de Q
            this.actualizarQtable(i,j, accion);
            //se toma como estado actual al siguiente,(de la accion tomada)
            Posicion estadoSiguiente = this.elsiguiente(estadoActual, accion);
            i = estadoSiguiente.getI();j = estadoSiguiente.getJ();
            
            border = new MatteBorder(2,2,2,2,Color.RED) {};
            matrizCelda[i][j].setBorder(border);
            //en caso de llegarse al final se vuelve a comenzar de un aleatorio
            switch (map[i] [j]){
                case 0: estadoActual=estadoSiguiente;break; //normal
                case 1: estadoActual=estadoSiguiente;break; //malo
                case 2: estadoActual=estadoSiguiente;break;  //bueno
                case 3: estadoActual=estadoSiguiente;break;  //excelente
                case 4: estadoActual = estadoInicialAleatorio();break; // es el final, llegamos al objetivo, arranca de nuevo
                case 5: estadoActual=estadoSiguiente;break;  //pozo  
            }
        }
        
        border = new MatteBorder(1,1,1,1,Color.GRAY) {};
        matrizCelda[i][j].setBorder(border);
        System.out.println("terminado aprendizaje");
    }

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
    
    
    
}
