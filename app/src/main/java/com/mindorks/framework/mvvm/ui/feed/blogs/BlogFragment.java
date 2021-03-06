/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.mindorks.framework.mvvm.ui.feed.blogs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindorks.framework.mvvm.R;
import com.mindorks.framework.mvvm.data.model.api.BlogResponse;
import com.mindorks.framework.mvvm.databinding.FragmentBlogBinding;
import com.mindorks.framework.mvvm.di.component.ActivityComponent;
import com.mindorks.framework.mvvm.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 10/07/17.
 */

public class BlogFragment extends BaseFragment implements BlogNavigator, BlogAdapter.BlogAdapterListener {

    @Inject
    BlogViewModel mBlogViewModel;

    @Inject
    BlogAdapter mBlogAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    private FragmentBlogBinding mBinding;

    public static BlogFragment newInstance() {
        Bundle args = new Bundle();
        BlogFragment fragment = new BlogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_blog, container, false);
        View view = mBinding.getRoot();

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
        }

        mBinding.setViewModel(mBlogViewModel);

        mBlogViewModel.setNavigator(this);

        mBlogAdapter.setListener(this);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp();
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.blogRecyclerView.setLayoutManager(mLayoutManager);
        mBinding.blogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBinding.blogRecyclerView.setAdapter(mBlogAdapter);

        mBlogViewModel.fetchBlogs();
    }

    @Override
    public void onDestroyView() {
        mBlogViewModel.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void updateBlog(List<BlogResponse.Blog> blogList) {
        mBlogAdapter.addItems(blogList);
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onRetryClick() {
        mBlogViewModel.fetchBlogs();
    }
}
