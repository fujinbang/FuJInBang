package com.fujinbang.ui.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.fujinbang.R;

/**
 * 当发生需要支付用户积分的动作时
 * 需要填写支付积分的密码（6位数字）
 */
public class PayDialog extends DialogFragment {

    private PasswordEditText editText;
    private OnPasswordFinish mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_pay, container, false);
        editText = (PasswordEditText) view.findViewById(R.id.pet_psd);
        if (mListener != null) editText.setOnFinishListener(new OnPasswordFinish() {
            @Override
            public void onFinish(boolean passwordCorrectOrNull, String psd) {
                dismiss();
                mListener.onFinish(passwordCorrectOrNull, psd);
            }
        });
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();

        return view;
    }

    public PayDialog setOnFinishListener(final OnPasswordFinish listener) {
        mListener = listener;
        if (editText != null) editText.setOnFinishListener(new OnPasswordFinish() {
            @Override
            public void onFinish(boolean passwordCorrectOrNull, String psd) {
                dismiss();
                listener.onFinish(passwordCorrectOrNull, psd);
            }
        });
        return this;
    }

    public interface OnPasswordFinish {
        void onFinish(boolean passwordCorrectOrNull, String psd);
    }
}


