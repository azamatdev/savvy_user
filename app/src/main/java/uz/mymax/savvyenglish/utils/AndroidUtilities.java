/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package uz.mymax.savvyenglish.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.webkit.MimeTypeMap;
import android.widget.EdgeEffect;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uz.mymax.savvyenglish.R;
import uz.mymax.savvyenglish.SavvyApplication;

public class AndroidUtilities {

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    private static int prevOrientation = -10;
    private static boolean waitingForSms = false;
    private static boolean waitingForCall = false;
    private static final Object smsLock = new Object();
    private static final Object callLock = new Object();

    public static int statusBarHeight = 0;
    public static boolean firstConfigurationWas;
    public static float density = 1;
    public static Point displaySize = new Point();
    public static float screenRefreshRate = 60;
    public static int roundMessageSize;
    public static int roundMessageInset;
    public static boolean incorrectDisplaySizeFix;
    public static Integer photoSize = null;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int leftBaseline;
    public static boolean usingHardwareInput;
    public static boolean isInMultiwindow;

    public static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    public static AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    public static OvershootInterpolator overshootInterpolator = new OvershootInterpolator();

    private static Boolean isTablet = null;
    private static int adjustOwnerClassGuid = 0;

    private static Paint roundPaint;
    private static RectF bitmapRect;

    public static Pattern WEB_URL = null;

