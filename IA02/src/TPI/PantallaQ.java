/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author fede
 */
public class PantallaQ extends javax.swing.JFrame {

    JPanel panelQ1;
    JPanel panelQ2;
    JPanel panelQ3;
    JPanel panelQ4;
    JPanel panelQ5;
    JPanel panelQ51;
    JPanel panelQ52;
    JPanel panelQ6;
    JPanel panelQ7;
    JPanel panelQ8;
    JPanel panelQ9;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JLabel label5;
    JLabel label51;
    JLabel label52;
    JLabel label6;
    JLabel label7;
    JLabel label8;
    JLabel label9;
    JTextField textoI;
    JTextField textoJ;
    JButton botonMostrar;
    JButton botonUp;
    JButton botonDown;
    JButton botonRight;
    JButton botonLeft;
    JButton botonUpRight;
    JButton botonUpLeft;
    JButton botonDownRight;
    JButton botonDownLeft;
    
    //matriz que contiene las acciones posibles
    Boolean matrizA[][][];
    // tabla de Qvalues en [i][j][accion]
    double matrizQ [][][] = new double [10][10][8];
    //
    int tm=6;
    public PantallaQ(){};
    
    public PantallaQ(Grilla g,QLearning b) {
        matrizA=g.matrizA;
        if(b!=null){
        matrizQ=b.Qvalues;
        } else {
            for(int j=0;j<10;j++){
                for(int i=0;i<10;i++){
                    for (int a=0;a<8;a++){
                        matrizQ[i][j][a]=0.0;
                    }
                }
            }
        }
        tm=g.tmno;
        initComponents();
        //layout del frame
        setLayout(new GridBagLayout());
        GridBagConstraints gbcQ = new GridBagConstraints();
        //borde para los paneles internos
        Border border;
        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
        // dimensiones
        Dimension dimension;
        dimension = new Dimension(100,100);
        Dimension dimension2;
        dimension2 = new Dimension(90,45);
        Dimension dimension3;
        dimension3 = new Dimension(45,22);
        Rectangle bound;
        bound = new Rectangle(100,30);
        //paneles internos
        panelQ1 = new JPanel();
        panelQ1.setLayout(new GridBagLayout());
        panelQ1.setBorder(border);
        panelQ1.setPreferredSize(dimension);
        
        label1 = new JLabel();
        label1.setBounds(bound);
        label1.setText("-");
        panelQ1.add(label1);
        gbcQ.gridx=2;gbcQ.gridy=2;
        this.add(panelQ1,gbcQ);
        
        panelQ2 = new JPanel();
        panelQ2.setLayout(new GridBagLayout());
        panelQ2.setBorder(border);
        panelQ2.setPreferredSize(dimension);
        
        label2 = new JLabel();
        label2.setBounds(bound);
        label2.setText("-");
        panelQ2.add(label2);
        gbcQ.gridx=3;gbcQ.gridy=2;
        this.add(panelQ2,gbcQ);
        
        panelQ3 = new JPanel();
        panelQ3.setLayout(new GridBagLayout());
        panelQ3.setBorder(border);
        panelQ3.setPreferredSize(dimension);
        
        label3 = new JLabel();
        label3.setBounds(bound);
        label3.setText("-");
        panelQ3.add(label3);
        gbcQ.gridx=4;gbcQ.gridy=2;
        this.add(panelQ3,gbcQ);
        
        panelQ4 = new JPanel();
        panelQ4.setLayout(new GridBagLayout());
        panelQ4.setBorder(border);
        panelQ4.setPreferredSize(dimension);
        
        label4 = new JLabel();
        label4.setBounds(bound);
        label4.setText("-");
        panelQ4.add(label4);
        gbcQ.gridx=2;gbcQ.gridy=3;
        this.add(panelQ4,gbcQ);
        
        panelQ5 = new JPanel();
        panelQ5.setLayout(new GridBagLayout());
        panelQ5.setBorder(border);
        panelQ5.setPreferredSize(dimension);
        //
        panelQ51 = new JPanel ();
        panelQ51.setPreferredSize(dimension2);
        panelQ51.setLayout(new GridBagLayout());
        label51 = new JLabel ();
        label51.setText("col:");
        label51.setSize(dimension3);
        gbcQ.gridx=1;gbcQ.gridy=1;
        panelQ51.add(label51,gbcQ);
        textoI = new JTextField();
        textoI.setPreferredSize(dimension3);
        gbcQ.gridx=2;gbcQ.gridy=1;
        panelQ51.add(textoI,gbcQ);
        
        label52 = new JLabel ();
        label52.setText("fila:");
        label52.setSize(dimension3);
        gbcQ.gridx=1;gbcQ.gridy=2;
        panelQ51.add(label52,gbcQ);
        textoJ = new JTextField();
        textoJ.setPreferredSize(dimension3);
        gbcQ.gridx=2;gbcQ.gridy=2;
        panelQ51.add(textoJ,gbcQ);
        
        gbcQ.gridx=1;gbcQ.gridy=1;
        panelQ5.add(panelQ51,gbcQ);
        
        panelQ52 = new JPanel ();
        panelQ52.setPreferredSize(dimension2);
        gbcQ.gridx=1;gbcQ.gridy=3;
        botonMostrar = new JButton ();
        botonMostrar.setPreferredSize(dimension2);
        botonMostrar.setText("ver valores");
        panelQ52.add(botonMostrar);
        botonMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMostrarActionPerformed(evt);
            }
        });
        panelQ5.add(panelQ52,gbcQ);
        //
        gbcQ.gridx=3;gbcQ.gridy=3;
        this.add(panelQ5,gbcQ);
        
        panelQ6 = new JPanel();
        panelQ6.setLayout(new GridBagLayout());
        panelQ6.setBorder(border);
        panelQ6.setPreferredSize(dimension);
        label6 = new JLabel();
        label6.setBounds(bound);
        label6.setText("-");
        panelQ6.add(label6);
        gbcQ.gridx=4;gbcQ.gridy=3;
        this.add(panelQ6,gbcQ);
        
        panelQ7 = new JPanel();
        panelQ7.setLayout(new GridBagLayout());
        panelQ7.setBorder(border);
        panelQ7.setPreferredSize(dimension);
        label7 = new JLabel();
        label7.setBounds(bound);
        label7.setText("-");
        panelQ7.add(label7);
        gbcQ.gridx=2;gbcQ.gridy=4;
        this.add(panelQ7,gbcQ);
        
        panelQ8 = new JPanel();
        panelQ8.setLayout(new GridBagLayout());
        panelQ8.setBorder(border);
        panelQ8.setPreferredSize(dimension);
        label8 = new JLabel();
        label8.setBounds(bound);
        label8.setText("-");
        panelQ8.add(label8);
        gbcQ.gridx=3;gbcQ.gridy=4;
        this.add(panelQ8,gbcQ);
        
        panelQ9 = new JPanel();
        panelQ9.setLayout(new GridBagLayout());
        panelQ9.setBorder(border);
        panelQ9.setPreferredSize(dimension);
        label9 = new JLabel();
        label9.setBounds(bound);
        label9.setText("-");
        panelQ9.add(label9);
        gbcQ.gridx=4;gbcQ.gridy=4;
        this.add(panelQ9,gbcQ);
        
        // ahora los 4 botones externos
        botonUp = new JButton();
        botonUp.setPreferredSize(dimension);
        botonUp.setText("Ver Superior");
        gbcQ.gridx=3;gbcQ.gridy=1;
        this.add(botonUp,gbcQ);
        botonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonUpActionPerformed(evt);
            }
        });
        
        botonRight = new JButton();
        botonRight.setPreferredSize(dimension);
        botonRight.setText("Ver Derecha");
        gbcQ.gridx=5;gbcQ.gridy=3;
        this.add(botonRight,gbcQ);
        botonRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRightActionPerformed(evt);
            }
        });
        
        botonDown = new JButton();
        botonDown.setPreferredSize(dimension);
        botonDown.setText("Ver Inferior");
        gbcQ.gridx=3;gbcQ.gridy=5;
        this.add(botonDown,gbcQ);
        botonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDownActionPerformed(evt);
            }
        });
        
        botonLeft = new JButton();
        botonLeft.setPreferredSize(dimension);
        botonLeft.setText("Ver Izquierda");
        gbcQ.gridx=1;gbcQ.gridy=3;
        this.add(botonLeft,gbcQ);
        botonLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLeftActionPerformed(evt);
            }
        });
        
        botonUpRight = new JButton();
        botonUpRight.setPreferredSize(dimension);
        botonUpRight.setText("Ver Sup-Der");
        gbcQ.gridx=5;gbcQ.gridy=1;
        this.add(botonUpRight,gbcQ);
        botonUpRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonUpRightActionPerformed(evt);
            }
        });
        
        botonUpLeft = new JButton();
        botonUpLeft.setPreferredSize(dimension);
        botonUpLeft.setText("Ver Sup-Izq");
        gbcQ.gridx=1;gbcQ.gridy=1;
        this.add(botonUpLeft,gbcQ);
        botonUpLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonUpLeftActionPerformed(evt);
            }
        });
        
        botonDownRight = new JButton();
        botonDownRight.setPreferredSize(dimension);
        botonDownRight.setText("Ver Inf-Der");
        gbcQ.gridx=5;gbcQ.gridy=5;
        this.add(botonDownRight,gbcQ);
        botonDownRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDownRightActionPerformed(evt);
            }
        });
        botonDownLeft = new JButton();
        botonDownLeft.setPreferredSize(dimension);
        botonDownLeft.setText("Ver Inf-Izq");
        gbcQ.gridx=1;gbcQ.gridy=5;
        this.add(botonDownLeft,gbcQ);
        botonDownLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDownLeftActionPerformed(evt);
            }
        });
        
    }
    // los eventos
    private void botonMostrarActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText()); 
        int j = Integer.parseInt(textoJ.getText());
        
        String texto;
        double maximo=this.mejorQ();
        double a=0.0;
        if ( i<0 || j<0 || j>(tm-1) ||i>(tm+1)){
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }else{
        for(int x=0;x<8;x++){
            if(matrizA[i][j][x]){
                a=matrizQ[i][j][x];
                texto = Double.toString(a);
            }else{
                texto = "-";
            }
            switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO
                    }
        }
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void botonUpActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (j>0){
            textoJ.setText(Integer.toString(j-1));
            double maximo=this.mejorQ();
            double a=0.0;
            String texto;
            for(int x=0;x<8;x++){
                if(matrizA[i][j-1][x]){
                    a=matrizQ[i][j-1][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO 
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void botonDownActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (j<(tm-1)){
            textoJ.setText(Integer.toString(j+1));
            double maximo=this.mejorQ();
            double a=0.0;
            String texto;
            for(int x=0;x<8;x++){
                if(matrizA[i][j+1][x]){
                    a=matrizQ[i][j+1][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO   
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        } 
    }
    
    private void botonLeftActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (i>0){
            textoI.setText(Integer.toString(i-1));
            String texto;
            double maximo=this.mejorQ();
            double a=0.0;
            for(int x=0;x<8;x++){
                if(matrizA[i-1][j][x]){
                    a=matrizQ[i-1][j][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO  
                        }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void botonRightActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (i<(tm-1)){
            textoI.setText(Integer.toString(i+1));
            String texto;
            double maximo=this.mejorQ();
            double a=0.0;
            for(int x=0;x<8;x++){
                if(matrizA[i+1][j][x]){
                    a=matrizQ[i+1][j][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO  
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void botonUpRightActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (i<(tm-1) && (j>0) ){
            textoI.setText(Integer.toString(i+1));
            textoJ.setText(Integer.toString(j-1));
            String texto;
            double maximo=this.mejorQ();
            double a=0.0;
            for(int x=0;x<8;x++){
                if(matrizA[i+1][j-1][x]){
                    a=matrizQ[i+1][j-1][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO 
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void botonUpLeftActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (i<(tm-1) && (j>0) ){
            textoI.setText(Integer.toString(i-1));
            textoJ.setText(Integer.toString(j-1));
            String texto;
            double maximo=this.mejorQ();
            double a=0.0;
            for(int x=0;x<8;x++){
                if(matrizA[i-1][j-1][x]){
                    a=matrizQ[i-1][j-1][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void botonDownRightActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if (i<(tm-1) && (j<(tm-1)) ){
            textoI.setText(Integer.toString(i+1));
            textoJ.setText(Integer.toString(j+1));
            String texto;
            double maximo=this.mejorQ();
            double a=0.0;
            for(int x=0;x<8;x++){
                if(matrizA[i+1][j+1][x]){
                    a=matrizQ[i+1][j+1][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
    
     private void botonDownLeftActionPerformed(java.awt.event.ActionEvent evt) {
        try{
        int i = Integer.parseInt(textoI.getText());
        int j = Integer.parseInt(textoJ.getText());
        
        if ((i>0) && (j<(tm-1)) ){
            textoI.setText(Integer.toString(i-1));
            textoJ.setText(Integer.toString(j+1));
            String texto;
            double maximo=this.mejorQ();
            double a=0.0;
            for(int x=0;x<8;x++){
                if(matrizA[i-1][j+1][x]){
                    a=matrizQ[i-1][j+1][x];
                    texto = Double.toString(a);
                }else{
                    texto = "-";
                }
                switch (x){
                        case 0:label4.setText(texto);if(a==maximo){label4.setForeground(Color.BLUE);}else{label4.setForeground(Color.BLACK);};break; //O 
                        case 1:label7.setText(texto);if(a==maximo){label7.setForeground(Color.BLUE);}else{label7.setForeground(Color.BLACK);};break; //SO 
                        case 2:label8.setText(texto);if(a==maximo){label8.setForeground(Color.BLUE);}else{label8.setForeground(Color.BLACK);};break;  //S 
                        case 3:label9.setText(texto);if(a==maximo){label9.setForeground(Color.BLUE);}else{label9.setForeground(Color.BLACK);};break;   //SE 
                        case 4:label6.setText(texto);if(a==maximo){label6.setForeground(Color.BLUE);}else{label6.setForeground(Color.BLACK);};break;  //E  
                        case 5:label3.setText(texto);if(a==maximo){label3.setForeground(Color.BLUE);}else{label3.setForeground(Color.BLACK);};break; //NE 
                        case 6:label2.setText(texto);if(a==maximo){label2.setForeground(Color.BLUE);}else{label2.setForeground(Color.BLACK);};break;    //N    
                        case 7:label1.setText(texto);if(a==maximo){label1.setForeground(Color.BLUE);}else{label1.setForeground(Color.BLACK);};break;   //NO  
                        }
            }
        }else {
            JOptionPane.showMessageDialog(null, "No se puede desplazar fuera de la grilla", "ERROR, Movimiento invalido", JOptionPane.WARNING_MESSAGE);
        }
        }catch (Exception err) {
            textoI.setText("0");
            textoJ.setText("0");
            JOptionPane.showMessageDialog(null, "Inserte valores enteros, entre 0 y "+(tm-1), "ERROR, la grilla es de "+tm+"X"+tm, JOptionPane.WARNING_MESSAGE);
        }
    }
     
      private double mejorQ(){
          int i=Integer.parseInt(textoI.getText());
          int j=Integer.parseInt(textoJ.getText());
          double valor=-1000.00;
          for(int x=0;x<8;x++){
                if(matrizA[i][j][x]){
                    double a=matrizQ[i][j][x];
                    if(a>valor){
                        valor=a;
                    }
                }
          }
          return valor;
      }
    
    //control de insercion de datos
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PantallaQ.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PantallaQ.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PantallaQ.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PantallaQ.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaQ().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
