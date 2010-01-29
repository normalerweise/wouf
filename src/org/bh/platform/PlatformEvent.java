package org.bh.platform;

import java.util.EventObject;

/**
 * Instances of this class are used to notify {@link IPlatformListener}s about
 * events which occured somewhere in the application and which are of importance
 * for the whole application.
 * 
 * @author Robert Vollmer
 * @author Marco Hammel
 */
public class PlatformEvent extends EventObject {
	private static final long serialVersionUID = 6392511944744301979L;

	private Type eventType;

	public static enum Type {
		/**
		 * plugin should put the dto copy back to ui
		 */
		GETCOPY,
		/**
		 * Platform has been loaded completely
		 */
		PLATFORM_LOADING_COMPLETED,
		/**
		 * 
		 */
		DATA_CHANGED,
		/**
		 * The Locale has changed
		 */
		LOCALE_CHANGED,
		/**
		 * save
		 */
		SAVE,
		/**
		 * save as
		 */
		SAVEAS,
		/**
		 * save all
		 */
		SAVEALL,
	}

	/**
	 * Creates a new platform event, but does not fire it yet.
	 * 
	 * @param source
	 *            Must not be null.
	 * @param type
	 *            The type of the event.
	 */
	public PlatformEvent(Object source, Type type) {
		super(source);
		eventType = type;
	}

	/**
	 * Returns the event type.
	 * 
	 * @return The event type.
	 */
	public Type getEventType() {
		return eventType;
	}

	@Override
	public String toString() {
		return getClass().getName() + " [type=" + eventType + ", source="
				+ source + "]";
	}

}
