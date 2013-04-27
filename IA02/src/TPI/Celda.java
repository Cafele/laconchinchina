package TPI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                    border = new MatteBorder(3, 3, 3, 3, Color.WHITE);
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
    
    @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }
}
