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
    //atributos:
    
    Posicion posicion;
    // colores segun el estado, para tener una referencia de que representa
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
    Boolean noEsCamino = true;
    int caminoAnt=10;
    int caminoSig=10;
    //indica que el tipo de seleccion es de una celda inicial
    //modifica el accionar del mouseListener
    Boolean seleccionInicio = false;
    
    //constructor
    public Celda(Posicion pos){
        posicion = pos;
        noEsCamino = true;
        //añadimos el listener
        addMouseListener (new MouseAdapter() {
            
            //el evento va a ser mouse presionado
            public void mousePressed(MouseEvent e) {
                // si el tipo de seleccion es para celda inicial:
                if (seleccionInicio){
                    Border border;
                    border = new MatteBorder(3, 3, 3, 3, Color.BLUE);
                    setBorder(border);
                    esInicial = true;
                } else {
                    //sino, es seleccion de tipo normal:
                    if (colorNormal.equals(getBackground())){
                    setBackground(colorMalo);
                    } else {
                        if (colorMalo.equals(getBackground())){
                            setBackground(colorBueno);
                        } else {
                            if (colorBueno.equals(getBackground())){
                                setBackground(colorExcelente);
                            } else {
                                if (colorExcelente.equals(getBackground())){
                                    setBackground(colorPozo);
                                } else {
                                    if (colorPozo.equals(getBackground())){
                                        setBackground (colorFinal);
                                    } else {
                                        setBackground (colorNormal);
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
    public void paint(Graphics g){
                super.paint(g);
                Graphics2D g2d = ( Graphics2D ) g;
                int x1=20; int y1=20;int x2=20; int y2=20;
                if(caminoSig!=10){
                    // es parte del camino
                    switch (caminoAnt){
                        case 0:x1=40;y1=20;break;//N 
                        case 1:x1=40;y1=0;break; //NE 
                        case 2:x1=20;y1=0;break;  //E 
                        case 3:x1=0;y1=0;break;   //SE 
                        case 4:x1=0;y1=20;break;  //S  
                        case 5:x1=0;y1=40;break; //SO 
                        case 6:x1=20;y1=40;break;    //O    
                        case 7:x1=40;y1=40;break;   //NO 
                        default:x1=20;y1=20;break; //inicio  
                    }
                    switch (caminoSig){
                        case 0:x2=0;y2=20;break; //N 
                        case 1:x2=0;y2=40;break; //NE 
                        case 2:x2=20;y2=40;break;  //E 
                        case 3:x2=40;y2=40;break;   //SE 
                        case 4:x2=40;y2=20;break;  //S  
                        case 5:x2=40;y2=0;break; //SO 
                        case 6:x2=20;y2=0;break;    //O    
                        case 7:x2=0;y2=0;break;   //NO 
                    }  
                    
                    //g.drawLine(x1, y1, 25, 25);
                    //g.drawLine(25, 25, x2, y2);
                    g2d.setStroke(new BasicStroke(4.0f));
                    g2d.setColor(Color.BLUE);
                    g2d.draw( new Line2D.Double(x1,y1,20,20));
                    g2d.draw( new Line2D.Double(20,20,x2,y2));
                }
            }

    @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }
}
