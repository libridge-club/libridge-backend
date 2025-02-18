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
import club.libridge.libridgebackend.core.exceptions.ImpossibleBoardException;
import club.libridge.libridgebackend.core.openingtrainer.OpeningSystem;
import club.libridge.libridgebackend.dto.CallWithProbabilityDTO;
import club.libridge.libridgebackend.dto.ExpectedCallDTO;
import club.libridge.libridgebackend.dto.HandWithCandidateBidsDTO;
import club.libridge.libridgebackend.networking.server.LibridgeServer;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Call;
import scalabridge.CompleteDeckInFourHands;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.Hand;
import scalabridge.PositiveInteger;

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
        return this.boardFactory.getRandom().getHandOf(Direction.getNorth()).hand();
    }

    private Call getCall(Hand hand) {
        DuplicateBoard boardWithProvidedHand = boardFactory.fromHandAndDirection(hand, Direction.getNorth());
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
            CompleteDeckInFourHands fromEntity = boardFactory.fromEntity(boardEntity.get());
            int positiveInteger = 0;
            if (Direction.NORTH == direction) {
                positiveInteger = 1;
            } else if (Direction.EAST == direction) {
                positiveInteger = 2;
            } else if (Direction.SOUTH == direction) {
                positiveInteger = 3;
            } else if (Direction.WEST == direction) {
                positiveInteger = 4;
            }
            DuplicateBoard duplicateBoard = new DuplicateBoard(new PositiveInteger(positiveInteger), fromEntity);
            ExpectedCallDTO expectedCallDTO = new ExpectedCallDTO(boardId, libridgeServer.getExpectedCall(duplicateBoard));
            return Optional.of(expectedCallDTO);
        } else {
            return Optional.empty();
        }
    }
}
