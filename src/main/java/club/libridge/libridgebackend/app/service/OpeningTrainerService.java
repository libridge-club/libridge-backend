package club.libridge.libridgebackend.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import club.libridge.libridgebackend.app.persistence.BoardEntity;
import club.libridge.libridgebackend.app.persistence.BoardFactory;
import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.ben.BenCandidate;
import club.libridge.libridgebackend.ben.BenResponse;
import club.libridge.libridgebackend.ben.BenWebClient;
import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.exceptions.ImpossibleBoardException;
import club.libridge.libridgebackend.core.openingtrainer.OpeningSystem;
import club.libridge.libridgebackend.dto.CallWithProbabilityDTO;
import club.libridge.libridgebackend.dto.ExpectedCallDTO;
import club.libridge.libridgebackend.dto.HandWithCandidateBidsDTO;
import club.libridge.libridgebackend.networking.server.LibridgeServer;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Call;
import scalabridge.Direction;
import scalabridge.Hand;

@Service
@AllArgsConstructor
public class OpeningTrainerService {

    @NonNull
    private final BoardFactory boardFactory;

    @NonNull
    private final BoardRepository boardRepository;

    @NonNull
    private final OpeningSystem openingSystem;

    @NonNull
    private final BenWebClient benWebClient;

    @NonNull
    private final LibridgeServer libridgeServer;

    private static final int MAXIMUM_NUMBER_OF_TRIES = 1000;

    private Hand getRandom() {
        return this.boardFactory.getRandom().getHandOf(Direction.getNorth());
    }

    private Call getCall(Hand hand) {
        Board boardWithProvidedHand = boardFactory.fromHandAndDirection(hand, Direction.getNorth());
        return this.openingSystem.getCall(boardWithProvidedHand);
    }

    public HandWithCandidateBidsDTO getRandomHandWithCandidateBids(@NonNull Boolean avoidPass) {
        Hand hand;
        Call call;
        int numberOfTries = 0;
        do {
            if (numberOfTries > MAXIMUM_NUMBER_OF_TRIES) {
                throw new ImpossibleBoardException();
            } else {
                numberOfTries++;
            }
            hand = this.getRandom();
            call = this.getCall(hand);
        } while (avoidPass && call.isPass());
        Optional<BenResponse> benResponseOptional = benWebClient.getBidForEmptyAuction(hand);
        if (benResponseOptional.isEmpty()) {
            return new HandWithCandidateBidsDTO(hand, call, null);
        } else {
            BenResponse benResponse = benResponseOptional.get();
            List<BenCandidate> candidates = benResponse.getCandidates();
            List<CallWithProbabilityDTO> list = candidates.stream().map(benCandidate -> {
                String pInsteadOfPass = "PASS".equals(benCandidate.getCall()) ? "P" : benCandidate.getCall();
                return new CallWithProbabilityDTO(pInsteadOfPass, benCandidate.getInstaScore());
            }).toList();
            return new HandWithCandidateBidsDTO(hand, call, list);
        }

    }

    public Optional<ExpectedCallDTO> getExpectedCall(UUID boardId, Direction direction) {
        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);
        if (boardEntity.isPresent()) {
            ExpectedCallDTO expectedCallDTO = new ExpectedCallDTO(boardId,
                    libridgeServer.getExpectedCall(boardFactory.fromEntity(boardEntity.get())));
            return Optional.of(expectedCallDTO);
        } else {
            return Optional.empty();
        }
    }
}
