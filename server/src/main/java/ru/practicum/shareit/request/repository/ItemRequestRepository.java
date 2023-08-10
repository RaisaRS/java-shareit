package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Repository
public interface ItemRequestRepository extends PagingAndSortingRepository<Request, Long> {
    List<Request> findAllByRequestorId(Long userId);

    @Query("SELECT i FROM Request  i WHERE i.requestor.id <> ?1 ORDER BY i.created DESC")
    List<Request> findByOwnerId(Long userId, Pageable pageable);
}
