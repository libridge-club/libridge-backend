package club.libridge.libridgebackend.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import club.libridge.libridgebackend.app.controller.OpeningTrainerController;
import club.libridge.libridgebackend.app.persistence.BoardEntity;
import club.libridge.libridgebackend.app.persistence.BoardFactory;
import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.core.BiddingBox;
import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.dto.ExpectedCallDTO;
import scalabridge.Call;
import scalabridge.OddTricks;
import scalabridge.Strain;

@SpringBootTest()
@ActiveProfiles("development")
public class OpeningTrainerControllerIT {

    @Autowired
    private OpeningTrainerController controller;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardFactory boardFactory;

    @Test
    public void getExpectedCall_shouldReturnTheExpectedCall() {

        String passOpener = "12381949714341091762816957538";
        String oneClubOpener = "57429642597604805982733244";
        String oneDiamondOpener = "38187237748758131789457842580";
        String oneHeartOpener = "22569737664198034018807822509";
        String oneSpadeOpener = "40933510485497006998353126544";
        String oneNoTrumpsOpener = "8540750164466352716412038403";
        String twoClubsOpener = "12979946542850180316978514000";
        String twoDiamondsOpener = "41419713096793509860591101525";
        String twoHeartsOpener = "35439325976723488947673703681";
        String twoSpadesOpener = "27370979702279925995903706517";
        String twoNoTrumpsOpener = "19847197207945221703164878961";
        String threeClubsOpener = "23095387425862837759608021051";
        String threeDiamondsOpener = "12296845910194886140381470344";
        String threeHeartsOpener = "18824432695205869323939723495";
        String threeSpadesOpener = "26852222565864510504448505389";
        String fourClubsOpener = "28859342425097651020818187535";
        String fourDiamondsOpener = "50092438550924273636071822612";
        String fourHeartsOpener = "35629420319552995596859196609";
        String fourSpadesOpener = "4798078317247592323843653928";

        Map<String, Call> handToBidMap = new HashMap<String, Call>();
        handToBidMap.put(passOpener, BiddingBox.PASS);
        handToBidMap.put(oneClubOpener, BiddingBox.getBid(OddTricks.ONE, Strain.CLUBS));
        handToBidMap.put(oneDiamondOpener, BiddingBox.getBid(OddTricks.ONE, Strain.DIAMONDS));
        handToBidMap.put(oneHeartOpener, BiddingBox.getBid(OddTricks.ONE, Strain.HEARTS));
        handToBidMap.put(oneSpadeOpener, BiddingBox.getBid(OddTricks.ONE, Strain.SPADES));
        handToBidMap.put(oneNoTrumpsOpener, BiddingBox.getBid(OddTricks.ONE, Strain.NOTRUMPS));
        handToBidMap.put(twoClubsOpener, BiddingBox.getBid(OddTricks.TWO, Strain.CLUBS));
        handToBidMap.put(twoDiamondsOpener, BiddingBox.getBid(OddTricks.TWO, Strain.DIAMONDS));
        handToBidMap.put(twoHeartsOpener, BiddingBox.getBid(OddTricks.TWO, Strain.HEARTS));
        handToBidMap.put(twoSpadesOpener, BiddingBox.getBid(OddTricks.TWO, Strain.SPADES));
        handToBidMap.put(twoNoTrumpsOpener, BiddingBox.getBid(OddTricks.TWO, Strain.NOTRUMPS));
        handToBidMap.put(threeClubsOpener, BiddingBox.getBid(OddTricks.THREE, Strain.CLUBS));
        handToBidMap.put(threeDiamondsOpener, BiddingBox.getBid(OddTricks.THREE, Strain.DIAMONDS));
        handToBidMap.put(threeHeartsOpener, BiddingBox.getBid(OddTricks.THREE, Strain.HEARTS));
        handToBidMap.put(threeSpadesOpener, BiddingBox.getBid(OddTricks.THREE, Strain.SPADES));
        handToBidMap.put(fourClubsOpener, BiddingBox.getBid(OddTricks.FOUR, Strain.CLUBS));
        handToBidMap.put(fourDiamondsOpener, BiddingBox.getBid(OddTricks.FOUR, Strain.DIAMONDS));
        handToBidMap.put(fourHeartsOpener, BiddingBox.getBid(OddTricks.FOUR, Strain.HEARTS));
        handToBidMap.put(fourSpadesOpener, BiddingBox.getBid(OddTricks.FOUR, Strain.SPADES));

        for (Entry<String, Call> entry : handToBidMap.entrySet()) {
            Board board = boardFactory.fromPavlicekNumber(entry.getKey());
            BoardEntity boardEntity = new BoardEntity(board);
            BoardEntity saved = boardRepository.save(boardEntity);
            ExpectedCallDTO expectedCallDTO = controller.getExpectedCall(saved.getId(), board.getDealer());
            assertEquals(entry.getValue(), expectedCallDTO.getCall());
        }

    }

}