    static {
        try {
            final String GOOD_IRI_CHAR = "a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF";
            final Pattern IP_ADDRESS = Pattern.compile(
                    "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                            + "|[1-9][0-9]|[0-9]))");
            final String IRI = "[" + GOOD_IRI_CHAR + "]([" + GOOD_IRI_CHAR + "\\-]{0,61}[" + GOOD_IRI_CHAR + "]){0,1}";
            final String GOOD_GTLD_CHAR = "a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF";
            final String GTLD = "[" + GOOD_GTLD_CHAR + "]{2,63}";
            final String HOST_NAME = "(" + IRI + "\\.)+" + GTLD;
            final Pattern DOMAIN_NAME = Pattern.compile("(" + HOST_NAME + "|" + IP_ADDRESS + ")");
            WEB_URL = Pattern.compile(
                    "((?:(http|https|Http|Https|ton|tg):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                            + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                            + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                            + "(?:" + DOMAIN_NAME + ")"
                            + "(?:\\:\\d{1,5})?)" // plus option port number
                            + "(\\/(?:(?:[" + GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
                            + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                            + "(?:\\b|$)");
        } catch (Exception e) {
//            //FileLog.e(e);
        }
    }

    static {
        leftBaseline = isTablet() ? 80 : 72;
        checkDisplaySize(SavvyApplication.context, null);
    }

    private static int[] documentIcons = {
//            R.drawable.media_doc_blue,
//            R.drawable.media_doc_green,
//            R.drawable.media_doc_red,
//            R.drawable.media_doc_yellow
    };

    private static int[] documentMediaIcons = {
//            R.drawable.media_doc_blue_b,
//            R.drawable.media_doc_green_b,
//            R.drawable.media_doc_red_b,
//            R.drawable.media_doc_yellow_b
    };

    private static boolean containsUnsupportedCharacters(String text) {
        if (text.contains("\u202C")) {
            return true;
        }
        if (text.contains("\u202D")) {
            return true;
        }
        if (text.contains("\u202E")) {
            return true;
        }
        return false;
    }

    private static class LinkSpec {
        String url;
        int start;
        int end;
    }

    private static String makeUrl(String url, String[] prefixes, Matcher matcher) {
        boolean hasPrefix = false;
        for (int i = 0; i < prefixes.length; i++) {
            if (url.regionMatches(true, 0, prefixes[i], 0, prefixes[i].length())) {
                hasPrefix = true;
                if (!url.regionMatches(false, 0, prefixes[i], 0, prefixes[i].length())) {
                    url = prefixes[i] + url.substring(prefixes[i].length());
                }
                break;
            }
        }
        if (!hasPrefix && prefixes.length > 0) {
            url = prefixes[0] + url;
        }
        return url;
    }

    private static void gatherLinks(ArrayList<LinkSpec> links, Spannable s, Pattern pattern, String[] schemes, Linkify.MatchFilter matchFilter) {
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();

            if (matchFilter == null || matchFilter.acceptMatch(s, start, end)) {
                LinkSpec spec = new LinkSpec();

                spec.url = makeUrl(m.group(0), schemes, m);
                spec.start = start;
                spec.end = end;

                links.add(spec);
            }
        }
    }

    public static final Linkify.MatchFilter sUrlMatchFilter = (s, start, end) -> {
        if (start == 0) {
            return true;
        }
        if (s.charAt(start - 1) == '@') {
            return false;
        }
        return true;
    };


    private static void pruneOverlaps(ArrayList<LinkSpec> links) {
        Comparator<LinkSpec> c = (a, b) -> {
            if (a.start < b.start) {
                return -1;
            }
            if (a.start > b.start) {
                return 1;
            }
            if (a.end < b.end) {
                return 1;
            }
            if (a.end > b.end) {
                return -1;
            }
            return 0;
        };

        Collections.sort(links, c);

        int len = links.size();
        int i = 0;

        while (i < len - 1) {
            LinkSpec a = links.get(i);
            LinkSpec b = links.get(i + 1);
            int remove = -1;

            if ((a.start <= b.start) && (a.end > b.start)) {
                if (b.end <= a.end) {
                    remove = i + 1;
                } else if ((a.end - a.start) > (b.end - b.start)) {
                    remove = i + 1;
                } else if ((a.end - a.start) < (b.end - b.start)) {
                    remove = i;
                }
                if (remove != -1) {
                    links.remove(remove);
                    len--;
                    continue;
                }
            }
            i++;
        }
    }

    public static void fillStatusBarHeight(Context context) {
        if (context == null || AndroidUtilities.statusBarHeight > 0) {
            return;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            AndroidUtilities.statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
    }

    public static int getThumbForNameOrMime(String name, String mime, boolean media) {
        if (name != null && name.length() != 0) {
            int color = -1;
            if (name.contains(".doc") || name.contains(".txt") || name.contains(".psd")) {
                color = 0;
            } else if (name.contains(".xls") || name.contains(".csv")) {
                color = 1;
            } else if (name.contains(".pdf") || name.contains(".ppt") || name.contains(".key")) {
                color = 2;
            } else if (name.contains(".zip") || name.contains(".rar") || name.contains(".ai") || name.contains(".mp3")  || name.contains(".mov") || name.contains(".avi")) {
                color = 3;
            }
            if (color == -1) {
                int idx;
                String ext = (idx = name.lastIndexOf('.')) == -1 ? "" : name.substring(idx + 1);
                if (ext.length() != 0) {
                    color = ext.charAt(0) % documentIcons.length;
                } else {
                    color = name.charAt(0) % documentIcons.length;
                }
            }
            return media ? documentMediaIcons[color] : documentIcons[color];
        }
        return media ? documentMediaIcons[0] : documentIcons[0];
    }


    public static double[] rgbToHsv(int color) {
        return rgbToHsv(Color.red(color), Color.green(color), Color.blue(color));
    }

    public static double[] rgbToHsv(int r, int g, int b) {
        double rf = r / 255.0;
        double gf = g / 255.0;
        double bf = b / 255.0;
        double max = (rf > gf && rf > bf) ? rf : (gf > bf) ? gf : bf;
        double min = (rf < gf && rf < bf) ? rf : (gf < bf) ? gf : bf;
        double h, s;
        double d = max - min;
        s = max == 0 ? 0 : d / max;
        if (max == min) {
            h = 0;
        } else {
            if (rf > gf && rf > bf) {
                h = (gf - bf) / d + (gf < bf ? 6 : 0);
            } else if (gf > bf) {
                h = (bf - rf) / d + 2;
            } else {
                h = (rf - gf) / d + 4;
            }
            h /= 6;
        }
        return new double[]{h, s, max};
    }

    public static int hsvToColor(double h, double s, double v) {
        int[] rgb = hsvToRgb(h, s, v);
        return Color.argb(0xff, rgb[0], rgb[1], rgb[2]);
    }

    public static int[] hsvToRgb(double h, double s, double v) {
        double r = 0, g = 0, b = 0;
        double i = (int) Math.floor(h * 6);
        double f = h * 6 - i;
        double p = v * (1 - s);
        double q = v * (1 - f * s);
        double t = v * (1 - (1 - f) * s);
        switch ((int) i % 6) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
                r = v;
                g = p;
                b = q;
                break;
        }
        return new int[]{(int) (r * 255), (int) (g * 255), (int) (b * 255)};
    }

    public static void requestAdjustResize(Activity activity, int classGuid) {
        requestAdjustResize(activity, classGuid, false);
    }

    public static void requestAdjustResize(Activity activity, int classGuid, boolean allowWithSmoothKeyboard) {
        if (activity == null || isTablet() || !allowWithSmoothKeyboard) {
            return;
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        adjustOwnerClassGuid = classGuid;
    }

    public static void setAdjustResizeToNothing(Activity activity, int classGuid) {
        if (activity == null || isTablet()) {
            return;
        }
        if (adjustOwnerClassGuid == classGuid) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
    }

    public static void removeAdjustResize(Activity activity, int classGuid) {
        removeAdjustResize(activity, classGuid, false);
    }

    public static void removeAdjustResize(Activity activity, int classGuid, boolean allowWithSmoothKeyboard) {
        if (activity == null || isTablet() || !allowWithSmoothKeyboard) {
            return;
        }
        if (adjustOwnerClassGuid == classGuid) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    public static void createEmptyFile(File f) {
        try {
            if (f.exists()) {
                return;
            }
            FileWriter writer = new FileWriter(f);
            writer.flush();
            writer.close();
        } catch (Throwable e) {
//            //FileLog.e(e);
        }
    }

    public static int[] toIntArray(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i);
        }
        return ret;
    }



    @SuppressLint("WrongConstant")
    public static void lockOrientation(Activity activity) {
        if (activity == null || prevOrientation != -10) {
            return;
        }
        try {
            prevOrientation = activity.getRequestedOrientation();
            WindowManager manager = (WindowManager) activity.getSystemService(Activity.WINDOW_SERVICE);
            if (manager != null && manager.getDefaultDisplay() != null) {
                int rotation = manager.getDefaultDisplay().getRotation();
                int orientation = activity.getResources().getConfiguration().orientation;

                if (rotation == Surface.ROTATION_270) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }
                } else if (rotation == Surface.ROTATION_90) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    } else {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                } else if (rotation == Surface.ROTATION_0) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                } else {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    } else {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    }
                }
            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }

    @SuppressLint("WrongConstant")
    public static void unlockOrientation(Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            if (prevOrientation != -10) {
                activity.setRequestedOrientation(prevOrientation);
                prevOrientation = -10;
            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }

    private static class VcardData {
        String name;
        ArrayList<String> phones = new ArrayList<>();
        StringBuilder vcard = new StringBuilder();
    }


    public static byte[] getStringBytes(String src) {
        try {
            return src.getBytes("UTF-8");
        } catch (Exception ignore) {

        }
        return new byte[0];
    }


    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t;
                    if (Build.VERSION.SDK_INT >= 26) {
                        Typeface.Builder builder = new Typeface.Builder(SavvyApplication.context.getAssets(), assetPath);
                        if (assetPath.contains("medium")) {
                            builder.setWeight(700);
                        }
                        if (assetPath.contains("italic")) {
                            builder.setItalic(true);
                        }
                        t = builder.build();
                    } else {
                        t = Typeface.createFromAsset(SavvyApplication.context.getAssets(), assetPath);
                    }
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
//                    if (BuildVars.LOGS_ENABLED) {
//                        //FileLog.e("Could not get typeface '" + assetPath + "' because " + e.getMessage());
//                    }
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }

    public static boolean isWaitingForSms() {
        boolean value;
        synchronized (smsLock) {
            value = waitingForSms;
        }
        return value;
    }

    public static int getShadowHeight() {
        if (density >= 4.0f) {
            return 3;
        } else if (density >= 2.0f) {
            return 2;
        } else {
            return 1;
        }
    }

    public static boolean isWaitingForCall() {
        boolean value;
        synchronized (callLock) {
            value = waitingForCall;
        }
        return value;
    }

    public static boolean showKeyboard(View view) {
        if (view == null) {
            return false;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return false;
    }

    public static String[] getCurrentKeyboardLanguage() {
        try {
            InputMethodManager inputManager = (InputMethodManager) SavvyApplication.context.getSystemService(Context.INPUT_METHOD_SERVICE);
            InputMethodSubtype inputMethodSubtype = inputManager.getCurrentInputMethodSubtype();
            String locale = null;
            if (inputMethodSubtype != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    locale = inputMethodSubtype.getLanguageTag();
                }
                if (TextUtils.isEmpty(locale)) {
                    locale = inputMethodSubtype.getLocale();
                }
            } else {
                inputMethodSubtype = inputManager.getLastInputMethodSubtype();
                if (inputMethodSubtype != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        locale = inputMethodSubtype.getLanguageTag();
                    }
                    if (TextUtils.isEmpty(locale)) {
                        locale = inputMethodSubtype.getLocale();
                    }
                }
            }
            if (TextUtils.isEmpty(locale)) {
//                locale = LocaleController.getSystemLocaleStringIso639();
//                String locale2;
//                LocaleController.LocaleInfo localeInfo = LocaleController.getInstance().getCurrentLocaleInfo();
//                locale2 = localeInfo.getBaseLangCode();
//                if (TextUtils.isEmpty(locale2)) {
//                    locale2 = localeInfo.getLangCode();
//                }
//                if (locale.contains(locale2) || locale2.contains(locale)) {
//                    if (!locale.contains("en")) {
//                        locale2 = "en";
//                    } else {
//                        locale2 = null;
//                    }
//                }
//                if (!TextUtils.isEmpty(locale2)) {
//                    return new String[]{locale.replace('_', '-'), locale2};
//                } else {
//                    return new String[]{locale.replace('_', '-')};
//                }
            } else {
                return new String[]{locale.replace('_', '-')};
            }
        } catch (Exception ignore) {

        }
        return new String[]{"en"};
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }

    public static File getCacheDir() {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            //FileLog.e(e);
        }
        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
            try {
                File file = SavvyApplication.context.getExternalCacheDir();
                if (file != null) {
                    return file;
                }
            } catch (Exception e) {
                //FileLog.e(e);
            }
        }
        try {
            File file = SavvyApplication.context.getCacheDir();
            if (file != null) {
                return file;
            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return new File("");
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static int dpr(float value) {
        if (value == 0) {
            return 0;
        }
        return Math.round(density * value);
    }

    public static int dp2(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.floor(density * value);
    }

    public static int compare(int lhs, int rhs) {
        if (lhs == rhs) {
            return 0;
        } else if (lhs > rhs) {
            return 1;
        }
        return -1;
    }

    public static float dpf2(float value) {
        if (value == 0) {
            return 0;
        }
        return density * value;
    }

    public static void checkDisplaySize(Context context, Configuration newConfiguration) {
        try {
            float oldDensity = density;
            density = context.getResources().getDisplayMetrics().density;
            float newDensity = density;
//            if (firstConfigurationWas && Math.abs(oldDensity - newDensity) > 0.001) {
////                Theme.reloadAllResources(context);
//            }
            firstConfigurationWas = true;
            Configuration configuration = newConfiguration;
            if (configuration == null) {
                configuration = context.getResources().getConfiguration();
            }
            usingHardwareInput = configuration.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    display.getSize(displaySize);
                    screenRefreshRate = display.getRefreshRate();
                }
            }
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenWidthDp * density);
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize;
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenHeightDp * density);
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize;
                }
            }
            if (roundMessageSize == 0) {
                if (AndroidUtilities.isTablet()) {
                    roundMessageSize = (int) (AndroidUtilities.getMinTabletSide() * 0.6f);
                } else {
                    roundMessageSize = (int) (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) * 0.6f);
                }
                roundMessageInset = dp(2);
            }
