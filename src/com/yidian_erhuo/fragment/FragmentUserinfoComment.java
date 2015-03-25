package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.widget.ListView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterUserinfoComment;
import com.yidian_erhuo.adapter.AdapterUserinfoComment.delete;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityUserComment;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshListView;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("ClickableViewAccessibility")
public class FragmentUserinfoComment extends FragmentBase implements delete,
		OnRefreshListener<ListView> {
	private String uid = "";

	private ListView listview;
	private FrameLoading frameLoading;
	private PullToRefreshListView pullToRefreshListView;
	private AdapterUserinfoComment adapterUserinfoComment;

	@Override
	public void initView() {
		setContentView(R.layout.fragment_userinfo_list);

		frameLoading = new FrameLoading(getActivity(), contentView);
		pullToRefreshListView = (PullToRefreshListView) contentView
				.findViewById(R.id.pulltorefresh_userinfo);
		adapterUserinfoComment = new AdapterUserinfoComment(getActivity());
		adapterUserinfoComment.setOnDelete(this);
		pullToRefreshListView.setPullLoadEnabled(false);
		pullToRefreshListView.setOnRefreshListener(this);
		pullToRefreshListView.setPullRefreshEnabled(true);
		listview = pullToRefreshListView.getRefreshableView();
		listview.setSelector(android.R.color.transparent);
		listview.setAdapter(adapterUserinfoComment);
		listview.setFadingEdgeLength(0);
		listview.setDividerHeight(0);
	}

	@Override
	public void onRefresh() {
		frameLoading.showFrame();
		uid = Utile.getUserInfo(getActivity()).getId();
		new CloudAPI().UserGoodsComment(uid, new GoodsComment());
	}

	/**
	 * 用户评论列表
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsComment implements OnPostRequest<ArrayList<EntityUserComment>> {

		@Override
		public void onPostOk(ArrayList<EntityUserComment> temp) {
			frameLoading.hideFrame();
			adapterUserinfoComment.setEntityUserComments(temp);
			adapterUserinfoComment.notifyDataSetChanged();
			pullToRefreshListView.onPullUpRefreshComplete();
			pullToRefreshListView.onPullDownRefreshComplete();
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().UserGoodsComment(uid, new GoodsComment());
		}
	}

	@Override
	public void onDelete(final int id) {
		Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setMessage("是否删除评论?");
		dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "评论删除中", Toast.LENGTH_SHORT)
						.show();
				new CloudAPI().CommentRemove(adapterUserinfoComment
						.getEntityUserComments().get(id).getSid(),
						adapterUserinfoComment.getEntityUserComments().get(id)
								.getCid(), new OnPostRequest<EntityBase>() {
							@Override
							public void onPostOk(EntityBase temp) {
								new CloudAPI().UserGoodsComment(uid,
										new GoodsComment());
								Toast.makeText(getActivity(), "删除成功",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onPostFailure(String err) {
								Toast.makeText(getActivity(), "删除失败",
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
		dialog.setNegativeButton("否", null);
		dialog.show();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		new CloudAPI().UserGoodsComment(uid, new GoodsComment());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}
}