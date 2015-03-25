package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.widget.ListView;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterMessage;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityMessage;

public class FragmentMessage extends FragmentBase {
	private ListView listView;
	private AdapterMessage adapterMessage;

	@Override
	public void initView() {
		setContentView(R.layout.fragment_message);

		adapterMessage = new AdapterMessage(getActivity());
		listView = (ListView) contentView.findViewById(R.id.listview_message);
		listView.setAdapter(adapterMessage);
	}

	@Override
	public void onRefresh() {
		ArrayList<EntityMessage> entityMessages = new DatabaseService(
				getActivity()).QueryMessage();
		ArrayList<EntityMessage> list = new ArrayList<EntityMessage>();
		for (int i = entityMessages.size() - 1; i >= 0; i--) {
			list.add(entityMessages.get(i));
		}
		adapterMessage.setEntityMessages(list);
		adapterMessage.notifyDataSetChanged();
	}
}