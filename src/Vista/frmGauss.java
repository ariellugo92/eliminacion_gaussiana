package Vista;

import Vista.Paneles.pnlResultado1;
import Vista.Paneles.pnlResultado2;
import Vista.Paneles.pnlResultado3;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author ariellugo92
 */
public class frmGauss extends javax.swing.JFrame {

    private int cant_incognitas = 2;
    private int pos_x = 1;
    private int pos_y = 1;
    private float[][] arr_matriz;
    private boolean flag_llenado = false;
    private boolean flag_spiner_celda = false;
    public float [][] matriz_result_transform;

    public frmGauss() {
        initComponents();
        this.setExtendedState(frmGauss.MAXIMIZED_BOTH);
    }
    
    public void setMatrizResultTransform(float [][] arr_matriz){
        this.matriz_result_transform = this.arr_matriz;
    }

    // metodo para rellenar la matriz
    private void rrellenandoMatriz() {
        String texto = "Esperando la cantidad de incognitas...";
        if (this.cant_incognitas > 0) {
            if (this.pos_x == 1 && this.pos_y == 1 && !this.flag_llenado) {
                // esto es cuando se inicie el programa
                this.arr_matriz = new float[(this.cant_incognitas)][(this.cant_incognitas + 1)];
                for (int i = 0; i < this.cant_incognitas; i++) {
                    for (int j = 0; j < (this.cant_incognitas + 1); j++) {
                        this.arr_matriz[i][j] = 0;
                    }
                }
            }

            texto = "<html>";
            for (int i = 0; i < this.cant_incognitas; i++) {
                for (int j = 0; j < (this.cant_incognitas + 1); j++) {
                    float valor = this.arr_matriz[i][j];
                    if (j == (this.cant_incognitas - 1)) {
                        if (i == (this.pos_x - 1) && j == (this.pos_y - 1)) {
                            texto += "<span style='color:red;'>" + valor + "X" + (j + 1) + "</span>";
                        } else {
                            texto += valor + "X" + (j + 1);
                        }
                    }

                    if (j == this.cant_incognitas) {
                        if (i == (this.pos_x - 1) && j == (this.pos_y - 1)) {
                            texto += " = <span style='color:red;'>" + valor + "</span> <br>";
                        } else {
                            texto += " = " + valor + " <br>";
                        }
                    }

                    if (j < (this.cant_incognitas - 1)) {
                        if (i == (this.pos_x - 1) && j == (this.pos_y - 1)) {
                            texto += "<span style='color:red;'>" + valor + "X" + (j + 1) + "</span> + ";
                        } else {
                            texto += valor + "X" + (j + 1) + " + ";
                        }
                    }
                }
            }
            texto += "</html>";
        }
        this.txtMatriz.setText(texto);
    }

    // metodo cuando le den en el boton avanzar (>)
    private void posicionAvanzar() {
        if (this.pos_y == (this.cant_incognitas + 1) && this.pos_x == this.cant_incognitas) {
            return;
        }

        if (this.pos_y == this.cant_incognitas) {
            this.lblPosValue.setText("Valor en la posicion [I," + this.pos_x + "]");
            this.pos_y++;
            return;
        }

        if (this.pos_y < this.cant_incognitas) {
            this.pos_y++;
            this.lblPosValue.setText("Valor en la posicion [" + this.pos_x + "," + this.pos_y + "]");
            return;
        }

        if (this.pos_y == (this.cant_incognitas + 1) && this.pos_x <= this.cant_incognitas) {
            this.pos_x++;
            this.lblPosValue.setText("Valor en la posicion [" + this.pos_x + ",1]");
            this.pos_y = 1;
        }
    }

