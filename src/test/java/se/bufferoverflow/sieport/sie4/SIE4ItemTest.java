package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private static List<SIE4Item.Transaction> createValidTransactions() {
        return List.of(
                SIE4Item.Transaction.Trans.of(1930, new BigDecimal(-10)),
                SIE4Item.Transaction.Trans.of(1920, new BigDecimal(10))
        );
    }
}
