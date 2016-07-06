package com.francis.simple_mvp.mvp.presenter;

import android.text.TextUtils;

import com.francis.simple_mvp.mvp.model.Country;
import com.francis.simple_mvp.mvp.model.Fact;
import com.francis.simple_mvp.mvp.model.FactRestService;
import com.francis.simple_mvp.mvp.view.FactListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.RestAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dream on 16/7/6.
 */
public class FactPresenter implements MvpPresenter<FactListView>{

    private final static String TAG = "FactPresenter";

    private FactListView view;
    private List<Fact> facts;

    public FactPresenter() {
        facts = new ArrayList<>();
    }

    /**
     * asynchronous function to start load Fact list
     *
     */
    public void startLoadFacts() {
        if(view == null){
            return;
        }

        view.showLoading();

        // User Retrofit to get JSON object
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FactRestService.SERVICE_ENDPOINT).build();
        FactRestService restService = restAdapter.create(FactRestService.class);
        restService.getCountry()
                .map(new Func1<Country, Country>() {
                    @Override
                    public Country call(Country country) {
                        /**
                         * Use Object stream of RxJava to filter null list item
                         */
                        List<Fact> list = country.getRows();
                        if(list != null){
                            Iterator<Fact> iterator = list.iterator();
                            while(iterator.hasNext()){
                                Fact fact = iterator.next();
                                if(TextUtils.isEmpty(fact.getTitle())){
                                    iterator.remove();
                                }
                            }
                        }
                        return country;
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Country>() {
                    @Override
                    public void onCompleted() {
                        view.hideLoading();
                        view.showResult(facts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Country country) {
                        List<Fact> list = country.getRows();
                        facts.clear();
                        facts.addAll(list);
                        view.showTitle(country.getTitle());
                    }
                });
    }
    @Override
    public void attachView(FactListView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {

    }
}
