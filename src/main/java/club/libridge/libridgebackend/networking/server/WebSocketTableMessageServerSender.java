package club.libridge.libridgebackend.networking.server;

import static club.libridge.libridgebackend.core.MessageTypes.DEAL_MESSAGE;
import static club.libridge.libridgebackend.core.MessageTypes.FINISH_DEAL_MESSAGE;
import static club.libridge.libridgebackend.core.MessageTypes.INITIALIZE_DEAL_MESSAGE;
import static club.libridge.libridgebackend.core.MessageTypes.INVALID_RULESET_MESSAGE;
import static club.libridge.libridgebackend.core.MessageTypes.STRAIN_CHOOSER_MESSAGE;
import static club.libridge.libridgebackend.core.MessageTypes.VALID_RULESET_MESSAGE;

import club.libridge.libridgebackend.app.TableWebsocketController;
import club.libridge.libridgebackend.core.Deal;
import scalabridge.Direction;
import club.libridge.libridgebackend.networking.websockets.TableMessageDTO;
import club.libridge.libridgebackend.networking.websockets.TableMessageDTO.Builder;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class WebSocketTableMessageServerSender {

    @NonNull
    private final TableWebsocketController tableController;

    public void sendDealToTable(Deal deal, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table).withMessage(DEAL_MESSAGE).withDeal(deal).build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendFinishDealToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table).withMessage(FINISH_DEAL_MESSAGE).build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendInitializeDealToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table).withMessage(INITIALIZE_DEAL_MESSAGE).build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendInvalidRulesetToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table).withMessage(INVALID_RULESET_MESSAGE).build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendValidRulesetToTable(Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table).withMessage(VALID_RULESET_MESSAGE).build();
        this.tableController.sendMessage(tableDealDTO);
    }

    public void sendStrainChooserToTable(Direction direction, Table table) {
        TableMessageDTO tableDealDTO = createBuilderWithTable(table).withMessage(STRAIN_CHOOSER_MESSAGE).withDirection(direction).build();
        this.tableController.sendMessage(tableDealDTO);
    }

    private Builder createBuilderWithTable(Table table) {
        return new TableMessageDTO.Builder().withTableId(table.getId().toString());
    }

}
