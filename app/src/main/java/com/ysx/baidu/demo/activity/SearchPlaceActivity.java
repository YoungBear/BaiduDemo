package com.ysx.baidu.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.ysx.baidu.demo.R;
import com.ysx.baidu.demo.adapter.PlaceSugAdapter;
import com.ysx.baidu.demo.bean.BaiduPlaceSugBean;
import com.ysx.baidu.demo.bean.BaiduPlaceSugBean.ResultBean;
import com.ysx.baidu.demo.constant.PlaceSugConstant;
import com.ysx.baidu.demo.constant.SearchPlaceConstant;
import com.ysx.baidu.demo.utils.SnUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * 搜索地理位置
 *
 * @author ysx
 *         date 2017/10/27
 */
public class SearchPlaceActivity extends AppCompatActivity {
    private static final String TAG = "SearchPlaceActivity";
    @BindView(R.id.edit_search)
    EditText mEditSearch;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.recycler_view_result)
    RecyclerView mRecyclerViewResult;

    private Context mContext;

    private PlaceSugAdapter mPlaceSugAdapter;
    private List<ResultBean> mData = new ArrayList<>();

    private ProgressDialog mDialog;
    private LatLng mCurrentLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        ButterKnife.bind(this);
        mContext = this;
        initRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                String keyword = mEditSearch.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    Toast.makeText(mContext, getString(R.string.search_hint), Toast.LENGTH_SHORT).show();
                } else {
                    doPlaceSearch(keyword);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化地市搜索结果RecyclerView
     */
    private void initRecyclerView() {
        mPlaceSugAdapter = new PlaceSugAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewResult.setLayoutManager(layoutManager);
        mRecyclerViewResult.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_divider));
        mRecyclerViewResult.addItemDecoration(dividerItemDecoration);
        mRecyclerViewResult.setAdapter(mPlaceSugAdapter);
        mPlaceSugAdapter.setOnItemClickListener(new PlaceSugAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "Result onItemClick, position: " + position);
                backToMapActivity(mData.get(position));
            }
        });
    }

    /**
     * @param resultBean 百度位置信息
     *                   返回到GisActivity
     */
    private void backToMapActivity(BaiduPlaceSugBean.ResultBean resultBean) {
        Intent data = new Intent();
        data.putExtra(SearchPlaceConstant.EXTRA_LATITUDE, resultBean.getLocation().getLat());
        data.putExtra(SearchPlaceConstant.EXTRA_LONGITUDE, resultBean.getLocation().getLng());
        setResult(Activity.RESULT_OK, data);
        Log.d(TAG, "backToGisActivity:"
                + "\nlatitude: " + resultBean.getLocation().getLat()
                + "\nlongitude: " + resultBean.getLocation().getLng());

        finish();
    }

    private void showLoadingDialog() {
        if (mDialog == null) {
            mDialog = ProgressDialog.show(this, null, getResources().getString(R.string.dialog_loading));
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        mDialog.dismiss();
                    }
                    return false;
                }
            });
        } else {
            mDialog.show();
        }
    }

    private void dismissLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 百度地理位置搜索
     *
     * @param keyword 关键词
     */
    private void doPlaceSearch(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this,
                    getResources().getString(R.string.search_hint)
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put(PlaceSugConstant.KEY_KEYWORD, keyword);
        paramsMap.put(PlaceSugConstant.KEY_REGION, PlaceSugConstant.VALUE_REGION);


        if (mCurrentLatLng != null) {
            String locationValue = mCurrentLatLng.latitude + "," + mCurrentLatLng.longitude;
            Log.d(TAG, "PlaceSug, locationValue: " + locationValue);
            paramsMap.put(PlaceSugConstant.KEY_LOCATION, locationValue);
        }

        paramsMap.put(PlaceSugConstant.KEY_OUTPUT, PlaceSugConstant.VALUE_OUTPUT_JSON);
        paramsMap.put(PlaceSugConstant.KEY_AK, PlaceSugConstant.VALUE_AK);
        paramsMap.put(PlaceSugConstant.KEY_TIME_STAMP, String.valueOf(System.currentTimeMillis()));

        String sn = SnUtils.getSnValue(PlaceSugConstant.SN_HOST, paramsMap);
        Log.d(TAG, "doPlaceSearch: sn: " + sn);
        /**
         * sn值要放在最后
         */
        paramsMap.put(PlaceSugConstant.KEY_SN, sn);

        String url = null;
        try {
            url = PlaceSugConstant.BAIDU_PLACE_SUG_HOST + "?" + SnUtils.toQueryString(paramsMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "PlaceSug: url: " + url);

        OkHttpClient okHttpClient = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissLoadingDialog();
                Log.d(TAG, "onFailure: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, getString(R.string.search_failed), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                Gson gson = new Gson();
                final BaiduPlaceSugBean placeSugBean = gson.fromJson(response.body().string(), BaiduPlaceSugBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        Log.d(TAG, "run: placeSugBean.getStatus(): " + placeSugBean.getStatus());
                        if (placeSugBean.getStatus() == 0) {
                            mData.clear();
                            mData.addAll(placeSugBean.getResult());
                            mPlaceSugAdapter.notifyDataSetChanged();
                            if (mData.size() <= 0) {
                                Toast.makeText(mContext, getString(R.string.search_result_empty), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.search_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        showLoadingDialog();
    }
}
