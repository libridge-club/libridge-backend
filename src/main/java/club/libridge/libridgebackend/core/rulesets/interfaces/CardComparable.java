package club.libridge.libridgebackend.core.rulesets.interfaces;

import java.util.Comparator;

import scalabridge.Card;

public interface CardComparable {

    Comparator<Card> getCardComparator();

}
