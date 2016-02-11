package com.etiennelawlor.imagegallery.library.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by etiennelawlor on 8/20/15.
 */
public abstract class ImageGalleryAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // region Member Variables
    protected List<T> mImages;
    private int mGridItemWidth;
    private int mGridItemHeight;
    private OnImageClickListener mOnImageClickListener;
    private OnImageSelectionChangedListener mOnImageSelectionChangedListener;
    // endregion

    // region Interfaces
    public interface OnImageClickListener {
        void onImageClick(int position);
    }
    public interface OnImageSelectionChangedListener {
        void onImageSelectionChanged(int position, boolean checked);
    }
    // endregion

    // region Constructors
    public ImageGalleryAdapter(List<T> images) {
        mImages = images;
    }
    // endregion

    // region Abstract methods
    protected abstract boolean isSelected(T item);

    protected abstract void setSelected(T item, boolean selected);

    protected abstract String imageUrlForItem(T item);

    public abstract ArrayList<String> getImagesUrl();

    // endregion

    public void updateAndRefreshData(List<T> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    public ArrayList<T> getSelectedImages() {
        ArrayList<T> selectedImages = new ArrayList<>();
        for (T item : mImages) {
            if (isSelected(item))
                selectedImages.add(item);
        }
        return selectedImages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_thumbnail, viewGroup, false);
        v.setLayoutParams(getGridItemLayoutParams(v));

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ImageViewHolder holder = (ImageViewHolder) viewHolder;

        T item = mImages.get(position);
        String image = imageUrlForItem(item);
        boolean selected = isSelected(item);

        setUpImage(holder.mImageView, image);
        holder.mCheckBox.setChecked(selected);

        holder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnImageClickListener != null) {
                    int adapterPos = holder.getAdapterPosition();
                    if (adapterPos != RecyclerView.NO_POSITION) {
                        mOnImageClickListener.onImageClick(adapterPos);
                    }
                }
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    T item = mImages.get(adapterPos);
                    setSelected(item, isChecked);
                    if (mOnImageSelectionChangedListener != null) {
                        mOnImageSelectionChangedListener.onImageSelectionChanged(adapterPos, isChecked);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mImages != null) {
            return mImages.size();
        } else {
            return 0;
        }
    }

    // region Helper Methods
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.mOnImageClickListener = listener;
    }

    public void setOnImageSelectionChangedListener(OnImageSelectionChangedListener listener) {
        this.mOnImageSelectionChangedListener = listener;
    }

    private ViewGroup.LayoutParams getGridItemLayoutParams(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int screenWidth = ImageGalleryUtils.getScreenWidth(view.getContext());
        int numOfColumns;
        if (ImageGalleryUtils.isInLandscapeMode(view.getContext())) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        mGridItemWidth = screenWidth / numOfColumns;
        mGridItemHeight = screenWidth / numOfColumns;

        layoutParams.width = mGridItemWidth;
        layoutParams.height = mGridItemHeight;

        return layoutParams;
    }

    private void setUpImage(ImageView iv, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(mGridItemWidth, mGridItemHeight)
                    .centerCrop()
                    .into(iv);
        } else {
            iv.setImageDrawable(null);
        }
    }
    // endregion

    // region Inner Classes

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        // region Member Variables
        private final ImageView mImageView;
        private final FrameLayout mFrameLayout;
        private final CheckBox mCheckBox;
        // endregion

        // region Constructors
        public ImageViewHolder(final View view) {
            super(view);

            mCheckBox = (CheckBox) view.findViewById(R.id.cb);
            mImageView = (ImageView) view.findViewById(R.id.iv);
            mFrameLayout = (FrameLayout) view.findViewById(R.id.fl);
        }
        // endregion
    }

    // endregion
}
