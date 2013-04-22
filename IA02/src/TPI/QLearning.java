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
    public QLearning (int tmño,long itmax, double exp, double amort, double recB, double recE, double recN, double recF,double recM,int [][] mapa,Celda [][] mapaCeldas){
        this.maxIteracion=itmax;
        this.tamaño=tmño;
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
            int indice = java.lang.Math.round((float)java.lang.Math.random()*(8));
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
        for(int x=0;x<8;x++){
            //int a = (int) celda.listaAcciones.get(x);
            if(Qvalues[i][j][x]>mejorQ){
                mejorQ=Qvalues[i][j][x];
                laMejor=x;
            }            
        }
        return laMejor;
    }
    //calcular max Q para la formula de Q(s,a)
    public double mejorQ(Posicion pos){
        double mejorQ=-10000.0;
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        for(int x=0;x<8;x++){
            //int a = (int) celda.listaAcciones.get(x);
            if(Qvalues[i][j][x]>mejorQ){
                mejorQ=Qvalues[i][j][x];
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
        private Posicion siguiente(Posicion pos, int accion){
        Posicion sig = new Posicion();
        int i = pos.getI(); int j = pos.getJ();
        switch (accion){
            case N:
            if(i==0){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //estoy fila 0 no puedo ir arriba, quedo mismo lugar
            } else {
                if (map[(i-1)][j]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    //el siguiente estado seria pozo, no deberia poder ir
                } else {
                    sig.setI(i-1);sig.setJ(j);
                    //resultado[0]=(i-1); resultado [1]=j;
                }
            }
            break;
            case NE:
            if(i==0 || j==(tamaño-1)){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //primera fila o ultima columna, no puedo moverme
            } else {
                if (map[(i-1)][j+1]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    //el siguiente estado seria pozo, no deberia poder ir
                } else {
                    sig.setI(i-1);sig.setJ(j+1);
                    //resultado[0]=(i-1); resultado [1]=(j+1);
                }
            }
            break;
            case E:
            if (j==(tamaño-1)){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //estoy en la ultima columna no puedo ir a la derecha
            } else {
                if (map[i][(j+1)]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    //el siguiente estado es pozo, no debo poder ir
                } else {
                    //resultado[0]=i; resultado [1]=j+1;
                    sig.setI(i);sig.setJ(j+1);
                }
            }
            break;
            case SE:
            if (i==(tamaño-1) || j==(tamaño-1)){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //ultima columna y ultima fila, no se debe poder mover
            } else {
                if (map[i+1][(j+1)]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    //el siguiente estado es pozo, no debo poder ir
                } else {
                    //resultado[0]=(i+1); resultado [1]=(j+1);
                    sig.setI(i+1);sig.setJ(j+1);
                }
            }
            break;    
            case S:
            if (i==(tamaño-1)){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //estoy en la ultima columna no deberia poder ir
            } else {
                if (map[i+1][j]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    //siguiente estado es pozo, no deberia ir
                } else {
                    //resultado[0]=i+1; resultado [1]=j;
                    sig.setI(i+1);sig.setJ(j);
                }
            }
            break;
            case SO:
            if (i==(tamaño-1) || j==0){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //primera columna o ultima columna no se debe mover
            } else {
                if (map[i+1][j-1]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    //siguiente estado es pozo, no deberia ir
                } else {
                    //resultado[0]=i+1; resultado [1]=j-1;
                    sig.setI(i+1);sig.setJ(j);
                }
            }
            break;    
            case O:
            if (j==0){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //primera columna no puedo ir a la izquierda
            } else {
                if (map[i][j-1]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    // siguiente es un pozo, no puedo ir
                } else {
                    //resultado[0]=i; resultado [1]=j-1;
                    sig.setI(i);sig.setJ(j-1);
                }
            }
            break;    
            case NO:
            if (i==0 || j==0){
                //resultado[0]=i; resultado [1]=j;
                sig.setI(i);sig.setJ(j);
                //primera columna o primera fila, no me puedo mover
            } else {
                if (map[i-1][j-1]==5){
                    //resultado[0]=i; resultado [1]=j;
                    sig.setI(i);sig.setJ(j);
                    // siguiente es un pozo, no puedo ir
                } else {
                    //resultado[0]=i-1; resultado [1]=j-1;
                    sig.setI(i-1);sig.setJ(j-1);
                }
            }
            break; 
        }
        return sig;
    }
    // funcion que devuelve la recompensa directa
    public double recompensar(Posicion pos,int accion){
                //valor por defecto
        double resultado=0.0;
        Posicion sig = this.siguiente(pos, accion);
        //calidad, bueno malo, etc..        
        int calidad = map[sig.getI()][sig.getJ()];
        if (pos.getI()!=sig.getI() && pos.getJ()!=sig.getJ()){
          switch (calidad){
            case 0:resultado=this.recNormal;break; //es normal
            case 1:resultado=this.recMalo;break; //es malo 
            case 2:resultado=this.recBueno;break; //es bueno
            case 3:resultado=this.recExcelente;break; //es excelente
            case 4:resultado=this.recFinal;break; // es el obj final
            case 5:break; //es pozo no deberia tomarla igual
        }  
        }
        return resultado;
    }
    // metodos de seleccion, por ahora solo egreedy
    public int elegirSiguiente (Posicion pos){
        int accion;
        double random = java.lang.Math.random();
        int i=pos.getI(); int j=pos.getJ();
        Celda celda = matrizCelda[i][j];
        if (random<this.epsilon){
            //cae dentro de la exploracion, accion es aleatoria
            accion = (int) java.lang.Math.round((float)java.lang.Math.random()*(8));
            //la accion es la accion posible que esta en un indice aleatorio ( dado por un numero aleatorio multiplicado por la cant de acciones posibles, redondeado para que sea entero )
        } else {
            //cae dentro de la parte de explotacion, accion es la mejor
            accion = mejorAccion(pos);
        }
        return accion;
    }
    
    //estado de partida aleatoria
    private Posicion estadoInicialAleatorio() {
        Posicion resultado = new Posicion();
        resultado.setI((int)java.lang.Math.round((float)(java.lang.Math.random()*(tamaño-1))));
        resultado.setJ((int)java.lang.Math.round((float)(java.lang.Math.random()*(tamaño-1))));
        return resultado;
    }
    //actualizar tabla Qvalues
    private void actualizarQtable (Posicion pos, int accion){
        int i = pos.getI();int j = pos.getJ();
        double qViejo = Qvalues[i][j][accion];
        double recompensa = this.recompensar(pos, accion);
        Posicion siguiente = this.siguiente(pos, accion);
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
        Posicion estadoActual = this.estadoInicialAleatorio();
        //prueba seteo 
        int i = estadoActual.getI();int j = estadoActual.getJ();
        Border border = new MatteBorder(1,1,1,1,Color.ORANGE) {};
        matrizCelda[i][j].setBorder(border);
        //fin prueba
        for (long iter=0; iter<this.maxIteracion;iter++){
            //prueba
            border = new MatteBorder(1,1,1,1,Color.GRAY) {};
            matrizCelda[i][j].setBorder(border);
            //fin prueba
            this.iteracion=iter;
            int accion=this.elegirSiguiente(estadoActual);
            Posicion estadoSiguiente = this.siguiente(estadoActual, accion);
            // prueba
            i = estadoSiguiente.getI();j = estadoSiguiente.getJ();
            border = new MatteBorder(1,1,1,1,Color.ORANGE) {};
            matrizCelda[i][j].setBorder(border);
            //fin prueba
            switch (map[estadoSiguiente.getI()] [estadoSiguiente.getJ()]){
                case 0: estadoActual=estadoSiguiente;break; //normal
                case 1: estadoActual=estadoSiguiente;break; //malo
                case 2: estadoActual=estadoSiguiente;break;  //bueno
                case 3: estadoActual=estadoSiguiente;break;  //excelente
                case 4: this.estadoInicialAleatorio();break; // es el final, llegamos al objetivo, arranca de nuevo
                case 5: estadoActual=estadoSiguiente;break;  //pozo  
            }
        }
        //prueba
        border = new MatteBorder(1,1,1,1,Color.GRAY) {};
        matrizCelda[i][j].setBorder(border);
        //fin prueba
    }
}
