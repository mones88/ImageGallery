package com.etiennelawlor.imagegallery.library.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.enums.PaletteColorType;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.etiennelawlor.imagegallery.library.view.GridSpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public abstract class ImageGalleryActivity extends AppCompatActivity implements ImageGalleryAdapter.OnImageClickListener, ImageGalleryAdapter.OnImageSelectionChangedListener {

    public static final String PALETTE_COLOR_EXTRA = "palette_color_type";
    public static final String IMAGES_EXTRA = "images";

    // region Member Variables
    private PaletteColorType mPaletteColorType;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ImageGalleryAdapter mImageGalleryAdapter;
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());

        bindViews();

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mPaletteColorType = (PaletteColorType) extras.get(PALETTE_COLOR_EXTRA);
            }
        }

        setUpRecyclerView();
    }

    protected @LayoutRes int getLayoutResource() {
        return R.layout.activity_image_gallery;
    }
    // endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    // region ImageGalleryAdapter.OnImageClickListener Methods
    @Override
    public void onImageClick(int position) {
        if (shouldPreviewImagesOnClick()) {
            Intent intent = new Intent(ImageGalleryActivity.this, FullScreenImageGalleryActivity.class);
            intent.putStringArrayListExtra("images", mImageGalleryAdapter.getImagesUrl());
            intent.putExtra("position", position);
            if (mPaletteColorType != null) {
                intent.putExtra("palette_color_type", mPaletteColorType);
            }
            startActivity(intent);
        }
    }

    @Override
    public void onImageSelectionChanged(int position, boolean checked) {

    }

    protected abstract boolean shouldPreviewImagesOnClick();

    // endregion

    // region Helper Methods
    private void bindViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setUpRecyclerView() {
        int numOfColumns;
        if (ImageGalleryUtils.isInLandscapeMode(this)) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(ImageGalleryActivity.this, numOfColumns));
        mRecyclerView.addItemDecoration(new GridSpacesItemDecoration(ImageGalleryUtils.dp2px(this, 2), numOfColumns));
        mImageGalleryAdapter = createImageGalleryAdapter();
        mImageGalleryAdapter.setOnImageClickListener(this);
        mImageGalleryAdapter.setOnImageSelectionChangedListener(this);
        mRecyclerView.setAdapter(mImageGalleryAdapter);
    }

    protected abstract ImageGalleryAdapter createImageGalleryAdapter();

    // endregion
}
