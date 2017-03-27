/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.reddeer.eclipse.ui.views.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.views.log.SharedImages;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Represents Error Log view
 * 
 * @author rawagner
 * @author Jan Novak <jnovak@redhat.com>
 *
 */
public class LogView extends WorkbenchView{

	private final String DELETE_LOG = "Delete Log";
	private final String CLEAR_LOG = "Clear Log Viewer";
	private final String RESTORE_LOG = "Restore Log";
	private final String CONFIRM_DLG = "Confirm Delete";

	/**
	 * Constructs the view with "Error Log".
	 */
	public LogView(){
		super("Error Log");
	}
	
	/**
	 * Gets the OK messages.
	 *
	 * @return list of messages with severity OK (according to IStatus)
	 */
	public List<LogMessage> getOKMessages() {
		return getMessages(Severity.OK);
	}
	
	/**
	 * Gets the info messages.
	 *
	 * @return list of messages with severity INFO (according to IStatus)
	 */
	public List<LogMessage> getInfoMessages() {
		return getMessages(Severity.INFO);
	}
	
	/**
	 * Gets the warning messages.
	 *
	 * @return list of messages with severity WARNING (according to IStatus)
	 */
	public List<LogMessage> getWarningMessages() {
		return getMessages(Severity.WARNING);
	}
	
	/**
	 * Gets the error messages.
	 *
	 * @return list of messages with severity ERROR (according to IStatus)
	 */
	public List<LogMessage> getErrorMessages() {
		return getMessages(Severity.ERROR);
	}
	
	/**
	 * Clears Error log messages.
	 */
	public void clearLog() {
		activate();
		new DefaultTree().setFocus();
		Menu cm = new ContextMenu(CLEAR_LOG);
		cm.select();
	}

	/**
	 * Deletes Error log messages.
	 */
	public void deleteLog() {
		activate();
		new DefaultTree().setFocus();
		Menu cm = new ContextMenu(DELETE_LOG);
		if (!cm.isEnabled()) {
			log.debug("Unable to delete log. \"" + DELETE_LOG + "\" menu item is not enabled.");
			return;
		}
		cm.select();
		new DefaultShell(CONFIRM_DLG);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(CONFIRM_DLG));
	}

	/**
	 * Restores Error log messages.
	 */
	public void restoreLog() {
		activate();
		new DefaultTree().setFocus();
		Menu cm = new ContextMenu(RESTORE_LOG);
		cm.select();
	}

	private List<LogMessage> getMessages(Severity severity){
		activate();

		ArrayList<LogMessage> messages = new ArrayList<>();
		for(TreeItem item : new DefaultTree().getItems()){
			if(severityMatch(item, severity)) {
				messages.add(new LogMessage(item, severity.getIStatus()));
			}
		}
		return messages;
	}

	private boolean severityMatch(TreeItem item, Severity severity) {
		Image itemImage = item.getImage();
		return severity.getImages().contains(itemImage);
	}

	private enum Severity {
		
		OK(IStatus.OK, 
			SharedImages.getImage(SharedImages.DESC_OK_ST_OBJ)),
		INFO(IStatus.INFO, 
			SharedImages.getImage(SharedImages.DESC_INFO_ST_OBJ)),
		WARNING(IStatus.WARNING, 
			SharedImages.getImage(SharedImages.DESC_WARNING_ST_OBJ)),
		ERROR(IStatus.ERROR, 
			SharedImages.getImage(SharedImages.DESC_ERROR_ST_OBJ),
			SharedImages.getImage(SharedImages.DESC_ERROR_STACK_OBJ));

		private List<Image> images;
		private int iStatus;

		Severity(int iStatus, Image... image) {
			this.images = Arrays.asList(image);
			this.iStatus = iStatus;
		}

		public List<Image> getImages() {
			return images;
		}

		public int getIStatus() {
			return iStatus;
		}
	}

}
