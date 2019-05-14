package com.itcom202.weroom.account.onboarding.controllers.tagDescription;

public interface TagItemListener {
    void onGetAddedItem( TagModel tagModel );

    void onGetRemovedItem( TagModel model );
}
