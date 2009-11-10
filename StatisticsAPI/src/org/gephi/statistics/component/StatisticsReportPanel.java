/**
Copyright 2008 WebAtlas
Authors : Patrick J. McSweeney (pjmcswee@syr.edu)
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.statistics.component;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.WindowConstants;
import javax.swing.text.View;
import org.gephi.ui.components.JHTMLEditorPane;

/**
 * 
 * @author pjmcswee
 */
class ReportSelection implements Transferable {
    private static ArrayList flavors = new ArrayList();
    static {
        try {
            flavors.add(new DataFlavor("text/html;class=java.lang.String"));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    private String html;

    /**
     *
     * @param html
     */
    public ReportSelection(String html) {
        this.html = html;
        String newHTML = new String();
        String[] result = html.split("file:");
        System.out.println(result.length);
        boolean first = true;
        for (int i = 0; i < result.length; i++) {
            if (result[i].contains("</IMG>")) {
                String next = result[i];
                System.out.println(">  " + next);
                String[] elements = next.split("\"");
                String filename = elements[0];


                ByteArrayOutputStream out = new ByteArrayOutputStream();

                File file = new File(filename);
                try {
                    BufferedImage image = ImageIO.read(file);
                    ImageIO.write((RenderedImage) image, "PNG", out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] imageBytes = out.toByteArray();
                String base64String = Base64.encode(imageBytes);
                if (!first) {

                    newHTML += "\"";
                }
                first = false;
                newHTML += "data:image/png;base64," + base64String;
                for (int j = 1; j < elements.length; j++) {
                    newHTML += "\"" + elements[j];
                }
            } else {
                newHTML += result[i];
            }
        }
        this.html = new String(newHTML);
    }

    /**
     *
     * @return
     */
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) flavors.toArray(new DataFlavor[flavors.size()]);
    }

    /**
     *
     * @param flavor
     * @return
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavors.contains(flavor);
    }

    /**
     *
     * @param flavor
     * @return
     * @throws java.awt.datatransfer.UnsupportedFlavorException
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (String.class.equals(flavor.getRepresentationClass())) {
            return html;
        }
        throw new UnsupportedFlavorException(flavor);
    }
}

/**
 *
 * @author pjmcswee
 */
public class StatisticsReportPanel extends javax.swing.JDialog implements Printable {

    private String mHTMLReport;

    /** Creates new form StatisticsReportPanel */
    public StatisticsReportPanel(java.awt.Frame parent, String html) {
        super(parent, false);
        mHTMLReport = html;
        initComponents();
        displayPane.setContentType("text/html;");
        displayPane.setText(this.mHTMLReport);
        Dimension dimension = new Dimension(500, 250);
        setSize(dimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        displayPane = (javax.swing.JEditorPane)(new JHTMLEditorPane());
        printButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(displayPane);

        printButton.setText(org.openide.util.NbBundle.getMessage(StatisticsReportPanel.class, "StatisticsReportPanel.printButton.text")); // NOI18N
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        saveButton.setText(org.openide.util.NbBundle.getMessage(StatisticsReportPanel.class, "StatisticsReportPanel.saveButton.text")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        copyButton.setText(org.openide.util.NbBundle.getMessage(StatisticsReportPanel.class, "StatisticsReportPanel.copyButton.text")); // NOI18N
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });

        closeButton.setText(org.openide.util.NbBundle.getMessage(StatisticsReportPanel.class, "StatisticsReportPanel.closeButton.text")); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(closeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(copyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(printButton)
                .addGap(8, 8, 8))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printButton)
                    .addComponent(saveButton)
                    .addComponent(copyButton)
                    .addComponent(closeButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        PrinterJob pjob = PrinterJob.getPrinterJob();
        PageFormat pf = pjob.defaultPage();
        pjob.setPrintable(this, pf);

        try {
            if (pjob.printDialog()) {
                pjob.print();
            }
        } catch (PrinterException e) {
            e.printStackTrace();
        }
}//GEN-LAST:event_printButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        //??        // TODO add your handling code here:
    }//GEN-LAST:event_saveButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            toolkit.getSystemClipboard().setContents(new ReportSelection(this.mHTMLReport), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_copyButtonActionPerformed

    /**
     * 
     * @param evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_closeButtonActionPerformed
    /**
     * @param args the command line arguments
    
    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
    public void run() {
    StatisticsReportPanel dialog = new StatisticsReportPanel(new javax.swing.JFrame(), true);
    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
    public void windowClosing(java.awt.event.WindowEvent e) {
    System.exit(0);
    }
    });
    dialog.setVisible(true);
    }
    });
    }
     * */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton copyButton;
    private javax.swing.JEditorPane displayPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton printButton;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param graphics
     * @param pageFormat
     * @param pageIndex
     * @return
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {

        boolean last = false;
        try {

            View rootView = displayPane.getUI().getRootView(displayPane);

            double scaleX = pageFormat.getImageableWidth() / displayPane.getMinimumSize().getWidth();

            scaleX = Math.min(scaleX, 1.0);
            double scaleY = scaleX;

            int end = (int) (pageIndex * ((1.0f / scaleY) * (double) pageFormat.getImageableHeight()));
            Rectangle allocation = new Rectangle(0,
                    -end,
                    (int) pageFormat.getImageableWidth(),
                    (int) pageFormat.getImageableHeight());
            ((Graphics2D) graphics).scale(scaleX, scaleY);

            graphics.setClip((int) (pageFormat.getImageableX() / scaleX),
                    (int) (pageFormat.getImageableY() / scaleY),
                    (int) (pageFormat.getImageableWidth() / scaleX),
                    (int) (pageFormat.getImageableHeight() / scaleY));

            ((Graphics2D) graphics).translate(((Graphics2D) graphics).getClipBounds().getX(),
                    ((Graphics2D) graphics).getClipBounds().getY());

            rootView.paint(graphics, allocation);

            last = end > displayPane.getUI().getPreferredSize(displayPane).getHeight();

            if ((last)) {
                return Printable.NO_SUCH_PAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Printable.PAGE_EXISTS;
    }
}