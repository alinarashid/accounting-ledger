package com.pluralsight.theme;

// Java's built-in sound tools
import javax.sound.midi.MidiChannel;   // one instrument slot (like one hand)
import javax.sound.midi.MidiSystem;    // hands us the keyboard
import javax.sound.midi.Synthesizer;   // the keyboard itself: makes the sound

/**
 * CafeSound
 * The cafe sounds: a dreamy welcome when the app starts,
 * a deposit sound (notes rise) and a payment sound (notes fall).
 */
public class CafeSound {

    // Our own on/off switch: set to false while testing so it doesn't play every run
    public static boolean soundOn = true;

    // ===== Welcome: a soft chord with a quiet pad underneath (about 2.8 s) =====
    public static void playDreamyWelcome() {
        if (!soundOn) {
            return;
        }

        int[] chord = {53, 57, 60, 64};   // 4 notes played together: warm and low

        try {
            // Get the keyboard and turn it on
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();

            // Two slots = two instruments at once (16 slots, 0-15; slot 9 is drums)
            MidiChannel piano = synth.getChannels()[0];
            MidiChannel pad = synth.getChannels()[1];

            // Pick each slot's instrument from the General MIDI list (0-127)
            piano.programChange(4);   // electric piano
            pad.programChange(89);    // warm pad

            // noteOn(which key, how hard): 60 = middle C, 0-127 (lower = softer)
            pad.noteOn(41, 30);       // one low note held underneath the whole time

            // Roll the chord: each note a tiny bit after the last
            // noteOn presses AND holds, so the notes overlap and keep ringing
            for (int note : chord) {
                piano.noteOn(note, 40);
                Thread.sleep(90);
            }

            // sleep only pauses the code; the keyboard keeps playing by itself
            Thread.sleep(500);
            piano.noteOn(69, 32);     // one soft high note joins in
            Thread.sleep(1200);

            // Let go, let it fade, then turn the keyboard off
            piano.allNotesOff();
            pad.allNotesOff();
            Thread.sleep(700);        // closing right away would cut the sound off
            synth.close();

        } catch (Exception e) {
            // No sound available: skip it instead of crashing
            System.out.println("(sound unavailable)");
        }
    }

    // ===== Deposit: money in, the notes go up =====
    public static void playDepositSound() {
        playSoftNotes(new int[]{60, 64, 67});   // C -> E -> G
    }

    // ===== Payment: money out, the same notes go down =====
    public static void playPaymentSound() {
        playSoftNotes(new int[]{67, 64, 60});   // G -> E -> C
    }

    // Shared helper: plays any notes softly on the electric piano
    // Deposit and payment reuse it, they just pass different notes
    public static void playSoftNotes(int[] notes) {
        if (!soundOn) {
            return;
        }

        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel piano = synth.getChannels()[0];
            piano.programChange(4);   // same electric piano as the welcome

            for (int note : notes) {
                piano.noteOn(note, 38);
                Thread.sleep(110);
            }

            Thread.sleep(450);        // let the notes ring
            piano.allNotesOff();
            Thread.sleep(150);        // short fade
            synth.close();

        } catch (Exception e) {
            System.out.println("(sound unavailable)");
        }
    }
}