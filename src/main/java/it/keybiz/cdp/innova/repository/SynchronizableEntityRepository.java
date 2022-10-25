package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.SynchronizableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.stream.Stream;

@NoRepositoryBean
public interface SynchronizableEntityRepository<Entity extends SynchronizableEntity, ID> extends JpaRepository<Entity, ID>, JpaSpecificationExecutor<Entity> {
    @Modifying
    @Query("delete from #{#entityName} e where e.toDelete=true")
    void deleteAllByToDeleteIsTrue();

    @Modifying
    @Query("update #{#entityName} e set e.toDelete=true")
    void setAllToDeleteTrue();

    @Query("select e.id from #{#entityName} e where e.toDelete=false")
    Stream<ID> streamAllIdsNotToDelete();
}
