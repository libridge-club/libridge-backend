package club.libridge.libridgebackend.app.service;

import org.springframework.stereotype.Service;

import club.libridge.libridgebackend.app.persistence.TableEntity;
import club.libridge.libridgebackend.app.persistence.TableRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class TableService {

    @NonNull
    private final TableRepository tableRepository;

    public TableEntity createTable() {
        TableEntity table = new TableEntity();
        return tableRepository.save(table);
    }

}
