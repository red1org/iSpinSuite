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
package org.spinsuite.view.lookup;

import java.util.logging.Level;

import org.spinsuite.base.DB;
import org.spinsuite.interfaces.I_Lookup;
import org.spinsuite.util.DisplayRecordItem;
import org.spinsuite.util.DisplayType;
import org.spinsuite.util.Env;
import org.spinsuite.util.FilterValue;
import org.spinsuite.util.LogM;
import org.spinsuite.util.TabParameter;

import android.app.Activity;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com Mar 2, 2015, 2:53:28 AM
 *
 */
public class VLookupSearch extends GridField 
								implements I_Lookup {

	/**
	 * 
	 * *** Constructor ***
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param activity
	 */
	public VLookupSearch(Activity activity) {
		super(activity);
		this.v_Activity = activity;
		init();
	}

	/**
	 * 
	 * *** Constructor ***
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param activity
	 * @param attrs
	 */
	public VLookupSearch(Activity activity, AttributeSet attrs) {
		super(activity, attrs);
		this.v_Activity = activity;
		init();
	}

	/**
	 * 
	 * *** Constructor ***
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param activity
	 * @param attrs
	 * @param defStyle
	 */
	public VLookupSearch(Activity activity, AttributeSet attrs, int defStyle) {
		super(activity, attrs, defStyle);
		this.v_Activity = activity;
		init();
	}

	/**
	 * 
	 * *** Constructor ***
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param activity
	 * @param m_field
	 */
	public VLookupSearch(Activity activity, InfoField m_field) {
		this(activity, m_field, null);
	}
	
	/**
	 * With Tab Parameter
	 * *** Constructor ***
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param activity
	 * @param m_field
	 * @param tabParam
	 */
	public VLookupSearch(Activity activity, InfoField m_field, TabParameter tabParam) {
		super(activity, m_field, tabParam);
		this.v_Activity = activity;
		init();
	}
	
	/**	Search 				*/
	private VSearch 			v_Search 	= null;
	/**	Activity from		*/
	private Activity 			v_Activity 	= null;
	/**	Set Old Value		*/
	private Object				m_OldValue 	= null;
	/**	Lookup				*/
	private Lookup 				m_Lookup 	= null;
	
	@Override
	protected void init() {
		//activity.ona
		v_Search = new VSearch(v_Activity, this, m_field);
		setEnabled(!m_field.IsReadOnly);
		//	Add to View
		addView(v_Search);
		//	Instance Lookup
		m_Lookup = new Lookup(getContext(), m_field);
	}

	@Override
	public void setValue(Object value) {
		//	Set Old Value
		m_OldValue = getValue();
		//	
		if(value instanceof Integer
				&& ((Integer)value) == v_Search.getRecord_ID())
			return;
		loadValue(value);
	}

	@Override
	public Object getValue() {
		return v_Search.getRecord_ID();
	}
	
	@Override
	public Object getOldValue() {
		return m_OldValue;
	}

	@Override
	public boolean isEmpty() {
		return (v_Search.getRecord_ID() <= 0);
	}

	@Override
	public View getChildView() {
		return v_Search;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		v_Search.setEnabled(enabled);
	}
	
	/**
	 * Set current item
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param item
	 * @return void
	 */
	public void setItem(DisplayRecordItem item){
		v_Search.setItem(item);
		Object value = null;
		if(item != null)
			value = getValue();
		else
			value = -1;
		//	Set Context
		DisplayType.setContextValue(getContext(), getActivityNo(), getTabNo(), m_field, value);
        //	Listener
        if(m_Listener != null)
        	m_Listener.onFieldEvent(this);
	}

	@Override
	public String getDisplayValue() {
		return v_Search.getValue();
	}
	
	/**
	 * Load Object from vaue
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @param value
	 * @return void
	 */
	private void loadValue(Object value){
		//	Null Value
		if(value == null)
			setItem(new DisplayRecordItem(0, null));
		//	
		if(!(value instanceof Integer))
			return;
		try{
			DB conn = new DB(getContext());
			DB.loadConnection(conn, DB.READ_ONLY);
			Cursor rs = null;
			FilterValue criteria = new FilterValue();
			InfoLookup lookupInfo = m_Lookup.getInfoLookup();
			criteria.setWhereClause(lookupInfo.TableName + "." + lookupInfo.KeyColumn[0] + " = ?");
			criteria.addValue(value);
			m_Lookup.setCriteria(criteria.getWhereClause());
			//	Query
			rs = conn.querySQL(m_Lookup.getSQL(), criteria.getValues());
			if(rs.moveToFirst()) {
				int keyCount = m_Lookup.getInfoLookup().KeyColumn.length;
				//	Declare Keys
				int[] keys = new int[keyCount];
				//	Get Keys
				for(int i = 0; i < keyCount; i++) {
					keys[i] = rs.getInt(i);
				}
				//	Add
				setItem(new DisplayRecordItem(keys, 
						Env.parseLookup(v_Activity, m_Lookup.getInfoLookup(), 
								rs.getString(keyCount++), 
								InfoLookup.TABLE_SEARCH_VIEW_SEPARATOR)));
			} else {
				setItem(new DisplayRecordItem(0, null));
			}
			//	Close
			DB.closeConnection(conn);
		} catch(Exception e){
			LogM.log(getContext(), getClass(), Level.SEVERE, "Error in Load " + e.toString(), e);
		}
	}

	@Override
	public String getValidation() {
		return m_Lookup.getValidation();
	}
	
	@Override
	public String toString() {
		return "VLookupSearch [v_Search=" + v_Search + ", v_Activity="
				+ v_Activity + ", m_OldValue=" + m_OldValue + ", m_Lookup="
				+ m_Lookup + "]";
	}

	@Override
	public void setValueAndOldValue(Object value) {
		//	
		if(value instanceof Integer
				&& ((Integer)value) == v_Search.getRecord_ID())
			return;
		//	Set Old Value
		m_OldValue = getValue();
		//	
		loadValue(value);
	}
}
