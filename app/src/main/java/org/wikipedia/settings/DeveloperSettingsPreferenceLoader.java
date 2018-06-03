package org.wikipedia.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.TwoStatePreference;

import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.crash.RemoteLogException;
import org.wikipedia.page.PageTitle;
import org.wikipedia.readinglist.database.ReadingList;
import org.wikipedia.readinglist.database.ReadingListDbHelper;
import org.wikipedia.readinglist.database.ReadingListPage;
import org.wikipedia.util.log.L;

import java.util.ArrayList;
import java.util.List;

class DeveloperSettingsPreferenceLoader extends BasePreferenceLoader {
    @NonNull private final Context context;

    @NonNull private final Preference.OnPreferenceChangeListener setRestBaseManuallyChangeListener
            = new Preference.OnPreferenceChangeListener() {
        /**
         * Called when the useRestBaseSetManually preference has been changed by the user. This is
         * called before the state of the Preference is about to be updated and
         * before the state is persisted.
         *
         * @param preference The changed Preference.
         * @param newValue   The new value of the Preference.
         * @return True to update the state of the Preference with the new value.
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            setUseRestBasePreference((Boolean) newValue);
            return true;
        }
    };

    @NonNull private final Preference.OnPreferenceChangeListener setMediaWikiBaseUriChangeListener
            = new Preference.OnPreferenceChangeListener() {
        /**
         * Called when the mediaWikiBaseUri preference has been changed by the user. This is
         * called before the state of the Preference is about to be updated and
         * before the state is persisted.
         *
         * @param preference The changed Preference.
         * @param newValue   The new value of the Preference.
         * @return True to update the state of the Preference with the new value.
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            resetMediaWikiSettings();
            return true;
        }
    };

    @NonNull private final Preference.OnPreferenceChangeListener setMediaWikiMultiLangSupportChangeListener
            = new Preference.OnPreferenceChangeListener() {
        /**
         * Called when the mediaWikiBaseUriSupportsLangCode preference has been changed by the user.
         * This is called before the state of the Preference is about to be updated and
         * before the state is persisted.
         *
         * @param preference The changed Preference.
         * @param newValue   The new value of the Preference.
         * @return True to update the state of the Preference with the new value.
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            resetMediaWikiSettings();
            return true;
        }
    };

    DeveloperSettingsPreferenceLoader(@NonNull PreferenceFragmentCompat fragment) {
        super(fragment);
        this.context = fragment.getActivity();
    }

    @Override
    public void loadPreferences() {
        loadPreferences(R.xml.developer_preferences);
        setUpRestBaseCheckboxes();
        setUpMediaWikiSettings();
        setUpCookies((PreferenceCategory) findPreference(R.string.preferences_developer_cookies_key));

        findPreference(context.getString(R.string.preferences_developer_crash_key))
                .setOnPreferenceClickListener(preference -> {
                    throw new TestException("User tested crash functionality.");
                });

        findPreference(R.string.preference_key_remote_log)
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    L.logRemoteError(new RemoteLogException(newValue.toString()));
                    WikipediaApp.getInstance().checkCrashes(getActivity());
                    return true;
                });

        findPreference(R.string.preference_key_add_articles)
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    if (!newValue.toString().trim().equals("") && !newValue.toString().trim().equals("0")) {
                        createTestReadingList("Test reading list", Integer.valueOf(newValue.toString().trim()));
                    }
                    return true;
                });

        findPreference(R.string.preference_key_add_reading_lists)
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue.toString().trim().equals("") || newValue.toString().trim().equals("0")) {
                        return true;
                    }
                    int numOfLists = Integer.valueOf(newValue.toString().trim());
                    for (int i = 1; i <= numOfLists; i++) {
                        createTestReadingList("Reading list " + i, 10);
                    }
                    return true;
                });
    }

    private void setUpRestBaseCheckboxes() {
        TwoStatePreference manualPreference = (TwoStatePreference) findPreference(getManualKey());
        manualPreference.setOnPreferenceChangeListener(setRestBaseManuallyChangeListener);
        setUseRestBasePreference(manualPreference.isChecked());
    }

    private String getManualKey() {
        return context.getString(R.string.preference_key_use_restbase_manual);
    }

    private void setUseRestBasePreference(boolean manualMode) {
        RbSwitch.INSTANCE.update();
        TwoStatePreference useRestBasePref = getUseRestBasePreference();
        useRestBasePref.setEnabled(manualMode);
        useRestBasePref.setChecked(RbSwitch.INSTANCE.isRestBaseEnabled());
    }

    private TwoStatePreference getUseRestBasePreference() {
        return (TwoStatePreference) findPreference(getUseRestBaseKey());
    }

    private String getUseRestBaseKey() {
        return context.getString(R.string.preference_key_use_restbase);
    }

    private void setUpMediaWikiSettings() {
        Preference uriPreference = findPreference(context.getString(R.string.preference_key_mediawiki_base_uri));
        uriPreference.setOnPreferenceChangeListener(setMediaWikiBaseUriChangeListener);
        TwoStatePreference multiLangPreference
                = (TwoStatePreference) findPreference(context.getString(R.string.preference_key_mediawiki_base_uri_supports_lang_code));
        multiLangPreference.setOnPreferenceChangeListener(setMediaWikiMultiLangSupportChangeListener);
    }

    private void resetMediaWikiSettings() {
        WikipediaApp.getInstance().resetWikiSite();
    }

    private void createTestReadingList(String title, int listSize) {
        ReadingList list = ReadingListDbHelper.instance().createList(title, "");
        List<ReadingListPage> pages = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            PageTitle pageTitle = new PageTitle("" + (i + 1), WikipediaApp.getInstance().getWikiSite());
            pages.add(new ReadingListPage(pageTitle));
        }
        ReadingListDbHelper.instance().addPagesToList(list, pages, true);
    }

    private void setUpCookies(@NonNull PreferenceCategory cat) {
        List<String> domains = Prefs.getCookieDomainsAsList();
        for (String domain : domains) {
            String key = Prefs.getCookiesForDomainKey(domain);
            Preference pref = newDataStringPref(key, domain);
            cat.addPreference(pref);
        }
    }

    private EditTextAutoSummarizePreference newDataStringPref(String key, String title) {
        EditTextAutoSummarizePreference pref = new EditTextAutoSummarizePreference(context, null,
                R.attr.editTextAutoSummarizePreferenceStyle);
        pref.setKey(key);
        pref.setTitle(title);
        return pref;
    }

    private static class TestException extends RuntimeException {
        TestException(String message) {
            super(message);
        }
    }
}
