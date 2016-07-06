package com.francis.simple_mvp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.francis.simple_mvp.mvp.model.Fact;
import com.francis.simple_mvp.mvp.model.FactRestService;
import com.francis.simple_mvp.mvp.presenter.FactPresenter;
import com.francis.simple_mvp.mvp.view.FactListView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FactListView, SwipeRefreshLayout.OnRefreshListener {


    private FactPresenter presenter;
    private ProgressWheel progressWheel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FactAdapter adapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get action bar
        actionBar = getSupportActionBar();

        // load & parse json progress view
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        // refresh to load layout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // listView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FactAdapter();
        recyclerView.setAdapter(adapter);

        // MVP: presenter
        presenter = new FactPresenter();
        presenter.attachView(this); // important, must attachView before use presenter
        presenter.startLoadFacts();
    }

    @Override
    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        // use objectAnimator let progress from InVisible to Visible
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 0, 1, 1);
        progressFadeInAnim.start();
    }

    @Override
    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false); // close refresh animator

        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
    }

    @Override
    public void showError(String msg) {
// use snackbar to replace Toast to show Error message
        Snackbar.make(swipeRefreshLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showResult(List<Fact> list) {
        adapter.setFacts(list);
        String msg = String.format(getString(R.string.update_data_hint), list.size());
        Snackbar.make(swipeRefreshLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTitle(String title) {
        actionBar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }

    @Override
    public void onRefresh() {
    /**
    * 关于下拉更新这块是有歧义的，是只更新变的地方？还是清除原来的，直接更新网络上的呢？
    * 目前我选择后一种简单的做法
    */
        presenter.startLoadFacts();
    }

    private static class FactAdapter extends RecyclerView.Adapter<FactViewHolder> {

        List<Fact> facts;

        public FactAdapter() {
            facts = new ArrayList<>();
        }

        public void setFacts(List<Fact> list){
            facts.clear();
            facts.addAll(list);
            notifyDataSetChanged();
        }
        @Override
        public FactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fact_item, parent, false);
            FactViewHolder viewholder = new FactViewHolder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(FactViewHolder holder, int position) {
            Fact fact = facts.get(position);
            holder.titleView.setText(fact.getTitle());
            holder.descriptionView.setText(fact.getDescription());

            String imageUrl = fact.getImageHref();
            if (TextUtils.isEmpty(imageUrl)) {
                holder.imageView.setVisibility(View.GONE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                Glide.with(holder.imageView.getContext())
                        .load(fact.getImageHref())
                        .fitCenter()
                        .into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            return facts.size();
        }
    }

    /**
     * ViewHolder for list item
     */

    private static class FactViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView descriptionView;
        public ImageView imageView;

        public FactViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}
