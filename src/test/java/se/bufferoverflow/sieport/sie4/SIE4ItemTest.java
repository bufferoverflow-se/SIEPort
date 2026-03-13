package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SIE4ItemTest {

    @Test
    void flagga() {
        assertThat(new SIE4Item.Flagga(0)).isEqualTo(SIE4Item.Flagga.UNSET);
        assertThat(new SIE4Item.Flagga(1)).isEqualTo(SIE4Item.Flagga.SET);
        assertThatThrownBy(() -> new SIE4Item.Flagga(-1)).isInstanceOf(SIE4Exception.class);
        assertThatThrownBy(() -> new SIE4Item.Flagga(2)).isInstanceOf(SIE4Exception.class);
    }

    @Test
    void ver() {
        assertThat(SIE4Item.Ver.of(LocalDate.EPOCH, "Title", createValidTransactions()))
                .isEqualTo(new SIE4Item.Ver(
                        LocalDate.EPOCH,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of("Title"),
                        Optional.empty(),
                        Optional.empty(),
                        createValidTransactions()));

        assertThat(SIE4Item.Ver.of(LocalDate.EPOCH, "Title", "A", "2", createValidTransactions()))
                .isEqualTo(new SIE4Item.Ver(
                        LocalDate.EPOCH,
                        Optional.of("A"),
                        Optional.of("2"),
                        Optional.of("Title"),
                        Optional.empty(),
                        Optional.empty(),
                        createValidTransactions()));
    }

    @Test
    void ver_incorrectTransactions_shouldThrowException() {
        assertThatThrownBy(() -> SIE4Item.Ver.of(LocalDate.EPOCH, "Title", List.of()))
                .isInstanceOf(SIE4Exception.class)
                .hasMessageContaining("at least two transactions");

        assertThatThrownBy(() -> SIE4Item.Ver.of(LocalDate.EPOCH, "Title", List.of(
                SIE4Item.Transaction.Trans.of(1930, BigDecimal.TEN),
                SIE4Item.Transaction.Trans.of(1920, BigDecimal.TEN)
        )))
                .isInstanceOf(SIE4Exception.class)
                .hasMessageContaining("zero sum");
    }

    @Test
    void ver_fractionalNonZeroSum_shouldThrowException() {
        assertThatThrownBy(() -> SIE4Item.Ver.of(LocalDate.EPOCH, "Title", List.of(
                new SIE4Item.Transaction.Trans(1930, new BigDecimal("100.50"), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
                new SIE4Item.Transaction.Trans(1920, new BigDecimal("-100.49"), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())
        )))
                .isInstanceOf(SIE4Exception.class)
                .hasMessageContaining("zero sum");
    }

    @Test
    void trans() {
        // Single argument static factory method
        BigDecimal amount = new BigDecimal(12345);

        assertThat(SIE4Item.Transaction.Trans.of(1930, amount)).isEqualTo(new SIE4Item.Transaction.Trans(
                1930,
                amount,
                List.of(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
    }

    @Test
    void ver_withBtransAndRtrans_shouldNotThrow() {
        // Only care about #TRANS, #BTRANS and #RTRANS can safely be ignored
        assertThat(SIE4Item.Ver.of(LocalDate.EPOCH, "Title", List.of(
                SIE4Item.Transaction.Trans.of(1910, new BigDecimal("-1000.00")),
                SIE4Item.Transaction.Trans.of(2640, new BigDecimal("200.00")),
                new SIE4Item.Transaction.Btrans(6110, new BigDecimal("800.00"), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
                new SIE4Item.Transaction.Rtrans(6250, new BigDecimal("800.00"), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
                SIE4Item.Transaction.Trans.of(6250, new BigDecimal("800.00"))
        ))).isNotNull();
    }

    @Test
    void ver_mutatingSourceList_doesNotAffectRecord() {
        var txList = new ArrayList<>(createValidTransactions());
        SIE4Item.Ver ver = SIE4Item.Ver.of(LocalDate.EPOCH, "Title", txList);
        txList.clear();

        assertThat(ver.transactions()).hasSize(2);
    }

    @Test
    void trans_mutatingSourceList_doesNotAffectRecord() {
        var refs = new ArrayList<ObjectReference>();
        refs.add(ObjectReference.of(1, "A"));
        SIE4Item.Transaction.Trans trans = new SIE4Item.Transaction.Trans(
                1930, BigDecimal.TEN, refs, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        refs.clear();

        assertThat(trans.objectReferences()).hasSize(1);
    }

    @Test
    void rtrans_of_createsWithAllOptionalsEmpty() {
        BigDecimal amount = new BigDecimal("500.00");
        assertThat(SIE4Item.Transaction.Rtrans.of(1520, amount)).isEqualTo(new SIE4Item.Transaction.Rtrans(
                1520, amount, List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
    }

    @Test
    void btrans_of_createsWithAllOptionalsEmpty() {
        BigDecimal amount = new BigDecimal("500.00");
        assertThat(SIE4Item.Transaction.Btrans.of(1520, amount)).isEqualTo(new SIE4Item.Transaction.Btrans(
                1520, amount, List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
    }

    private static List<SIE4Item.Transaction> createValidTransactions() {
        return List.of(
                SIE4Item.Transaction.Trans.of(1930, new BigDecimal(-10)),
                SIE4Item.Transaction.Trans.of(1920, new BigDecimal(10))
        );
    }
}
