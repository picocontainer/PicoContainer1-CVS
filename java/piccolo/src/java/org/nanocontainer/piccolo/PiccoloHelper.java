package org.picoextras.piccolo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.picocontainer.PicoContainer;
import org.picoextras.piccolo.Constants;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;

public class PiccoloHelper {

	public static PNode createNodeForContainer(PicoContainer container, float x, float y, float width) {
		Collection instances = container.getComponentInstances();
		Collection nodes = new ArrayList();

		float unit = width / 10;
		float childrenOffset = unit/2;
		float instancesOffset = unit/2;

//		Iterator it = children.iterator();
//		while (it.hasNext()) {
//			Object o = it.next();
//
//			// Create the path representing the nested container
//			PNode parent = createNodeForContainer((PicoContainer) o, x + unit *3/2, y + childrenOffset, 6 * unit);
//
//			nodes.add(parent);
//
//			childrenOffset += parent.getHeight();
//			childrenOffset += unit / 2;
//		}

		Iterator it = instances.iterator();
		while (it.hasNext()) {
			Object o = it.next();

			// Create the Rectangle representing the instance
			PNode child = createNodeForInstance(o, x + 8 * unit, y + instancesOffset, 6 * unit);

			nodes.add(child);

			instancesOffset += child.getHeight();
			instancesOffset += unit / 2;
		}

		// Get the max offset
		if (instancesOffset < childrenOffset) {
			instancesOffset = childrenOffset;
		}

		// Create the Rectangle representing the Container
		PPath path = PPath.createRectangle(x, y, width, instancesOffset);
		path.setStroke(new PFixedWidthStroke());
		path.setStrokePaint(Constants.PICO_STROKE);
		path.setPaint(Constants.PICO_FILL);

		// Add an icon on the top-left corner
		PImage pcimg = new PImage(Constants.picoContainerIcon.getImage(), false);
		pcimg.setPickable(false);
		pcimg.setBounds(x, y, unit, unit);
		path.addChild(pcimg);

		// Add some metadata
		path.addClientProperty(Constants.USER_OBJECT, container);
		path.addClientProperty(Constants.TOOL_TIP, "Container [" + container.hashCode() + "]");

		// Add every child nodes created
		it = nodes.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			path.addChild((PNode) o);
		}

		return path;
	}

	public static PNode createNodeForInstance(Object o, float x, float y, float width) {
		float unit = width / 8;

		// Create the Rectangle representing the instance
		PPath path = PPath.createEllipse(x, y, 2 * unit, 2 * unit);
		path.setStroke(new PFixedWidthStroke());
		path.setStrokePaint(Constants.PICO_STROKE);
		path.setPaint(Constants.PICO_FILL);

		// Add an icon on the center
		PImage cimg = new PImage(Constants.defaultComponentIcon.getImage(), false);
		cimg.setPickable(false);
		cimg.setBounds(x + unit / 2, y + unit / 2, unit, unit);
		path.addChild(cimg);

		// Add some metadata
		path.addClientProperty(Constants.USER_OBJECT, o);
		path.addClientProperty(Constants.TOOL_TIP, o.toString());

		return path;
	}
}