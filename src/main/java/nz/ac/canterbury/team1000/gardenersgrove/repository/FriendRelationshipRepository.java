package nz.ac.canterbury.team1000.gardenersgrove.repository;

import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRelationshipRepository extends CrudRepository<FriendRelationship, Long> {

	Optional<FriendRelationship> findBySenderId(Long senderId);

	Optional<FriendRelationship> findByReceiverId(Long receiverId);

	Optional<FriendRelationship> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

	void updateStatusById(Long id, Status status);


}
