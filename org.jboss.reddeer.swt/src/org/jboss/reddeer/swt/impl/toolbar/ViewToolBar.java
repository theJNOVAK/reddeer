package org.jboss.reddeer.swt.impl.toolbar;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.jboss.reddeer.swt.lookup.impl.ToolBarLookup;

/**
 * Workbench toolbar implementation
 * @author Jiri Peterka
 *
 */
public class ViewToolBar extends AbstractToolBar {

	public ViewToolBar() {

	
		ToolBarLookup tl = new ToolBarLookup();
		toolBar = tl.getViewToolbar();
	}
	
	
}
