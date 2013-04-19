/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

/**
 *
 * @author fede
 */
public class QLearning {
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
    long maxIteracion=100000;
    //recompensas
    double recBueno = 25.0;
    double recExcelente = 50.0;
    double recFinal = 100.0;
    double recMalo = -10.0;
    double recNormal = 10.0;
    // parametros
    double Tau; //temperatura para softmax
    double epsilon = 0.7; //exploration rate para egreedy
    //alpha parece q no va, se saca de ultima
    double alpha = 0.1; //para la formula de Q(s,a), es la de aprendizaje
    double gamma = 0.9; //tambien para esa formula, es la de amortizacion
    // tabla de Qvalues en [i][j][accion]
    double Qvalues [][][];
    //grilla 
    int map[][];
    //tamaño de la columna/fila
    int tamaño=0;
    // a modo de prueba por ahora
    Celda matrizCelda[][];
    //constructor
    public QLearning (int tmño,long itmax, double exp,double apren, double temp, double amort, double recB, double recE, double recN, double recF,double recM,int [][] mapa,Celda [][] mapCell){
        this.maxIteracion=itmax;
        this.tamaño=tmño;
        this.Tau=temp;
        this.alpha=apren;
        this.gamma=amort;
        this.recBueno=recB;
        this.recMalo=recM;
        this.recExcelente=recE;
        this.recFinal=recF;
        this.recNormal=recN;
        this.map=mapa;
        this.matrizCelda=mapCell;
        //iniciar la tabla de q, el 8 va por las 8 acciones posibles 
        Qvalues=new double[tamaño][tamaño][8];
        for(int i=0;i<tamaño;i++){
            for(int j=0;j<tamaño;j++){
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
        if (random<this.epsilon){
            //cae dentro de la exploracion, accion es aleatoria
            int indice = java.lang.Math.round((float)java.lang.Math.random()*(celda.getCantAccion()));
            accion = (int) celda.listaAcciones.get(indice);
        } else {
            //cae dentro de la parte de explotacion, accion es la mejor
            accion = mejorAccion(pos);
        }
        return accion;
    }
    //para egreedy mejor accion, es greedy, devuelve la accion que da el mejor Q
    public int mejorAccion(Posicion pos){
        int laMejor = 0;
        double mejorQ=-10000.0;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        //entre las acciones posibles, miro la que devuelve el mejorQ
        for(int x=0;x<=celda.getCantAccion();x++){
            int a = (int) celda.listaAcciones.get(x);
            if(Qvalues[i][j][a]>mejorQ){
                mejorQ=Qvalues[i][j][a];
                laMejor=a;
            }            
        }
        return laMejor;
    }
    
}
