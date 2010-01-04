package org.bh.data;

public class DTOKeyPair implements Comparable<DTOKeyPair> {
	private final String dtoId;
	private final String key;

	public DTOKeyPair(String dtoId, String key) {
		this.dtoId = dtoId;
		this.key = key;
	}
	
	public DTOKeyPair(String dtoId, Object key) {
		this(dtoId, key.toString());
	}

	public String getDtoId() {
		return dtoId;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int compareTo(DTOKeyPair o) {
		int num = dtoId.compareTo(o.dtoId);
		if (num != 0)
			return num;

		return key.compareTo(o.key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtoId == null) ? 0 : dtoId.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof DTOKeyPair))
			return false;
		DTOKeyPair other = (DTOKeyPair) obj;
		return dtoId.equals(other.dtoId) && key.equals(other.key);
	}
}