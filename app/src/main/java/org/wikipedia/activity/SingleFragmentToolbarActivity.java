package org.wikipedia.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wikipedia.R;
import org.wikipedia.util.DimenUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


/**
 * Boilerplate for a {@link android.support.v4.app.FragmentActivity} containing a single stack of
 * Fragments, with a Toolbar overlaid on top.
 */
public abstract class SingleFragmentToolbarActivity<T extends Fragment> extends SingleFragmentActivity<T> {

    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this,
                "reklam73");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(getToolbar());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void setWordmarkVisible(boolean visible) {
        getToolbarWordmark().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @LayoutRes
    @Override
    protected int getLayout() {
        return R.layout.activity_single_fragment_with_toolbar;
    }

    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.single_fragment_toolbar);
    }

    protected View getToolbarWordmark() {
        return findViewById(R.id.single_fragment_toolbar_wordmark);
    }

    protected void setToolbarElevationDefault() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getToolbar().setElevation(DimenUtil
                    .dpToPx(DimenUtil.getDimension(R.dimen.toolbar_default_elevation)));
        }
    }

    protected void clearToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getToolbar().setElevation(0f);
        }
    }
}
