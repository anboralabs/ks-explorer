/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2021 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kse.gui;

import org.kse.utilities.os.OperatingSystem;

import javax.swing.*;
import java.awt.*;

/**
 * Look and Feel utility methods.
 *
 */
public class LnfUtil {

	// VAqua LnF class as constant to avoid compile dependency
	private static final String VAQUA_LAF_CLASS = "org.violetlib.aqua.AquaLookAndFeel";

	private LnfUtil() {
	}

	/**
	 * Is a Mac l&amp;f (Aqua) currently being used?
	 *
	 * @return True if it is
	 */
	public static boolean usingMacLnf() {
		String lnfClass = UIManager.getLookAndFeel().getClass().getName();

		return OperatingSystem.isMacOs()
				&& (UIManager.getSystemLookAndFeelClassName().equals(lnfClass) || lnfClass.equals(VAQUA_LAF_CLASS));
	}

	/**
	 * Get default font size for a label.
	 *
	 * @return Font size
	 */
	public static int getDefaultFontSize() {
		Font defaultFont = new JLabel().getFont();
		return defaultFont.getSize();
	}
}
