package com.marcospoerl.simplypace;

import androidx.core.app.NavUtils;
import android.view.MenuItem;

import com.marcospoerl.simplypace.views.WebViewActivity;

public class LicenseInfoActivity extends WebViewActivity {

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_license_info;
    }

    @Override
    protected int getWebViewId() {
        return R.id.license_info;
    }

    @Override
    protected int getRawResourceId() {
        return R.raw.license_info;
    }

}
