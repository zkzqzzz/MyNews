package com.demo.zk.mynews.callback;

import android.view.View;

/**
 * ClassName: OnItemClickListener<p>
 * Fuction: 点击长按的接口<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public interface OnItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
