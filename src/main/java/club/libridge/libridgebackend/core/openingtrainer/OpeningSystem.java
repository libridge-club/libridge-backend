package club.libridge.libridgebackend.core.openingtrainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import club.libridge.libridgebackend.core.BiddingBox;
import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.boardrules.BoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.AlwaysValidBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasFourWeakOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasOneMajorOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasOneMinorOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasOneNoTrumpOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasThreeWeakOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasTwoClubsOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasTwoNoTrumpOpeningBoardRule;
import club.libridge.libridgebackend.core.boardrules.bridgeopenings.DealerHasTwoWeakOpeningBoardRule;
import scalabridge.Call;
import scalabridge.OddTricks;
import scalabridge.Strain;
import scalabridge.Suit;

@Component
public class OpeningSystem {

    interface BiddableFromBoard {
        Call getCorrectCall(Board board);
    }

    private static final Map<BoardRule, BiddableFromBoard> RULE_TO_CALL_MAP;
    static {

        class TwoClubsBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                return BiddingBox.get("2C");
            }
        }

        class OneMajorBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                Suit longestMajor = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestMajor();
                return BiddingBox.getBid(OddTricks.ONE, Strain.fromSuit(longestMajor));
            }
        }

        class OneNoTrumpBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                return BiddingBox.get("1N");
            }
        }

        class TwoNoTrumpsBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                return BiddingBox.get("2N");
            }
        }

        class OneMinorBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                Suit longestMinor = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestMinor();
                return BiddingBox.getBid(OddTricks.ONE, Strain.fromSuit(longestMinor));
            }
        }

        class FourWeakBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                Suit longestSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestSuit();
                return BiddingBox.getBid(OddTricks.FOUR, Strain.fromSuit(longestSuit));
            }
        }

        class ThreeWeakBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                Suit longestSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestSuit();
                return BiddingBox.getBid(OddTricks.THREE, Strain.fromSuit(longestSuit));
            }
        }

        class TwoWeakBiddable implements BiddableFromBoard {
            @Override
            public Call getCorrectCall(Board board) {
                Suit longestSuit = board.getHandOf(board.getDealer()).getHandEvaluations().getLongestSuit();
                return BiddingBox.getBid(OddTricks.TWO, Strain.fromSuit(longestSuit));
            }
        }

        RULE_TO_CALL_MAP = new HashMap<BoardRule, BiddableFromBoard>();
        RULE_TO_CALL_MAP.put(new DealerHasTwoClubsOpeningBoardRule(), new TwoClubsBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasOneMajorOpeningBoardRule(), new OneMajorBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasOneNoTrumpOpeningBoardRule(), new OneNoTrumpBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasTwoNoTrumpOpeningBoardRule(), new TwoNoTrumpsBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasOneMinorOpeningBoardRule(), new OneMinorBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasFourWeakOpeningBoardRule(), new FourWeakBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasThreeWeakOpeningBoardRule(), new ThreeWeakBiddable());
        RULE_TO_CALL_MAP.put(new DealerHasTwoWeakOpeningBoardRule(), new TwoWeakBiddable());
    }

    private List<BoardRule> rulePriority = new ArrayList<>();

    public OpeningSystem() {
        // 22+
        rulePriority.add(new DealerHasTwoClubsOpeningBoardRule());

        // 12-21
        rulePriority.add(new DealerHasOneMajorOpeningBoardRule());
        rulePriority.add(new DealerHasOneNoTrumpOpeningBoardRule());
        rulePriority.add(new DealerHasTwoNoTrumpOpeningBoardRule());
        rulePriority.add(new DealerHasOneMinorOpeningBoardRule());

        // 6-10
        rulePriority.add(new DealerHasFourWeakOpeningBoardRule());
        rulePriority.add(new DealerHasThreeWeakOpeningBoardRule());
        rulePriority.add(new DealerHasTwoWeakOpeningBoardRule());

        // 11 What to do with 11 HCP?

        // default
        rulePriority.add(new AlwaysValidBoardRule());

    }

    public Call getCall(Board board) {
        try {
            BoardRule firstValidOpeningRule = this.getFirstValidOpeningRule(board);
            BiddableFromBoard biddableFromBoard = RULE_TO_CALL_MAP.get(firstValidOpeningRule);
            if (biddableFromBoard == null) {
                return BiddingBox.PASS;
            }
            return biddableFromBoard.getCorrectCall(board);
        } catch (Exception e) {
            e.printStackTrace();
            return BiddingBox.PASS;
        }
    }

    private BoardRule getFirstValidOpeningRule(Board board) {
        for (BoardRule boardRule : rulePriority) {
            if (boardRule.isValid(board)) {
                return boardRule;
            }
        }
        return new AlwaysValidBoardRule();
    }
}
