package com.xiankezu.sirceo.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xiankezu.sirceo.R;

import java.util.List;

public class AddressAdapter extends com.xiankezu.sirceo.adapters.CommonAdapter<SuggestionResult.SuggestionInfo> {

    public AddressAdapter(List<SuggestionResult.SuggestionInfo> mDatas, Context mContext,
                          int mItemLayoutId) {
        super(mDatas, mContext, mItemLayoutId);
    }

    @Override
    public void convert(ViewHolder viewHolder, SuggestionResult.SuggestionInfo item) {
        TextView tv_name = viewHolder.getView(R.id.tv_name);
        TextView tv_address = viewHolder.getView(R.id.tv_address);
        TextView tv_history = viewHolder.getView(R.id.tv_history);
        tv_history.setVisibility(View.INVISIBLE);

        tv_name.setText(item.key);
        tv_address.setText(item.city + " " + item.district);
    }
}
