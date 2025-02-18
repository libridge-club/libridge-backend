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
import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.dto.ExpectedCallDTO;
import club.libridge.libridgebackend.pbn.PBNUtils;
import scalabridge.BiddingBox;
import scalabridge.Call;
import scalabridge.CompleteDeckInFourHands;
import scalabridge.Direction;
import scalabridge.OddTricks;
import scalabridge.Strain;

@SpringBootTest()
@ActiveProfiles("development")
public class OpeningTrainerControllerIT {

    @Autowired
    private OpeningTrainerController controller;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void getExpectedCall_shouldReturnTheExpectedCall() {

        String passOpener = "N:AT9654.3.T73.K86 2.AKQJ87.9865.74 Q8.9542.AQJ4.QJ3 KJ73.T6.K2.AT952";
        String oneClubOpener = "N:AKQJ.A752.T.A542 T87.KJ63.9543.93 64.Q984.KQJ82.T6 9532.T.A76.KQJ87";
        String oneDiamondOpener = "N:T5.AKQ8.AJ765.Q8 Q932.632.KQT4.72 AJ4.T7.83.KJ9643 K876.J954.92.AT5";
        String oneHeartOpener = "N:JT2.AQ7653.85.KQ A654.T9.AQJ4.J43 K83.J42.T9.A9872 Q97.K8.K7632.T65";
        String oneSpadeOpener = "N:KQ753.KQ6.Q5.T63 T64.T32.K.AQ8742 982.A9854.T72.J5 AJ.J7.AJ98643.K9";
        String oneNoTrumpsOpener = "N:A98.Q95.AQ.QJ973 JT5.KJ.J752.A865 KQ76.AT86.K983.T 432.7432.T64.K42";
        String twoClubsOpener = "N:A.AKQ862.AKT8.K9 J97632.95.J76.T3 8.T3.9542.AQ8764 KQT54.J74.Q3.J52";
        String twoDiamondsOpener = "N:K5.T7.AQ7642.863 QJ.AK9654.853.KT T93.QJ83.9.AQJ94 A87642.2.KJT.752";
        String twoHeartsOpener = "N:J95.AQT652.KT4.T 87.KJ87.Q862.843 AKQ3.43.AJ3.K952 T642.9.975.AQJ76";
        String twoSpadesOpener = "N:KQ9872.T52.2.K75 6.J9.QJT93.T8632 AJ4.AK86.A87.QJ9 T53.Q743.K654.A4";
        String twoNoTrumpsOpener = "N:94.AKQ8.KT94.AKQ AKT5.JT93.6.JT74 8762.65.J7532.52 QJ3.742.AQ8.9863";
        String threeClubsOpener = "N:9.J.JT83.KQJT943 A432.K9.A9642.87 KJT65.QT8432.K.2 Q87.A765.Q75.A65";
        String threeDiamondsOpener = "N:A86.63.KJT8732.T 5.QJT9754.A54.J2 QJT973.K8.Q96.76 K42.A2..AKQ98543";
        String threeHeartsOpener = "N:832.KQJT652.72.K AKT7.983.64.AJ87 QJ965.74.93.T653 4.A.AKQJT85.Q942";
        String threeSpadesOpener = "N:KQJT764.864..T94 853.AJ.AT92.A862 A2.K9.8653.KJ753 9.QT7532.KQJ74.Q";
        String fourClubsOpener = "N:K.6.732.AT986532 J9853.K432.K96.7 AQT764.97.854.KJ 2.AQJT85.AQJT.Q4";
        String fourDiamondsOpener = "N:5.97.KQT98743.K3 J64.A854.5.J9542 K9732.J2.J62.T86 AQT8.KQT63.A.AQ7";
        String fourHeartsOpener = "N:97.AQJ95432..T82 J642.K7.K976.J76 AKQ53..AQJ84.KQ3 T8.T86.T532.A954";
        String fourSpadesOpener = "N:AJT98532.J.JT8.T K6.T762.742.A876 Q.KQ9543.K9.Q532 74.A8.AQ653.KJ94";

        Map<String, Call> handToBidMap = new HashMap<String, Call>();
        handToBidMap.put(passOpener, BiddingBox.getPass());
        handToBidMap.put(oneClubOpener, BiddingBox.getBid(OddTricks.getONE(), Strain.getCLUBS()));
        handToBidMap.put(oneDiamondOpener, BiddingBox.getBid(OddTricks.getONE(), Strain.getDIAMONDS()));
        handToBidMap.put(oneHeartOpener, BiddingBox.getBid(OddTricks.getONE(), Strain.getHEARTS()));
        handToBidMap.put(oneSpadeOpener, BiddingBox.getBid(OddTricks.getONE(), Strain.getSPADES()));
        handToBidMap.put(oneNoTrumpsOpener, BiddingBox.getBid(OddTricks.getONE(), Strain.getNOTRUMPS()));
        handToBidMap.put(twoClubsOpener, BiddingBox.getBid(OddTricks.getTWO(), Strain.getCLUBS()));
        handToBidMap.put(twoDiamondsOpener, BiddingBox.getBid(OddTricks.getTWO(), Strain.getDIAMONDS()));
        handToBidMap.put(twoHeartsOpener, BiddingBox.getBid(OddTricks.getTWO(), Strain.getHEARTS()));
        handToBidMap.put(twoSpadesOpener, BiddingBox.getBid(OddTricks.getTWO(), Strain.getSPADES()));
        handToBidMap.put(twoNoTrumpsOpener, BiddingBox.getBid(OddTricks.getTWO(), Strain.getNOTRUMPS()));
        handToBidMap.put(threeClubsOpener, BiddingBox.getBid(OddTricks.getTHREE(), Strain.getCLUBS()));
        handToBidMap.put(threeDiamondsOpener, BiddingBox.getBid(OddTricks.getTHREE(), Strain.getDIAMONDS()));
        handToBidMap.put(threeHeartsOpener, BiddingBox.getBid(OddTricks.getTHREE(), Strain.getHEARTS()));
        handToBidMap.put(threeSpadesOpener, BiddingBox.getBid(OddTricks.getTHREE(), Strain.getSPADES()));
        handToBidMap.put(fourClubsOpener, BiddingBox.getBid(OddTricks.getFOUR(), Strain.getCLUBS()));
        handToBidMap.put(fourDiamondsOpener, BiddingBox.getBid(OddTricks.getFOUR(), Strain.getDIAMONDS()));
        handToBidMap.put(fourHeartsOpener, BiddingBox.getBid(OddTricks.getFOUR(), Strain.getHEARTS()));
        handToBidMap.put(fourSpadesOpener, BiddingBox.getBid(OddTricks.getFOUR(), Strain.getSPADES()));

        for (Entry<String, Call> entry : handToBidMap.entrySet()) {
            CompleteDeckInFourHands board = PBNUtils.getBoardFromDealTag(entry.getKey());
            BoardEntity boardEntity = new BoardEntity(board);
            BoardEntity saved = boardRepository.save(boardEntity);
            ExpectedCallDTO expectedCallDTO = controller.getExpectedCall(saved.getId(), Direction.NORTH);
            assertEquals(entry.getValue(), expectedCallDTO.getCall());
        }

    }

}
