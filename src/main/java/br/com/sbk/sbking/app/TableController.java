package br.com.sbk.sbking.app;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import br.com.sbk.sbking.networking.websockets.TableMessageDTO;

@Controller
public class TableController {

    @Autowired
    private SimpMessagingTemplate template;

    private String getDestination(TableMessageDTO tableDealDTO) {
        return "/topic/table/" + tableDealDTO.getTableId();
    }

    public void getDeal(TableMessageDTO tableDealDTO) {
        String destination = this.getDestination(tableDealDTO);
        LOGGER.debug("Sending deal to: {0}", destination);
        this.template.convertAndSend(destination, tableDealDTO);
    }

    public void sendFinishDeal(TableMessageDTO tableDealDTO) {
        String destination = this.getDestination(tableDealDTO);
        LOGGER.info("Sending finishDeal to: {0}", destination);
        this.template.convertAndSend(destination, tableDealDTO);
    }

}
