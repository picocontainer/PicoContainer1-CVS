package org.picocontainer.swing;

import org.picocontainer.gui.tree.ComponentTreeNode;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class PicoTreeCellRenderer extends DefaultTreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if( value instanceof ComponentTreeNode ) {
            Class componentImplementation = (Class) ((ComponentTreeNode)value).getUserObject();
            label.setText(componentImplementation.getName());
        } else {
            label.setText("PicoContainer");
        }
        return label;
    }
}