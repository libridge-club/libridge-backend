package club.libridge.libridgebackend.app;

import static club.libridge.libridgebackend.logging.SBKingLogger.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import club.libridge.libridgebackend.networking.websockets.TableMessageDTO;

@Controller
public class TableController {

    @Autowired
    private SimpMessagingTemplate template;

    private String getDestination(TableMessageDTO tableDealDTO) {
        return "/topic/table/" + tableDealDTO.getTableId();
    }

    public void sendMessage(TableMessageDTO tableDealDTO) {
        String destination = this.getDestination(tableDealDTO);
        LOGGER.debug("Sending {} to: {}", tableDealDTO.getMessage(), destination);
        this.template.convertAndSend(destination, tableDealDTO);
    }

}
