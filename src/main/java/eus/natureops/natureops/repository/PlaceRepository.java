package eus.natureops.natureops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.Place;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}
