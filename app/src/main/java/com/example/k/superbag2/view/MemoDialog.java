package com.example.k.superbag2.view;

import android.content.Context;
import android.drm.DrmStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.superbag2.R;
import com.flyco.dialog.widget.base.BaseDialog;

/**
 * Created by K on 2016/8/14.
 */
public class MemoDialog extends BaseDialog {
    TextView preDelete, preEdit;
    private Context context;

    private dialogClickListener listener;

    public MemoDialog(Context context) {
        super(context);
        this.context = context;
    }

    public interface dialogClickListener{
        void leftClick();
        void rightClick();
    }

    public void setOnDialogClickListener(dialogClickListener listener){
        this.listener = listener;
    }


    @Override
    public View onCreateView() {
        View preview = LayoutInflater.from(context).inflate(R.layout.preview_memo, null);
        preDelete = (TextView) preview.findViewById(R.id.pre_memo_delete_bt);
        preEdit = (TextView) preview.findViewById(R.id.pre_memo_edit_bt);
        return preview;
    }

    @Override
    public void setUiBeforShow() {

        preDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.leftClick();
                dismiss();
            }
        });

        preEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.rightClick();
                dismiss();
            }
        });
    }
}
