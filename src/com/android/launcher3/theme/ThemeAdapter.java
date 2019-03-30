package com.android.launcher3.theme;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.launcher3.R;
import com.android.launcher3.theme.db.DbTools;
import com.android.launcher3.theme.table.ThemeTable;
import com.android.launcher3.theme.tools.ThemeIconTool;
import com.android.launcher3.theme.view.PrizeImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prize on 2018/1/23.
 */

public class ThemeAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    List<ThemeTable> mDatas;
    private LayoutInflater inflater;

    private Context mContext;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.theme_item, parent, false);
        return new MyViewHolder(view);
    }

    public ThemeAdapter(Context context, List<ThemeTable> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ThemeTable item = mDatas.get(position);
        holder.title.setText(item.name);
        if (item.iconPath != null) {
            if(item.isSelected==1) {
                holder.image.setSelected(true);
            }
            holder.image.loadImage(item.iconPath);
			//prize add by huhuan ,Click pictures to switch wallpapers,20181116-start
			holder.image.setOnClickListener(this);
			//prize add by huhuan ,Click pictures to switch wallpapers,20181116-end
            holder.image.setTag(item);
            holder.bt.setOnClickListener(this);
            holder.bt.setTag(holder);

        }

    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }


    @Override
    public void onClick(View v) {
        View vs = getRecyclerView(v);
		//prize add by huhuan ,Click pictures to switch wallpapers,20181116-start
        View parentView = (View) v.getParent();
	    if(parentView != null){
           View vv = parentView.findViewById(R.id.btn);
		    update((RecyclerView) vs, vv);
	    }
	    //update((RecyclerView) vs, v);
	    //prize add by huhuan ,Click pictures to switch wallpapers,20181116-end

    }

    protected void update(RecyclerView rv, View v) {
        MyViewHolder hold = (MyViewHolder) v.getTag();
        for (int i = 0; i < this.getItemCount(); i++) {
            View child = rv.getChildAt(i);
			//add by zhouerlong
            if(child!=null) {
                PrizeImageView img = child.findViewById(R.id.item_image);
                if (img != null) {
                    img.setSelected(false);
                    img.invalidate();
                }
			//add by zhouerlong
            }

        }
        if (hold.image != null) {
            ThemeTable item = (ThemeTable) hold.image.getTag();
            hold.image.setSelected(true);
            DbTools.updateDb(item);
            if(!item.themePath.equals(ThemeIconTool.getPath(v.getContext()))) {
                ThemeIconTool.setPath(v.getContext(),item.themePath);
//add by zhouerlong 0225
                ThemeIconTool ts = new ThemeIconTool();
                ts.setWallpaper(v.getContext());
//add by zhouerlong 0225
                Activity t = (Activity) v.getContext();
                t.finish();
            }
        }
    }

    private View getRecyclerView(View child) {
        if (child == null) {
            return null;
        }
        if (child instanceof RecyclerView) {
            return child;
        }
        child = (View) child.getParent();
        child = getRecyclerView(child);
        return child;
    }

}


class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView bt;
    public PrizeImageView image;

    public MyViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.item_title);
        image = (PrizeImageView) itemView.findViewById(R.id.item_image);
        bt = itemView.findViewById(R.id.btn);
    }
}