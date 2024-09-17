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
         * Make this owner side so that, in the one-to-one relationship, the double dummy table is optional, but the board is not. i.e. It *is*
         * possible to create a board without a double dummy table but it *is not* possible to create a double dummy table without a board.
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
                this.northSpades = doubleDummyTable.getTricksAvailableFor(Strain.getSPADES(), Direction.getNorth()).tricks();
                this.eastSpades = doubleDummyTable.getTricksAvailableFor(Strain.getSPADES(), Direction.getEast()).tricks();
                this.southSpades = doubleDummyTable.getTricksAvailableFor(Strain.getSPADES(), Direction.getSouth()).tricks();
                this.westSpades = doubleDummyTable.getTricksAvailableFor(Strain.getSPADES(), Direction.getWest()).tricks();

                this.northHearts = doubleDummyTable.getTricksAvailableFor(Strain.getHEARTS(), Direction.getNorth()).tricks();
                this.eastHearts = doubleDummyTable.getTricksAvailableFor(Strain.getHEARTS(), Direction.getEast()).tricks();
                this.southHearts = doubleDummyTable.getTricksAvailableFor(Strain.getHEARTS(), Direction.getSouth()).tricks();
                this.westHearts = doubleDummyTable.getTricksAvailableFor(Strain.getHEARTS(), Direction.getWest()).tricks();

                this.northDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.getDIAMONDS(), Direction.getNorth()).tricks();
                this.eastDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.getDIAMONDS(), Direction.getEast()).tricks();
                this.southDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.getDIAMONDS(), Direction.getSouth()).tricks();
                this.westDiamonds = doubleDummyTable.getTricksAvailableFor(Strain.getDIAMONDS(), Direction.getWest()).tricks();

                this.northClubs = doubleDummyTable.getTricksAvailableFor(Strain.getCLUBS(), Direction.getNorth()).tricks();
                this.eastClubs = doubleDummyTable.getTricksAvailableFor(Strain.getCLUBS(), Direction.getEast()).tricks();
                this.southClubs = doubleDummyTable.getTricksAvailableFor(Strain.getCLUBS(), Direction.getSouth()).tricks();
                this.westClubs = doubleDummyTable.getTricksAvailableFor(Strain.getCLUBS(), Direction.getWest()).tricks();

                this.northNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.getNOTRUMPS(), Direction.getNorth()).tricks();
                this.eastNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.getNOTRUMPS(), Direction.getEast()).tricks();
                this.southNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.getNOTRUMPS(), Direction.getSouth()).tricks();
                this.westNotrumps = doubleDummyTable.getTricksAvailableFor(Strain.getNOTRUMPS(), Direction.getWest()).tricks();
        }

}