    // metodo cuando le den al boton retroceder (<)
    private void posicionRetroceder() {
        if (this.pos_x == 1 && this.pos_y == 1) {
            return;
        }

        if (this.pos_y > 1 && this.pos_y <= (this.cant_incognitas + 1)) {
            this.pos_y--;
            this.lblPosValue.setText("Valor en la posicion [" + this.pos_x + "," + this.pos_y + "]");
            return;
        }

        if (this.pos_y == 1 && this.pos_x > 1) {
            this.pos_x--;
            this.pos_y = (this.cant_incognitas + 1);
            this.lblPosValue.setText("Valor en la posicion [I," + this.pos_x + "]");
        }
    }

    // cuando se le ingresen valores al campo
    private void cambiandoValorMatriz() {
        this.flag_llenado = true;
        
        if(txtValorPos.getText().equals("-") || txtValorPos.getText().equals(".")){
            return;
        }

        if (!txtValorPos.getText().isEmpty()) {
            float valor_num = (float) Float.parseFloat(txtValorPos.getText());
            this.arr_matriz[(this.pos_x - 1)][(this.pos_y - 1)] = valor_num;
            this.rrellenandoMatriz();

            if (this.spinerIncognitas.isEnabled()) {
                this.spinerIncognitas.setEnabled(false);
                System.out.println("entro");
            }
        }
    }

