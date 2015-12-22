package com.xiankezu.sirceo.activitys.old;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.AddressAdapter;
import com.xiankezu.sirceo.globals.IntentFileds;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputAddressActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = InputAddressActivity.this.getClass().getSimpleName();
    private EditText et_query;                                  //模糊查询
    private ImageButton ib_search_clear;                        //清除
    private ListView lv_search;                                 //搜索
    private View progressDialog;

    int index = 0;                                              //0,代表家;1，代表公司

    private AddressAdapter adapter;

    private SuggestionSearch mSuggestionSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_input_address_activity);
        initView();
        initData();
    }

    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            loadState(false);

            if (res == null || res.getAllSuggestions() == null) {
                return;
            }
            List<SuggestionResult.SuggestionInfo> allSuggestions = res.getAllSuggestions();
            refreshData(allSuggestions);
        }
    };

    /**
     * 得到模糊搜索的list<String>
     *
     * @param query
     */
    private void getData(String query) {
        loadState(true);

        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(et_query.getText().toString().trim())
                .city("全国"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuggestionResult.SuggestionInfo info = adapter.getItem(position);
                Intent intent = getIntent();
                intent.putExtra("city",info.city);
                intent.putExtra("district",info.district);
                intent.putExtra("key",info.key);
                InputAddressActivity.this.setResult(RESULT_OK, intent);
                InputAddressActivity.this.finish();
            }
        });
        et_query.setHint("输入地址");
    }

    private void initView() {
        progressDialog = findViewById(R.id.progressDialog);
        lv_search = (ListView) findViewById(R.id.lv_search);
        et_query = (EditText) findViewById(R.id.et_query);
        ib_search_clear = (ImageButton) findViewById(R.id.ib_search_clear);
        ib_search_clear.setOnClickListener(this);
        et_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ib_search_clear.setVisibility(View.VISIBLE);
                } else {
                    ib_search_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_query.getText().toString().length() != 0) {
                    getData(s.toString());
                }
            }
        });

        loadState(false);
    }

    /**
     * 加载状态
     *
     * @param state
     */
    private void loadState(boolean state) {
        if (state) {
            progressDialog.setVisibility(View.VISIBLE);
            lv_search.setVisibility(View.INVISIBLE);
        } else {
            progressDialog.setVisibility(View.GONE);
            lv_search.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 刷新数据
     */
    private void refreshData(List<SuggestionResult.SuggestionInfo> allSuggestions) {
        adapter = new AddressAdapter(allSuggestions, this, R.layout.item_search_bean);
        lv_search.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_back:                   //返回
                finish();
                break;
            case R.id.ib_search_clear:          //清除
                et_query.getText().clear();
                break;
        }
    }

    public static void startInputAddressActivity(Activity activity,int requestCose) {
        Intent intent = new Intent(activity, InputAddressActivity.class);
        if (requestCose == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCose);
        }
    }

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initSomething() {
		// TODO Auto-generated method stub
		
	}
}
