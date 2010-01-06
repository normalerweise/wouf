package org.bh.gui;

import java.util.EventObject;

public class ViewEvent extends EventObject {
	private static final long serialVersionUID = 1252498469949682609L;

	private Type eventType;

	public enum Type {
		/**
		 * The value of a component in the view has been changed and was
		 * successfully validated
		 */
		VALUE_CHANGED,
	}

	public ViewEvent(Object source, Type type) {
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
		return getClass().getName() + "[source=" + source + ", type="
				+ eventType + "]";
	}
}
