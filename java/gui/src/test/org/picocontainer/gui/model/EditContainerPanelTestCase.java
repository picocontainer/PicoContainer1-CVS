package org.picocontainer.gui.model;

import junit.framework.TestCase;
import org.picocontainer.gui.swing.EditContainerPanel;
import org.picocontainer.gui.model.ComponentRegistryTreeNode;
import org.picocontainer.gui.model.ComponentTreeNode;
import org.picocontainer.gui.model.ComponentRegistryTreeNodeTestCase;
import org.picocontainer.internals.ComponentRegistry;
import org.picocontainer.defaults.DefaultComponentRegistry;

import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class EditContainerPanelTestCase extends TestCase {
    private JTree tree;
    private EditContainerPanel panel;
    private ComponentRegistryTreeNode root;

    protected void setUp() {
        ComponentRegistry rootRegistry = new DefaultComponentRegistry();
        root = new ComponentRegistryTreeNode();

        tree = new JTree(root);
        panel = new EditContainerPanel(tree);
    }

    public void testRegisterComponent() {
        tree.setSelectionPath(new TreePath(root.getPath()));

        JTextField componentField = panel.getComponentField();
        componentField.setText(ComponentRegistryTreeNodeTestCase.Foo.class.getName());

        panel.registerComponent();

        assertEquals(1, root.getChildCount());
    }

    public void testUnregisterComponent() {
        testRegisterComponent();
        ComponentTreeNode fooNode = (ComponentTreeNode) root.getChildAt(0);
        tree.setSelectionPath(new TreePath(fooNode.getPath()));

        panel.removeSelected();

        assertEquals(0, root.getChildCount());
    }

    public void testAddRegistry() {
        tree.setSelectionPath(new TreePath(root.getPath()));
        panel.addRegistry();
        assertEquals(1, root.getChildCount());
    }

    public void testRemoveRegistry() {
        testAddRegistry();
        ComponentRegistryTreeNode fooNode = (ComponentRegistryTreeNode) root.getChildAt(0);
        tree.setSelectionPath(new TreePath(fooNode.getPath()));

        panel.removeSelected();

        assertEquals(0, root.getChildCount());
    }

    public void testExecuteContainer() {
        tree.setSelectionPath(new TreePath(root.getPath()));
        JTextField componentField = panel.getComponentField();

        // Make a Foo in the root
        componentField.setText(ComponentRegistryTreeNodeTestCase.Foo.class.getName());
        panel.registerComponent();

        // Make a Bar in a child
        panel.addRegistry();
        componentField.setText(ComponentRegistryTreeNodeTestCase.Bar.class.getName());
        panel.registerComponent();

        ComponentRegistryTreeNode childRegNode = (ComponentRegistryTreeNode) root.getChildAt(1);
        tree.setSelectionPath(new TreePath(childRegNode.getPath()));

        panel.executeSelected();
    }

}