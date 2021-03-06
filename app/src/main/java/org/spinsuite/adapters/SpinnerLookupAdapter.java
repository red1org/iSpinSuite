/*************************************************************************************
 * Product: Spin-Suite (Making your Business Spin)                                   *
 * This program is free software; you can redistribute it and/or modify it           *
 * under the terms version 2 of the GNU General Public License as published          *
 * by the Free Software Foundation. This program is distributed in the hope          *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied        *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                  *
 * See the GNU General Public License for more details.                              *
 * You should have received a copy of the GNU General Public License along           *
 * with this program; if not, write to the Free Software Foundation, Inc.,           *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                            *
 * For the text or an alternative of this public license, you may reach us           *
 * Copyright (C) 2012-2015 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpcya.com                                        *
 *************************************************************************************/
package org.spinsuite.adapters;

import java.util.List;

import org.spinsuite.base.R;
import org.spinsuite.util.DisplayLookupSpinner;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com Jul 13, 2015, 3:35:20 AM
 *
 */
public class SpinnerLookupAdapter extends ArrayAdapter<DisplayLookupSpinner> {
	
	/**
	 * 
	 * *** Constructor ***
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param context
	 * @param objects
	 */
	public SpinnerLookupAdapter(Context context, List<DisplayLookupSpinner> objects) {
		super(context, R.layout.v_lookup_spinner, objects);
		setDropDownViewResource(R.layout.v_lookup_spinner_drop_down);
	}
}