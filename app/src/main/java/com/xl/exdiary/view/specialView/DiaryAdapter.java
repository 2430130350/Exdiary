package com.xl.exdiary.view.specialView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xl.exdiary.R;
import com.xl.exdiary.model.impl.Diary;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>{
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private Diary[] data;

    public DiaryAdapter(Diary[] data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noname_recyclerview, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        //将数据设置到item上
        Diary diary = data[position];
        holder.timeView.setText(diary.getStartTime());
        holder.titleView.setText(diary.getTitle());
        holder.bodyView.setText(diary.getBody());
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView timeView, titleView;
        LinedEditView bodyView;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.nonameTime);
            titleView = itemView.findViewById(R.id.nonameTitle);
            bodyView = itemView.findViewById(R.id.nonameBody);
        }
    }
}
