package TPI;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.lang.InterruptedException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
        Grilla grilla = new Grilla();
        QLearning bot;
        Thread aprendizaje;
        Celda [][] matrizC;
        
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
        
        XYSeries serie = null;
        XYSeriesCollection lista;
        int conts;
        
        XYDataset datos=null;
        JFreeChart grafico;
        //ArrayList listdat;
        ArrayList<double[]> prueba;

        //constructor
    public PantallaPpal() {
        initComponents();

        //conjdatoap.addSeries(serieAp);
        //conjdatosap=conjdatoap;
        //listdat = new ArrayList();
        conts=0;
        
        //serie= new XYSeries("Prueba");
        
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
        radioButtonInicio.setEnabled(false);
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
        radioButtonInicio.setEnabled(false);
        radioButtonNormal.setEnabled(true);
        //botonReset.doClick();
        prueba = new ArrayList<double[]>();
        //conjdato = new XYSeriesCollection();
        

    }
    
    //funcion que pinta el camino aprendido
    public void pintarCamino() {
        //booleano que si es celda final se utiliza luego
        Boolean noesFinal=true;
        //posicion inicial asignada
        Posicion pos = grilla.getInicial();
        Posicion sig;
        int i; int j; int accion;
        //borde de la posicion inicial
        Border border = new MatteBorder(3,3,3,3,Color.BLUE) {};
        i= pos.getI();j =pos.getJ();
        do{
            //actualizo variable noescamino
            matrizC[i][j].noEsCamino = false;
            //me fijo la accion siguiente y la guardo
            accion = bot.mejorAccion(pos);
            matrizC[i][j].caminoSig=accion;
            //pinto la inicial
            if(matrizC[i][j].esInicial){
                matrizC[i][j].setBorder(border);
            }
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
        //mensaje de error si no puedo aprender llegar al final
        if(!(matrizC[i][j].noEsCamino)){
            JOptionPane.showMessageDialog(panelGrilla, "El camino aprendido, desde el punto indicado, no llega al final. Revise la cantidad de episodios.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
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

        panelControles = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        textEpsilon = new javax.swing.JTextField();
        textGamma = new javax.swing.JTextField();
        textMaxIt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        textN = new javax.swing.JTextField();
        textM = new javax.swing.JTextField();
        textB = new javax.swing.JTextField();
        textE = new javax.swing.JTextField();
        textF = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        textP = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        texttau = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        radioButtonInicio = new javax.swing.JRadioButton();
        radioButtonNormal = new javax.swing.JRadioButton();
        jLabel21 = new javax.swing.JLabel();
        BotonStart = new javax.swing.JButton();
        BotonCamino1 = new javax.swing.JButton();
        BotonInicial = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        menuTamano = new javax.swing.JComboBox();
        botonReset = new javax.swing.JButton();
        BotonAleatorio = new javax.swing.JButton();
        radioButtonEgreedy = new javax.swing.JRadioButton();
        radioButtonSoftmax = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        radioButtonEd = new javax.swing.JRadioButton();
        radioButtonSd = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        panelGrilla = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(709, 700));
        setResizable(false);

        panelControles.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Referencias:");

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Estado Normal");
        jLabel3.setMaximumSize(new java.awt.Dimension(255, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel3.setOpaque(true);

        jLabel4.setBackground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Estado Malo");
        jLabel4.setOpaque(true);

        jLabel5.setBackground(new java.awt.Color(255, 255, 0));
        jLabel5.setText("Estado Bueno");
        jLabel5.setOpaque(true);

        jLabel6.setBackground(new java.awt.Color(0, 255, 0));
        jLabel6.setText("Estado Excelente");
        jLabel6.setOpaque(true);

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Estado Pozo");
        jLabel7.setOpaque(true);

        jLabel8.setBackground(new java.awt.Color(0, 0, 255));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Estado Final");
        jLabel8.setOpaque(true);

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Estado Inicial");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255), 3));

        jLabel10.setText("Parametros:");

        textEpsilon.setText("0.1");
        textEpsilon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textEpsilonKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEpsilonKeyReleased(evt);
            }
        });

        textGamma.setText("0.8");
        textGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textGammaActionPerformed(evt);
            }
        });
        textGamma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textGammaKeyReleased(evt);
            }
        });

        textMaxIt.setText("10000");
        textMaxIt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMaxItActionPerformed(evt);
            }
        });
        textMaxIt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textMaxItKeyReleased(evt);
            }
        });

        jLabel11.setText("Epsilon");

        jLabel12.setText("Gamma");
        jLabel12.setMaximumSize(new java.awt.Dimension(45, 14));
        jLabel12.setMinimumSize(new java.awt.Dimension(45, 14));
        jLabel12.setPreferredSize(new java.awt.Dimension(45, 14));

        jLabel13.setText("Episodios");

        textN.setText("0");
        textN.setMinimumSize(new java.awt.Dimension(22, 40));
        textN.setPreferredSize(new java.awt.Dimension(22, 40));
        textN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textNKeyReleased(evt);
            }
        });

        textM.setText("-75");
        textM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMActionPerformed(evt);
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

        textB.setText("10");
        textB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBActionPerformed(evt);
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

        textE.setText("25");
        textE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textEKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEKeyReleased(evt);
            }
        });

        textF.setText("100");
        textF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFKeyReleased(evt);
            }
        });

        jLabel14.setText("Normal");
        jLabel14.setMaximumSize(new java.awt.Dimension(45, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(45, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(45, 14));

        jLabel15.setText("Bueno");
        jLabel15.setMaximumSize(new java.awt.Dimension(40, 14));
        jLabel15.setMinimumSize(new java.awt.Dimension(40, 14));
        jLabel15.setPreferredSize(new java.awt.Dimension(40, 14));

        jLabel16.setText("Malo");

        jLabel17.setText("Excelente");

        jLabel18.setText("Final");

        jLabel19.setText("Recompensas:");

        textP.setText("500");
        textP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPActionPerformed(evt);
            }
        });
        textP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPKeyReleased(evt);
            }
        });

        jLabel20.setText("Iteracciones");

        texttau.setText("100");
        texttau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texttauActionPerformed(evt);
            }
        });
        texttau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                texttauKeyReleased(evt);
            }
        });

        jLabel25.setText("Tau");

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textEpsilon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textGamma, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textMaxIt)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(texttau, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19))
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(textN, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(textE, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textF, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textE, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textM, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textN, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textEpsilon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textGamma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textMaxIt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel10)
                    .addComponent(texttau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel2))
                .addGap(15, 15, 15))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        radioButtonInicio.setText("Seleccionar celda inicial");
        radioButtonInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonInicioActionPerformed(evt);
            }
        });

        radioButtonNormal.setSelected(true);
        radioButtonNormal.setText("Seleccionar tipo de celdas");
        radioButtonNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonNormalActionPerformed(evt);
            }
        });

        jLabel21.setText("Tipo de Seleccion:");

        BotonStart.setText("Aprender");
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

        botonReset.setText("Resetear");
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

        jButton1.setText("Graficar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel26.setText("Graficas:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BotonStart)
                .addGap(38, 38, 38))
            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(radioButtonNormal)
                    .addComponent(radioButtonInicio)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(menuTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(botonReset))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(BotonAleatorio))
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BotonInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonCamino1)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radioButtonEgreedy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioButtonEd))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radioButtonSoftmax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioButtonSd))
                    .addComponent(jLabel26))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonReset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonAleatorio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonEgreedy)
                    .addComponent(radioButtonEd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonSoftmax)
                    .addComponent(radioButtonSd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioButtonNormal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonCamino1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonInicial)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(58, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(429, Short.MAX_VALUE)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(110, 110, 110)))
        );

        panelGrilla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelGrilla.setMaximumSize(new java.awt.Dimension(505, 515));
        panelGrilla.setMinimumSize(new java.awt.Dimension(505, 515));
        panelGrilla.setPreferredSize(new java.awt.Dimension(505, 515));

        javax.swing.GroupLayout panelGrillaLayout = new javax.swing.GroupLayout(panelGrilla);
        panelGrilla.setLayout(panelGrillaLayout);
        panelGrillaLayout.setHorizontalGroup(
            panelGrillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );
        panelGrillaLayout.setVerticalGroup(
            panelGrillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelControles, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelGrilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelControles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGrilla, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuTamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTamanoActionPerformed

    }//GEN-LAST:event_menuTamanoActionPerformed

    private void menuTamanoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_menuTamanoItemStateChanged
        //cuando se selecciona un item en el menu de tamano de gridworl se arma
        //la grilla, se setean los botones y se actualiza la matriz de celdas
        panelGrilla.removeAll();
        tmno = Integer.parseInt(menuTamano.getSelectedItem().toString());
        panelGrilla.setLayout(new GridLayout());
        panelGrilla.add(grilla = new Grilla(tmno));
        setVisible(true);
        radioButtonInicio.setEnabled(false);
        radioButtonNormal.setEnabled(true);
        matrizC = grilla.matrizCeldas;
        grilla.setearNormal();
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
    }//GEN-LAST:event_menuTamanoItemStateChanged

    private void botonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonResetActionPerformed
        //cuando se presiona el boton de reset, se crea una nueva grilla, se 
        //actualiza la matriz de celdas y se setean los botones
        for (int in=0; in<tmno; in++){
            for (int jn=0; jn<tmno; jn++){
                int res = grilla.grilla[in][jn];
                System.out.println(res);
            }
        } 
        
        panelGrilla.removeAll();
        tmno = Integer.parseInt(menuTamano.getSelectedItem().toString());
        panelGrilla.setLayout(new GridLayout());
        panelGrilla.add(grilla = new Grilla(tmno));
        setVisible(true);   
        matrizC = grilla.matrizCeldas;
        radioButtonInicio.setEnabled(false);
        radioButtonNormal.setEnabled(true);
        grilla.setearNormal();
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
    }//GEN-LAST:event_botonResetActionPerformed

    private void BotonAleatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAleatorioActionPerformed
        //cuando se presiona el boton de estados aleatorios, devuelve una grilla 
        //pintada con estados aleatorios, setea botones y actualiza referencias
        panelGrilla.removeAll();
        tmno = Integer.parseInt(menuTamano.getSelectedItem().toString());
        panelGrilla.setLayout(new GridLayout());
        panelGrilla.add(grilla = new Grilla(tmno));
        setVisible(true);   
        matrizC = grilla.matrizCeldas;
        radioButtonInicio.setEnabled(false);
        radioButtonNormal.setEnabled(true);
        BotonInicial.setEnabled(false);
        BotonCamino1.setEnabled(false);
        grilla.estadosAleatorios();
        grilla.pintarCeldas();
        grilla.setearNormal();
    }//GEN-LAST:event_BotonAleatorioActionPerformed

    private void BotonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonStartActionPerformed
          //serie= null;
        //datos=null;
        conts=conts+1;
        //al presionar el boton de start, se actualizan las referencias
            grilla.limpiar();
            JOptionPane.showMessageDialog(panelGrilla, "Aguarde a que finalize el ciclo de aprendizaje", "Puede tardar unos minutos", JOptionPane.WARNING_MESSAGE);
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
            grilla.setMatrizCeldas(matrizC);
            grilla.actualizarGrilla();
            grilla.actualizarAcciones();
          //se crea una instancia de Qlearning con las referencias
            bot = new QLearning(tau,grilla.tmno,itmax,e,gamma,recB,recE,recN,recF,recM,grilla,pasos,vaSoftmax,vaEgreedy,ede,sofde);
          //se crea un hilo para correr el aprendizaje
            aprendizaje = new Thread(bot);
            aprendizaje.start();
          //por ultimo se actualizan los botones y se espera un inicio
            radioButtonNormal.setEnabled(true);
            radioButtonInicio.setEnabled(true);
            radioButtonInicio.setSelected(true);
            radioButtonNormal.setSelected(false);
            BotonInicial.setEnabled(true);
            BotonCamino1.setEnabled(true);
            grilla.setearInicio();

    }//GEN-LAST:event_BotonStartActionPerformed

    private void BotonCamino1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCamino1ActionPerformed

                //al presionar el boton pinta el camino aprendido desde el inicio
                grilla.limpiarCaminos();
                grilla.limpiar();
                pintarCamino();

    }//GEN-LAST:event_BotonCamino1ActionPerformed

    private void radioButtonNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonNormalActionPerformed
        //al presionar el radiobutton, destilda la opcion inicio y setea
        //el tipo de seleccion de celdas como normal
        radioButtonInicio.setSelected(false);
        grilla.setearNormal();
    }//GEN-LAST:event_radioButtonNormalActionPerformed

    private void radioButtonInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonInicioActionPerformed
        //al presionar el radiobutton, destilda la opcion normal y setea
        //el tipo de seleccion de celdas como de inicio
        radioButtonNormal.setSelected(false);
        grilla.setearInicio();
    }//GEN-LAST:event_radioButtonInicioActionPerformed

    private void BotonInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonInicialActionPerformed
        //al presionar el boton, limpia los caminos pintados y los antiguos 
        //iniciales, ademas de setear el tipo de seleccion como inicial
        grilla.limpiar();
        grilla.setearInicio();
    }//GEN-LAST:event_BotonInicialActionPerformed

    private void radioButtonEgreedyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonEgreedyActionPerformed
        radioButtonSoftmax.setSelected(false);
        vaEgreedy=true;
        vaSoftmax=false;
        radioButtonSd.setSelected(false);
    }//GEN-LAST:event_radioButtonEgreedyActionPerformed

    private void radioButtonSoftmaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonSoftmaxActionPerformed
        radioButtonEgreedy.setSelected(false);
        vaEgreedy=false;
        vaSoftmax=true;
        radioButtonEd.setSelected(false);
    }//GEN-LAST:event_radioButtonSoftmaxActionPerformed

    private void radioButtonEdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonEdActionPerformed
        ede=radioButtonEd.isSelected();
    }//GEN-LAST:event_radioButtonEdActionPerformed

    private void textPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPKeyReleased
        //al presionar enter tsetea el valor de la cantidad maxima de pasos
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            pasos =(Double.parseDouble(textP.getText()));
        }
    }//GEN-LAST:event_textPKeyReleased

    private void textPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPActionPerformed

    private void textFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFKeyReleased
        //al presionar enter setea el valor de recompensa final
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            recF =(Double.parseDouble(textF.getText()));
        }
    }//GEN-LAST:event_textFKeyReleased

    private void textFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFKeyPressed

    }//GEN-LAST:event_textFKeyPressed

    private void textEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEKeyReleased
        //al presionar enter setea el valor de recompensa excelente
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            recE =(Double.parseDouble(textE.getText()));
        }
    }//GEN-LAST:event_textEKeyReleased

    private void textEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEKeyPressed

    }//GEN-LAST:event_textEKeyPressed

    private void textBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBKeyReleased
        //al presionar enter setea el valor de recompensa buena
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            recB =(Double.parseDouble(textB.getText()));
        }
    }//GEN-LAST:event_textBKeyReleased

    private void textBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBKeyPressed

    }//GEN-LAST:event_textBKeyPressed

    private void textBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBActionPerformed

    }//GEN-LAST:event_textBActionPerformed

    private void textMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMKeyReleased
        //al presionar enter setea el valor de recompensa mala
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            recM =(Double.parseDouble(textM.getText()));
        }
    }//GEN-LAST:event_textMKeyReleased

    private void textMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMKeyPressed

    }//GEN-LAST:event_textMKeyPressed

    private void textNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNKeyReleased
        //al presionar enter setea el valor de recompensa normal
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            recN =(Double.parseDouble(textN.getText()));
        }
    }//GEN-LAST:event_textNKeyReleased

    private void textNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNKeyPressed

    }//GEN-LAST:event_textNKeyPressed

    private void textMaxItKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textMaxItKeyReleased
        //al presionar enter setea el valor de maxima iteracion
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            itmax =(Long.parseLong(textMaxIt.getText()));
        }
    }//GEN-LAST:event_textMaxItKeyReleased

    private void textMaxItActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMaxItActionPerformed

    }//GEN-LAST:event_textMaxItActionPerformed

    private void textGammaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textGammaKeyReleased
        //al presionar enter setea el valor de gamma
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            gamma =(Double.parseDouble(textGamma.getText()));
        }
    }//GEN-LAST:event_textGammaKeyReleased

    private void textGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textGammaActionPerformed

    }//GEN-LAST:event_textGammaActionPerformed

    private void textEpsilonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEpsilonKeyReleased
        //al presionar enter setea el valor de epsilon
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            e=(Double.parseDouble(textEpsilon.getText()));
        }
    }//GEN-LAST:event_textEpsilonKeyReleased

    private void textEpsilonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEpsilonKeyPressed

    }//GEN-LAST:event_textEpsilonKeyPressed

    private void texttauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texttauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_texttauActionPerformed

    private void texttauKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texttauKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_texttauKeyReleased

    private void radioButtonSdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonSdActionPerformed
        sofde=radioButtonSd.isSelected();
    }//GEN-LAST:event_radioButtonSdActionPerformed

    private void textMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        XYSeriesCollection conjdato = new XYSeriesCollection();
        double [] asd = bot.getListaSerie();
        prueba.add(asd);
        // una nuevo archivo de excel
        HSSFWorkbook libro = new HSSFWorkbook();
        //la hoja de excel
        HSSFSheet hoja = libro.createSheet("Estadisticas IA");
        //
        for (int b=0; b<conts;b++){
            XYSeries seriei = new XYSeries ("prueba"+b);
            double [] asdasd = prueba.get(b);
            //XYSeries seriei = new XYSeries ("prueba");
            
            for (int a=0;a<asdasd.length;a++){
              
              // nueva tupla
              HSSFRow fila = hoja.createRow(a);
              // celda de la tupla
              HSSFCell celda = fila.createCell(0);
              //cargo valor
              celda.setCellValue(a);
              //segunda celda
              HSSFCell celda2 = fila.createCell(b+1);
              //cargo valor
              celda2.setCellValue(asdasd[a]);
              //añado para el grafico el punto
              seriei.add(asdasd[a],a);
          }
            conjdato.addSeries(seriei);
        }
        for (int a=0;a<asd.length;a++){
            HSSFRow fila = hoja.createRow(a);
            HSSFCell celda = fila.createCell(0);
            celda.setCellValue(a);
            for (int b=0; b<conts;b++){
                double [] asdasd = prueba.get(b);
                HSSFCell celda2 = fila.createCell(b+1);
                celda2.setCellValue(asdasd[a]);
            }
        }
        //prueba.add(conts,seriei);

           //for (int c=0;c<(conts+1);c++){
           //conjdato.addSeries((XYSeries) prueba.get(conts));
           //}
           //serie=bot.serieAp;
           //conjdato=bot.conjdatosap;
           

        JFrame pantallaGra = new JFrame();
        pantallaGra.setSize(800, 600);

        grafico = ChartFactory.createXYLineChart("esto es una prueba", "totalR", "iter", conjdato, PlotOrientation.HORIZONTAL, true, true, true);
        ChartPanel chartPanel = new ChartPanel(grafico);
        
        pantallaGra.add(chartPanel);
        
        pantallaGra.setLocationRelativeTo(null);
        pantallaGra.setVisible(true);
        
        
        
        
        
        try {
            FileOutputStream elFichero = new FileOutputStream("holamundo.xls");
            libro.write(elFichero);
            elFichero.close();
        } catch (Exception err) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton botonReset;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JComboBox menuTamano;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelGrilla;
    private javax.swing.JRadioButton radioButtonEd;
    private javax.swing.JRadioButton radioButtonEgreedy;
    private javax.swing.JRadioButton radioButtonInicio;
    private javax.swing.JRadioButton radioButtonNormal;
    private javax.swing.JRadioButton radioButtonSd;
    private javax.swing.JRadioButton radioButtonSoftmax;
    private javax.swing.JTextField textB;
    private javax.swing.JTextField textE;
    private javax.swing.JTextField textEpsilon;
    private javax.swing.JTextField textF;
    private javax.swing.JTextField textGamma;
    private javax.swing.JTextField textM;
    private javax.swing.JTextField textMaxIt;
    private javax.swing.JTextField textN;
    private javax.swing.JTextField textP;
    private javax.swing.JTextField texttau;
    // End of variables declaration//GEN-END:variables
}