//            if (BuildVars.LOGS_ENABLED) {
//                FileLog.e("density = " + density + " display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
//            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }

    public static double fixLocationCoord(double value) {
        return ((long) (value * 1000000)) / 1000000.0;
    }


    public static float getPixelsInCM(float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? displayMetrics.xdpi : displayMetrics.ydpi);
    }

    public static int getMyLayerVersion(int layer) {
        return layer & 0xffff;
    }

    public static int getPeerLayerVersion(int layer) {
        return (layer >> 16) & 0xffff;
    }

    public static int setMyLayerVersion(int layer, int version) {
        return layer & 0xffff0000 | version;
    }

    public static int setPeerLayerVersion(int layer, int version) {
        return layer & 0x0000ffff | (version << 16);
    }

    public static boolean isTablet() {
        if (isTablet == null) {
            isTablet = SavvyApplication.context.getResources().getBoolean(R.bool.isTablet);
        }
        return isTablet;
    }

    public static boolean isSmallTablet() {
        float minSide = Math.min(displaySize.x, displaySize.y) / density;
        return minSide <= 700;
    }

    public static int getMinTabletSide() {
        if (!isSmallTablet()) {
            int smallSide = Math.min(displaySize.x, displaySize.y);
            int leftSide = smallSide * 35 / 100;
            if (leftSide < dp(320)) {
                leftSide = dp(320);
            }
            return smallSide - leftSide;
        } else {
            int smallSide = Math.min(displaySize.x, displaySize.y);
            int maxSide = Math.max(displaySize.x, displaySize.y);
            int leftSide = maxSide * 35 / 100;
            if (leftSide < dp(320)) {
                leftSide = dp(320);
            }
            return Math.min(smallSide, maxSide - leftSide);
        }
    }

    public static int getPhotoSize() {
        if (photoSize == null) {
            photoSize = 1280;
        }
        return photoSize;
    }

    /*public static void clearCursorDrawable(EditText editText) {
        if (editText == null) {
            return;
        }
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.setInt(editText, 0);
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }*/

    private static ContentObserver callLogContentObserver;
    private static Runnable unregisterRunnable;
    private static boolean hasCallPermissions = Build.VERSION.SDK_INT >= 23;





    private static Field mAttachInfoField;
    private static Field mStableInsetsField;


    public static Point getRealScreenSize() {
        Point size = new Point();
        try {
            WindowManager windowManager = (WindowManager) SavvyApplication.context.getSystemService(Context.WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowManager.getDefaultDisplay().getRealSize(size);
            } else {
                try {
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    size.set((Integer) mGetRawW.invoke(windowManager.getDefaultDisplay()), (Integer) mGetRawH.invoke(windowManager.getDefaultDisplay()));
                } catch (Exception e) {
                    size.set(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight());
                    //FileLog.e(e);
                }
            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return size;
    }

    public static void setEnabled(View view, boolean enabled) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setEnabled(viewGroup.getChildAt(i), enabled);
            }
        }
    }

    public static CharSequence getTrimmedString(CharSequence src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        while (src.length() > 0 && (src.charAt(0) == '\n' || src.charAt(0) == ' ')) {
            src = src.subSequence(1, src.length());
        }
        while (src.length() > 0 && (src.charAt(src.length() - 1) == '\n' || src.charAt(src.length() - 1) == ' ')) {
            src = src.subSequence(0, src.length() - 1);
        }
        return src;
    }

    public static void setViewPagerEdgeEffectColor(ViewPager viewPager, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Field field = ViewPager.class.getDeclaredField("mLeftEdge");
                field.setAccessible(true);
                EdgeEffect mLeftEdge = (EdgeEffect) field.get(viewPager);
                if (mLeftEdge != null) {
                    mLeftEdge.setColor(color);
                }

                field = ViewPager.class.getDeclaredField("mRightEdge");
                field.setAccessible(true);
                EdgeEffect mRightEdge = (EdgeEffect) field.get(viewPager);
                if (mRightEdge != null) {
                    mRightEdge.setColor(color);
                }
            } catch (Exception ignore) {

            }
        }
    }

    public static void setScrollViewEdgeEffectColor(HorizontalScrollView scrollView, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Field field = HorizontalScrollView.class.getDeclaredField("mEdgeGlowLeft");
                field.setAccessible(true);
                EdgeEffect mEdgeGlowTop = (EdgeEffect) field.get(scrollView);
                if (mEdgeGlowTop != null) {
                    mEdgeGlowTop.setColor(color);
                }

                field = HorizontalScrollView.class.getDeclaredField("mEdgeGlowRight");
                field.setAccessible(true);
                EdgeEffect mEdgeGlowBottom = (EdgeEffect) field.get(scrollView);
                if (mEdgeGlowBottom != null) {
                    mEdgeGlowBottom.setColor(color);
                }
            } catch (Exception e) {
                //FileLog.e(e);
            }
        }
    }

    public static void setScrollViewEdgeEffectColor(ScrollView scrollView, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Field field = ScrollView.class.getDeclaredField("mEdgeGlowTop");
                field.setAccessible(true);
                EdgeEffect mEdgeGlowTop = (EdgeEffect) field.get(scrollView);
                if (mEdgeGlowTop != null) {
                    mEdgeGlowTop.setColor(color);
                }

                field = ScrollView.class.getDeclaredField("mEdgeGlowBottom");
                field.setAccessible(true);
                EdgeEffect mEdgeGlowBottom = (EdgeEffect) field.get(scrollView);
                if (mEdgeGlowBottom != null) {
                    mEdgeGlowBottom.setColor(color);
                }
            } catch (Exception e) {
                //FileLog.e(e);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void clearDrawableAnimation(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        Drawable drawable;
        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }

    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_COLOR = 4;
    public static final int FLAG_TAG_URL = 8;
    public static final int FLAG_TAG_ALL = FLAG_TAG_BR | FLAG_TAG_BOLD | FLAG_TAG_URL;

    public static SpannableStringBuilder replaceTags(String str) {
        return replaceTags(str, FLAG_TAG_ALL);
    }

    public static SpannableStringBuilder replaceTags(String str, int flag, Object... args) {
        try {
            int start;
            int end;
            StringBuilder stringBuilder = new StringBuilder(str);
            if ((flag & FLAG_TAG_BR) != 0) {
                while ((start = stringBuilder.indexOf("<br>")) != -1) {
                    stringBuilder.replace(start, start + 4, "\n");
                }
                while ((start = stringBuilder.indexOf("<br/>")) != -1) {
                    stringBuilder.replace(start, start + 5, "\n");
                }
            }
            ArrayList<Integer> bolds = new ArrayList<>();
            if ((flag & FLAG_TAG_BOLD) != 0) {
                while ((start = stringBuilder.indexOf("<b>")) != -1) {
                    stringBuilder.replace(start, start + 3, "");
                    end = stringBuilder.indexOf("</b>");
                    if (end == -1) {
                        end = stringBuilder.indexOf("<b>");
                    }
                    stringBuilder.replace(end, end + 4, "");
                    bolds.add(start);
                    bolds.add(end);
                }
                while ((start = stringBuilder.indexOf("**")) != -1) {
                    stringBuilder.replace(start, start + 2, "");
                    end = stringBuilder.indexOf("**");
                    if (end >= 0) {
                        stringBuilder.replace(end, end + 2, "");
                        bolds.add(start);
                        bolds.add(end);
                    }
                }
            }
            if ((flag & FLAG_TAG_URL) != 0) {
                while ((start = stringBuilder.indexOf("**")) != -1) {
                    stringBuilder.replace(start, start + 2, "");
                    end = stringBuilder.indexOf("**");
                    if (end >= 0) {
                        stringBuilder.replace(end, end + 2, "");
                        bolds.add(start);
                        bolds.add(end);
                    }
                }
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder);
            for (int a = 0; a < bolds.size() / 2; a++) {
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), bolds.get(a * 2), bolds.get(a * 2 + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableStringBuilder;
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return new SpannableStringBuilder(str);
    }

    public static class LinkMovementMethodMy extends LinkMovementMethod {
        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                boolean result = super.onTouchEvent(widget, buffer, event);
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Selection.removeSelection(buffer);
                }
                return result;
            } catch (Exception e) {
                //FileLog.e(e);
            }
            return false;
        }
    }



    public static void shakeView(final View view, final float x, final int num) {
        if (view == null) {
            return;
        }
        if (num == 6) {
            view.setTranslationX(0);
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", dp(x)));
        animatorSet.setDuration(50);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                shakeView(view, num == 5 ? 0 : -x, num + 1);
            }
        });
        animatorSet.start();
    }

    /*public static String ellipsize(String text, int maxLines, int maxWidth, TextPaint paint) {
        if (text == null || paint == null) {
            return null;
        }
        int count;
        int offset = 0;
        StringBuilder result = null;
        TextView
        for (int a = 0; a < maxLines; a++) {
            count = paint.breakText(text, true, maxWidth, null);
            if (a != maxLines - 1) {
                if (result == null) {
                    result = new StringBuilder(count * maxLines + 1);
                }
                boolean foundSpace = false;
                for (int c = count - 1; c >= offset; c--) {
                    if (text.charAt(c) == ' ') {
                        foundSpace = true;
                        result.append(text.substring(offset, c - 1));
                        offset = c - 1;
                    }
                }
                if (!foundSpace) {
                    offset = count;
                }
                text = text.substring(0, offset);
            } else if (maxLines == 1) {
                return text.substring(0, count);
            } else {
                result.append(text.substring(0, count));
            }
        }
        return result.toString();
    }*/

    /*public static void turnOffHardwareAcceleration(Window window) {
        if (window == null || Build.MODEL == null) {
            return;
        }
        if (Build.MODEL.contains("GT-S5301") ||
                Build.MODEL.contains("GT-S5303") ||
                Build.MODEL.contains("GT-B5330") ||
                Build.MODEL.contains("GT-S5302") ||
                Build.MODEL.contains("GT-S6012B") ||
                Build.MODEL.contains("MegaFon_SP-AI")) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
    }*/


    public static void addToClipboard(CharSequence str) {
        try {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) SavvyApplication.context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("label", str);
            clipboard.setPrimaryClip(clip);
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }

    public static void addMediaToGallery(String fromPath) {
        if (fromPath == null) {
            return;
        }
        File f = new File(fromPath);
        Uri contentUri = Uri.fromFile(f);
        addMediaToGallery(contentUri);
    }

    public static void addMediaToGallery(Uri uri) {
        if (uri == null) {
            return;
        }
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            SavvyApplication.context.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            //FileLog.e(e);
        }
    }


    @SuppressLint("NewApi")
    public static String getPath(final Uri uri) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            if (isKitKat && DocumentsContract.isDocumentUri(SavvyApplication.context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(SavvyApplication.context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    switch (type) {
                        case "image":
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "video":
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "audio":
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            break;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(SavvyApplication.context, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(SavvyApplication.context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                String value = cursor.getString(column_index);
                if (value.startsWith("content://") || !value.startsWith("/") && !value.startsWith("file://")) {
                    return null;
                }
                return value;
            }
        } catch (Exception ignore) {

        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    public static boolean isAirplaneModeOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(SavvyApplication.context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(SavvyApplication.context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }


    public static String formatFileSize(long size) {
        return formatFileSize(size, false);
    }

    public static String formatFileSize(long size, boolean removeZero) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            float value = size / 1024.0f;
            if (removeZero && (value - (int) value) * 10 == 0) {
                return String.format("%d KB", (int) value);
            } else {
                return String.format("%.1f KB", value);
            }
        } else if (size < 1024 * 1024 * 1024) {
            float value = size / 1024.0f / 1024.0f;
            if (removeZero && (value - (int) value) * 10 == 0) {
                return String.format("%d MB", (int) value);
            } else {
                return String.format("%.1f MB", value);
            }
        } else {
            float value = size / 1024.0f / 1024.0f / 1024.0f;
            if (removeZero && (value - (int) value) * 10 == 0) {
                return String.format("%d GB", (int) value);
            } else {
                return String.format("%.1f GB", value);
            }
        }
    }

    public static String formatShortDuration(int duration) {
        return formatDuration(duration, false);
    }

    public static String formatLongDuration(int duration) {
        return formatDuration(duration, true);
    }

    public static String formatDuration(int duration, boolean isLong) {
        int h = duration / 3600;
        int m = duration / 60 % 60;
        int s = duration % 60;
        if (h == 0) {
            if (isLong) {
                return String.format(Locale.US, "%02d:%02d", m, s);
            } else {
                return String.format(Locale.US, "%d:%02d", m, s);
            }
        } else {
            return String.format(Locale.US, "%d:%02d:%02d", h, m, s);
        }
    }

    public static String formatDurationNoHours(int duration, boolean isLong) {
        int m = duration / 60;
        int s = duration % 60;
        if (isLong) {
            return String.format(Locale.US, "%02d:%02d", m, s);
        } else {
            return String.format(Locale.US, "%d:%02d", m, s);
        }
    }

    public static String formatShortDuration(int progress, int duration) {
        return formatDuration(progress, duration, false);
    }

    public static String formatLongDuration(int progress, int duration) {
        return formatDuration(progress, duration, true);
    }

    public static String formatDuration(int progress, int duration, boolean isLong) {
        int h = duration / 3600;
        int m = duration / 60 % 60;
        int s = duration % 60;

        int ph = progress / 3600;
        int pm = progress / 60 % 60;
        int ps = progress % 60;

        if (duration == 0) {
            if (ph == 0) {
                if (isLong) {
                    return String.format(Locale.US, "%02d:%02d / -:--", pm, ps);
                } else {
                    return String.format(Locale.US, "%d:%02d / -:--", pm, ps);
                }
            } else {
                return String.format(Locale.US, "%d:%02d:%02d / -:--", ph, pm, ps);
            }
        } else {
            if (ph == 0 && h == 0) {
                if (isLong) {
                    return String.format(Locale.US, "%02d:%02d / %02d:%02d", pm, ps, m, s);
                } else {
                    return String.format(Locale.US, "%d:%02d / %d:%02d", pm, ps, m, s);
                }
            } else {
                return String.format(Locale.US, "%d:%02d:%02d / %d:%02d:%02d", ph, pm, ps, h, m, s);
            }
        }
    }

    public static String formatVideoDuration(int progress, int duration) {
        int h = duration / 3600;
        int m = duration / 60 % 60;
        int s = duration % 60;

        int ph = progress / 3600;
        int pm = progress / 60 % 60;
        int ps = progress % 60;

        if (ph == 0 && h == 0) {
            return String.format(Locale.US, "%02d:%02d / %02d:%02d", pm, ps, m, s);
        } else {
            if (h == 0) {
                return String.format(Locale.US, "%d:%02d:%02d / %02d:%02d", ph, pm, ps, m, s);
            } else if (ph == 0) {
                return String.format(Locale.US, "%02d:%02d / %d:%02d:%02d", pm, ps, h, m, s);
            }
            else {
                return String.format(Locale.US, "%d:%02d:%02d / %d:%02d:%02d", ph, pm, ps, h, m, s);
            }
        }
    }

    public static String formatCount(int count) {
        if (count < 1000) return Integer.toString(count);

        ArrayList<String> strings = new ArrayList<>();
        while (count != 0) {
            int mod = count % 1000;
            count /= 1000;
            if (count > 0) {
                strings.add(String.format(Locale.ENGLISH, "%03d", mod));
            } else {
                strings.add(Integer.toString(mod));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = strings.size() - 1; i >= 0; i--) {
            stringBuilder.append(strings.get(i));
            if (i != 0) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }

    public static final String[] numbersSignatureArray = {"", "K", "M", "G", "T", "P"};

    public static String formatWholeNumber(int v, int dif) {
        if (v == 0) {
            return "0";
        }
        float num_ = v;
        int count = 0;
        if (dif == 0) dif = v;
        if (dif < 1000) {
            return AndroidUtilities.formatCount(v);
        }
        while (dif >= 1000 && count < numbersSignatureArray.length - 1) {
            dif /= 1000;
            num_ /= 1000;
            count++;
        }
        if (num_ < 0.1) {
            return "0";
        } else {
            if (num_ == (int) num_) {
                return String.format(Locale.ENGLISH, "%s%s", AndroidUtilities.formatCount((int) num_), numbersSignatureArray[count]);
            } else {
                return String.format(Locale.ENGLISH, "%.1f%s", (int) (num_ * 10) / 10f, numbersSignatureArray[count]);
            }
        }
    }

    public static byte[] decodeQuotedPrintable(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            final int b = bytes[i];
            if (b == '=') {
                try {
                    final int u = Character.digit((char) bytes[++i], 16);
                    final int l = Character.digit((char) bytes[++i], 16);
                    buffer.write((char) ((u << 4) + l));
                } catch (Exception e) {
                    //FileLog.e(e);
                    return null;
                }
            } else {
                buffer.write(b);
            }
        }
        byte[] array = buffer.toByteArray();
        try {
            buffer.close();
        } catch (Exception e) {
            //FileLog.e(e);
        }
        return array;
    }

    public static boolean copyFile(InputStream sourceFile, File destFile) throws IOException {
        OutputStream out = new FileOutputStream(destFile);
        byte[] buf = new byte[4096];
        int len;
        while ((len = sourceFile.read(buf)) > 0) {
            Thread.yield();
            out.write(buf, 0, len);
        }
        out.close();
        return true;
    }

    public static boolean copyFile(File sourceFile, File destFile) throws IOException {
        if (sourceFile.equals(destFile)) {
            return true;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        try (FileInputStream source = new FileInputStream(sourceFile); FileOutputStream destination = new FileOutputStream(destFile)) {
            destination.getChannel().transferFrom(source.getChannel(), 0, source.getChannel().size());
        } catch (Exception e) {
            //FileLog.e(e);
            return false;
        }
        return true;
    }






    public static void setRectToRect(Matrix matrix, RectF src, RectF dst, int rotation, boolean translate) {
        float tx, sx;
        float ty, sy;
        boolean xLarger = false;
        if (rotation == 90 || rotation == 270) {
            sx = dst.height() / src.width();
            sy = dst.width() / src.height();
        } else {
            sx = dst.width() / src.width();
            sy = dst.height() / src.height();
        }
        if (sx < sy) {
            sx = sy;
            xLarger = true;
        } else {
            sy = sx;
        }
        if (translate) {
            matrix.setTranslate(dst.left, dst.top);
        }
        if (rotation == 90) {
            matrix.preRotate(90);
            matrix.preTranslate(0, -dst.width());
        } else if (rotation == 180) {
            matrix.preRotate(180);
            matrix.preTranslate(-dst.width(), -dst.height());
        } else if (rotation == 270) {
            matrix.preRotate(270);
            matrix.preTranslate(-dst.height(), 0);
        }

        if (translate) {
            tx = -src.left * sx;
            ty = -src.top * sy;
        } else {
            tx = dst.left - src.left * sx;
            ty = dst.top - src.top * sy;
        }

        float diff;
        if (xLarger) {
            diff = dst.width() - src.width() * sy;
        } else {
            diff = dst.height() - src.height() * sy;
        }
        diff = diff / 2.0f;
        if (xLarger) {
            tx += diff;
        } else {
            ty += diff;
        }

        matrix.preScale(sx, sy);
        if (translate) {
            matrix.preTranslate(tx, ty);
        }
    }


    public static boolean shouldEnableAnimation() {
        if (Build.VERSION.SDK_INT < 26 || Build.VERSION.SDK_INT >= 28) {
            return true;
        }
        PowerManager powerManager = (PowerManager) SavvyApplication.context.getSystemService(Context.POWER_SERVICE);
        if (powerManager.isPowerSaveMode()) {
            return false;
        }
        float scale = Settings.Global.getFloat(SavvyApplication.context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f);
        if (scale <= 0.0f) {
            return false;
        }
        return true;
    }


    @SuppressLint("PrivateApi")
    public static String getSystemProperty(String key) {
        try {
            Class props = Class.forName("android.os.SystemProperties");
            return (String) props.getMethod("get", String.class).invoke(null, key);
        } catch (Exception ignore) {
        }
        return null;
    }



    public static CharSequence concat(CharSequence... text) {
        if (text.length == 0) {
            return "";
        }

        if (text.length == 1) {
            return text[0];
        }

        boolean spanned = false;
        for (CharSequence piece : text) {
            if (piece instanceof Spanned) {
                spanned = true;
                break;
            }
        }

        if (spanned) {
            final SpannableStringBuilder ssb = new SpannableStringBuilder();
            for (CharSequence piece : text) {
                // If a piece is null, we append the string "null" for compatibility with the
                // behavior of StringBuilder and the behavior of the concat() method in earlier
                // versions of Android.
                ssb.append(piece == null ? "null" : piece);
            }
            return new SpannedString(ssb);
        } else {
            final StringBuilder sb = new StringBuilder();
            for (CharSequence piece : text) {
                sb.append(piece);
            }
            return sb.toString();
        }
    }

    public static float[] RGBtoHSB(int r, int g, int b) {
        float hue, saturation, brightness;
        float[] hsbvals = new float[3];
        int cmax = (r > g) ? r : g;
        if (b > cmax) {
            cmax = b;
        }
        int cmin = (r < g) ? r : g;
        if (b < cmin) {
            cmin = b;
        }

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0) {
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        } else {
            saturation = 0;
        }
        if (saturation == 0) {
            hue = 0;
        } else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = 2.0f + redc - bluec;
            } else {
                hue = 4.0f + greenc - redc;
            }
            hue = hue / 6.0f;
            if (hue < 0) {
                hue = hue + 1.0f;
            }
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    public static float computePerceivedBrightness(int color) {
        return (Color.red(color) * 0.2126f + Color.green(color) * 0.7152f + Color.blue(color) * 0.0722f) / 255f;
    }

    public static int getPatternColor(int color) {
        float[] hsb = RGBtoHSB(Color.red(color), Color.green(color), Color.blue(color));
        if (hsb[1] > 0.0f || (hsb[2] < 1.0f && hsb[2] > 0.0f)) {
            hsb[1] = Math.min(1.0f, hsb[1] + 0.05f + 0.1f * (1.0f - hsb[1]));
        }
        if (hsb[2] > 0.5f) {
            hsb[2] = Math.max(0.0f, hsb[2] * 0.65f);
        } else {
            hsb[2] = Math.max(0.0f, Math.min(1.0f, 1.0f - hsb[2] * 0.65f));
        }
        return HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0x66ffffff;
    }

    public static int getPatternSideColor(int color) {
        float[] hsb = RGBtoHSB(Color.red(color), Color.green(color), Color.blue(color));
        hsb[1] = Math.min(1.0f, hsb[1] + 0.05f);
        if (hsb[2] > 0.5f) {
            hsb[2] = Math.max(0.0f, hsb[2] * 0.90f);
        } else{
            hsb[2] = Math.max(0.0f, hsb[2] * 0.90f);
        }
        return HSBtoRGB(hsb[0], hsb[1], hsb[2]) | 0xff000000;
    }

    public static int getWallpaperRotation(int angle, boolean iOS) {
        if (iOS) {
            angle += 180;
        } else {
            angle -= 180;
        }
        while (angle >= 360) {
            angle -= 360;
        }
        while (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    public static float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f *= 0.47123894F;
        return (float) Math.sin((double) f);
    }

    public static void makeAccessibilityAnnouncement(CharSequence what) {
        AccessibilityManager am = (AccessibilityManager) SavvyApplication.context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am.isEnabled()) {
            AccessibilityEvent ev = AccessibilityEvent.obtain();
            ev.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            ev.getText().add(what);
            am.sendAccessibilityEvent(ev);
        }
    }

    public static int getOffsetColor(int color1, int color2, float offset, float alpha) {
        int rF = Color.red(color2);
        int gF = Color.green(color2);
        int bF = Color.blue(color2);
        int aF = Color.alpha(color2);
        int rS = Color.red(color1);
        int gS = Color.green(color1);
        int bS = Color.blue(color1);
        int aS = Color.alpha(color1);
        return Color.argb((int) ((aS + (aF - aS) * offset) * alpha), (int) (rS + (rF - rS) * offset), (int) (gS + (gF - gS) * offset), (int) (bS + (bF - bS) * offset));
    }

    public static int indexOfIgnoreCase(final String origin, final String searchStr) {
        if (searchStr.isEmpty() || origin.isEmpty()) {
            return origin.indexOf(searchStr);
        }

        for (int i = 0; i < origin.length(); i++) {
            if (i + searchStr.length() > origin.length()) {
                return -1;
            }
            int j = 0;
            int ii = i;
            while (ii < origin.length() && j < searchStr.length()) {
                char c = Character.toLowerCase(origin.charAt(ii));
                char c2 = Character.toLowerCase(searchStr.charAt(j));
                if (c != c2) {
                    break;
                }
                j++;
                ii++;
            }
            if (j == searchStr.length()) {
                return i;
            }
        }

        return -1;
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float lerp(float[] ab, float f) {
        return lerp(ab[0], ab[1], f);
    }




    private static char[] characters = new char[] {' ', '!', '"', '#', '%', '&', '\'', '(', ')', '*', ',', '-', '.', '/', ':', ';', '?', '@', '[', '\\', ']', '_', '{', '}', '¡', '§', '«', '¶', '·', '»', '¿', ';', '·', '՚', '՛', '՜', '՝', '՞', '՟', '։', '֊', '־', '׀', '׃', '׆', '׳', '״', '؉', '؊', '،', '؍', '؛', '؞', '؟', '٪', '٫', '٬', '٭', '۔', '܀', '܁', '܂', '܃', '܄', '܅', '܆', '܇', '܈', '܉', '܊', '܋', '܌', '܍', '߷', '߸', '߹', '࠰', '࠱', '࠲', '࠳', '࠴', '࠵', '࠶', '࠷', '࠸', '࠹', '࠺', '࠻', '࠼', '࠽', '࠾', '࡞', '।', '॥', '॰', '৽', '੶', '૰', '౷', '಄', '෴', '๏', '๚', '๛', '༄', '༅', '༆', '༇', '༈', '༉', '༊', '་', '༌', '།', '༎', '༏', '༐', '༑', '༒', '༔', '༺', '༻', '༼', '༽', '྅', '࿐', '࿑', '࿒', '࿓', '࿔', '࿙', '࿚', '၊', '။', '၌', '၍', '၎', '၏', '჻', '፠', '፡', '።', '፣', '፤', '፥', '፦', '፧', '፨', '᐀', '᙮', '᚛', '᚜', '᛫', '᛬', '᛭', '᜵', '᜶', '។', '៕', '៖', '៘', '៙', '៚', '᠀', '᠁', '᠂', '᠃', '᠄', '᠅', '᠆', '᠇', '᠈', '᠉', '᠊', '᥄', '᥅', '᨞', '᨟', '᪠', '᪡', '᪢', '᪣', '᪤', '᪥', '᪦', '᪨', '᪩', '᪪', '᪫', '᪬', '᪭', '᭚', '᭛', '᭜', '᭝', '᭞', '᭟', '᭠', '᯼', '᯽', '᯾', '᯿', '᰻', '᰼', '᰽', '᰾', '᰿', '᱾', '᱿', '᳀', '᳁', '᳂', '᳃', '᳄', '᳅', '᳆', '᳇', '᳓', '‐', '‑', '‒', '–', '—', '―', '‖', '‗', '‘', '’', '‚', '‛', '“', '”', '„', '‟', '†', '‡', '•', '‣', '․', '‥', '…', '‧', '‰', '‱', '′', '″', '‴', '‵', '‶', '‷', '‸', '‹', '›', '※', '‼', '‽', '‾', '‿', '⁀', '⁁', '⁂', '⁃', '⁅', '⁆', '⁇', '⁈', '⁉', '⁊', '⁋', '⁌', '⁍', '⁎', '⁏', '⁐', '⁑', '⁓', '⁔', '⁕', '⁖', '⁗', '⁘', '⁙', '⁚', '⁛', '⁜', '⁝', '⁞', '⁽', '⁾', '₍', '₎', '⌈', '⌉', '⌊', '⌋', '〈', '〉', '❨', '❩', '❪', '❫', '❬', '❭', '❮', '❯', '❰', '❱', '❲', '❳', '❴', '❵', '⟅', '⟆', '⟦', '⟧', '⟨', '⟩', '⟪', '⟫', '⟬', '⟭', '⟮', '⟯', '⦃', '⦄', '⦅', '⦆', '⦇', '⦈', '⦉', '⦊', '⦋', '⦌', '⦍', '⦎', '⦏', '⦐', '⦑', '⦒', '⦓', '⦔', '⦕', '⦖', '⦗', '⦘', '⧘', '⧙', '⧚', '⧛', '⧼', '⧽', '⳹', '⳺', '⳻', '⳼', '⳾', '⳿', '⵰', '⸀', '⸁', '⸂', '⸃', '⸄', '⸅', '⸆', '⸇', '⸈', '⸉', '⸊', '⸋', '⸌', '⸍', '⸎', '⸏', '⸐', '⸑', '⸒', '⸓', '⸔', '⸕', '⸖', '⸗', '⸘', '⸙', '⸚', '⸛', '⸜', '⸝', '⸞', '⸟', '⸠', '⸡', '⸢', '⸣', '⸤', '⸥', '⸦', '⸧', '⸨', '⸩', '⸪', '⸫', '⸬', '⸭', '⸮', '⸰', '⸱', '⸲', '⸳', '⸴', '⸵', '⸶', '⸷', '⸸', '⸹', '⸺', '⸻', '⸼', '⸽', '⸾', '⸿', '⹀', '⹁', '⹂', '⹃', '⹄', '⹅', '⹆', '⹇', '⹈', '⹉', '⹊', '⹋', '⹌', '⹍', '⹎', '⹏', '、', '。', '〃', '〈', '〉', '《', '》', '「', '」', '『', '』', '【', '】', '〔', '〕', '〖', '〗', '〘', '〙', '〚', '〛', '〜', '〝', '〞', '〟', '〰', '〽', '゠', '・', '꓾', '꓿', '꘍', '꘎', '꘏', '꙳', '꙾', '꛲', '꛳', '꛴', '꛵', '꛶', '꛷', '꡴', '꡵', '꡶', '꡷', '꣎', '꣏', '꣸', '꣹', '꣺', '꣼', '꤮', '꤯', '꥟', '꧁', '꧂', '꧃', '꧄', '꧅', '꧆', '꧇', '꧈', '꧉', '꧊', '꧋', '꧌', '꧍', '꧞', '꧟', '꩜', '꩝', '꩞', '꩟', '꫞', '꫟', '꫰', '꫱', '꯫', '﴾', '﴿', '︐', '︑', '︒', '︓', '︔', '︕', '︖', '︗', '︘', '︙', '︰', '︱', '︲', '︳', '︴', '︵', '︶', '︷', '︸', '︹', '︺', '︻', '︼', '︽', '︾', '︿', '﹀', '﹁', '﹂', '﹃', '﹄', '﹅', '﹆', '﹇', '﹈', '﹉', '﹊', '﹋', '﹌', '﹍', '﹎', '﹏', '﹐', '﹑', '﹒', '﹔', '﹕', '﹖', '﹗', '﹘', '﹙', '﹚', '﹛', '﹜', '﹝', '﹞', '﹟', '﹠', '﹡', '﹣', '﹨', '﹪', '﹫', '！', '＂', '＃', '％', '＆', '＇', '（', '）', '＊', '，', '－', '．', '／', '：', '；', '？', '＠', '［', '＼', '］', '＿', '｛', '｝', '｟', '｠', '｡', '｢', '｣', '､', '･'};
    //private static String[] longCharacters = new String[] {"𐄀", "𐄁", "𐄂", "𐎟", "𐏐", "𐕯", "𐡗", "𐤟", "𐤿", "𐩐", "𐩑", "𐩒", "𐩓", "𐩔", "𐩕", "𐩖", "𐩗", "𐩘", "𐩿", "𐫰", "𐫱", "𐫲", "𐫳", "𐫴", "𐫵", "𐫶", "𐬹", "𐬺", "𐬻", "𐬼", "𐬽", "𐬾", "𐬿", "𐮙", "𐮚", "𐮛", "𐮜", "𐽕", "𐽖", "𐽗", "𐽘", "𐽙", "𑁇", "𑁈", "𑁉", "𑁊", "𑁋", "𑁌", "𑁍", "𑂻", "𑂼", "𑂾", "𑂿", "𑃀", "𑃁", "𑅀", "𑅁", "𑅂", "𑅃", "𑅴", "𑅵", "𑇅", "𑇆", "𑇇", "𑇈", "𑇍", "𑇛", "𑇝", "𑇞", "𑇟", "𑈸", "𑈹", "𑈺", "𑈻", "𑈼", "𑈽", "𑊩", "𑑋", "𑑌", "𑑍", "𑑎", "𑑏", "𑑛", "𑑝", "𑓆", "𑗁", "𑗂", "𑗃", "𑗄", "𑗅", "𑗆", "𑗇", "𑗈", "𑗉", "𑗊", "𑗋", "𑗌", "𑗍", "𑗎", "𑗏", "𑗐", "𑗑", "𑗒", "𑗓", "𑗔", "𑗕", "𑗖", "𑗗", "𑙁", "𑙂", "𑙃", "𑙠", "𑙡", "𑙢", "𑙣", "𑙤", "𑙥", "𑙦", "𑙧", "𑙨", "𑙩", "𑙪", "𑙫", "𑙬", "𑜼", "𑜽", "𑜾", "𑠻", "𑧢", "𑨿", "𑩀", "𑩁", "𑩂", "𑩃", "𑩄", "𑩅", "𑩆", "𑪚", "𑪛", "𑪜", "𑪞", "𑪟", "𑪠", "𑪡", "𑪢", "𑱁", "𑱂", "𑱃", "𑱄", "𑱅", "𑱰", "𑱱", "𑻷", "𑻸", "𑿿", "𒑰", "𒑱", "𒑲", "𒑳", "𒑴", "𖩮", "𖩯", "𖫵", "𖬷", "𖬸", "𖬹", "𖬺", "𖬻", "𖭄", "𖺗", "𖺘", "𖺙", "𖺚", "𖿢", "𛲟", "𝪇", "𝪈", "𝪉", "𝪊", "𝪋", "𞥞", "𞥟"};
    private static HashSet<Character> charactersMap;

    public static boolean isPunctuationCharacter(char ch) {
        if (charactersMap == null) {
            charactersMap = new HashSet<>();
            for (int a = 0; a < characters.length; a++) {
                charactersMap.add(characters[a]);
            }
        }
        //int len = longCharacters[0].length();
        return charactersMap.contains(ch);
    }

    public static int getColorDistance(int color1, int color2) {
        int r1 = Color.red(color1);
        int g1 = Color.green(color1);
        int b1 = Color.blue(color1);

        int r2 = Color.red(color2);
        int g2 = Color.green(color2);
        int b2 = Color.blue(color2);

        int rMean = (r1 + r2) / 2;
        int r = r1 - r2;
        int g = g1 - g2;
        int b = b1 - b2;
        return (((512 + rMean) * r * r) >> 8) + (4 * g * g) + (((767 - rMean) * b * b) >> 8);
    }

    public static int getAverageColor(int color1, int color2) {
        int r1 = Color.red(color1);
        int r2 = Color.red(color2);
        int g1 = Color.green(color1);
        int g2 = Color.green(color2);
        int b1 = Color.blue(color1);
        int b2 = Color.blue(color2);
        return Color.argb(255, (r1 / 2 + r2 / 2), (g1 / 2 + g2 / 2), (b1 / 2 + b2 / 2));
    }

    public static void setLightStatusBar(Window window, boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final View decorView = window.getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (enable) {
                if ((flags & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == 0) {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    decorView.setSystemUiVisibility(flags);
                    window.setStatusBarColor(0x0f000000);
                }
            } else {
                if ((flags & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0) {
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    decorView.setSystemUiVisibility(flags);
                    window.setStatusBarColor(0x33000000);
                }
            }
        }
    }

    public static void setLightNavigationBar(Window window, boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final View decorView = window.getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (enable) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(flags);
        }
    }

    public static boolean shouldShowUrlInAlert(String url) {
        boolean hasLatin = false;
        boolean hasNonLatin = false;
        try {
            Uri uri = Uri.parse(url);
            url = uri.getHost();

            for (int a = 0, N = url.length(); a < N; a++) {
                char ch = url.charAt(a);
                if (ch == '.' || ch == '-' || ch == '/' || ch == '+' || ch >= '0' && ch <= '9') {
                    continue;
                }
                if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
                    hasLatin = true;
                } else {
                    hasNonLatin = true;
                }
                if (hasLatin && hasNonLatin) {
                    break;
                }
            }

        } catch (Exception e) {
            //FileLog.e(e);
        }
        return hasLatin && hasNonLatin;
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
