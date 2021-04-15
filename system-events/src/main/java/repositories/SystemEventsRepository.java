package repositories;

import model.SystemEventsModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemEventsRepository extends JpaRepository<SystemEventsModel, Integer> {

}
