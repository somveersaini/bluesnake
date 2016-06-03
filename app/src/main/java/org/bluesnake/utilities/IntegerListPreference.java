package org.bluesnake.utilities;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * A list preference which persists its values as integers instead of strings.
 * Code reading the values should use
 * {@link android.content.SharedPreferences#getInt}.
 * When using XML-declared arrays for entry values, the arrays should be regular
 * string arrays containing valid integer values.
 *
 */
public class IntegerListPreference extends ListPreference {
	/**
	 * Create a new instance of the IntegerListPreference.
	 * 
	 * @param context Context.
	 */
	public IntegerListPreference(final Context context) {
		super(context);
		
		this.verifyEntryValues(null);
	}

	/**
	 * Create a new instnace of the IntegerListPreference.
	 * 
	 * @param context Context.
	 * @param attrs Attributes.
	 */
	public IntegerListPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		this.verifyEntryValues(null);
	}

	
	
	@Override
	public void setEntryValues(final CharSequence[] entryValues) {
		final CharSequence[] oldValues = getEntryValues();
		super.setEntryValues(entryValues);
		this.verifyEntryValues(oldValues);
	}

	@Override
	public void setEntryValues(final int entryValuesResId) {
		final CharSequence[] oldValues = getEntryValues();
		super.setEntryValues(entryValuesResId);
		this.verifyEntryValues(oldValues);
	}

	@Override
	protected String getPersistedString(final String defaultReturnValue) {
		//During initial load, there's no known default value
		int defaultIntegerValue = Integer.MIN_VALUE;
		if (defaultReturnValue != null) {
			defaultIntegerValue = Integer.parseInt(defaultReturnValue);
		}

		// When the list preference asks us to read a string, instead read an integer.
		int value = this.getPersistedInt(defaultIntegerValue);
		return Integer.toString(value);
	}

	@Override
	protected boolean persistString(final String value) {
		// When asked to save a string, instead save an integer
		return this.persistInt(Integer.parseInt(value));
	}

	/**
	 * Verify all of the values in the list.
	 * 
	 * @param oldValues Old value.
	 */
	private void verifyEntryValues(final CharSequence[] oldValues) {
		final CharSequence[] entryValues = this.getEntryValues();
		if (entryValues == null) {
			return;
		}

		for (final CharSequence entryValue : entryValues) {
			try {
				Integer.parseInt(entryValue.toString());
			} catch (NumberFormatException nfe) {
				super.setEntryValues(oldValues);
				throw nfe;
			}
		}
	}
}