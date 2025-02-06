package club.libridge.libridgebackend.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import club.libridge.libridgebackend.app.persistence.TableEntity;
import club.libridge.libridgebackend.app.persistence.TableRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class LobbyService {

    @NonNull
    private final TableRepository tableRepository;

    public List<TableEntity> getLobbyTables() {
        return tableRepository.getActiveTables();
    }

}
