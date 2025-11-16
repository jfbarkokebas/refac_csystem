package model;

import java.sql.Timestamp;

import enums.TypeEntry;

public class EntryTransaction {

	private Long id;
	private Long entryID;
	private TypeEntry typeEntry;
	private Double clientEntry;
	private Double ownerEntry;
	private Timestamp createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntryID() {
		return entryID;
	}

	public void setEntryID(Long entryID) {
		this.entryID = entryID;
	}

	public TypeEntry getTypeEntry() {
		return typeEntry;
	}

	public void setTypeEntry(TypeEntry typeEntry) {
		this.typeEntry = typeEntry;
	}

	public Double getClientEntry() {
		return clientEntry;
	}

	public void setClientEntry(Double clientEntry) {
		this.clientEntry = clientEntry;
	}

	public Double getOwnerEntry() {
		return ownerEntry;
	}

	public void setOwnerEntry(Double ownerEntry) {
		this.ownerEntry = ownerEntry;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "TransactionEntry [id=" + id + ", entryID=" + entryID + ", clientEntry=" + clientEntry + ", ownerEntry="
				+ ownerEntry + "]";
	}

}
