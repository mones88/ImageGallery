package com.etiennelawlor.imagegallery.activities;

import com.etiennelawlor.imagegallery.library.activities.ImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fastcode on 11/02/2016.
 */
public class GalleryActivity extends ImageGalleryActivity {

    class MyAdapter extends ImageGalleryAdapter<String> {

        public MyAdapter(List<String> images) {
            super(images);
        }

        @Override
        protected boolean isSelected(String item) {
            return false;
        }

        @Override
        protected void setSelected(String item, boolean selected) {

        }

        @Override
        protected String imageUrlForItem(String item) {
            return item;
        }

        @Override
        public ArrayList<String> getImagesUrl() {
            return new ArrayList<>(mImages);
        }
    }

    @Override
    protected boolean shouldPreviewImagesOnClick() {
        return true;
    }

    @Override
    protected ImageGalleryAdapter createImageGalleryAdapter() {
        List<String> images = getIntent().getExtras().getStringArrayList(ImageGalleryActivity.IMAGES_EXTRA);
        return new MyAdapter(images);
    }
}
