package com.example.ootd.ootd_1.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ootd.ootd_1.Adapter.ImageViewAdapter;
import com.example.ootd.ootd_1.R;
import com.example.ootd.ootd_1.model.ShowImage_Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GridFragment extends Fragment {

//    String[] result = null;
//    private Context c = null;
//    private Bundle bundle;

    private ArrayList<ShowImage_Model> images = new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private ImageViewAdapter adapter;
    private RecyclerView recyclerView;

    SharedPreferences sp;

    public GridFragment() {
        // Required empty public constructor
        Log.d("GridFragment", "GRID OPEN");
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle_grid, container, false);
        sp = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.grid_recycler_view);
        recyclerView.setHasFixedSize(true);

        HashSet<String> strings;
        strings = (HashSet) sp.getStringSet("result", null);
        if (strings != null) {
            images.clear();
            Iterator it = strings.iterator();
            while (it.hasNext()) {
                images.add(new ShowImage_Model("title", (String) it.next(), ""));
            }
        }

        //그리드
        int numberOfColumns = 3;
        mGridLayoutManager = new GridLayoutManager(context, numberOfColumns);
        recyclerView.setLayoutManager(mGridLayoutManager);

        adapter = new ImageViewAdapter(context, images);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return view;
    }

    public void updateView() {
        images.clear();

        if (sp != null) {
            HashSet<String> strings = (HashSet) sp.getStringSet("result", null);
            if (strings != null) {
                Iterator it = strings.iterator();
                while (!it.hasNext()) {
                    images.add(new ShowImage_Model("title", (String) it.next(), "memo"));
                }
                adapter.notifyDataSetChanged();
            }
        }

    }
}

    /*사용할 데이터를 미리 준비해 놓는다. 준비하는 형태에 따라 구현방법이 조금 달라 질 수 있다.
        1. 수동으로 Item을 입력해서 추가 하도록 할 수 있다.
        2. 온라인에서 DB에서 일괄 가져 올 수 도 있다.
        어떻게든 itmes 배열에 데이터를 형식에 맞게 넣어 어답터와 연결만 하면 화면에 내용이 출력될것이다.
        */
    /*
    public void initDataset() {
        images.clear();
        images.add(new ShowImage_Model("01", "https://firebasestorage.googleapis.com/v0/b/ootd1-954e1.appspot.com/o/image%2F2.jpg?alt=media&token=16ae082a-a1ed-4e0e-994c-a68fbfa72212", "hi babo"));
        images.add(new ShowImage_Model("02", "https://firebasestorage.googleapis.com/v0/b/ootd1-954e1.appspot.com/o/image%2F4.jpg?alt=media&token=47f3f167-9d89-414d-ba74-527fac0daac3", "hi babo2"));
        images.add(new ShowImage_Model("03", "https://firebasestorage.googleapis.com/v0/b/ootd1-954e1.appspot.com/o/image%2F6.jpg?alt=media&token=f34d4145-a520-4471-8e94-2cf62555e2a0", "hi babo3"));
    }

    public void initDataset_notnull(String[] imgURLArray, ImageViewAdapter adapter) {
        images.clear();

        String[] imageURL = imgURLArray;

        if(imageURL != null) {
            for (int i = 0; i < imageURL.length; i++) {
                images.add(new ShowImage_Model("image", imageURL[i], "memo"));
                Log.d("qwer", imageURL[i]);
            }
        }
        adapter.notifyDataSetChanged();

    }

    public void changeDataSet(String[] result) {
        images.clear();
        System.out.println("dd");
        System.out.println(result.length);

        for (int i = 0; i < result.length; i++) {
            System.out.println("img:"+result[i]);
            images.add(new ShowImage_Model("image", result[i], "memo"));
        }
        for(int i=0;i<images.size();i++) {
            System.out.println("img2:"+ images.get(i).getImage());
        }
        System.out.println("adapter" + adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Toast.makeText(context, "dsds", Toast.LENGTH_SHORT).show();
    }
     */
