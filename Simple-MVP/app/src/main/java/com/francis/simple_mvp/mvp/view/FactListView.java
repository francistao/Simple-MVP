package com.francis.simple_mvp.mvp.view;

import com.francis.simple_mvp.mvp.model.Fact;

import java.util.List;

/**
 * Created by dream on 16/7/6.
 */
public interface FactListView extends MvpView{

    /**
     * show loading view
     */
    void showLoading();

    /**
     * hide loading view when finish load or exception
     */
    void hideLoading();

    /**
     * show error message
     * @param msg
     */
    void showError(String msg);

    /**
     * show list item
     * @param list
     */
    void showResult(List<Fact> list);

    /**
     * update action bar title
     * @param title
     */
    void showTitle(String title);

}
