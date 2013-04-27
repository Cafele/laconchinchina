package TPI;

import java.awt.Color;
import javax.swing.JOptionPane;
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
    double cantPasos=500;
    //recompensas
    double recBueno = 5;
    double recExcelente = 10;
    double recFinal = 100.0;
    double recMalo = -10.0;
    double recNormal = 0;
    
    //estos parametros capaz se sacan: (alpha, tau hay q hacer softmax)
    double tau=1000; //temperatura para softmax
    double alpha = 0.2; //para la formula de Q(s,a), es la de aprendizaje
    
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
    // contador
    double recompensa =0;

    //constructor
    public QLearning (int tmno,long itmax, double exp, double amort, double recB, double recE, double recN, double recF,double recM,Grilla grid,double pasos, double apren){
        this.maxIteracion=itmax;
        this.cantPasos=pasos;
        this.tamano=tmno;
        //this.Tau=temp;
        this.alpha=apren;
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
        int accion=0;
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
    //-aleatorio
    public int aleatorio(Posicion pos){
        
        int accion;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        Boolean x;
        double random;
        do {
            random = java.lang.Math.random();
            accion=(int)(random*7);
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
                probN=(Math.exp((Qvalues[i][j][a])/tau))/total;
            case NE:
                probNE=((Math.exp((Qvalues[i][j][a])/tau))/total)+probN;
            case E:
                probE=((Math.exp((Qvalues[i][j][a])/tau))/total)+probNE;    
            case SE:
                probSE=((Math.exp((Qvalues[i][j][a])/tau))/total)+probE;
            case S:
                probS=((Math.exp((Qvalues[i][j][a])/tau))/total)+probSE;
            case SO:
                probSO=((Math.exp((Qvalues[i][j][a])/tau))/total)+probS;
            case O:
                probO=((Math.exp((Qvalues[i][j][a])/tau))/total)+probSO;    
            case NO:
                probNO=((Math.exp((Qvalues[i][j][a])/tau))/total)+probO;   
            }
        }
          
        do {
            random = java.lang.Math.random();
            
            if(random < probN){
                accion = 0;//va norte
            }else{
                if(random < probNE){
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
        double qViejo = Qvalues [i][j][accion];
        Posicion actual = new Posicion(i,j);
        double recomp = this.recompensar(actual, accion);
        Posicion siguiente = this.elsiguiente(actual, accion);
        double maxQ = this.mejorQ(siguiente);
        // se aplica la formula
        //Qvalues [i][j][accion] = recomp+ gamma*maxQ;
        Qvalues [i][j][accion] =  qViejo + this.alpha*(recomp+(this.gamma*maxQ) - qViejo);
    }


    @Override
    public void run(){
        int i ;int j ; int x; int accion;
        Posicion pos;
        Posicion sig;
        Border border;
        
        for (long iter=0; iter<this.maxIteracion;iter++){
            
            
            i=(int) (iter/tamano);
            while(i>=tamano){
                i=i-tamano;
            }
            j=(int) (iter%tamano);
            pos = new Posicion(i,j);
            
            x=0;
            do{
                border = new MatteBorder(1,1,1,1,Color.RED);
                matrizCelda[i][j].setBorder(border);
                //
                accion=this.eGreedy(pos);
                //
                actualizarQtable(i,j, accion);
                //
                border = new MatteBorder(1,1,1,1,Color.GRAY);
                matrizCelda[i][j].setBorder(border);
                //
                sig = elsiguiente(pos, accion);
                //
                pos=sig;
                x++;
                //System.out.println(recompensa);
            }while (x<cantPasos && (map[i][j]!=4)) ;
            System.out.println(iter);
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
    
    
}
