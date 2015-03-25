package com.yidian_erhuo.activity;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterChat;
import com.yidian_erhuo.cloud.IMReceiver;
import com.yidian_erhuo.cloud.IMReceiver.onChat;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityChat;
import com.yidian_erhuo.entity.EntityMessage;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

public class ActivityChat extends ActivityBase implements onChat {
	private EntityUserInfo entityUserInfoMy, entityUserInfoFriend;

	private ListView listView;
	private TitleView titleView;
	private Button button_submit;
	private EditText editText_input;
	private AdapterChat adapterChat;
	private EMConversation conversation;
	private IMReceiver imReceiver = new IMReceiver();

	@Override
	public void initView() {
		setContentView(R.layout.activity_chat);
		// �õ��ҵ���Ϣ�Լ�������Ϣ
		entityUserInfoMy = Utile.getUserInfo(this);
		entityUserInfoFriend = (EntityUserInfo) getIntent()
				.getSerializableExtra("friendinfo");
		// IM�����Ự
		conversation = EMChatManager.getInstance().getConversation(
				entityUserInfoFriend.getId());
		adapterChat = new AdapterChat(this);
		titleView = new TitleView(this);
		titleView.MidTextView(entityUserInfoFriend.getNickName(), 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		listView = (ListView) findViewById(R.id.listview_chat);
		button_submit = (Button) findViewById(R.id.button_chat_submit);
		editText_input = (EditText) findViewById(R.id.edittext_chat_input);
		listView.setAdapter(adapterChat);
		button_submit.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
		// ע��ص�
		imReceiver.setOnChat(this);
		// ��ȡ������Ϣ
		List<EMMessage> emMessages = conversation.getAllMessages();
		// ����������Ϣ��ʾ��list��
		for (int i = 0; i < emMessages.size(); i++) {
			EntityChat entityChat;
			String from = emMessages.get(i).getFrom();
			// TextMessageBody textMessageBody = (TextMessageBody)
			// emMessages.get(
			// i).getBody();
			// entityChat = json2Entity(textMessageBody.getMessage());
			entityChat = new EntityChat();
			try {
				entityChat.setHeader(emMessages.get(i).getStringAttribute(
						"fromheader"));
				entityChat.setMessage(emMessages.get(i).getStringAttribute(
						"message"));
				entityChat
						.setName(emMessages.get(i).getStringAttribute("fromname"));				
				entityChat.setMe(false);
			} catch (EaseMobException e) {
				e.printStackTrace();
			}
			entityChat.setUserId(from);
			entityChat.setHistory(true);
			if (entityUserInfoMy.getId().equals(from)) {// �ҵ���Ϣ
				entityChat.setMe(true);
			} else if (entityUserInfoFriend.getId().equals(from)) {// ���ѵ���Ϣ
				entityChat.setMe(false);
			}
			adapterChat.getEntityChats().add(entityChat);
		}
		adapterChat.notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			// ע���ص�
			imReceiver.setOnChat(null);
			KeyBack();
			break;
		case R.id.button_chat_submit:
			String str_input = editText_input.getText().toString();
			if ("".equals(str_input)) {
				Toast.makeText(ActivityChat.this, "���ݲ���Ϊ��", Toast.LENGTH_SHORT)
						.show();
			} else {
				// ��װ��ʾ����
				EntityChat entityChat = new EntityChat();
				entityChat.setName(entityUserInfoMy.getName());
				entityChat.setHeader(entityUserInfoMy.getHeader());
				entityChat.setMe(true);
				entityChat.setMessage(str_input);
				// ��װIM����
				EMMessage emMessage = EMMessage
						.createSendMessage(EMMessage.Type.TXT);
				emMessage.addBody(new TextMessageBody(""));
				emMessage.setReceipt(entityUserInfoFriend.getId());
				emMessage.setAttribute("fromheader", entityChat.getHeader());
				emMessage.setAttribute("message", entityChat.getMessage());
				emMessage.setAttribute("fromname", entityChat.getName());			
				conversation.addMessage(emMessage);
				try {
					EMChatManager.getInstance().sendMessage(emMessage);
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				// ���em����
				entityChat.setEmMessage(emMessage);
				// ˢ���б�
				adapterChat.getEntityChats().add(entityChat);
				adapterChat.notifyDataSetChanged();
				// ��������
				editText_input.setText("");
				// �������ײ�
				listView.setSelection(listView.getCount() - 1);
				// �ж��Ƿ���Ҫ�������ݿ�
				ArrayList<EntityMessage> entityMessages = new DatabaseService(
						ActivityChat.this).QueryMessage();
				boolean need = false;
				if (entityMessages.size() == 0) {
					need = true;
				} else {
					for (int i = 0; i < entityMessages.size(); i++) {
						if (entityMessages.get(i).getFriendId()
								.equals(entityUserInfoFriend.getId())) {
							need = false;
							break;
						} else {
							need = true;
						}
					}
				}
				EntityMessage entityMessage = new EntityMessage();
				entityMessage.setFriendId(entityUserInfoFriend.getId());
				entityMessage.setFriendHeader(entityUserInfoFriend.getHeader());
				entityMessage.setLastMessage(entityChat.getMessage());
				entityMessage.setFriendNickName(entityUserInfoFriend.getNickName());
				entityMessage.setLastMessageTimeTemp(System.currentTimeMillis()
						/ 1000 + "");
			
				// �������ݿ�
				if (need) {
					new DatabaseService(ActivityChat.this)
							.InsertMessage(entityMessage);
				} else {
					new DatabaseService(ActivityChat.this)
							.UpdataMessage(entityMessage);
				}
			}
			break;
		}
	}

	// /**
	// * ��װ����ת����json
	// */
	// private String entity2Json(EntityChat entityChat) {
	// String json = "";
	// try {
	// // ��װjson
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("header", entityChat.getHeader());
	// jsonObject.put("message", entityChat.getMessage());
	// jsonObject.put("nickname", entityChat.getNickName());
	// // ת�����ַ�
	// json = jsonObject.toString();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return json;
	// }

	// /**
	// * jsonת���ɷ�װ����
	// */
	// private EntityChat json2Entity(String json) {
	// EntityChat entityChat = new EntityChat();
	// try {
	// JSONObject jsonObject = new JSONObject(json);
	// entityChat.setMe(false);
	// entityChat.setHeader(jsonObject.getString("header"));
	// entityChat.setMessage(jsonObject.getString("message"));
	// entityChat.setNickName(jsonObject.getString("nickname"));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return entityChat;
	// }

	@Override
	public void chat(EntityChat entityChat) {
		if (entityUserInfoFriend.getId().equals(entityChat.getUserId())) {// �ǵ�ǰ�û�
			adapterChat.getEntityChats().add(entityChat);
			adapterChat.notifyDataSetChanged();
			listView.setSelection(listView.getCount() - 1);
		} else {// �ǵ�ǰ�û�

		}
	}

	@Override
	public void KeyBack() {
		imReceiver.setOnChat(null);
		super.KeyBack();
	}
}