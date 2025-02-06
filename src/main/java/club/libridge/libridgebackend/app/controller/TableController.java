package club.libridge.libridgebackend.app.controller;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.libridge.libridgebackend.app.persistence.TableEntity;
import club.libridge.libridgebackend.app.service.LobbyService;
import club.libridge.libridgebackend.app.service.TableService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping("/tables")
@AllArgsConstructor
public class TableController {

    @NonNull
    private final LobbyService lobbyService;

    @NonNull
    private final TableService tableService;

    @GetMapping
    public List<TableEntity> getTables() {
        LOGGER.trace("getTables");
        return this.lobbyService.getLobbyTables();
    }

    @PostMapping
    public TableEntity createTable() {
        LOGGER.trace("createTable");
        return this.tableService.createTable();
    }

}
