package com.yidian_erhuo.cloud;

import internal.org.apache.http.entity.mime.HttpMultipartMode;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsBuying;
import com.yidian_erhuo.entity.EntityGoodsClassify;
import com.yidian_erhuo.entity.EntityGoodsComment;
import com.yidian_erhuo.entity.EntityGoodsFocus;
import com.yidian_erhuo.entity.EntityGoodsInfo;
import com.yidian_erhuo.entity.EntityGoodsSearch;
import com.yidian_erhuo.entity.EntityGoodsSelling;
import com.yidian_erhuo.entity.EntityHttpResponse;
import com.yidian_erhuo.entity.EntityImage;
import com.yidian_erhuo.entity.EntityUserCollect;
import com.yidian_erhuo.entity.EntityUserComment;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.Utile;

@SuppressWarnings("unchecked")
public class CloudAPI {

	/**
	 * �û�ע��
	 * 
	 * @param userName
	 *            �û���
	 * @param userPassword
	 *            �û�����
	 * @param onPostRequest
	 *            �ص�
	 */
	public void Register(String userName, String userPassword,
			final OnPostRequest<EntityUserInfo> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_REGISTER)
					.MultipartString("uname", userName)
					.MultipartString("pwd", userPassword).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityUserInfo entityUserInfo = new EntityUserInfo();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityUserInfo.setS(jsonObject.getInt("s"));
							if (0 == entityUserInfo.getS()) {
								entityUserInfo.setId(jsonObject
										.getString("uid"));
								result = entityUserInfo;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityUserInfo) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �û���½
	 * 
	 * @param userName
	 *            �û���
	 * @param userPassword
	 *            �û�����
	 * @param onPostRequest
	 *            �ص�
	 */
	public void Login(String userName, String userPassword,
			final OnPostRequest<EntityUserInfo> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_LOGIN)
					.MultipartString("uname", userName)
					.MultipartString("pwd", userPassword).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityUserInfo entityUserInfo = new EntityUserInfo();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityUserInfo.setS(jsonObject.getInt("s"));
							if (0 == entityUserInfo.getS()) {
								entityUserInfo.setId(jsonObject
										.getString("uid"));
								result = entityUserInfo;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityUserInfo) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��������½
	 * 
	 * @param openId
	 *            openid
	 * @param third_party
	 *            ������ƽ̨
	 * @param nickName
	 *            �ǳ�
	 * @param header
	 *            ͷ���ַ
	 * @param sex
	 *            �Ա�
	 * @param onPostRequest
	 *            �ص�
	 */

