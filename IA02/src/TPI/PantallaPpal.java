package TPI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author fede
 */
public class PantallaPpal extends javax.swing.JFrame {
        Boolean delay=false;
        Boolean concur=true;
        JProgressBar bar = new JProgressBar();
   //
        PantallaQ pantQ;
        // las clases grilla, qlearning, thread para la corrida y la matriz de celdas
        Grilla grilla = new Grilla();
        QLearning bot;
        Thread aprendizaje;
        Celda [][] matrizC;
        // valores que son necesarios para el qlearning
        int tmno = 6;
        long itmax= 1000000;
        double recB = 5;
        double recE = 10;
        double recF = 100.0;
        double recM = -10.0;
        double recN = 0;
        double e=0.8;
        double gamma=0.9;
        double pasos = 500.0;
        double tau = 20;
        Boolean vaEgreedy=true;
        Boolean vaSoftmax=false;
        Boolean ede=false;
        Boolean sofde=false;
        Boolean optimista = false;
        int rep;
        int salt;
        //contador de prueba
        int conts;
        int contP;
        // para el grafico por programa
        JFreeChart grafico;
        //lista que guarda el log de cada prueba
        ArrayList<double[]> listaLogs;
        // lista del punto de convergencia ( 15 episodios sin cambiar el valor )
        ArrayList<double[]> listaConv;
        //archivo de excel
        Long [] tiempos;
        ArrayList<double[]> listaPasos;
        //HSSFWorkbook libro;
        //constructor
    public PantallaPpal() {
        initComponents();
        bar.setStringPainted(true);
        radioButonEst.setSelected(true);
        radioButonEst.doClick();
        // inicializo contador
        conts=0;
        contP=0;
        //cargo el menu de tamaños
        menuTamano.addItem("6");
        menuTamano.addItem("7");
        menuTamano.addItem("8");
        menuTamano.addItem("9");
        menuTamano.addItem("10");
        //se crea el gridworld
        panelGrilla.removeAll();
        panelGrilla.setLayout(new GridLayout());
        grilla = new Grilla(tmno);
        panelGrilla.add(grilla);
        setLocationRelativeTo(null);
        setVisible(true);
        //referencio la matriz de celdas
        matrizC = grilla.matrizCeldas;
        //se inicializan botones
        rbI.setEnabled(false);
        BotonInicial.setEnabled(true);
        BotonCamino1.setEnabled(true);
        rbI.setEnabled(false);
        rbN.setSelected(true);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        //botonReset.doClick();
        listaLogs = new ArrayList<double[]>();
        listaConv = new ArrayList<double[]>();
        tiempos = new Long [1000];
        listaPasos = new ArrayList<double[]>();
        //buttonStop.setEnabled(false);
        //buttonResume.setEnabled(false);
    }
    
