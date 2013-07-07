package TPI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author fede
 */

 //representa una celda de la grilla o GridWorld
public class Celda extends JPanel {
    //Atributos:
    
    //Posicion de la celda dentro de la grilla
    Posicion posicion;
    //Colores segun el estado, para tener una referencia de que representa
    Color colorBueno = Color.YELLOW;
    Color colorExcelente = Color.GREEN;
    Color colorMalo = Color.RED;
    Color colorNormal = Color.LIGHT_GRAY;
    Color colorPozo = Color.BLACK;
    Color colorFinal = Color.BLUE;
    Color colorCamino = Color.CYAN;
    Color colorAgente = Color.WHITE;
    Color colorInicio = Color.ORANGE;
    //indica que la celda es punto de partida para graficar el camino aprendido
    Boolean esInicial = false;
    //indica que la celda es el objetivo final
    Boolean esFinal = false;
    //para control de bucles, se indica que una celda ya es parte del camino, para no volver a pintarla
    Boolean noEsCamino = true;
    //con estas variables se indica hacia donde va y desde donde viene, para graficar el camino
    int caminoAnt=10;
    int caminoSig=10;
    //indica que el tipo de seleccion de celda si es inicial, final, bueno, malo, excelente, final o pozo
    //modifica el accionar del mouseListener
    Boolean seleccionInicio = false;
    Boolean selN = true;
    Boolean selM = false;
    Boolean selB = false;
    Boolean selE = false;
    Boolean selF = false;
    Boolean selP = false;
    
    //constructor
    public Celda(Posicion pos){
        //Se inicial la celda con su posicion correspondiente
        posicion = pos;
        //esta variable se utiliza para graficar el camino, se inicializa como no parte del mismo.
        noEsCamino = true;
        //se añade el mouse listener
        addMouseListener (new MouseAdapter() {
            //el evento va a ser mouse presionado
            public void mousePressed(MouseEvent e) {
                //si el tipo de seleccion es para celda inicial:
                if (seleccionInicio){
                    Border border;
                    border = new MatteBorder(3, 3, 3, 3, Color.BLUE);
                    setBorder(border);
                    esInicial = true;
                } else {
                    //sino, es seleccion de tipo normal y se revisa el tipo de celda
                    // y se pinta la celda con su color correspondiente
                    esFinal = false;
                    if (selM){
                    setBackground(colorMalo);
                    } else {
                        if (selB){
                            setBackground(colorBueno);
                        } else {
                            if (selE){
                                setBackground(colorExcelente);
                            } else {
                                if (selP){
                                    setBackground(colorPozo);
                                } else {
                                    if (selF){
                                        setBackground (colorFinal);
                                        esFinal = true;
                                    } else {
                                        if(selN){
                                        setBackground (colorNormal);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
           }
       });
    }

    //algunos getters y setter
    public Color getColor() {
        return this.getBackground();
    }
    public int getTipo() {
            //por defecto se devuelve tipo normal
            int tipo = 0;

                if (colorNormal.equals(getColor())){
                    tipo = 0;
                //tipo normal
                } else {
                    if (colorMalo.equals(getColor())){
                        tipo = 1;
                    //tipo malo
                    } else {
                        if ( colorBueno.equals(getColor())){
                            tipo = 2;
                        //tipo bueno
                        } else {
                            if ( colorExcelente.equals(getColor())){
                                tipo = 3;
                            //tipo excelente
                            } else {
                                if ( colorFinal.equals(getColor())){
                                    tipo = 4;
                                //tipo final
                                } else {
                                    if (colorPozo.equals(getColor())){
                                        tipo = 5;
                                    //tipo pozo
                                    }
                                }
                            }
                        }
                    }
                }
            return tipo;
    }
    // funcion que revisa si la celda es de tipo inicial
    public Boolean esInicial(){
        return esInicial;
    }
    //funcion que pinta los caminos aprendidos
    //cada celda pinta dos lineas, una desde el centro en direccion desde donde vino
    //y otra desde el centro en direccion hacia donde va
    public void paint(Graphics g){
                super.paint(g);
                Graphics2D g2d = ( Graphics2D ) g;
                int x1=20; int y1=20;int x2=20; int y2=20;
                if(caminoSig!=10){
                    // es parte del camino, se pinta
                    //segun donde viene se pinta en direccion hacia ahi.
                    switch (caminoAnt){
                        case 0:x1=40;y1=20;break;//O 
                        case 1:x1=40;y1=0;break; //SO 
                        case 2:x1=20;y1=0;break;  //S 
                        case 3:x1=0;y1=0;break;   //SE 
                        case 4:x1=0;y1=20;break;  //E  
                        case 5:x1=0;y1=40;break; //NE 
                        case 6:x1=20;y1=40;break;    //N    
                        case 7:x1=40;y1=40;break;   //NO 
                        default:x1=20;y1=20;break; //inicio  
                    }
                    //segun donde va se pinta en direccion ahi ese lugar
                    switch (caminoSig){
                        case 0:x2=0;y2=20;break; //O 
                        case 1:x2=0;y2=40;break; //SO 
                        case 2:x2=20;y2=40;break;  //S 
                        case 3:x2=40;y2=40;break;   //SE 
                        case 4:x2=40;y2=20;break;  //E  
                        case 5:x2=40;y2=0;break; //NE 
                        case 6:x2=20;y2=0;break;    //N    
                        case 7:x2=0;y2=0;break;   //NO 
                    }
                    // por ultimo se grafican las lineas
                    g2d.setStroke(new BasicStroke(4.0f));
                    g2d.setColor(Color.BLUE);
                    g2d.draw( new Line2D.Double(x1,y1,20,20));
                    g2d.draw( new Line2D.Double(20,20,x2,y2));
                }
            }
    //tamaño de cada panel, 40x40 pixeles
    @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }
}
