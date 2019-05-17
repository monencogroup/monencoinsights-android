package com.monenco.insights;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.Requester;

import util.IabHelper;
import util.IabResult;
import util.Purchase;


public class MainActivity extends AppCompatActivity {

    public interface PurchaseListener {
        void onFinish(String paymentId,String paymentToken, boolean success);
    }

    private Requester requester;
    private MainView mainView;
    private IabHelper helper;
    private boolean helperOk;
    private boolean purchasing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Translator.initialLanguage(this, Translator.ENGLISH);
        setContentView(R.layout.activity_main);
        if (Translator.isPersian()) {
            findViewById(R.id.main_view).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        requester = new Requester(this);
        mainView = new MainView(this);
        helper = new IabHelper(this, Settings.base64EncodedPublicKey);
        this.initialHelper();
    }

    private void initialHelper() {
        helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                helperOk = result.isSuccess();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mainView.popFragment(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (helper != null) helper.dispose();
        helper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!helper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Boolean purchace(final String paymentId, final PurchaseListener listener) {
        if (!purchasing) {
            if (helperOk) {
                purchasing = true;
                helper.launchPurchaseFlow(this, paymentId, 1001, new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase info) {
                        purchasing = false;
                        if (result.isSuccess()) {
                            String token = info.getToken();
                            listener.onFinish(paymentId, token,true);
                        } else {
                            listener.onFinish(paymentId, null,false);
                        }
                    }
                }, mainView.getConfigs().username);
                return true;
            } else {
                initialHelper();
                return false;
            }
        } else {
            return false;
        }
    }

    public MainView getMainView() {
        return mainView;
    }

    public void request(Request request) {
        requester.request(request);
    }


    public void handleOsBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mainView.popFragment(false);
    }

    public Requester getRequester() {
        return this.requester;
    }

    public void clear() {
        requester.clear();
        mainView.clear();
        if (Translator.isPersian()) {
            findViewById(R.id.main_view).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            findViewById(R.id.main_view).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        }
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(
                    activity.getCurrentFocus(), 0);
        }
    }

    public void share(String title, String body) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n\n" + body + "\n\n" + mainView.getConfigs().shareFooter);
        startActivity(Intent.createChooser(sharingIntent, "Choose..."));
    }

    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
        return 0;
    }
}
