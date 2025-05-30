package nz.ac.canterbury.team1000.gardenersgrove.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.repository.FriendRelationshipRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for FriendRelationships, defined by the @link{Service} annotation. This class links
 * automatically with @link{FriendRelationshipRepository}, see the @link{Autowired} annotation
 * below.
 */
@Service
public class FriendRelationshipService {

	private final FriendRelationshipRepository friendRelationshipRepository;

	@Autowired
	public FriendRelationshipService(FriendRelationshipRepository friendRelationshipRepository) {
		this.friendRelationshipRepository = friendRelationshipRepository;
	}

	/**
	 * Gets a list of friend relationships from persistence by searching by the sender id.
	 *
	 * @param senderId id of the user who initiated the relationships.
	 * @return the list of FriendRelationship objects initiated by the user.
	 */
	public List<FriendRelationship> getRelationshipsBySenderId(Long senderId) {
		return friendRelationshipRepository.findBySenderId(senderId).orElse(null);
	}

	/**
	 * Gets a list of friend relationships from persistence by searching by the receiver id.
	 *
	 * @param receiverId id of the user who is the recipient of the relationship request.
	 * @return the list of FriendRelationship objects sent to the user.
	 */
	public List<FriendRelationship> getRelationshipsByReceiverId(Long receiverId) {
		return friendRelationshipRepository.findByReceiverId(receiverId).orElse(null);
	}

	/**
	 * Gets a list of friend relationships from persistence by searching by the receiver id and
	 * status.
	 *
	 * @param receiverId id of the user who is the recipient of the relationship request.
	 * @param status     Status enum of the type of relationship.
	 * @return the list of FriendRelationship objects sent to the user.
	 */
	public List<FriendRelationship> getRelationshipsByReceiverIdAndStatus(Long receiverId,
		Status status) {
		return friendRelationshipRepository.findByReceiverIdAndStatus(receiverId, status)
			.orElse(null);
	}

	/**
	 * Gets a list of friend relationships from persistence by searching by the sender id and
	 * status.
	 *
	 * @param senderId id of the user who sent the relationship request.
	 * @param status   Status enum of the type of relationship.
	 * @return the list of FriendRelationship objects sent to the user.
	 */
	public List<FriendRelationship> getRelationshipsBySenderIdAndStatus(Long senderId,
		Status status) {
		return friendRelationshipRepository.findBySenderIdAndStatus(senderId, status).orElse(null);
	}


	/**
	 * Adds a new FriendRelationship to persistence
	 *
	 * @param friendRelationship object to persist
	 * @return the saved friend relationship object
	 */
	public FriendRelationship addFriendRelationship(FriendRelationship friendRelationship) {
		return friendRelationshipRepository.save(friendRelationship);
	}


	/**
	 * Updates the relationship of the given sender and receiver to the new status.
	 *
	 * @param senderId   id of the user who initiated the relationship.
	 * @param receiverId id of the user who received the relationship request.
	 * @param status     Status object representing the new state of the relationship.
	 */
	@Transactional
	public void updateFriendRelationship(Long senderId, Long receiverId, Status status) {
		Optional<FriendRelationship> currentRelationship = friendRelationshipRepository.findBySenderIdAndReceiverId(
			senderId, receiverId);
		currentRelationship.ifPresent(friendRelationship -> {
			friendRelationship.setStatus(status);
			friendRelationshipRepository.save(friendRelationship);
		});
	}

	/**
	 * Retrieves the relationship, if any, of a given sender and receiver.
	 *
	 * @param senderId   id of the user who initiated the relationship.
	 * @param receiverId id of the user who received the relationship request.
	 * @return the two user's associated FriendRelationship object if present, or else null.
	 */
	public FriendRelationship getFriendRelationship(Long senderId, Long receiverId) {
		return friendRelationshipRepository.findBySenderIdAndReceiverId(senderId, receiverId)
			.orElse(null);
	}

	/**
	 * Cancels the relationship, if any, of a given sender and receiver.
	 *
	 * @param senderId   id of the user who initiated the relationship.
	 * @param receiverId id of the user who received the relationship request.
	 * @return the two user's associated FriendRelationship object if present, or else null.
	 */
	@Transactional
	public void cancelFriendRelationship(Long senderId, Long receiverId) {
		friendRelationshipRepository.deleteBySenderIdAndReceiverId(senderId, receiverId);
		friendRelationshipRepository.deleteBySenderIdAndReceiverId(receiverId, senderId);
	}
}
