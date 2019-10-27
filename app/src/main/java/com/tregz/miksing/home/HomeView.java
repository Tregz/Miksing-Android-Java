package com.tregz.miksing.home;

import com.tregz.miksing.data.work.Work;

public interface HomeView {

    void onClearItemDetails();

    void onFillItemDetails(Work work);

    void onSaved();

    void onSaveItem();

}
