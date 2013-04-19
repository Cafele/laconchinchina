/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
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
public class Celda extends JPanel {
    //representa una celda de la grilla o GridWorld
    Posicion posicion;
    ArrayList listaAcciones;
    Boolean esInicial = false;
    
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
    
    //constructor
    public Celda(Posicion posicion){
        this.posicion = posicion;
        //añadimos el listener
        addMouseListener (new MouseAdapter() {
            //el evento va a ser mouse presionado
            public void mousePressed(MouseEvent e) {
                
                if (colorNormal.equals(getBackground())){
                    Border border = new MatteBorder(1, 1, 1, 1, Color.RED);
                    setBorder(border);
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
                                setBackground (colorFinal);
                            }
                        }
                    }
                }
            }
           });
    }

    //algunos getters y setter
    
    public void esInicial(){
        this.esInicial = true;
    }
    
    public Posicion getPosicion() {
        return posicion;
    }

    public ArrayList getAcciones() {
        return listaAcciones;
    }

    public Color getColor() {
        return this.getBackground();
    }
    
    public int getCantAccion(){
        return this.listaAcciones.size();
    }
    
    public int getTipo() {
            int tipo = 0;
            //inicializamos como tipo normal
            if (esInicial){
                tipo=6;
                //tipo inicial
            } else {
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
            }
            return tipo;
    }
    
}
