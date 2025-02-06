package club.libridge.libridgebackend.app.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, UUID> {

    default List<TableEntity> getActiveTables() {
        return this.findAll();
    }

}
