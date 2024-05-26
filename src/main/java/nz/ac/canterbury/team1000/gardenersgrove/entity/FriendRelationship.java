package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;

/**
 * Entity class reflecting a relationship between two users.
 * Holds values for the sender, receiver, and status of the relationship.
 */
@Entity
public class FriendRelationship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable = false)
	private User receiver;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	/**
	 * The default constructor that is required.
	 */
	protected FriendRelationship() {}

	public FriendRelationship(User sender, User receiver, Status status) {
		this.sender = sender;
		this.receiver = receiver;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
