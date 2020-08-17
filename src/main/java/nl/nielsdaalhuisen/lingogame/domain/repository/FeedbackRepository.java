package nl.nielsdaalhuisen.lingogame.domain.repository;

import nl.nielsdaalhuisen.lingogame.domain.model.Feedback;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}
