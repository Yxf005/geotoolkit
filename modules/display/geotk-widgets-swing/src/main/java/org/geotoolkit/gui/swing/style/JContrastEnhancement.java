/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007 - 2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 - 2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.gui.swing.style;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.geotoolkit.gui.swing.resource.MessageBundle;
import org.geotoolkit.map.MapLayer;
import org.opengis.style.ContrastEnhancement;
import org.opengis.style.ContrastMethod;

/**
 * ContrastEnchancement panel
 * 
 * @author  Johann Sorel
 * @module
 */
public class JContrastEnhancement extends StyleElementEditor<ContrastEnhancement>{

    private MapLayer layer = null;

    /** 
     * Creates new form JFillPanel 
     */
    public JContrastEnhancement() {
        super(ContrastEnhancement.class);
        initComponents();
    }
    
    public void setExpressionVisible(boolean visible){
        guiGamma.setExpressionVisible(visible);
    } 

    /**
     * {@inheritDoc }
     */
    @Override
    public void setLayer(final MapLayer layer) {
        this.layer = layer;
        guiGamma.setLayer(layer);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MapLayer getLayer(){
        return layer;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void parse(final ContrastEnhancement enhancement) {
        if (enhancement != null) {
            guiGamma.parse(enhancement.getGammaValue());
            final ContrastMethod method = enhancement.getMethod();
            if(ContrastMethod.HISTOGRAM.equals(method)){
                guiHistogram.setSelected(true);
            }else if(ContrastMethod.NORMALIZE.equals(method)){
                guiNormalize.setSelected(true);
            }else{
                guiNone.setSelected(true);
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ContrastEnhancement create() {
        if(guiHistogram.isSelected()){
            return getStyleFactory().contrastEnhancement(guiGamma.create(),ContrastMethod.HISTOGRAM );
        }else if(guiNormalize.isSelected()){
            return getStyleFactory().contrastEnhancement(guiGamma.create(),ContrastMethod.NORMALIZE);
        }else{
            return getStyleFactory().contrastEnhancement(guiGamma.create(),ContrastMethod.NONE);
        }
    }
    
    @Override
    protected Object[] getFirstColumnComponents() {
        return new Object[]{guiLabelContrast};
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        methodGroup = new ButtonGroup();
        guiLabelContrast = new JLabel();
        guiGamma = new JNumberExpressionPane();
        guiNone = new JRadioButton();
        guiHistogram = new JRadioButton();
        guiNormalize = new JRadioButton();

        setOpaque(false);

        guiLabelContrast.setText(MessageBundle.format("gamma")); // NOI18N

        guiGamma.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JContrastEnhancement.this.propertyChange(evt);
            }
        });

        methodGroup.add(guiNone);
        guiNone.setText(MessageBundle.format("method_none")); // NOI18N
        guiNone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guiNoneActionPerformed(evt);
            }
        });

        methodGroup.add(guiHistogram);
        guiHistogram.setText(MessageBundle.format("method_histogram")); // NOI18N
        guiHistogram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guiNoneActionPerformed(evt);
            }
        });

        methodGroup.add(guiNormalize);
        guiNormalize.setText(MessageBundle.format("method_normalize")); // NOI18N
        guiNormalize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guiNoneActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(guiLabelContrast)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiGamma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addComponent(guiHistogram)
            .addComponent(guiNone)
            .addComponent(guiNormalize)
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {guiHistogram, guiNone, guiNormalize});

        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(guiGamma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(guiLabelContrast))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiNone)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiHistogram)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiNormalize))
        );

        layout.linkSize(SwingConstants.VERTICAL, new Component[] {guiGamma, guiLabelContrast});

    }// </editor-fold>//GEN-END:initComponents

    private void propertyChange(PropertyChangeEvent evt) {//GEN-FIRST:event_propertyChange
        // TODO add your handling code here:
         if (PROPERTY_UPDATED.equalsIgnoreCase(evt.getPropertyName())) {            
            firePropertyChange(PROPERTY_UPDATED, null, create());
            parse(create());
        }
    }//GEN-LAST:event_propertyChange

    private void guiNoneActionPerformed(ActionEvent evt) {//GEN-FIRST:event_guiNoneActionPerformed
        // TODO add your handling code here:
        firePropertyChange(PROPERTY_UPDATED, null, create());
    }//GEN-LAST:event_guiNoneActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JNumberExpressionPane guiGamma;
    private JRadioButton guiHistogram;
    private JLabel guiLabelContrast;
    private JRadioButton guiNone;
    private JRadioButton guiNormalize;
    private ButtonGroup methodGroup;
    // End of variables declaration//GEN-END:variables
}
