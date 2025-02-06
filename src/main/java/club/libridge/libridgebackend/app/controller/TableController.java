package club.libridge.libridgebackend.app.controller;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.libridge.libridgebackend.app.persistence.TableEntity;
import club.libridge.libridgebackend.app.service.LobbyService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping("/tables")
@AllArgsConstructor
public class TableController {

    @NonNull
    private final LobbyService lobbyService;

    @GetMapping
    public List<TableEntity> getTables() {
        LOGGER.trace("getTables");
        return this.lobbyService.getLobbyTables();
    }

}
