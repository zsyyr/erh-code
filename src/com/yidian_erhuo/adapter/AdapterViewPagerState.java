package com.yidian_erhuo.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yidian_erhuo.fragment.FragmentBase;

public class AdapterViewPagerState extends FragmentStatePagerAdapter {
	private ArrayList<FragmentBase> fragmentBases = new ArrayList<FragmentBase>();

	public ArrayList<FragmentBase> getFragmentBases() {
		return fragmentBases;
	}

	public void setFragmentBases(ArrayList<FragmentBase> fragmentBases) {
		this.fragmentBases = fragmentBases;
	}

	public AdapterViewPagerState(FragmentManager fm) {
		super(fm);
	}

	public AdapterViewPagerState(FragmentManager fm,
			ArrayList<FragmentBase> fragmentBases) {
		super(fm);
		this.fragmentBases = fragmentBases;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentBases.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentBases.size();
	}
}