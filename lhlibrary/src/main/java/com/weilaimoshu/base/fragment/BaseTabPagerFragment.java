package com.weilaimoshu.base.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.weilaimoshu.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by liaohui on 2017/9/12.
 */

public abstract class BaseTabPagerFragment extends BaseFragment {

    @BindView(R.id.pager_indicator)
    ScrollIndicatorView pagerIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<Fragment> childFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.common_tab_pager_layout;
    }

    @Override
    protected void initViews() {
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.17f;
        pagerIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFFf2a0ea, R.color.red).setSize(selectSize, unSelectSize));
        pagerIndicator.setScrollBar(new ColorBar(activity, 0xFFf2a0ea, DensityUtil.dp2px(2.5f)));
        pagerIndicator.setSplitAuto(true);
        childFragments = getChildFragments();
        viewPager.setOffscreenPageLimit(childFragments.size());//预加载页面数量
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(pagerIndicator, viewPager);
        indicatorViewPager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return childFragments.size();
            }

            @Override
            public View getViewForTab(int position, View convertView, ViewGroup container) {
                return initTabLayout(position, convertView, container);
            }

            @Override
            public Fragment getFragmentForPage(int position) {
                return childFragments.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
                // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
                return PagerAdapter.POSITION_NONE;
            }
        });
    }

    /**
     * 获取到Tab的布局样式并初始化
     */
    protected abstract View initTabLayout(int position, View convertView, ViewGroup container);

    protected abstract
    @NonNull
    List<Fragment> getChildFragments();
}
