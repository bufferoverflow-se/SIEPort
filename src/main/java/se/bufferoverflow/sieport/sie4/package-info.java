/**
 * SIEPort — a Java library for reading and writing SIE4 files used in Swedish financial
 * accounting software.
 *
 * <h2>Entry points</h2>
 * <ul>
 *   <li>{@link se.bufferoverflow.sieport.sie4.SIE4#parse SIE4.parse()} — parse a SIE4 file into
 *       a {@link se.bufferoverflow.sieport.sie4.SIE4Document}.</li>
 *   <li>{@link se.bufferoverflow.sieport.sie4.SIE4#write SIE4.write()} — write a
 *       {@code SIE4Document} or item list to a SIE4 file.</li>
 *   <li>{@link se.bufferoverflow.sieport.sie4.SIE4#validate SIE4.validate()} — validate an item
 *       list against SIE4E or SIE4I rules without writing.</li>
 * </ul>
 *
 * <h2>Data model</h2>
 * <p>Every SIE4 label (e.g. {@code #FNAMN}, {@code #VER}, {@code #TRANS}) is represented as a
 * record implementing {@link se.bufferoverflow.sieport.sie4.SIE4Item}. Records are immutable and
 * grouped by label type via the {@link se.bufferoverflow.sieport.sie4.SIE4ItemType} enum.
 *
 * <h2>File encoding</h2>
 * <p>SIE4 files use IBM Code Page 437 ({@code SIE4.SIE4_CHARSET}). All file I/O performed by
 * this library uses that charset automatically.
 *
 * <h2>Supported formats</h2>
 * <ul>
 *   <li>SIE 4E ({@code .SE}) — full export from a financial reporting program.</li>
 *   <li>SIE 4I ({@code .SI}) — transaction-only import file.</li>
 * </ul>
 */
package se.bufferoverflow.sieport.sie4;