    // cuando toquen el boton de cancelar
    private void pressBtnCalcular() {
        String opc = this.btnCalcular.getText().toLowerCase();
        switch (opc) {
            case "calcular":
                this.tabebResultados.setVisible(true);
                // agregando el primer panel
                pnlResultado1 pnlR1 = new pnlResultado1();
                pnlR1.setCalcularMatrizAmpliada(this.arr_matriz, this.cant_incognitas);
                this.tabebResultados.addTab("Matriz Ampliada", pnlR1);
                // agregando el segundo panel
                pnlResultado2 pnlR2 = new pnlResultado2();
                pnlR2.setFrmPadre(this);
                pnlR2.setTransfomacionesElementales(this.arr_matriz, this.cant_incognitas);
                this.tabebResultados.addTab("Transformaciones Elementales", pnlR2);
                // agregando el panel de los resultados
                pnlResultado3 pnlR3 = new pnlResultado3();
                pnlR3.setResultados(this.matriz_result_transform, this.cant_incognitas);
                this.tabebResultados.addTab("Resultados", pnlR3);
                this.btnCalcular.setText("Cancelar");
                break;

            case "Cancelar":

                break;
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        spinerIncognitas = new javax.swing.JSpinner();
        jPanel10 = new javax.swing.JPanel();
        lblPosValue = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnRetroceder = new javax.swing.JButton();
        btnAvanzar = new javax.swing.JButton();
        txtValorPos = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMatriz = new javax.swing.JEditorPane();
        btnCalcular = new javax.swing.JButton();
        pnlCentro = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        tabebResultados = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(40, 40, 40), 3));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(42, 41, 40));
        jPanel2.setPreferredSize(new java.awt.Dimension(719, 101));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(42, 41, 40));
        jPanel5.setPreferredSize(new java.awt.Dimension(719, 30));
        jPanel5.setLayout(new java.awt.BorderLayout());

        btnCerrar.setBackground(new java.awt.Color(49, 49, 48));
        btnCerrar.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        btnCerrar.setForeground(new java.awt.Color(255, 0, 0));
        btnCerrar.setText("X");
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setOpaque(true);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCerrar, java.awt.BorderLayout.LINE_END);

        jPanel6.setBackground(new java.awt.Color(42, 41, 40));
        jPanel6.setPreferredSize(new java.awt.Dimension(20, 30));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.LINE_START);

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 1, 15)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("ELIMINACION GAUSSIANA CON SUSTITUCION HACIA ATRAS");
        jPanel5.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(42, 41, 40));
        jPanel3.setForeground(new java.awt.Color(40, 40, 40));
        jPanel3.setPreferredSize(new java.awt.Dimension(824, 40));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 981, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(java.awt.Color.white);
        jPanel7.setPreferredSize(new java.awt.Dimension(350, 343));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setLayout(new java.awt.BorderLayout());

        jLabel3.setText("Ingrese el numero de incognitas");
        jLabel3.setPreferredSize(new java.awt.Dimension(230, 17));
        jPanel9.add(jLabel3, java.awt.BorderLayout.LINE_START);

        spinerIncognitas.setModel(new javax.swing.SpinnerNumberModel(2, 2, null, 1));
        spinerIncognitas.setPreferredSize(new java.awt.Dimension(34, 28));
        spinerIncognitas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinerIncognitasStateChanged(evt);
            }
        });
        jPanel9.add(spinerIncognitas, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 50));

        jPanel10.setLayout(new java.awt.BorderLayout());

        lblPosValue.setText("Valor en la posicion");
        lblPosValue.setPreferredSize(new java.awt.Dimension(180, 17));
        jPanel10.add(lblPosValue, java.awt.BorderLayout.LINE_START);

        jPanel11.setLayout(new java.awt.BorderLayout());

        btnRetroceder.setText("<");
        btnRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetrocederActionPerformed(evt);
            }
        });
        jPanel11.add(btnRetroceder, java.awt.BorderLayout.LINE_START);

        btnAvanzar.setText(">");
        btnAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvanzarActionPerformed(evt);
            }
        });
        jPanel11.add(btnAvanzar, java.awt.BorderLayout.LINE_END);

        txtValorPos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValorPos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtValorPosKeyReleased(evt);
            }
        });
        jPanel11.add(txtValorPos, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 330, 50));

        jPanel12.setLayout(new java.awt.BorderLayout());

        txtMatriz.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(txtMatriz);

        jPanel12.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 330, 180));

        btnCalcular.setText("CALCULAR");
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });
        jPanel7.add(btnCalcular, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 330, 50));

        jPanel4.add(jPanel7, java.awt.BorderLayout.LINE_START);

        pnlCentro.setLayout(new java.awt.BorderLayout());

        jPanel13.setPreferredSize(new java.awt.Dimension(631, 40));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton2.setText("<");
        jPanel13.add(jButton2);

        jButton1.setText(">");
        jPanel13.add(jButton1);

        pnlCentro.add(jPanel13, java.awt.BorderLayout.PAGE_END);
        pnlCentro.add(tabebResultados, java.awt.BorderLayout.CENTER);

        jPanel4.add(pnlCentro, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(997, 585));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void spinerIncognitasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinerIncognitasStateChanged
        this.cant_incognitas = (int) this.spinerIncognitas.getValue();
        this.rrellenandoMatriz();
    }//GEN-LAST:event_spinerIncognitasStateChanged

    private void btnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarActionPerformed
        this.posicionAvanzar();
        this.rrellenandoMatriz();
    }//GEN-LAST:event_btnAvanzarActionPerformed

    private void btnRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetrocederActionPerformed
        this.posicionRetroceder();
        this.rrellenandoMatriz();
    }//GEN-LAST:event_btnRetrocederActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        this.pressBtnCalcular();
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.lblPosValue.setText("Valor en la posicion [" + this.pos_x + "," + this.pos_y + "]");
        this.rrellenandoMatriz();
        this.tabebResultados.setVisible(false);
    }//GEN-LAST:event_formWindowOpened

    private void txtValorPosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorPosKeyReleased
        this.cambiandoValorMatriz();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.posicionAvanzar();
            this.rrellenandoMatriz();
            this.txtValorPos.setText("");
        }
    }//GEN-LAST:event_txtValorPosKeyReleased

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
            java.util.logging.Logger.getLogger(frmGauss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmGauss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmGauss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmGauss.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmGauss().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAvanzar;
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnRetroceder;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPosValue;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JSpinner spinerIncognitas;
    private javax.swing.JTabbedPane tabebResultados;
    private javax.swing.JEditorPane txtMatriz;
    private javax.swing.JTextField txtValorPos;
    // End of variables declaration//GEN-END:variables
}