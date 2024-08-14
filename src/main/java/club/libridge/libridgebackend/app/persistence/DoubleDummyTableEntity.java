package club.libridge.libridgebackend.app.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import club.libridge.libridgebackend.dds.DoubleDummyTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import scalabridge.Direction;
import scalabridge.Strain;

/**
 * Columns are the cross product of strains and directions NORTH_CLUBS, NORTH_DIAMONDS, ..., NORTH_NOTRUMPS, EAST_CLUBS, etc
 *
 * Hopefully this will offer search option in the future like "a hand that makes 6 hearts and 6 spades"
 *
 * -1 symbolizes no information
 */
@Entity(name = "DoubleDummyTable")
@Validated
public class DoubleDummyTableEntity {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int northClubs = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int northDiamonds = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int northHearts = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int northSpades = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int northNotrumps = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int eastClubs = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int eastDiamonds = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int eastHearts = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int eastSpades = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int eastNotrumps = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int southClubs = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int southDiamonds = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int southHearts = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int southSpades = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int southNotrumps = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int westClubs = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int westDiamonds = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int westHearts = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int westSpades = -1;

    @Column(columnDefinition = "smallint")
    @NotNull
    private int westNotrumps = -1;

    /**
     * Make this owner side so that, in the one-to-one relationship, the double dummy table is optional, but the board is not. i.e. It *is* possible
     * to create a board without a double dummy table but it *is not* possible to create a double dummy table without a board.
     *
     * Also, there is no need for a nullable column in the Board side.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_entity_id", referencedColumnName = "id")
    @NotNull
    @Setter
    private BoardEntity boardEntity;

    /**
     * These two methods (doubleDummyTable getter and setter) do the necessary mapping between the model and the database columns.
     */

    public void setDoubleDummyTable(DoubleDummyTable doubleDummyTable) {
        this.updateAllFields(doubleDummyTable);
    }

    public DoubleDummyTable getDoubleDummyTable() {
        List<Integer> tricksAvailableList = new ArrayList<Integer>();

        // Following the order described at DoubleDummyTable
        tricksAvailableList.add(this.northSpades);
        tricksAvailableList.add(this.eastSpades);
        tricksAvailableList.add(this.southSpades);
        tricksAvailableList.add(this.westSpades);

        tricksAvailableList.add(this.northHearts);
        tricksAvailableList.add(this.eastHearts);
        tricksAvailableList.add(this.southHearts);
        tricksAvailableList.add(this.westHearts);

        tricksAvailableList.add(this.northDiamonds);
        tricksAvailableList.add(this.eastDiamonds);
        tricksAvailableList.add(this.southDiamonds);
        tricksAvailableList.add(this.westDiamonds);

        tricksAvailableList.add(this.northClubs);
        tricksAvailableList.add(this.eastClubs);
        tricksAvailableList.add(this.southClubs);
        tricksAvailableList.add(this.westClubs);

        tricksAvailableList.add(this.northNotrumps);
        tricksAvailableList.add(this.eastNotrumps);
        tricksAvailableList.add(this.southNotrumps);
        tricksAvailableList.add(this.westNotrumps);

        return new DoubleDummyTable(tricksAvailableList);
    }

    private void updateAllFields(DoubleDummyTable doubleDummyTable) {
        this.northSpades = doubleDummyTable.getTricksAvailableFor(Strain.SPADES, Direction.NORTH).getInt();
        this.eastSpades = doubleDummyTable.getTricksAvailableFor(Strain.SPADES, Direction.EAST).getInt();
        this.southSpades = doubleDummyTable.getTricksAvailableFor(Strain.SPADES, Direction.SOUTH).getInt();
        this.westSpades = doubleDummyTable.getTricksAvailableFor(Strain.SPADES, Direction.WEST).getInt();

        this.northHearts = doubleDummyTable.getTricksAvailableFor(Strain.HEARTS, Direction.NORTH).getInt();
        this.eastHearts = doubleDummyTable.getTricksAvailableFor(Strain.HEARTS, Direction.EAST).getInt();
        this.southHearts = doubleDummyTable.getTricksAvailableFor(Strain.HEARTS, Direction.SOUTH).getInt();
        this.westHearts = doubleDummyTable.getTricksAvailableFor(Strain.HEARTS, Direction.WEST).getInt();

        this.northDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.DIAMONDS, Direction.NORTH).getInt();
        this.eastDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.DIAMONDS, Direction.EAST).getInt();
        this.southDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.DIAMONDS, Direction.SOUTH).getInt();
        this.westDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.DIAMONDS, Direction.WEST).getInt();

        this.northClubs = doubleDummyTable.getTricksAvailableFor(Strain.CLUBS, Direction.NORTH).getInt();
        this.eastClubs = doubleDummyTable.getTricksAvailableFor(Strain.CLUBS, Direction.EAST).getInt();
        this.southClubs = doubleDummyTable.getTricksAvailableFor(Strain.CLUBS, Direction.SOUTH).getInt();
        this.westClubs = doubleDummyTable.getTricksAvailableFor(Strain.CLUBS, Direction.WEST).getInt();

        this.northNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.NOTRUMPS, Direction.NORTH).getInt();
        this.eastNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.NOTRUMPS, Direction.EAST).getInt();
        this.southNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.NOTRUMPS, Direction.SOUTH).getInt();
        this.westNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.NOTRUMPS, Direction.WEST).getInt();
    }

}
