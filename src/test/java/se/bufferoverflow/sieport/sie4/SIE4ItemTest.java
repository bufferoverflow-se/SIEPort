package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.Test;

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
        assertThat(SIE4Item.Ver.forSie4i(LocalDate.EPOCH, "Title", List.of())).isEqualTo(new SIE4Item.Ver(
            LocalDate.EPOCH,
            Optional.empty(),
            Optional.empty(),
            Optional.of("Title"),
            Optional.empty(),
            Optional.empty(),
            List.of()));

        assertThat(SIE4Item.Ver.forSie4e(LocalDate.EPOCH, "Title", "A", "2", List.of())).isEqualTo(new SIE4Item.Ver(
            LocalDate.EPOCH,
            Optional.of("A"),
            Optional.of("2"),
            Optional.of("Title"),
            Optional.empty(),
            Optional.empty(),
            List.of()));
    }
}