    //funcion que pinta el camino aprendido
    public void pintarCamino() {
        try {
        double result=0.0;
        //booleano que si es celda final se utiliza luego
        Boolean noesFinal=true;
        //posicion inicial asignada
        Posicion pos = grilla.getInicial();
        Posicion sig;
        int i; int j; int accion;
        //borde de la posicion inicial
        Border border = new MatteBorder(3,3,3,3,Color.BLUE) {};
        i= pos.getI();j =pos.getJ();
        matrizC[i][j].setBorder(border);
        do{
            //actualizo variable noescamino
            matrizC[i][j].noEsCamino = false;
            //me fijo la accion siguiente y la guardo
            accion = bot.mejorAccion(pos);
            result=result+bot.mejorQ(pos);
            matrizC[i][j].caminoSig=accion;
            //repaint de las celdas
            matrizC[i][j].repaint();
            //me muevo al siguiente casillero
            sig = bot.elsiguiente(pos, accion);
            //actualizo variable
            pos = sig;
            //actualizo valores
            i= pos.getI();j =pos.getJ();
            //marco de donde viene
            matrizC[i][j].caminoAnt=accion;
            //reviso si es final
            if(Color.BLUE.equals(matrizC[i][j].getBackground())){
                    noesFinal=false;
            }
            //termina de pintar si llego al final o no aprendio y quedo en bucle
        }while (noesFinal && matrizC[i][j].noEsCamino);
        labelResultado1.setText("Valor Acumulado :");
        labelResultado.setText(" "+Double.toString(result));
        //mensaje de error si no puedo aprender llegar al final
        if(!(matrizC[i][j].noEsCamino)){
            JOptionPane.showMessageDialog(panelGrilla, "El camino aprendido, desde el punto indicado, no llega al final. Revise la cantidad de episodios.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
        } catch (Exception expa){
                grilla.limpiar();
                JOptionPane.showMessageDialog(grilla, "No se ha realizado un aprendizaje", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jFrame1 = new javax.swing.JFrame();
        panelControles = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        bar1 = new javax.swing.JProgressBar();
        rbN = new javax.swing.JRadioButton();
        rbM = new javax.swing.JRadioButton();
        rbB = new javax.swing.JRadioButton();
        rbP = new javax.swing.JRadioButton();
        rbE = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        rbI = new javax.swing.JRadioButton();
        rbF = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        BotonStart = new javax.swing.JButton();
        BotonCamino1 = new javax.swing.JButton();
        BotonInicial = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        radioButtonEgreedy = new javax.swing.JRadioButton();
        radioButtonSoftmax = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        radioButtonEd = new javax.swing.JRadioButton();
        radioButtonSd = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        botonG = new javax.swing.JButton();
        botonArch = new javax.swing.JButton();
        labelL = new javax.swing.JLabel();
        textRep = new javax.swing.JTextField();
        labelS = new javax.swing.JLabel();
        textSalto = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        buttonStop = new javax.swing.JButton();
        buttonResume = new javax.swing.JButton();
        buttonResume1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        radioButonEst = new javax.swing.JRadioButton();
        jSeparator4 = new javax.swing.JSeparator();
        jRadioButton3 = new javax.swing.JRadioButton();
        labelResultado = new javax.swing.JLabel();
        labelResultado1 = new javax.swing.JLabel();
        panelGrilla = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        textEpsilon = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        textGamma = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        textMaxIt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        textP = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        texttau = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        textN = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        textB = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        textM = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        textE = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        textF = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        menuTamano = new javax.swing.JComboBox();
        botonReset = new javax.swing.JButton();
        BotonAleatorio = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(922, 610));
        setMinimumSize(new java.awt.Dimension(922, 610));
        setPreferredSize(new java.awt.Dimension(922, 610));
        setResizable(false);

        panelControles.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelControles.setMaximumSize(new java.awt.Dimension(890, 46));
        panelControles.setMinimumSize(new java.awt.Dimension(890, 46));
        panelControles.setName(""); // NOI18N
        panelControles.setPreferredSize(new java.awt.Dimension(890, 46));

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Estado Inicial");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255), 3));

        bar1.setMaximumSize(new java.awt.Dimension(890, 18));
        bar1.setMinimumSize(new java.awt.Dimension(890, 18));
        bar1.setPreferredSize(new java.awt.Dimension(890, 18));

        rbN.setText("Normal");
        rbN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNActionPerformed(evt);
            }
        });

        rbM.setBackground(Color.RED);
        rbM.setText("Mala");
        rbM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMActionPerformed(evt);
            }
        });

        rbB.setBackground(Color.YELLOW);
        rbB.setText("Buena");
        rbB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBActionPerformed(evt);
            }
        });

        rbP.setBackground(Color.BLACK);
        rbP.setText("Pozo");
        rbP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPActionPerformed(evt);
            }
        });

        rbE.setBackground(Color.GREEN);
        rbE.setText("Excelente");
        rbE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEActionPerformed(evt);
            }
        });

        jLabel2.setText(" Tipo de celda:");

        rbI.setText("Inicial");
        rbI.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLUE, 3));
        rbI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIActionPerformed(evt);
            }
        });

        rbF.setBackground(Color.BLUE);
        rbF.setText("Final");
        rbF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbN)
                        .addGap(2, 2, 2)
                        .addComponent(rbB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbI)
                        .addGap(520, 520, 520)
                        .addComponent(jLabel9))
                    .addComponent(bar1, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(rbN)
                    .addComponent(rbM)
                    .addComponent(rbB)
                    .addComponent(rbP)
                    .addComponent(rbE)
                    .addComponent(jLabel2)
                    .addComponent(rbI)
                    .addComponent(rbF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(200, 500));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 500));

        BotonStart.setText("Iniciar Entrenamiento");
        BotonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonStartActionPerformed(evt);
            }
        });

        BotonCamino1.setText("Pintar Camino");
        BotonCamino1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCamino1ActionPerformed(evt);
            }
        });

        BotonInicial.setText("Borrar Camino");
        BotonInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonInicialActionPerformed(evt);
            }
        });

        jLabel22.setText("Camino Aprendido:");

        radioButtonEgreedy.setSelected(true);
        radioButtonEgreedy.setText("egreedy");
        radioButtonEgreedy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonEgreedyActionPerformed(evt);
            }
        });

        radioButtonSoftmax.setText("softmax");
        radioButtonSoftmax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonSoftmaxActionPerformed(evt);
            }
        });

        jLabel23.setText("Tipo de seleccion de accion:");

        radioButtonEd.setText("decreciente");
        radioButtonEd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonEdActionPerformed(evt);
            }
        });

        radioButtonSd.setText("decreciente");
        radioButtonSd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonSdActionPerformed(evt);
            }
        });

        jLabel24.setText("Aprendizaje:");

        botonG.setText("Graficos");
        botonG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGActionPerformed(evt);
            }
        });

        botonArch.setText("Archivo Nuevo");
        botonArch.setMaximumSize(new java.awt.Dimension(71, 23));
        botonArch.setMinimumSize(new java.awt.Dimension(71, 23));
        botonArch.setPreferredSize(new java.awt.Dimension(71, 23));
        botonArch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonArchActionPerformed(evt);
            }
        });

        labelL.setText("Lapso:");

        textRep.setText("500");
        textRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRepActionPerformed(evt);
            }
        });
        textRep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textRepFocusLost(evt);
            }
        });
        textRep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textRepKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textRepKeyReleased(evt);
            }
        });

        labelS.setText("Salto:");

        textSalto.setText("50");
        textSalto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textSaltoActionPerformed(evt);
            }
        });
        textSalto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textSaltoFocusLost(evt);
            }
        });
        textSalto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textSaltoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textSaltoKeyReleased(evt);
            }
        });

        jRadioButton1.setText("Iniciar con valores optimistas");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonStop.setText("Parar  Entrenamiento");
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopActionPerformed(evt);
            }
        });

        buttonResume.setText("Reanudar  Entrenamiento");
        buttonResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResumeActionPerformed(evt);
            }
        });

        buttonResume1.setText("Visualizar Matriz Q");
        buttonResume1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResume1ActionPerformed(evt);
            }
        });

        radioButonEst.setText("Estadisticas");
        radioButonEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButonEstActionPerformed(evt);
            }
        });

        jRadioButton3.setText("Delay");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator3)
            .addComponent(jSeparator4)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addGap(53, 53, 53))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(radioButonEst))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel24))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(jRadioButton3)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelL, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textRep, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(textSalto, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(radioButtonSoftmax)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(radioButtonSd))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel23)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(radioButtonEgreedy)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(radioButtonEd))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(labelResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(buttonResume, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addComponent(buttonResume1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(botonArch, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BotonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(BotonInicial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                .addComponent(BotonCamino1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(botonG, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(labelResultado1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonEgreedy)
                    .addComponent(radioButtonEd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonSoftmax)
                    .addComponent(radioButtonSd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addGap(5, 5, 5)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonStart)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonResume)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonResume1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonCamino1)
                .addGap(1, 1, 1)
                .addComponent(BotonInicial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelResultado1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(labelResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButonEst)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonArch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textRep, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelL)
                    .addComponent(labelS)
                    .addComponent(textSalto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        panelGrilla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelGrilla.setMaximumSize(new java.awt.Dimension(500, 500));
        panelGrilla.setMinimumSize(new java.awt.Dimension(500, 500));
        panelGrilla.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout panelGrillaLayout = new javax.swing.GroupLayout(panelGrilla);
        panelGrilla.setLayout(panelGrillaLayout);
        panelGrillaLayout.setHorizontalGroup(
            panelGrillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );
        panelGrillaLayout.setVerticalGroup(
            panelGrillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 511, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setMaximumSize(new java.awt.Dimension(190, 500));
        jPanel2.setMinimumSize(new java.awt.Dimension(190, 500));
        jPanel2.setPreferredSize(new java.awt.Dimension(190, 500));

        jLabel10.setText("Parametro de Politica egreedy:");

        textEpsilon.setText("0.8");
        textEpsilon.setMaximumSize(new java.awt.Dimension(100, 22));
        textEpsilon.setMinimumSize(new java.awt.Dimension(100, 22));
        textEpsilon.setPreferredSize(new java.awt.Dimension(100, 22));
        textEpsilon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textEpsilonActionPerformed(evt);
            }
        });
        textEpsilon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textEpsilonFocusLost(evt);
            }
        });
        textEpsilon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textEpsilonKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEpsilonKeyReleased(evt);
            }
        });

        jLabel11.setText("Epsilon:");

        textGamma.setText("0.9");
        textGamma.setMaximumSize(new java.awt.Dimension(100, 22));
        textGamma.setMinimumSize(new java.awt.Dimension(100, 22));
        textGamma.setPreferredSize(new java.awt.Dimension(100, 22));
        textGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textGammaActionPerformed(evt);
            }
        });
        textGamma.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textGammaFocusLost(evt);
            }
        });
        textGamma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textGammaKeyReleased(evt);
            }
        });

        jLabel12.setText("  Gamma:");

        textMaxIt.setText("10000");
        textMaxIt.setMaximumSize(new java.awt.Dimension(100, 22));
        textMaxIt.setMinimumSize(new java.awt.Dimension(100, 22));
        textMaxIt.setPreferredSize(new java.awt.Dimension(100, 22));
        textMaxIt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMaxItActionPerformed(evt);
            }
        });
        textMaxIt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textMaxItFocusLost(evt);
            }
        });
        textMaxIt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textMaxItKeyReleased(evt);
            }
        });

        jLabel13.setText("Episodios:");

        textP.setText("500");
        textP.setMaximumSize(new java.awt.Dimension(100, 22));
        textP.setMinimumSize(new java.awt.Dimension(100, 22));
        textP.setPreferredSize(new java.awt.Dimension(100, 22));
        textP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPActionPerformed(evt);
            }
        });
        textP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textPFocusLost(evt);
            }
        });
        textP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPKeyReleased(evt);
            }
        });

        jLabel20.setText("Pasos:");

        texttau.setText("100");
        texttau.setMaximumSize(new java.awt.Dimension(100, 22));
        texttau.setMinimumSize(new java.awt.Dimension(100, 22));
        texttau.setPreferredSize(new java.awt.Dimension(100, 22));
        texttau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texttauActionPerformed(evt);
            }
        });
        texttau.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                texttauFocusLost(evt);
            }
        });
        texttau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                texttauKeyReleased(evt);
            }
        });

        jLabel25.setText("Tau:");

        jLabel19.setText("Recompensas:");

        textN.setText("0");
        textN.setMinimumSize(new java.awt.Dimension(22, 40));
        textN.setPreferredSize(new java.awt.Dimension(22, 40));
        textN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textNFocusLost(evt);
            }
        });
        textN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textNKeyReleased(evt);
            }
        });

        jLabel14.setText(" Normal:");
        jLabel14.setMaximumSize(new java.awt.Dimension(45, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(45, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(45, 14));

        textB.setText("5");
        textB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBActionPerformed(evt);
            }
        });
        textB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textBFocusLost(evt);
            }
        });
        textB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textBKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textBKeyReleased(evt);
            }
        });

        jLabel15.setText("Bueno:");
        jLabel15.setMaximumSize(new java.awt.Dimension(40, 14));
        jLabel15.setMinimumSize(new java.awt.Dimension(40, 14));
        jLabel15.setPreferredSize(new java.awt.Dimension(40, 14));

        textM.setText("-25");
        textM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMActionPerformed(evt);
            }
        });
        textM.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textMFocusLost(evt);
            }
        });
        textM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textMKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textMKeyReleased(evt);
            }
        });

        jLabel16.setText("Malo:");

        textE.setText("15");
        textE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textEFocusLost(evt);
            }
        });
        textE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textEKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEKeyReleased(evt);
            }
        });

        jLabel17.setText("Excelente:");

        textF.setText("100");
        textF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFFocusLost(evt);
            }
        });
        textF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFKeyReleased(evt);
            }
        });

        jLabel18.setText("Final:");

        jLabel1.setText("Elija tamaño de la grilla:");

        menuTamano.setToolTipText("Elija tamaño");
        menuTamano.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                menuTamanoItemStateChanged(evt);
            }
        });
        menuTamano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTamanoActionPerformed(evt);
            }
        });

        botonReset.setText("Resetear Grilla");
        botonReset.setToolTipText("");
        botonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonResetActionPerformed(evt);
            }
        });

        BotonAleatorio.setText("Estados Aleatorios");
        BotonAleatorio.setToolTipText("");
        BotonAleatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAleatorioActionPerformed(evt);
            }
        });

        jLabel21.setText("Parametro de Politica Softmax:");

        jLabel26.setText("Factor de descuento:");

        jLabel27.setText("Parametros de Iteraciones:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(menuTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel10)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(BotonAleatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel1)
                                            .addComponent(botonReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel17)
                                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(8, 8, 8))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(24, 24, 24))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(textE)
                                            .addComponent(textM)
                                            .addComponent(textB)
                                            .addComponent(textN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(textF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel20)
                                                .addComponent(jLabel13)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(textMaxIt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, Short.MAX_VALUE)
                                                .addComponent(textP, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                            .addComponent(textGamma, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(39, 39, 39)
                                        .addComponent(texttau, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24)
                                        .addComponent(textEpsilon, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textEpsilon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(7, 7, 7)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(texttau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(textGamma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textMaxIt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textN, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textE, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textF, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonReset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonAleatorio)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelControles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 696, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelGrilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelGrilla, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelControles, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuTamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTamanoActionPerformed

    }//GEN-LAST:event_menuTamanoActionPerformed

    private void menuTamanoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_menuTamanoItemStateChanged
        //cuando se selecciona un item en el menu de tamano de gridworl se arma
        //la grilla, se setean los botones y se actualiza la matriz de celdas
        labelResultado1.setText("");
        labelResultado.setText("");
        panelGrilla.removeAll();
        tmno = Integer.parseInt(menuTamano.getSelectedItem().toString());
        panelGrilla.setLayout(new GridLayout());
        panelGrilla.add(grilla = new Grilla(tmno));
        setVisible(true);
        rbI.setEnabled(false);
        rbN.setSelected(true);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        matrizC = grilla.matrizCeldas;
        grilla.setearNormal();
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
    }//GEN-LAST:event_menuTamanoItemStateChanged

    private void botonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonResetActionPerformed
        //cuando se presiona el boton de reset, se crea una nueva grilla, se 
        //actualiza la matriz de celdas y se setean los botones
        //bot.stop();
        labelResultado1.setText("");
        labelResultado.setText("");
        panelGrilla.removeAll();
        tmno = Integer.parseInt(menuTamano.getSelectedItem().toString());
        panelGrilla.setLayout(new GridLayout());
        panelGrilla.add(grilla = new Grilla(tmno));
        setVisible(true);   
        matrizC = grilla.matrizCeldas;
        rbI.setEnabled(false);
        rbN.setSelected(true);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        grilla.setearNormal();
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
    }//GEN-LAST:event_botonResetActionPerformed

    private void BotonAleatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAleatorioActionPerformed
        //cuando se presiona el boton de estados aleatorios, devuelve una grilla 
        //pintada con estados aleatorios, setea botones y actualiza referencias
        labelResultado1.setText("");
        labelResultado.setText("");
        panelGrilla.removeAll();
        tmno = Integer.parseInt(menuTamano.getSelectedItem().toString());
        panelGrilla.setLayout(new GridLayout());
        panelGrilla.add(grilla = new Grilla(tmno));
        setVisible(true);   
        matrizC = grilla.matrizCeldas;
        rbI.setEnabled(false);
        rbN.setSelected(true);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
        grilla.estadosAleatorios();
        grilla.pintarCeldas();
        grilla.setearNormal();
    }//GEN-LAST:event_BotonAleatorioActionPerformed

    private void textPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPKeyReleased
        //al presionar enter tsetea el valor de la cantidad maxima de pasos
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                pasos =(Integer.parseInt(textP.getText()));
            }catch (Exception exp){
                textP.setText("500");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            } 
        }
    }//GEN-LAST:event_textPKeyReleased

    private void textPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPActionPerformed

    private void textFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFKeyReleased
        //al presionar enter setea el valor de recompensa final
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                recF =(Double.parseDouble(textF.getText()));
            }catch (Exception exp){
                textF.setText("100");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            } 
        }
    }//GEN-LAST:event_textFKeyReleased

    private void textFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFKeyPressed

    }//GEN-LAST:event_textFKeyPressed

    private void textEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEKeyReleased
        //al presionar enter setea el valor de recompensa excelente
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                recE =(Double.parseDouble(textE.getText()));
            }catch (Exception exp){
                textE.setText("25");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            } 
        }
    }//GEN-LAST:event_textEKeyReleased

    private void textEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEKeyPressed

    }//GEN-LAST:event_textEKeyPressed

    private void textBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBKeyReleased
        //al presionar enter setea el valor de recompensa buena
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                recB =(Double.parseDouble(textB.getText()));
            }catch (Exception exp){
                textB.setText("10");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            } 
        }
    }//GEN-LAST:event_textBKeyReleased

    private void textBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBKeyPressed

    }//GEN-LAST:event_textBKeyPressed

    private void textBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBActionPerformed

    }//GEN-LAST:event_textBActionPerformed

    private void textMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMKeyReleased
        //al presionar enter setea el valor de recompensa mala
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                recM =(Double.parseDouble(textM.getText()));
            }catch (Exception exp){
                textM.setText("-75");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            } 
        }
    }//GEN-LAST:event_textMKeyReleased

    private void textMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMKeyPressed

    }//GEN-LAST:event_textMKeyPressed

    private void textNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNKeyReleased
        //al presionar enter setea el valor de recompensa normal
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                recN =(Double.parseDouble(textN.getText()));
            }catch (Exception exp){
                textN.setText("0");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            }
        }
    }//GEN-LAST:event_textNKeyReleased

    private void textNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNKeyPressed

    }//GEN-LAST:event_textNKeyPressed

    private void textMaxItKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMaxItKeyReleased
        //al presionar enter setea el valor de maxima iteracion
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
            itmax =(Long.parseLong(textMaxIt.getText()));
        }catch (Exception exp){
            textMaxIt.setText("10000");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (itmax >65533){
            textMaxIt.setText("10000");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor menor a 65533, por el formato de salida", "ERROR, Archivo excel permite solo 65535 filas", JOptionPane.WARNING_MESSAGE);
            
        } 
        }
        
    }//GEN-LAST:event_textMaxItKeyReleased

    private void textMaxItActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMaxItActionPerformed

    }//GEN-LAST:event_textMaxItActionPerformed

    private void textGammaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textGammaKeyReleased
        //al presionar enter setea el valor de gamma
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
            gamma =(Double.parseDouble(textGamma.getText()));
        }catch (Exception exp){
            textGamma.setText("0.8");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (gamma >1 || gamma <0){
            textGamma.setText("0.8");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor entre 0 y 1", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        }
    }//GEN-LAST:event_textGammaKeyReleased

    private void textGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textGammaActionPerformed

    }//GEN-LAST:event_textGammaActionPerformed

    private void textEpsilonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEpsilonKeyReleased
        //al presionar enter setea el valor de epsilon
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
            e =(Double.parseDouble(textEpsilon.getText()));
        }catch (Exception exp){
            textEpsilon.setText("0.1");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (e >1 || e <0){
            textEpsilon.setText("0.1");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor entre 0 y 1", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        }
    }//GEN-LAST:event_textEpsilonKeyReleased

    private void textEpsilonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEpsilonKeyPressed

    }//GEN-LAST:event_textEpsilonKeyPressed

    private void texttauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texttauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_texttauActionPerformed

    private void texttauKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texttauKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                tau =(Double.parseDouble(texttau.getText()));
            }catch (Exception exp){
                texttau.setText("100");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            }
            if (tau <=0){
                texttau.setText("100");
                JOptionPane.showMessageDialog(grilla, "Introduzca un valor mayor a 0", "ERROR", JOptionPane.WARNING_MESSAGE);
                
            } 
        }
    }//GEN-LAST:event_texttauKeyReleased

    private void textMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMActionPerformed

    private void textMaxItFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textMaxItFocusLost
        // TODO add your handling code here:
        try {
            itmax =(Long.parseLong(textMaxIt.getText()));
        }catch (Exception exp){
            textMaxIt.setText("10000");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (itmax >65533){
            textMaxIt.setText("10000");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor menor a 65533, por el formato de salida", "ERROR, Archivo excel permite solo 65535 filas", JOptionPane.WARNING_MESSAGE);
            
        }
        if (itmax <0){
            textMaxIt.setText("10000");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor positivo", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        
    }//GEN-LAST:event_textMaxItFocusLost

    private void textEpsilonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textEpsilonFocusLost
        // TODO add your handling code here:
        try {
            e =(Double.parseDouble(textEpsilon.getText()));
        }catch (Exception exp){
            textEpsilon.setText("0.1");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (e >1 || e <0){
            textEpsilon.setText("0.1");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor entre 0 y 1", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
    }//GEN-LAST:event_textEpsilonFocusLost

    private void textGammaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textGammaFocusLost
        // TODO add your handling code here:
        try {
            gamma =(Double.parseDouble(textGamma.getText()));
        }catch (Exception exp){
            textGamma.setText("0.8");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (gamma >1 || gamma <0){
            textGamma.setText("0.8");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor entre 0 y 1", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
    }//GEN-LAST:event_textGammaFocusLost

    private void texttauFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_texttauFocusLost
        try {
            tau =(Double.parseDouble(texttau.getText()));
        }catch (Exception exp){
            texttau.setText("100");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }
        if (tau <=0){
            texttau.setText("100");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor mayor a 0", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }        
    }//GEN-LAST:event_texttauFocusLost

    private void textPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textPFocusLost
        try {
            pasos =(Integer.parseInt(textP.getText()));
        }catch (Exception exp){
            textP.setText("500");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        } 
        if (pasos <=0){
            textP.setText("500");
            JOptionPane.showMessageDialog(grilla, "Introduzca un valor mayor a 0", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        }  
    }//GEN-LAST:event_textPFocusLost

    private void textNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNFocusLost
        try {
            recN =(Double.parseDouble(textN.getText()));
        }catch (Exception exp){
            textN.setText("0");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        } 
    }//GEN-LAST:event_textNFocusLost

    private void textBFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textBFocusLost
        try {
            recB =(Double.parseDouble(textB.getText()));
        }catch (Exception exp){
            textB.setText("10");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        } 
    }//GEN-LAST:event_textBFocusLost

    private void textMFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textMFocusLost
        try {
            recM =(Double.parseDouble(textM.getText()));
        }catch (Exception exp){
            textM.setText("-75");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        } 
    }//GEN-LAST:event_textMFocusLost

    private void textEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textEFocusLost
        try {
            recE =(Double.parseDouble(textE.getText()));
        }catch (Exception exp){
            textE.setText("25");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        } 
    }//GEN-LAST:event_textEFocusLost

    private void textFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFFocusLost
        try {
            recF =(Double.parseDouble(textF.getText()));
        }catch (Exception exp){
            textF.setText("100");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            
        } 
    }//GEN-LAST:event_textFFocusLost

    private void textSaltoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textSaltoKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                salt =(Integer.parseInt(textSalto.getText()));
            }catch (Exception exp){

                textSalto.setText("50");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_textSaltoKeyReleased

    private void textSaltoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textSaltoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textSaltoKeyPressed

    private void textSaltoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textSaltoFocusLost
        try {
            salt =(Integer.parseInt(textSalto.getText()));
        }catch (Exception exp){
            textSalto.setText("50");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);

        }
    }//GEN-LAST:event_textSaltoFocusLost

    private void textSaltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textSaltoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textSaltoActionPerformed

    private void textRepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRepKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                rep =(Integer.parseInt(textRep.getText()));
            }catch (Exception exp){
                textRep.setText("500");
                JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);

            }
        }
    }//GEN-LAST:event_textRepKeyReleased

    private void textRepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textRepKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRepKeyPressed

    private void textRepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textRepFocusLost
        try {
            rep =(Integer.parseInt(textRep.getText()));
        }catch (Exception exp){
            textRep.setText("500");
            JOptionPane.showMessageDialog(grilla, "Debe introducir un valor númerico", "ERROR", JOptionPane.WARNING_MESSAGE);

        }
    }//GEN-LAST:event_textRepFocusLost

    private void textRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textRepActionPerformed

    private void botonArchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonArchActionPerformed
        if (conts>0){
            // una nuevo archivo de excel
            HSSFWorkbook libro = new HSSFWorkbook();
            //la hoja de excel
            HSSFSheet hoja = libro.createSheet("Corridas");
            //crea una fila para los titulos
            HSSFRow filatitulo = hoja.createRow(0);
            //la primera celda para el titulo
            HSSFCell celdatitulo = filatitulo.createCell(0);
            //estilo que tendran las celdas de titulo
            HSSFCellStyle estilotitulo = libro.createCellStyle();
            estilotitulo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            estilotitulo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            estilotitulo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            estilotitulo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
            estilotitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // nombre y estilo de la primer celda del titulo
            celdatitulo.setCellValue("Episodio");
            celdatitulo.setCellStyle(estilotitulo);
            // estilos para las celdas de datos
            HSSFCellStyle estilonormal = libro.createCellStyle();
            estilonormal.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estilonormal.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estilonormal.setBorderRight(HSSFCellStyle.BORDER_THIN);
            //y creo la fila completa de titulos, una celda por corrida
            for (int b=0; b<conts;b++){
                XYSeries seriei = new XYSeries ("prueba"+b);
                double [] serieap = listaLogs.get(b);
                HSSFCell celdatitulo2 = filatitulo.createCell(b+1);
                celdatitulo2.setCellValue("prueba "+b);
                celdatitulo2.setCellStyle(estilotitulo);
            }
            //busco el tamaño maximo de corridas
            int max=0;
            for (int b=0; b<conts;b++){
                double [] serieap = listaLogs.get(b);
                int longitud = serieap.length;
                if (longitud>max){
                    max=longitud;
                }
            }
            //se crean el resto de las celdas
            for (int a=0;a<max;a++){
                //creo una fila para cada iteracion
                HSSFRow fila = hoja.createRow(a+1);
                // la primera celda de cada fila es el numero de iteracion
                HSSFCell celda = fila.createCell(0);
                celda.setCellValue(a);
                celda.setCellStyle(estilonormal);

                for (int b=0; b<conts;b++){
                    double [] serieap = listaLogs.get(b);
                    //una celda para cada corrida con su correspondiente estilo
                    HSSFCell celda2 = fila.createCell(b+1);
                    if (a<serieap.length){ 
                        celda2.setCellValue(serieap[a]);
                    } else {
                        celda2.setCellValue("");
                    }
                    celda2.setCellStyle(estilonormal);
                }
            }
            // la hoja para la convergencia
            HSSFSheet hoja2 = libro.createSheet("Convergencia");
            //crea una fila para los titulos
            HSSFRow filatitulo2 = hoja2.createRow(0);
            HSSFCell celdatitulo21 = filatitulo2.createCell(0);
            HSSFCell celdatitulo22 = filatitulo2.createCell(1);
            celdatitulo21.setCellValue("Iteracion");
            celdatitulo22.setCellValue("Valor");
            celdatitulo21.setCellStyle(estilotitulo);
            celdatitulo22.setCellStyle(estilotitulo);

            for (int x=0; x<conts;x++){
                //crea una fila por cada corrida
                HSSFRow filax = hoja2.createRow(x+1);
                //creo las celdas y cargo los valores
                HSSFCell celdax1 = filax.createCell(0);
                HSSFCell celdax2 = filax.createCell(1);
                celdax1.setCellValue(listaConv.get(x) [0]);
                celdax2.setCellValue(listaConv.get(x) [1]);
                celdax1.setCellStyle(estilonormal);
                celdax2.setCellStyle(estilonormal);
            }
            //la hoja de los tiempos
            HSSFSheet hoja3 = libro.createSheet("Tiempos");
            //titulos
            HSSFRow filatitulo3 = hoja3.createRow(0);
            HSSFCell celdatitulo31 = filatitulo3.createCell(0);
            HSSFCell celdatitulo32 = filatitulo3.createCell(1);
            celdatitulo31.setCellValue("Prueba");
            celdatitulo32.setCellValue("Tiempo");
            celdatitulo31.setCellStyle(estilotitulo);
            celdatitulo32.setCellStyle(estilotitulo);
            //filas
            for (int x=0; x<conts;x++){
                //crea una fila por cada corrida
                HSSFRow filax = hoja3.createRow(x+1);
                //creo las celdas y cargo los valores
                HSSFCell celdax1 = filax.createCell(0);
                HSSFCell celdax2 = filax.createCell(1);
                celdax2.setCellValue(tiempos [x]);
                celdax1.setCellValue(conts);
                celdax1.setCellStyle(estilonormal);
                celdax2.setCellStyle(estilonormal);
            }
            
            //la hoja de los pasos
            HSSFSheet hoja4 = libro.createSheet("Pasos");
            //titulos
            HSSFRow filatitulo4 = hoja4.createRow(0);
            HSSFCell celdatitulo41 = filatitulo4.createCell(0);
            celdatitulo41.setCellValue("Iteración");
            celdatitulo41.setCellStyle(estilotitulo);
            //y creo la fila completa de titulos, una celda por corrida
            for (int b=0; b<conts;b++){
                double [] seriep = listaPasos.get(b);
                HSSFCell celdatituloxp = filatitulo4.createCell(b+1);
                celdatituloxp.setCellValue("prueba "+b);
                celdatituloxp.setCellStyle(estilotitulo);
            }
            //se crean el resto de las celdas
            for (int a=0;a<max;a++){
                //creo una fila para cada iteracion
                HSSFRow fila4 = hoja4.createRow(a+1);
                // la primera celda de cada fila es el numero de iteracion
                HSSFCell celda4 = fila4.createCell(0);
                celda4.setCellValue(a);
                celda4.setCellStyle(estilonormal);

                for (int b=0; b<conts;b++){
                    double [] seriep = listaPasos.get(b);
                    //una celda para cada corrida con su correspondiente estilo
                    HSSFCell celda42 = fila4.createCell(b+1);
                    if (a<seriep.length){ 
                        celda42.setCellValue(seriep[a]);
                    } else {
                        celda42.setCellValue("");
                    }
                    celda42.setCellStyle(estilonormal);
                }
            }
            // creo el archivo excel
            try {
                FileOutputStream excel = new FileOutputStream("Prueba"+contP+".xls");
                libro.write(excel);
                excel.close();
            } catch (Exception err) {
            }
            //aumenta el contador de prueba
            contP=contP+1;
            //vuelve inializar nuevamente el contador decorrida de cada prueba
            conts=0;
            //arrancan de nuevo las listas
            listaLogs.removeAll(listaLogs);
            listaConv.removeAll(listaConv);
        } else {
            JOptionPane.showMessageDialog(grilla, "No se realizo ninguna fase de aprendizaje", "ERROR", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_botonArchActionPerformed

    private void botonGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGActionPerformed
        if (conts>0){
            // conjunto de series para el grafico
            XYSeriesCollection conjdato = new XYSeriesCollection();

            //creo el conjunto de datos para el grafico
            for (int b=0; b<conts;b++){
                //tomo una serie del log
                double [] serieap = listaLogs.get(b);
                //creo una serie de puntos del grafico
                XYSeries seriei = new XYSeries ("prueba"+b);
                for (int a=0;a<serieap.length;a++){
                    //cargo un punto a la serie
                    seriei.add(serieap[a],a);
                }
                // añado la serie al conjunto de datos graficos
                conjdato.addSeries(seriei);
            }

            //creo el frame del grafico
            JFrame pantallaGra = new JFrame();
            pantallaGra.setSize(800, 600);
            //creo el grafico y lo añado
            grafico = ChartFactory.createXYLineChart("Valor Acumulado", "Valor Acumulado", "iteración", conjdato, PlotOrientation.HORIZONTAL, true, true, true);
            ChartPanel chartPanel = new ChartPanel(grafico);

            pantallaGra.add(chartPanel);

            pantallaGra.setLocationRelativeTo(null);
            pantallaGra.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(grilla, "No realizo ninguna fase de aprendizaje", "ERROR", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_botonGActionPerformed

    private void radioButtonSdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonSdActionPerformed
        sofde=radioButtonSd.isSelected();
    }//GEN-LAST:event_radioButtonSdActionPerformed

    private void radioButtonEdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonEdActionPerformed
        ede=radioButtonEd.isSelected();
    }//GEN-LAST:event_radioButtonEdActionPerformed

    private void radioButtonSoftmaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonSoftmaxActionPerformed
        radioButtonEgreedy.setSelected(false);
        vaEgreedy=false;
        vaSoftmax=true;
        radioButtonEd.setSelected(false);
    }//GEN-LAST:event_radioButtonSoftmaxActionPerformed

    private void radioButtonEgreedyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonEgreedyActionPerformed
        radioButtonSoftmax.setSelected(false);
        vaEgreedy=true;
        vaSoftmax=false;
        radioButtonSd.setSelected(false);
    }//GEN-LAST:event_radioButtonEgreedyActionPerformed

    private void BotonInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonInicialActionPerformed
        //al presionar el boton, limpia los caminos pintados y los antiguos
        //iniciales, ademas de setear el tipo de seleccion como inicial
        labelResultado1.setText("");
        labelResultado.setText("");
        grilla.limpiar();
        grilla.setearInicio();
    }//GEN-LAST:event_BotonInicialActionPerformed

    private void BotonCamino1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCamino1ActionPerformed

        //al presionar el boton pinta el camino aprendido desde el inicio
        grilla.limpiarCaminos();
        grilla.limpiar();
        pintarCamino();
    }//GEN-LAST:event_BotonCamino1ActionPerformed

    private void BotonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonStartActionPerformed
        labelResultado1.setText("");
        labelResultado.setText("");
        buttonStop.setEnabled(true);
        buttonResume.setEnabled(true);
        //al presionar el boton de start, se actualizan las referencias
        grilla.limpiar();
        grilla.actualizarGrilla();
        grilla.pintarCeldas();
        grilla.actualizarAcciones();

        e=(Double.parseDouble(textEpsilon.getText()));
        gamma =(Double.parseDouble(textGamma.getText()));
        tau = (Double.parseDouble(texttau.getText()));
        itmax =(Long.parseLong(textMaxIt.getText()));
        recN =(Double.parseDouble(textN.getText()));
        recM =(Double.parseDouble(textM.getText()));
        recB =(Double.parseDouble(textB.getText()));
        recE =(Double.parseDouble(textE.getText()));
        recF =(Double.parseDouble(textF.getText()));
        pasos =(Double.parseDouble(textP.getText()));
        //grilla = new Grilla(tmno);
        
        rep = Integer.parseInt(textRep.getText());
        salt = Integer.parseInt(textSalto.getText());
        //se crea una instancia de Qlearning con las referencias
        bar1.setMaximum((int) itmax);
        bar1.setStringPainted(true);
        bar1.setValue(0);
        bot = new QLearning(delay,bar1,optimista,salt,rep,tau,grilla.tmno,itmax,e,gamma,recB,recE,recN,recF,recM,grilla,pasos,vaSoftmax,vaEgreedy,ede,sofde);
        
        //se crea un hilo para correr el aprendizaje
        if (grilla.hayFinal()){
            JOptionPane.showMessageDialog(panelGrilla, "Aguarde a que finalize el ciclo de aprendizaje", "Puede tardar unos minutos", JOptionPane.WARNING_MESSAGE);
            long tinicio,tfin;
            tinicio = System.currentTimeMillis();
            if(concur){
                bot.play();
            }else{
               aprendizaje = new Thread(bot);
               //ExecutorService threadExecutor = Executors.newFixedThreadPool(1);
               aprendizaje.run();
            }
            tfin = System.currentTimeMillis();
            tiempos [conts] = (tfin-tinicio);
            
        } else {
            JOptionPane.showMessageDialog(grilla, "Introduzca una celda Final para comenzar", "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        //por ultimo se actualizan los botones y se espera un inicio
        rbI.setEnabled(true);
        rbI.setSelected(true);
        rbN.setSelected(false);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        BotonInicial.setEnabled(true);
        BotonCamino1.setEnabled(true);
        grilla.setearInicio();
        matrizC=grilla.matrizCeldas;
        // se carga el log de la corrida
        double [] log = bot.getListaSerie();
        listaLogs.add(log);
        // se carga la lista de pasos
        double [] pas = bot.getListaPasos();
        listaPasos.add(pas);
        //creo para cada corrida un array
        double [] con = new double[2];
        //la primer posicion guarda la iteracion donde converge
        con [0] = bot.getIterConv();
        //la segunda posicion guarda el valor
        con [1]= bot.getConv();
        listaConv.add(con);
        //actualizo contador
        conts=conts+1;

    }//GEN-LAST:event_BotonStartActionPerformed

    private void rbIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIActionPerformed
        //al presionar el radiobutton, destilda la opcion normal y setea
        //el tipo de seleccion de celdas como de inicio
        rbN.setSelected(false);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        grilla.setearInicio();
    }//GEN-LAST:event_rbIActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        optimista=!optimista;
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void buttonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopActionPerformed
        try {
        bot.pause();
        }catch (Exception err) {
            JOptionPane.showMessageDialog(panelGrilla, "No hay etapa de aprendrizaje en proceso", "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_buttonStopActionPerformed

    private void buttonResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResumeActionPerformed
         try {
        bot.resume();
        }catch (Exception err) {
            JOptionPane.showMessageDialog(panelGrilla, "El aprendizaje debe estar parado para reanudarlo", "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_buttonResumeActionPerformed

    private void buttonResume1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResume1ActionPerformed
        // 
        pantQ=new PantallaQ(grilla,bot);
        
        pantQ.setVisible(true);
    }//GEN-LAST:event_buttonResume1ActionPerformed

    private void rbNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNActionPerformed
        rbM.setSelected(false);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbP.setSelected(false);
        rbI.setSelected(false);
        grilla.setearNormal();
    }//GEN-LAST:event_rbNActionPerformed

    private void rbMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMActionPerformed
        rbN.setSelected(false);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbP.setSelected(false);
        rbI.setSelected(false);
        grilla.setearMala();
    }//GEN-LAST:event_rbMActionPerformed

    private void rbBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBActionPerformed
        rbN.setSelected(false);
        rbM.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbP.setSelected(false);
        rbI.setSelected(false);
        grilla.setearBuena();
    }//GEN-LAST:event_rbBActionPerformed

    private void rbPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPActionPerformed
        rbN.setSelected(false);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbF.setSelected(false);
        rbM.setSelected(false);
        rbI.setSelected(false);
        grilla.setearPozo();
    }//GEN-LAST:event_rbPActionPerformed

    private void rbEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEActionPerformed
        rbN.setSelected(false);
        rbB.setSelected(false);
        rbM.setSelected(false);
        rbF.setSelected(false);
        rbP.setSelected(false);
        rbI.setSelected(false);
        grilla.setearExcelente();
    }//GEN-LAST:event_rbEActionPerformed

    private void rbFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFActionPerformed
        rbN.setSelected(false);
        rbB.setSelected(false);
        rbE.setSelected(false);
        rbM.setSelected(false);
        rbP.setSelected(false);
        rbI.setSelected(false);
        grilla.setearFinal();
    }//GEN-LAST:event_rbFActionPerformed

    private void radioButonEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButonEstActionPerformed
        if (radioButonEst.isSelected()){
            //muestro los controles
            botonG.setEnabled(true);
            botonArch.setEnabled(true);
            textRep.setEnabled(true);
            textSalto.setEnabled(true);
            labelL.setEnabled(true);
            labelS.setEnabled(true);
            //activo el modo no concurrente
            concur=false;
        }else{
            //oculto los controles
            botonG.setEnabled(false);
            botonArch.setEnabled(false);
            textRep.setEnabled(false);
            textSalto.setEnabled(false);
            labelL.setEnabled(false);
            labelS.setEnabled(false);
            //activo modo concurrente
            concur=true;
        }
    }//GEN-LAST:event_radioButonEstActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        if(jRadioButton3.isSelected()){
            //añado delay al aprendizaje
            delay=true;
        }else{
            //saco delay
            delay=false;
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void textEpsilonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textEpsilonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textEpsilonActionPerformed

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
            java.util.logging.Logger.getLogger(PantallaPpal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PantallaPpal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PantallaPpal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PantallaPpal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaPpal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAleatorio;
    private javax.swing.JButton BotonCamino1;
    private javax.swing.JButton BotonInicial;
    private javax.swing.JButton BotonStart;
    private javax.swing.JProgressBar bar1;
    private javax.swing.JButton botonArch;
    private javax.swing.JButton botonG;
    private javax.swing.JButton botonReset;
    private javax.swing.JButton buttonResume;
    private javax.swing.JButton buttonResume1;
    private javax.swing.JButton buttonStop;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel labelL;
    private javax.swing.JLabel labelResultado;
    private javax.swing.JLabel labelResultado1;
    private javax.swing.JLabel labelS;
    private javax.swing.JComboBox menuTamano;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelGrilla;
    private javax.swing.JRadioButton radioButonEst;
    private javax.swing.JRadioButton radioButtonEd;
    private javax.swing.JRadioButton radioButtonEgreedy;
    private javax.swing.JRadioButton radioButtonSd;
    private javax.swing.JRadioButton radioButtonSoftmax;
    private javax.swing.JRadioButton rbB;
    private javax.swing.JRadioButton rbE;
    private javax.swing.JRadioButton rbF;
    private javax.swing.JRadioButton rbI;
    private javax.swing.JRadioButton rbM;
    private javax.swing.JRadioButton rbN;
    private javax.swing.JRadioButton rbP;
    private javax.swing.JTextField textB;
    private javax.swing.JTextField textE;
    private javax.swing.JTextField textEpsilon;
    private javax.swing.JTextField textF;
    private javax.swing.JTextField textGamma;
    private javax.swing.JTextField textM;
    private javax.swing.JTextField textMaxIt;
    private javax.swing.JTextField textN;
    private javax.swing.JTextField textP;
    private javax.swing.JTextField textRep;
    private javax.swing.JTextField textSalto;
    private javax.swing.JTextField texttau;
    // End of variables declaration//GEN-END:variables
}
