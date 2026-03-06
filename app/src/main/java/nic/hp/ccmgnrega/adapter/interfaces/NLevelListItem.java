package nic.hp.ccmgnrega.adapter.interfaces;

import android.view.View;

public interface NLevelListItem {

     boolean isExpanded();

     void toggle();

     NLevelListItem getParent();

     View getView();
}
