package com.heocompany.hpswine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ListFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuFragment extends ListFragment {

	public static Map<String, String> menu = new HashMap<String, String>();
	
	onMenuSelectedListener mCallback;
	
	static {
		menu.put("ReadUsbDevices", "Read Usb Devices");
		menu.put("DetectHeo", "Detect Heo");
	}
	
    public interface onMenuSelectedListener {       
        public void onMenuSelected(String fragmentName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = android.R.layout.simple_list_item_activated_1;
        
        setListAdapter(new ArrayAdapter<String>(
        		getActivity(), layout, 
        		new ArrayList<String>(MenuFragment.menu.values())
		));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (onMenuSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onMenuSelectedListener");
        }
    }
	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}
	
    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
    	getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    	int i = 0;
    	String fragmentName = "";
    	
    	for (String key : menu.keySet()) {
    		if (i == position) {
    			fragmentName = key;
    			break;
    		}
    		i++;
    	}

		    	
        mCallback.onMenuSelected(fragmentName);
        
        getListView().setItemChecked(position, true);
    }
	
}