package com.example.ootd.ootd_1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ootd.ootd_1.Activity.ImageCaptureActivity;
import com.example.ootd.ootd_1.Activity.PhotoCaptureActivity;
import com.example.ootd.ootd_1.R;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public BottomSheetDialog() {
    }

    public static BottomSheetDialog getInstance() {
        return new BottomSheetDialog();
    }

    private LinearLayout cameraLo;
    private LinearLayout galleryLo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
        cameraLo = view.findViewById(R.id.cameraLo);
        galleryLo = view.findViewById(R.id.galleryLo);

        cameraLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(getActivity(), PhotoCaptureActivity.class);
                startActivity(in1);
            }
        });
        galleryLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(getActivity(), ImageCaptureActivity.class);
                startActivity(in2);
            }
        });
        return view;
    }
}




