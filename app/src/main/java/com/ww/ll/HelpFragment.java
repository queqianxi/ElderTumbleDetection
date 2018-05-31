package com.ww.ll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ww.ll.adapter.LeftListAdapter;
import com.ww.ll.base.BaseFragment;
import com.ww.ll.db.LeftList;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by Ww on 2018/5/21.
 */
public class HelpFragment extends BaseFragment{
    private ListView list;
    private List<LeftList> dataList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help,container,false);
        list = view.findViewById(R.id.help_list);
        LeftListAdapter adapter = new LeftListAdapter(LitePalApplication.getContext(), R.layout.left_list_item, dataList);
        list.setAdapter(adapter);
        return view;
    }
    @Override
    protected int setView() {
        return 0;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        LeftList leftList1 = new LeftList("手机：15061955330",R.drawable.phone);
        dataList.add(leftList1);
        LeftList leftList2 = new LeftList("邮箱:961927900@qq.com",R.drawable.email);
        dataList.add(leftList2);
        LeftList leftList3 = new LeftList("新浪微博：却浅熙ll",R.drawable.weibo);
        dataList.add(leftList3);
        LeftList leftList4 = new LeftList("学校：常州大学",R.drawable.school);
        dataList.add(leftList4);
    }
}