	public void LoginThird(String openId, String third_party, String nickName,
			String header, String sex,
			final OnPostRequest<EntityUserInfo> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_LOGIN_THIRD)
					.MultipartString("openid", openId)
					.MultipartString("nickname", nickName)
					.MultipartString("third_party", third_party)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityUserInfo entityUserInfo = new EntityUserInfo();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityUserInfo.setS(jsonObject.getInt("s"));
							if (0 == entityUserInfo.getS()) {
								entityUserInfo.setId(jsonObject
										.getString("uid"));
								try {
									EMChatManager.getInstance()
											.createAccountOnServer(
													entityUserInfo.getId(),
													entityUserInfo.getId());
								} catch (EaseMobException e) {
									System.out.println(e.toString());
								}
								result = entityUserInfo;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityUserInfo) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param userId
	 *            �û�id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void UserInfo(String userId,
			final OnPostRequest<EntityUserInfo> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_USER_INFO)
					.MultipartString("uid", userId).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityUserInfo entityUserInfo = new EntityUserInfo();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityUserInfo.setS(jsonObject.getInt("s"));
							if (0 == entityUserInfo.getS()) {
								JSONObject obj = jsonObject
										.getJSONObject("data");
								entityUserInfo.setId(obj.getString("uid"));
								entityUserInfo.setName(obj.getString("uname"));
								entityUserInfo
										.setPassword(obj.getString("pwd"));
								entityUserInfo.setNickName(obj
										.getString("nickname"));
								entityUserInfo.setHeader(obj
										.getString("header"));
								entityUserInfo.setSex(obj.getString("sex"));
								entityUserInfo.setPhone(obj.getString("phone"));
								entityUserInfo.setRegisterTime(obj
										.getString("reg_time"));
								entityUserInfo.setThird_part(obj
										.getString("third_party"));
								entityUserInfo.setOpenId(obj
										.getString("openid"));
								result = entityUserInfo;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityUserInfo) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �޸�����
	 * 
	 * @param userId
	 *            �û�ID
	 * @param oldPassword
	 *            ԭʼ����
	 * @param newPassword
	 *            ������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void EditPassword(String userId, String oldPassword,
			String newPassword, final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_EDIT_PASSWORD)
					.MultipartString("uid", userId)
					.MultipartString("old_pwd", oldPassword)
					.MultipartString("new_pwd", newPassword)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 == entityBase.getS()) {
								result = entityBase;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �޸��û���Ϣ
	 * 
	 * @param userId
	 *            �û�id
	 * @param file
	 *            ͷ���ļ�
	 * @param sex
	 *            �Ա�
	 * @param nickName
	 *            �ǳ�
	 * @param phone
	 *            �ֻ���
	 * @param onPostRequest
	 *            �ص�
	 */
	@SuppressLint("UseSparseArrays")
	public void EditUserInfo(final Context context, final String userId,
			File file, final String sex, final String nickName,
			final String phone,
			final OnPostRequest<EntityUserInfo> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_EDIT_USER_INFO)
					.MultipartString("uid", userId).MultipartString("sex", sex)
					.MultipartString("nickname", nickName)
					.MultipartString("phone", phone).getMultipartEntity();
			// ͷ��
			if (null != file) {
				multipartEntity.addPart("upfile", new FileBody(file,
						"image/jpeg"));
			}
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityUserInfo entityUserInfo = new EntityUserInfo();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityUserInfo.setS(jsonObject.getInt("s"));
							if (0 == entityUserInfo.getS()) {
								entityUserInfo = Utile.getUserInfo(context);
								entityUserInfo.setHeader(jsonObject
										.getString("header"));
								entityUserInfo.setNickName(nickName);
								entityUserInfo.setPhone(phone);
								entityUserInfo.setSex(sex);
								result = entityUserInfo;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityUserInfo) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��ҳ����ͼ
	 * 
	 * @param pageIndex
	 *            ��ǰҳ��
	 * @param displayNumber
	 *            ��ҳ��������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsFocus(String pageIndex, String displayNumber,
			final OnPostRequest<ArrayList<EntityGoodsFocus>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_FOCUS).PageIndex(pageIndex)
					.DisplayNumber(displayNumber).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsFocus> entityGoodsFocus = new ArrayList<EntityGoodsFocus>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsFocus entity = new EntityGoodsFocus();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setTitle(obj.getString("title"));
									entity.setPrice(obj.getString("price"));
									entity.setImage(obj
											.getString("recommend_img"));
									entity.setTotal(jsonObject.getInt("total"));

									entityGoodsFocus.add(entity);
								}
								result = entityGoodsFocus;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsFocus>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �����б�
	 * 
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsClassify(
			final OnPostRequest<ArrayList<EntityGoodsClassify>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging().Api(
					Utile.HTTP_API_GOODS_CLASSIFY).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsClassify> entityClassifies = new ArrayList<EntityGoodsClassify>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsClassify entity = new EntityGoodsClassify();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("cid"));
									entity.setName(obj.getString("cname"));
									entity.setIcon(obj.getString("icon"));
									entity.setIconHeight(obj.getInt("height"));
									entity.setIconWidth(obj.getInt("width"));

									entityClassifies.add(entity);
								}
								result = entityClassifies;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsClassify>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ������Ʒ�б�
	 * 
	 * @param cid
	 *            ���id
	 * @param userId
	 *            �û�id
	 * @param pageIndex
	 *            ҳ��
	 * @param displayNumber
	 *            ��ҳ��������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsSelling(String cid, String userId, String pageIndex,
			String displayNumber,
			final OnPostRequest<ArrayList<EntityGoodsSelling>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_SELLING)
					.MultipartString("cid", cid).MultipartString("uid", userId)
					.PageIndex(pageIndex).DisplayNumber(displayNumber)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsSelling> entityGoodsSellings = new ArrayList<EntityGoodsSelling>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsSelling entity = new EntityGoodsSelling();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setTitle(obj.getString("title"));
									entity.setMessage(obj.getString("describe"));
									entity.setPrice(obj.getString("price"));
									entity.setPublishTimeTemp(obj
											.getString("publish_time"));
									entity.setCollectNum(obj
											.getString("collect_num"));
									entity.setCommentNum(obj
											.getString("comment_num"));
									entity.setUserId(obj.getString("uid"));
									entity.setUserHeader(obj
											.getString("header"));
									entity.setUserName(obj.getString("uname"));
									entity.setUserNickName(obj
											.getString("nickname"));
									entity.setCollect(obj.getInt("is_collect"));
									entity.setEntityImages(entityImages(obj
											.getJSONArray("img")));
									entity.setTotal(jsonObject.getInt("total"));

									entityGoodsSellings.add(entity);
								}
								result = entityGoodsSellings;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsSelling>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��Ʒ����
	 * 
	 * @param id
	 *            ��Ʒid
	 * @param userId
	 *            �û�id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsInfo(String id, String userId,
			final OnPostRequest<EntityGoodsInfo> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_INFO).MultipartString("id", id)
					.MultipartString("uid", userId).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityGoodsInfo entityGoodsInfo = new EntityGoodsInfo();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityGoodsInfo.setS(jsonObject.getInt("s"));
							if (0 == entityGoodsInfo.getS()) {
								JSONObject obj = jsonObject
										.getJSONObject("data");
								entityGoodsInfo.setId(obj.getString("id"));
								entityGoodsInfo
										.setTitle(obj.getString("title"));
								entityGoodsInfo.setMessage(obj
										.getString("describe"));
								entityGoodsInfo
										.setPrice(obj.getString("price"));
								entityGoodsInfo.setOriginalPrice(obj
										.getString("original_price"));
								entityGoodsInfo.setPublishTimeTemp(obj
										.getString("publish_time"));
								entityGoodsInfo.setCollectNum(obj
										.getString("collect_num"));
								entityGoodsInfo.setCommentNum(obj
										.getString("comment_num"));
								entityGoodsInfo.setUserNickName(obj
										.getString("nickname"));
								entityGoodsInfo.setUserId(obj.getString("uid"));
								entityGoodsInfo.setUserHeader(obj
										.getString("header"));
								entityGoodsInfo.setCollect(obj
										.getInt("is_collect"));
								entityGoodsInfo
										.setEntityImages(entityImages(obj
												.getJSONArray("img")));
								result = entityGoodsInfo;
							} else {
								result = jsonObject.getString("e");

							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityGoodsInfo) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��Ʒ����
	 * 
	 * @param id
	 *            ��Ʒid
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsComment(String id,
			final OnPostRequest<ArrayList<EntityGoodsComment>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_COMMENT)
					.MultipartString("id", id).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsComment> entityGoodsComments = new ArrayList<EntityGoodsComment>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsComment entity = new EntityGoodsComment();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setMessage(obj.getString("content"));
									entity.setTimeTemp(obj
											.getString("dateline"));
									entity.setUserId(obj.getString("uid"));
									entity.setUserName(obj.getString("uname"));
									entity.setUserNickName(obj
											.getString("nickname"));
									entity.setUserHeader(obj
											.getString("header"));

									entityGoodsComments.add(entity);
								}
								result = entityGoodsComments;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsComment>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��Ʒ����
	 * 
	 * @param keyWord
	 *            ��Ʒ�����ؼ���
	 * @param orderBy
	 *            ���򷽷�
	 * @param userId
	 *            �û�id
	 * @param pageIndex
	 *            ҳ��
	 * @param displayNumber
	 *            ��ҳ��������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsSearch(String keyWord, String orderBy, String userId,
			String pageIndex, String displayNumber,
			final OnPostRequest<ArrayList<EntityGoodsSearch>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_SEARCH)
					.MultipartString("keyword", keyWord)
					.MultipartString("order_c", orderBy)
					.MultipartString("uid", userId).PageIndex(pageIndex)
					.DisplayNumber(displayNumber).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsSearch> entityGoodsSearchs = new ArrayList<EntityGoodsSearch>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsSearch entity = new EntityGoodsSearch();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setTitle(obj.getString("title"));
									entity.setMessage(obj.getString("describe"));
									entity.setPrice(obj.getString("price"));
									entity.setCollectNum(obj
											.getString("collect_num"));
									entity.setCommentNum(obj
											.getString("comment_num"));
									entity.setPublishTimeTemp(obj
											.getString("publish_time"));
									entity.setCollect(obj.getInt("is_collect"));
									entity.setEntityImages(entityImages(obj
											.getJSONArray("img")));
									entity.setTotal(jsonObject.getInt("total"));

									entityGoodsSearchs.add(entity);
								}
								result = entityGoodsSearchs;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsSearch>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ����Ʒ�б�
	 * 
	 * @param pageIndex
	 *            ҳ��
	 * @param displayNumber
	 *            ��ҳ��������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void GoodsBuying(String pageIndex, String displayNumber,
			final OnPostRequest<ArrayList<EntityGoodsBuying>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_BUYING).PageIndex(pageIndex)
					.DisplayNumber(displayNumber).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsBuying> entityGoodsBuyings = new ArrayList<EntityGoodsBuying>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsBuying entity = new EntityGoodsBuying();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setTitle(obj.getString("title"));
									entity.setMessage(obj.getString("describe"));
									entity.setPrice(obj.getString("price"));
									entity.setPublishTimeTemp(obj
											.getString("publish_time"));
									entity.setUserNickName(obj
											.getString("nickname"));
									entity.setUserId(obj.getString("uid"));
									entity.setUserHeader(obj
											.getString("header"));
									entity.setTotal(jsonObject.getInt("total"));

									entityGoodsBuyings.add(entity);
								}
								result = entityGoodsBuyings;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsBuying>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �󹺷���
	 * 
	 * @param title
	 *            ����
	 * @param message
	 *            ����
	 * @param price
	 *            �۸�
	 * @param userId
	 *            �û�id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void PublishBuying(String title, String message, String price,
			String userId, final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_PUBLISH_BUYING)
					.MultipartString("title", title)
					.MultipartString("describe", message)
					.MultipartString("price", price)
					.MultipartString("uid", userId).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ���ֻ�����
	 * 
	 * @param userId
	 *            �û�id
	 * @param cid
	 *            ����id
	 * @param title
	 *            ����
	 * @param message
	 *            ����
	 * @param price
	 *            �۸�
	 * @param originalPrice
	 *            ԭʼ�۸�
	 * @param files
	 *            ͼƬ
	 * @param onPostRequest
	 *            �ص�
	 */
	public void PublishSelling(String userId, String cid, String title,
			String message, String price, String originalPrice,
			HashMap<Integer, File> files,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_PUBLISH_SELLING)
					.MultipartString("uid", userId).MultipartString("cid", cid)
					.MultipartString("title", title)
					.MultipartString("describe", message)
					.MultipartString("price", price)
					.MultipartString("original_price", originalPrice)
					.getMultipartEntity();
			// ����ͼƬ����
			@SuppressWarnings("rawtypes")
			Iterator iterator = files.keySet().iterator();
			while (iterator.hasNext()) {
				multipartEntity.addPart("pic[]",
						new FileBody(files.get(iterator.next()), "image/jpeg"));
			}
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								entityBase.setSid(jsonObject.getString("sid"));
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �û�������������Ʒ�б�
	 * 
	 * @param userId
	 *            �û�id
	 * @param pageIndex
	 *            ҳ��
	 * @param displayNumber
	 *            ��ҳ��������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void UserGoodsSelling(String userId, String pageIndex,
			String displayNumber,
			final OnPostRequest<ArrayList<EntityGoodsSelling>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_SELLING_MY)
					.MultipartString("uid", userId).PageIndex(pageIndex)
					.DisplayNumber(displayNumber).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsSelling> entityGoodsSellings = new ArrayList<EntityGoodsSelling>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsSelling entity = new EntityGoodsSelling();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setCollectNum(obj
											.getString("collect_num"));
									entity.setCommentNum(obj
											.getString("comment_num"));
									entity.setPrice(obj.getString("price"));
									entity.setEntityImages(entityImages(obj
											.getJSONArray("img")));
									entity.setTotal(jsonObject.getInt("total"));
									entity.setCid(obj.getString("cid"));
									entity.setTitle(obj.getString("title"));
									entity.setMessage(obj.getString("describe"));
									entity.setOriginPrice(obj
											.getString("original_price"));

									entityGoodsSellings.add(entity);
								}
								result = entityGoodsSellings;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsSelling>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �û������󹺵���Ʒ�б�
	 * 
	 * @param userId
	 *            �û�id
	 * @param pageIndex
	 *            ҳ��
	 * @param displayNumber
	 *            ��ҳ��������
	 * @param onPostRequest
	 *            �ص�
	 */
	public void UserGoodsBuying(String userId, String pageIndex,
			String displayNumber,
			final OnPostRequest<ArrayList<EntityGoodsBuying>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_GOODS_BUYING_MY)
					.MultipartString("uid", userId).PageIndex(pageIndex)
					.DisplayNumber(displayNumber).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityGoodsBuying> entityGoodsBuyings = new ArrayList<EntityGoodsBuying>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityGoodsBuying entity = new EntityGoodsBuying();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setId(obj.getString("id"));
									entity.setTitle(obj.getString("title"));
									entity.setMessage(obj.getString("describe"));
									entity.setPrice(obj.getString("price"));
									entity.setPublishTimeTemp(obj
											.getString("publish_time"));
									entity.setTotal(jsonObject.getInt("total"));

									entityGoodsBuyings.add(entity);
								}
								result = entityGoodsBuyings;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityGoodsBuying>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ����ղ�
	 * 
	 * @param sid
	 *            ��Ʒid
	 * @param userId
	 *            �û�id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void CollectAdd(String sid, String userId,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_COLLECT_ADD)
					.MultipartString("uid", userId).MultipartString("sid", sid)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ȡ���ղ�
	 * 
	 * @param sid
	 *            ��Ʒid
	 * @param userId
	 *            �û�id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void CollectRemove(String sid, String userId,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_COLLECT_REMOVE)
					.MultipartString("uid", userId).MultipartString("sid", sid)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �û����ղ��б�
	 * 
	 * @param userId
	 * @param onPostRequest
	 */
	public void UserGoodsCollect(String userId,
			final OnPostRequest<ArrayList<EntityUserCollect>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_COLLECT_MY)
					.MultipartString("uid", userId).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityUserCollect> entityUserCollects = new ArrayList<EntityUserCollect>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityUserCollect entity = new EntityUserCollect();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setSid(obj.getString("sid"));
									entity.setCollectNum(obj
											.getString("collect_num"));
									entity.setEntityImages(entityImages(obj
											.getJSONArray("img")));

									entityUserCollects.add(entity);
								}
								result = entityUserCollects;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityUserCollect>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��������
	 * 
	 * @param sid
	 *            ��Ʒid
	 * @param userId
	 *            �û�id
	 * @param message
	 *            ����
	 * @param onPostRequest
	 *            �ص�
	 */
	public void CommentAdd(String sid, String userId, String message,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_COMMENT_ADD)
					.MultipartString("uid", userId).MultipartString("sid", sid)
					.MultipartString("content", message).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ɾ������
	 * 
	 * @param sid
	 *            ��Ʒid
	 * @param Id
	 *            ����id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void CommentRemove(String sid, String id,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_COMMENT_REMOVE)
					.MultipartString("id", id).MultipartString("sid", sid)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �û��������б�
	 * 
	 * @param userId
	 *            �û�id
	 * @param onPostRequest
	 *            �ص�
	 */
	public void UserGoodsComment(String userId,
			final OnPostRequest<ArrayList<EntityUserComment>> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_COMMENT_MY)
					.MultipartString("uid", userId).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					ArrayList<EntityUserComment> entityUserComments = new ArrayList<EntityUserComment>();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							if (0 == jsonObject.getInt("s")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									EntityUserComment entity = new EntityUserComment();
									JSONObject obj = jsonArray.getJSONObject(i);
									entity.setMessage(obj.getString("content"));
									entity.setTimeTemp(obj
											.getString("dateline"));
									entity.setSid(obj.getString("sid"));
									entity.setCid(obj.getString("cid"));
									entity.setTitle(obj.getString("title"));
									entity.setEntityImages(entityImages(obj
											.getJSONArray("img")));

									entityUserComments.add(entity);
								}
								result = entityUserComments;
							} else {
								result = jsonObject.getString("e");
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest
									.onPostOk((ArrayList<EntityUserComment>) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ������Ʒ�޸�
	 * 
	 * @param sid
	 *            ��Ʒid
	 * @param cid
	 *            ���id
	 * @param subject
	 *            ����
	 * @param message
	 *            ����
	 * @param price
	 *            �۸�
	 * @param originalPrice
	 *            ԭ��
	 * @param onPostRequest
	 *            �ص�
	 */
	public void PublishSellingUpdate(String sid, String cid, String subject,
			String message, String price, String originalPrice,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_PUBLISH_SELLING_UPDATE)
					.MultipartString("id", sid).MultipartString("cid", cid)
					.MultipartString("title", subject)
					.MultipartString("describe", message)
					.MultipartString("price", price)
					.MultipartString("original_price", originalPrice)
					.getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ɾ��������Ʒ
	 * 
	 * @param sid
	 * @param onPostRequest
	 */
	public void PublishSellingDelete(String sid,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_PUBLISH_SELLING_DELETE)
					.MultipartString("id", sid).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * �޸�����Ʒ
	 * 
	 * @param sid
	 *            ��Ʒid
	 * @param subject
	 *            ����
	 * @param message
	 *            ����
	 * @param price
	 *            �۸�
	 * @param onPostRequest
	 *            �ص�
	 */
	public void PublishBuyingUpdate(String sid, String subject, String message,
			String price, final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_PUBLISH_BUYING_UPDATE)
					.MultipartString("id", sid)
					.MultipartString("title", subject)
					.MultipartString("describe", message)
					.MultipartString("price", price).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ɾ������Ʒ
	 * 
	 * @param sid
	 * @param onPostRequest
	 */
	public void PublishBuyingDelete(String sid,
			final OnPostRequest<EntityBase> onPostRequest) {
		try {
			// ʹ�ÿ��ٷ�װ������װһ������
			MultipartEntity multipartEntity = new packaging()
					.Api(Utile.HTTP_API_PUBLISH_BUYING_DELETE)
					.MultipartString("id", sid).getMultipartEntity();
			// ��ʼ�첽��������
			new AsyncTask<MultipartEntity, Integer, Object>() {
				@Override
				protected Object doInBackground(MultipartEntity... params) {
					Object result = null;
					EntityBase entityBase = new EntityBase();
					try {
						EntityHttpResponse entityHttpResponse = Post(
								Utile.HTTP_API_URL, params[0]);
						if (entityHttpResponse.getStatusCode() == EntityHttpResponse.STATUS_CODE_OK) {
							JSONObject jsonObject = new JSONObject(
									entityHttpResponse.getMessage());
							entityBase.setS(jsonObject.getInt("s"));
							if (0 != entityBase.getS()) {
								result = jsonObject.getString("e");
							} else {
								result = entityBase;
							}
						} else {
							result = "�ӿ�����ʧ�ܣ������룺"
									+ entityHttpResponse.getStatusCode();
						}
					} catch (ParseException e) {
						result = e.toString();
					} catch (IOException e) {
						result = e.toString();
					} catch (JSONException e) {
						result = e.toString();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Object result) {
					if (null != onPostRequest) {
						if (!(result instanceof String)) {
							onPostRequest.onPostOk((EntityBase) result);
						} else {
							onPostRequest.onPostFailure((String) result);
						}
					}
				}
			}.execute(multipartEntity);
		} catch (UnsupportedEncodingException e1) {
			if (null != onPostRequest) {
				onPostRequest.onPostFailure(e1.toString());
			}
		}
	}

	/**
	 * ��װͼƬ����
	 * 
	 * @param jsonArray
	 *            ��Ҫ������json
	 * @return ���ط�װ�õļ���
	 * @throws JSONException
	 *             �쳣�����׳�
	 */
	private ArrayList<EntityImage> entityImages(JSONArray jsonArray)
			throws JSONException {
		ArrayList<EntityImage> entityImages = new ArrayList<EntityImage>();
		for (int i = 0; i < jsonArray.length(); i++) {
			EntityImage entityImage = new EntityImage();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			entityImage.setImage(jsonObject.getString("img"));
			entityImage.setImageHeight(jsonObject.getInt("height"));
			entityImage.setImageWidth(jsonObject.getInt("width"));
			entityImages.add(entityImage);
		}
		return entityImages;
	}

	/**
	 * ���ٷ�װ��
	 * 
	 * @author Administrator
	 * 
	 */
	public class packaging {
		private String key_api = "api";
		private String key_pageIndex = "pageIndex";
		private String key_displayNumber = "displayNumber";

		// ��ʼ����װ
		private MultipartEntity multipartEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);

		// �õ���װ
		public MultipartEntity getMultipartEntity() {
			return multipartEntity;
		}

		// ���÷�װ
		public void setMultipartEntity(MultipartEntity multipartEntity) {
			this.multipartEntity = multipartEntity;
		}

		// �����ͨ�ַ���
		public packaging MultipartString(String key, String str)
				throws UnsupportedEncodingException {
			multipartEntity.addPart(key,
					new StringBody(str, Charset.forName("UTF-8")));
			return this;
		}

		// ���api��������
		public packaging Api(String api) throws UnsupportedEncodingException {
			multipartEntity.addPart(key_api,
					new StringBody(api, Charset.forName("UTF-8")));
			return this;
		}

		// ���pageIndex��������
		public packaging PageIndex(String pageIndex)
				throws UnsupportedEncodingException {
			multipartEntity.addPart(key_pageIndex, new StringBody(pageIndex,
					Charset.forName("UTF-8")));
			return this;
		}

		// ���displayNumber��������
		public packaging DisplayNumber(String displayNumber)
				throws UnsupportedEncodingException {
			multipartEntity.addPart(key_displayNumber, new StringBody(
					displayNumber, Charset.forName("UTF-8")));
			return this;
		}
	}

	/**
	 * post���󷽷�
	 * 
	 * @param url
	 *            ����ӿڵ�ַ
	 * @param multipartEntity
	 *            ���͵ı�����
	 * @return ����һ����װ������code�Լ�����
	 * @throws IOException
	 * @throws ParseException
	 */
	public EntityHttpResponse Post(String url, MultipartEntity multipartEntity)
			throws ParseException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(multipartEntity);
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
		EntityHttpResponse entityHttpResponse = new EntityHttpResponse();
		entityHttpResponse.setMessage(EntityUtils.toString(httpResponse
				.getEntity()));
		entityHttpResponse.setStatusCode(httpResponse.getStatusLine()
				.getStatusCode());
		return entityHttpResponse;
	}

	/**
	 * �첽�������ʱ���ӿڻص�
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnPostRequest<obj> {
		void onPostOk(obj temp);

		void onPostFailure(String err);
	}
}