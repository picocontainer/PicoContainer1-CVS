package org.picoextras.swing;

import org.picocontainer.ComponentAdapter;
import org.picoextras.guimodel.ComponentAdapterModel;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Map;
import java.util.HashMap;

/**
 * A model that takes an Object instance. Property name are in the left column
 * and their value in the right column.
 * 
 * @author Laurent Etiemble
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class ComponentAdapterTableModel implements TableModel {
    private static Map models = new HashMap();
	private static final String[] TABLE_HEADER = new String[] { "Property", "Value" };
	public static final TableModel EMPTY_MODEL = new DefaultTableModel(ComponentAdapterTableModel.TABLE_HEADER, 0);

	private final ComponentAdapterModel model;

    private ComponentAdapterTableModel(ComponentAdapter componentAdapter) {
		model = ComponentAdapterModel.getInstance(componentAdapter);
	}

	public int getColumnCount() {
		return TABLE_HEADER.length;
	}

	public int getRowCount() {
		return model.getPropertyDescriptors().length;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Class getColumnClass(int columnIndex) {
		return Object.class;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		if (columnIndex == 0) {
			result = model.getPropertyDescriptors()[rowIndex].getDisplayName();
		} else {
			result = model.getPropertyValue(rowIndex);
		}
		return result;
	}

	public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
		model.setPropertyValue(rowIndex, newValue);
	}

	public String getColumnName(int columnIndex) {
		return TABLE_HEADER[columnIndex];
	}

	public void addTableModelListener(TableModelListener l) {
		// Do nothing
	}

	public void removeTableModelListener(TableModelListener l) {
		// Do nothing
	}

    public static synchronized TableModel getInstance(ComponentAdapter componentAdapter) {
        TableModel model = (TableModel) models.get(componentAdapter);
        if(model == null) {
            model = new ComponentAdapterTableModel(componentAdapter);
            models.put(componentAdapter, model);
        }
        return model;
    }

}