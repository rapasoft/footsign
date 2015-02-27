package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author rap
 */
public interface UserRepository extends CrudRepository<User, String> {

	User findByDomainShortName(String name);

	List<User> findAll();

}
