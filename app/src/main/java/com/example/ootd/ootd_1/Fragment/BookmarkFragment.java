package com.example.ootd.ootd_1.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ootd.ootd_1.Adapter.ImageViewAdapter;
import com.example.ootd.ootd_1.R;
import com.example.ootd.ootd_1.model.BookmarkImage;
import com.example.ootd.ootd_1.model.ShowImage_Model;
import com.example.ootd.ootd_1.model.SignUpModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {


    private ArrayList<ShowImage_Model> images = new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
    ImageViewAdapter adapter;

    //파이어베이스
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mData, mKey;
    private BookmarkImage bookmarkImage_moel;

    private String image_values;


//    private FirebaseStorage storage;
//    private BookmarkImage bookmarkImage_model;
//    private String imageUrl, imageMemo, imageUrl_back5;


    public BookmarkFragment() {
        // Required empty public constructor
        Log.d("BookmarkFragment", "BOOKMARK OPEN");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle_bookmark, container, false);
        System.out.println(this.getArguments());

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        mData = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("BookmarkImage");

        //TODO: Data 가져오기

        getDataFromFirebase();
        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.bookmark_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //리니어
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        //그리드
        int numberOfColumns = 3;
        mGridLayoutManager = new GridLayoutManager(context, numberOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        adapter = new ImageViewAdapter(context, images);
        mRecyclerView.setAdapter(adapter);
        //TODO notify!
        adapter.notifyDataSetChanged();

        return view;
    }

    //데이터 값 추출
    public void getDataFromFirebase() {
        images.clear();
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        bookmarkImage_model = dataSnapshot.getValue(BookmarkImage.class);
//                        Log.d("BookmarkFragment", "ValueEventListener : " +bookmarkImage_model);
                        image_values = snapshot.getValue().toString();
                        Log.d("BookmarkFragment", "ValueEventListener : " + snapshot.getKey());
                        Log.d("BookmarkFragment", "ValueEventListener : " + image_values);
                        //문자열 추출
                        String target1 = "http://";
                        String target2 = "memo=";
                        int target_num1 = image_values.indexOf(target1);
                        int target_num2 = image_values.indexOf(target2);
                        String target_url = image_values.substring(target_num1,(image_values.substring(target_num1).indexOf(",")+target_num1));
                        String target_image = image_values.substring(target_num2+5,(image_values.substring(target_num2).indexOf("}")+target_num2));
                        System.out.println(target_url);
                        System.out.println(target_image);
                        images.add(new ShowImage_Model("", target_url, target_image));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
