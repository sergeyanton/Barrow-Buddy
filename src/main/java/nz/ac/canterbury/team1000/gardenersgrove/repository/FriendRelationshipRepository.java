package nz.ac.canterbury.team1000.gardenersgrove.repository;

import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Friend relationship repository accessor using Spring's @link{CrudRepository}.
 * These (basic) methods are provided for us without the need to write our own implementations.
 */
@Repository
public interface FriendRelationshipRepository extends CrudRepository<FriendRelationship, Long> {

	Optional<List<FriendRelationship>> findBySenderId(Long senderId);

	Optional<List<FriendRelationship>> findByReceiverId(Long receiverId);

	Optional<List<FriendRelationship>> findByReceiverIdAndStatus(Long receiverId, Status status);

	Optional<List<FriendRelationship>> findBySenderIdAndStatus(Long receiverId, Status status);

	Optional<FriendRelationship> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

//	@Query("DELETE FROM FriendRelationship fr WHERE fr. = :senderId AND fr.receiverId = :receiverId")
	void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
