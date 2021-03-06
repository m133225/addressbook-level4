package seedu.address.events.storage;

import seedu.address.events.BaseEvent;
import seedu.address.model.UserPrefs;

/**
 * Indicates a request for saving preferences has been raised
 */
public class SavePrefsRequestEvent extends BaseEvent {

    public final UserPrefs prefs;

    public SavePrefsRequestEvent(UserPrefs prefs) {
        this.prefs = prefs;
    }

    @Override
    public String toString(){
        return prefs.toString();
    }
}
